import { createHash, createHmac, timingSafeEqual } from "node:crypto";
import { FieldValue } from "firebase-admin/firestore";
import { SharedEventInputError } from "./sharedEvents.js";

const IDEMPOTENCY_KEY_PATTERN = /^[0-9a-f]{32}$/;
const EVENT_ID_PATTERN = /^[A-Za-z0-9]{20}$/;
const INVITE_ID_PATTERN = /^[0-9a-f]{32}$/;
const INVITE_SECRET_PATTERN = /^[A-Za-z0-9_-]{43}$/;
const INVITE_ROLES = new Set(["contributor", "viewer"]);
const MIN_INVITE_SECONDS = 600;
const MAX_INVITE_SECONDS = 7 * 24 * 60 * 60;
const DISPLAY_NAME_MAX_LENGTH = 80;

export async function createInviteForActor(database, actor, input, tokenKey) {
  const normalized = normalizeCreateInviteInput(actor, input, tokenKey);
  const payloadHash = sha256(canonicalJson(normalized.payload));
  const inviteId = hmacHex(tokenKey, `invite-id:${normalized.actorUid}:${normalized.idempotencyKey}`).slice(0, 32);
  const secret = hmacBase64Url(tokenKey, `invite-secret:${normalized.actorUid}:${normalized.idempotencyKey}`);
  const secretHash = sha256(secret);
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);
  const eventRef = database.collection("events").doc(normalized.payload.eventId);
  const actorMembershipRef = eventRef.collection("members").doc(normalized.actorUid);
  const inviteRef = database.collection("invites").doc(inviteId);
  const auditRef = eventRef.collection("audit").doc();

  return database.runTransaction(async (transaction) => {
    const existingOperation = await transaction.get(operationRef);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      assertIdempotentOperation(data, "createInvite", payloadHash);
      return {
        ...data.result,
        inviteToken: `${data.result.inviteId}.${secret}`
      };
    }

    const [eventSnapshot, membershipSnapshot, inviteSnapshot] = await Promise.all([
      transaction.get(eventRef),
      transaction.get(actorMembershipRef),
      transaction.get(inviteRef)
    ]);
    if (!eventSnapshot.exists || eventSnapshot.data().status !== "active") {
      throw new SharedEventInputError("not-found", "Active shared event was not found.");
    }
    if (
      !membershipSnapshot.exists
      || membershipSnapshot.data().uid !== normalized.actorUid
      || membershipSnapshot.data().status !== "active"
      || membershipSnapshot.data().role !== "organizer"
    ) {
      throw new SharedEventInputError("permission-denied", "Only an active organizer can create an invite.");
    }
    if (inviteSnapshot.exists) {
      throw new SharedEventInputError("already-exists", "Invite identity is already in use.");
    }

    const timestamp = FieldValue.serverTimestamp();
    const expiresAtMillis = Date.now() + normalized.payload.expiresInSeconds * 1000;
    const result = {
      eventId: eventRef.id,
      inviteId,
      role: normalized.payload.role,
      expiresAtMillis
    };

    transaction.create(inviteRef, {
      eventId: eventRef.id,
      secretHash,
      role: normalized.payload.role,
      status: "active",
      expiresAtMillis,
      useLimit: 1,
      useCount: 0,
      createdByUid: normalized.actorUid,
      createdAtServer: timestamp,
      revokedAtServer: null
    });
    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: "invite.created",
      subjectId: inviteId,
      beforeRevision: eventSnapshot.data().revision,
      afterRevision: eventSnapshot.data().revision,
      reason: normalized.payload.role,
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "createInvite",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return {
      ...result,
      inviteToken: `${inviteId}.${secret}`
    };
  });
}

export async function acceptInviteForActor(database, actor, input) {
  const normalized = normalizeAcceptInviteInput(actor, input);
  const payloadHash = sha256(canonicalJson({
    inviteId: normalized.inviteId,
    secretHash: sha256(normalized.secret)
  }));
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);
  const inviteRef = database.collection("invites").doc(normalized.inviteId);

  return database.runTransaction(async (transaction) => {
    const [existingOperation, inviteSnapshot] = await Promise.all([
      transaction.get(operationRef),
      transaction.get(inviteRef)
    ]);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      assertIdempotentOperation(data, "acceptInvite", payloadHash);
      return data.result;
    }
    if (!inviteSnapshot.exists) {
      throw new SharedEventInputError("not-found", "Invite was not found.");
    }

    const invite = inviteSnapshot.data();
    if (!constantTimeHexEqual(invite.secretHash, sha256(normalized.secret))) {
      throw new SharedEventInputError("permission-denied", "Invite is invalid.");
    }
    if (invite.status !== "active" || invite.useCount >= invite.useLimit) {
      throw new SharedEventInputError("failed-precondition", "Invite is no longer available.");
    }
    if (!Number.isSafeInteger(invite.expiresAtMillis) || invite.expiresAtMillis <= Date.now()) {
      throw new SharedEventInputError("deadline-exceeded", "Invite has expired.");
    }

    const eventRef = database.collection("events").doc(invite.eventId);
    const membershipRef = eventRef.collection("members").doc(normalized.actorUid);
    const [eventSnapshot, membershipSnapshot] = await Promise.all([
      transaction.get(eventRef),
      transaction.get(membershipRef)
    ]);
    if (!eventSnapshot.exists || eventSnapshot.data().status !== "active") {
      throw new SharedEventInputError("not-found", "Active shared event was not found.");
    }
    if (membershipSnapshot.exists && membershipSnapshot.data().status === "revoked") {
      throw new SharedEventInputError("permission-denied", "Revoked membership cannot be restored by reusing an invite.");
    }
    const publicEventRef = database.collection("publicEvents").doc(eventRef.id);
    const publicEventSnapshot = eventSnapshot.data().visibilityPolicy === "public"
      ? await transaction.get(publicEventRef)
      : null;
    if (eventSnapshot.data().visibilityPolicy === "public" && !publicEventSnapshot.exists) {
      throw new SharedEventInputError("failed-precondition", "Public event profile is missing.");
    }

    const timestamp = FieldValue.serverTimestamp();
    if (membershipSnapshot.exists && membershipSnapshot.data().status === "active") {
      const result = {
        eventId: eventRef.id,
        role: membershipSnapshot.data().role,
        revision: eventSnapshot.data().revision,
        alreadyMember: true
      };
      transaction.create(operationRef, {
        operationType: "acceptInvite",
        eventId: eventRef.id,
        payloadHash,
        result,
        createdAtServer: timestamp
      });
      return result;
    }

    const revision = eventSnapshot.data().revision + 1;
    const activeMemberCount = eventSnapshot.data().activeMemberCount + 1;
    if (!Number.isSafeInteger(activeMemberCount)) {
      throw new SharedEventInputError("resource-exhausted", "Member count exceeds the supported range.");
    }
    const auditRef = eventRef.collection("audit").doc();
    const activityRef = eventRef.collection("activity").doc();
    const roleCountField = `${invite.role}Count`;
    const result = {
      eventId: eventRef.id,
      role: invite.role,
      revision,
      alreadyMember: false
    };

    transaction.create(membershipRef, {
      uid: normalized.actorUid,
      role: invite.role,
      status: "active",
      presenceGeneration: revision,
      displayName: normalized.displayName,
      confirmedReceiptCount: 0,
      confirmedMoneyInMinor: 0,
      confirmedMoneyOutMinor: 0,
      joinedAtServer: timestamp,
      revokedAtServer: null
    });
    transaction.update(inviteRef, {
      useCount: invite.useCount + 1,
      status: invite.useCount + 1 >= invite.useLimit ? "consumed" : "active",
      acceptedByUid: normalized.actorUid,
      acceptedAtServer: timestamp
    });
    transaction.update(eventRef, {
      revision,
      activeMemberCount,
      [roleCountField]: FieldValue.increment(1),
      updatedAtServer: timestamp
    });
    if (publicEventSnapshot) {
      transaction.update(publicEventRef, {
        activeMemberCount,
        updatedAtServer: timestamp
      });
    }
    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: "member.joined",
      subjectId: normalized.actorUid,
      beforeRevision: eventSnapshot.data().revision,
      afterRevision: revision,
      reason: invite.role,
      createdAtServer: timestamp
    });
    transaction.create(activityRef, {
      actorUid: normalized.actorUid,
      action: "member.joined",
      visibility: "member",
      subjectId: normalized.actorUid,
      role: invite.role,
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "acceptInvite",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return result;
  });
}

export async function joinPublicEventForActor(database, actor, input) {
  const normalized = normalizePublicJoinInput(actor, input);
  const payloadHash = sha256(canonicalJson({ eventId: normalized.eventId }));
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);
  const eventRef = database.collection("events").doc(normalized.eventId);
  const publicEventRef = database.collection("publicEvents").doc(normalized.eventId);
  const membershipRef = eventRef.collection("members").doc(normalized.actorUid);
  const auditRef = eventRef.collection("audit").doc();
  const activityRef = eventRef.collection("activity").doc();

  return database.runTransaction(async (transaction) => {
    const [existingOperation, eventSnapshot, publicEventSnapshot, membershipSnapshot] = await Promise.all([
      transaction.get(operationRef),
      transaction.get(eventRef),
      transaction.get(publicEventRef),
      transaction.get(membershipRef)
    ]);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      assertIdempotentOperation(data, "joinPublicEvent", payloadHash);
      return data.result;
    }
    if (
      !eventSnapshot.exists
      || eventSnapshot.data().status !== "active"
      || eventSnapshot.data().visibilityPolicy !== "public"
      || !publicEventSnapshot.exists
    ) {
      throw new SharedEventInputError("not-found", "Active public event was not found.");
    }
    if (membershipSnapshot.exists && membershipSnapshot.data().status === "revoked") {
      throw new SharedEventInputError("permission-denied", "Revoked membership cannot be restored by public join.");
    }

    const timestamp = FieldValue.serverTimestamp();
    if (membershipSnapshot.exists && membershipSnapshot.data().status === "active") {
      const result = {
        eventId: eventRef.id,
        role: membershipSnapshot.data().role,
        revision: eventSnapshot.data().revision,
        alreadyMember: true
      };
      transaction.create(operationRef, {
        operationType: "joinPublicEvent",
        eventId: eventRef.id,
        payloadHash,
        result,
        createdAtServer: timestamp
      });
      return result;
    }

    const revision = eventSnapshot.data().revision + 1;
    const activeMemberCount = eventSnapshot.data().activeMemberCount + 1;
    if (!Number.isSafeInteger(activeMemberCount)) {
      throw new SharedEventInputError("resource-exhausted", "Member count exceeds the supported range.");
    }
    const result = {
      eventId: eventRef.id,
      role: "viewer",
      revision,
      alreadyMember: false
    };

    transaction.create(membershipRef, {
      uid: normalized.actorUid,
      role: "viewer",
      status: "active",
      presenceGeneration: revision,
      displayName: normalized.displayName,
      confirmedReceiptCount: 0,
      confirmedMoneyInMinor: 0,
      confirmedMoneyOutMinor: 0,
      joinedAtServer: timestamp,
      revokedAtServer: null
    });
    transaction.update(eventRef, {
      revision,
      activeMemberCount,
      viewerCount: FieldValue.increment(1),
      updatedAtServer: timestamp
    });
    transaction.update(publicEventRef, {
      activeMemberCount,
      updatedAtServer: timestamp
    });
    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: "member.joined_public",
      subjectId: normalized.actorUid,
      beforeRevision: eventSnapshot.data().revision,
      afterRevision: revision,
      reason: "viewer",
      createdAtServer: timestamp
    });
    transaction.create(activityRef, {
      actorUid: normalized.actorUid,
      action: "member.joined",
      visibility: "member",
      subjectId: normalized.actorUid,
      role: "viewer",
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "joinPublicEvent",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return result;
  });
}

function normalizePublicJoinInput(actor, input) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "An authenticated account is required.", 128);
  const displayName = requiredString(
    actor?.displayName || "Member",
    "invalid-identity",
    "Account display name is invalid.",
    DISPLAY_NAME_MAX_LENGTH
  );
  const eventId = requiredString(input?.eventId, "invalid-argument", "Event ID is required.", 20);
  if (!EVENT_ID_PATTERN.test(eventId)) {
    throw new SharedEventInputError("invalid-argument", "Event ID is invalid.");
  }
  return {
    actorUid,
    displayName,
    eventId,
    idempotencyKey: normalizeIdempotencyKey(input?.idempotencyKey)
  };
}

function normalizeCreateInviteInput(actor, input, tokenKey) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "An authenticated account is required.", 128);
  requiredString(tokenKey, "failed-precondition", "Invite token key is not configured.", 512);
  const idempotencyKey = normalizeIdempotencyKey(input?.idempotencyKey);
  const eventId = requiredString(input?.eventId, "invalid-argument", "Event ID is required.", 20);
  if (!EVENT_ID_PATTERN.test(eventId)) {
    throw new SharedEventInputError("invalid-argument", "Event ID is invalid.");
  }
  const role = requiredString(input?.role, "invalid-argument", "Invite role is required.", 16);
  if (!INVITE_ROLES.has(role)) {
    throw new SharedEventInputError("invalid-argument", "Invite role must be contributor or viewer.");
  }
  if (!Number.isSafeInteger(input?.expiresInSeconds)
    || input.expiresInSeconds < MIN_INVITE_SECONDS
    || input.expiresInSeconds > MAX_INVITE_SECONDS) {
    throw new SharedEventInputError("invalid-argument", "Invite expiry is outside the supported range.");
  }
  return {
    actorUid,
    idempotencyKey,
    payload: {
      eventId,
      role,
      expiresInSeconds: input.expiresInSeconds
    }
  };
}

function normalizeAcceptInviteInput(actor, input) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "An authenticated account is required.", 128);
  const displayName = requiredString(
    actor?.displayName || "Member",
    "invalid-identity",
    "Account display name is invalid.",
    DISPLAY_NAME_MAX_LENGTH
  );
  const idempotencyKey = normalizeIdempotencyKey(input?.idempotencyKey);
  const inviteToken = requiredString(input?.inviteToken, "invalid-argument", "Invite token is required.", 100);
  const components = inviteToken.split(".");
  if (components.length !== 2
    || !INVITE_ID_PATTERN.test(components[0])
    || !INVITE_SECRET_PATTERN.test(components[1])) {
    throw new SharedEventInputError("invalid-argument", "Invite token format is invalid.");
  }
  return {
    actorUid,
    displayName,
    idempotencyKey,
    inviteId: components[0],
    secret: components[1]
  };
}

function normalizeIdempotencyKey(value) {
  const normalized = requiredString(value, "invalid-argument", "A valid idempotency key is required.", 32).toLowerCase();
  if (!IDEMPOTENCY_KEY_PATTERN.test(normalized)) {
    throw new SharedEventInputError("invalid-argument", "Idempotency key must be 32 lowercase hexadecimal characters.");
  }
  return normalized;
}

function assertIdempotentOperation(data, operationType, payloadHash) {
  if (data.operationType !== operationType || data.payloadHash !== payloadHash) {
    throw new SharedEventInputError(
      "idempotency-conflict",
      "This operation key was already used for different data."
    );
  }
}

function requiredString(value, code, message, maxLength) {
  if (typeof value !== "string") {
    throw new SharedEventInputError(code, message);
  }
  const normalized = value.trim();
  if (!normalized || normalized.length > maxLength) {
    throw new SharedEventInputError(code, message);
  }
  return normalized;
}

function canonicalJson(value) {
  if (Array.isArray(value)) return `[${value.map(canonicalJson).join(",")}]`;
  if (value && typeof value === "object") {
    return `{${Object.keys(value)
      .sort()
      .map((key) => `${JSON.stringify(key)}:${canonicalJson(value[key])}`)
      .join(",")}}`;
  }
  return JSON.stringify(value);
}

function hmacHex(key, value) {
  return createHmac("sha256", key).update(value, "utf8").digest("hex");
}

function hmacBase64Url(key, value) {
  return createHmac("sha256", key).update(value, "utf8").digest("base64url");
}

function sha256(value) {
  return createHash("sha256").update(value, "utf8").digest("hex");
}

function constantTimeHexEqual(left, right) {
  if (typeof left !== "string" || typeof right !== "string") return false;
  const leftBuffer = Buffer.from(left, "hex");
  const rightBuffer = Buffer.from(right, "hex");
  return leftBuffer.length === rightBuffer.length && timingSafeEqual(leftBuffer, rightBuffer);
}