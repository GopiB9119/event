import { createHash } from "node:crypto";
import { FieldValue } from "firebase-admin/firestore";

const EVENT_TITLE_MAX_LENGTH = 120;
const EVENT_DURATION_MAX_LENGTH = 160;
const DISPLAY_NAME_MAX_LENGTH = 80;
const CUSTOM_INFO_MAX_FIELDS = 20;
const CUSTOM_INFO_KEY_MAX_LENGTH = 60;
const CUSTOM_INFO_VALUE_MAX_LENGTH = 500;
const CUSTOM_INFO_KEY_PATTERN = /^[A-Za-z0-9][A-Za-z0-9 _.-]*$/;
const RESERVED_CUSTOM_INFO_KEYS = new Set(["__proto__", "constructor", "prototype"]);
const IDEMPOTENCY_KEY_PATTERN = /^[0-9a-f]{32}$/;
const VISIBILITY_POLICIES = new Set(["public", "private"]);

export class SharedEventInputError extends Error {
  constructor(code, message) {
    super(message);
    this.name = "SharedEventInputError";
    this.code = code;
  }
}

export async function createEventForActor(database, actor, input) {
  const normalized = normalizeCreateEventInput(actor, input);
  const payloadHash = sha256(canonicalJson(normalized.payload));
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);

  return database.runTransaction(async (transaction) => {
    const existingOperation = await transaction.get(operationRef);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      if (data.operationType !== "createEvent" || data.payloadHash !== payloadHash) {
        throw new SharedEventInputError(
          "idempotency-conflict",
          "This operation key was already used for different event data."
        );
      }
      return data.result;
    }

    const eventRef = database.collection("events").doc();
    const publicEventRef = database.collection("publicEvents").doc(eventRef.id);
    const membershipRef = eventRef.collection("members").doc(normalized.actorUid);
    const auditRef = eventRef.collection("audit").doc();
    const timestamp = FieldValue.serverTimestamp();
    const result = {
      eventId: eventRef.id,
      revision: 1
    };

    transaction.create(eventRef, {
      ownerUid: normalized.actorUid,
      title: normalized.payload.title,
      duration: normalized.payload.duration,
      visibilityPolicy: normalized.payload.visibilityPolicy,
      customInfo: normalized.payload.customInfo,
      status: "active",
      revision: 1,
      totalCollectedMinor: 0,
      totalSpentMinor: 0,
      confirmedReceiptCount: 0,
      activeMemberCount: 1,
      organizerCount: 1,
      contributorCount: 0,
      viewerCount: 0,
      currency: "INR",
      createdAtServer: timestamp,
      updatedAtServer: timestamp
    });
    if (normalized.payload.visibilityPolicy === "public") {
      transaction.create(publicEventRef, {
        eventId: eventRef.id,
        title: normalized.payload.title,
        duration: normalized.payload.duration,
        visibilityPolicy: "public",
        status: "active",
        activeMemberCount: 1,
        createdAtServer: timestamp,
        updatedAtServer: timestamp
      });
    }
    transaction.create(membershipRef, {
      uid: normalized.actorUid,
      role: "organizer",
      status: "active",
      presenceGeneration: 1,
      displayName: normalized.displayName,
      confirmedReceiptCount: 0,
      confirmedMoneyInMinor: 0,
      confirmedMoneyOutMinor: 0,
      joinedAtServer: timestamp,
      revokedAtServer: null
    });
    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: "event.created",
      subjectId: eventRef.id,
      beforeRevision: null,
      afterRevision: 1,
      reason: null,
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "createEvent",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return result;
  });
}

function normalizeCreateEventInput(actor, input) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "A verified account is required.", 128);
  const displayName = requiredString(
    actor?.displayName || "Organizer",
    "invalid-identity",
    "Account display name is required.",
    DISPLAY_NAME_MAX_LENGTH
  );
  const idempotencyKey = requiredString(
    input?.idempotencyKey,
    "invalid-argument",
    "A valid idempotency key is required.",
    32
  ).toLowerCase();
  if (!IDEMPOTENCY_KEY_PATTERN.test(idempotencyKey)) {
    throw new SharedEventInputError("invalid-argument", "Idempotency key must be 32 lowercase hexadecimal characters.");
  }

  const title = requiredString(
    input?.title,
    "invalid-argument",
    "Event title is required.",
    EVENT_TITLE_MAX_LENGTH
  );
  const duration = optionalString(input?.duration, EVENT_DURATION_MAX_LENGTH);
  const visibilityPolicy = requiredString(
    input?.visibilityPolicy,
    "invalid-argument",
    "Visibility policy is required.",
    16
  );
  if (!VISIBILITY_POLICIES.has(visibilityPolicy)) {
    throw new SharedEventInputError("invalid-argument", "Visibility policy must be public or private.");
  }
  const customInfo = normalizeCustomInfo(input?.customInfo);

  return {
    actorUid,
    displayName,
    idempotencyKey,
    payload: {
      title,
      duration,
      visibilityPolicy,
      customInfo
    }
  };
}

function normalizeCustomInfo(value) {
  if (value == null) return {};
  if (typeof value !== "object" || Array.isArray(value)) {
    throw new SharedEventInputError("invalid-argument", "Custom event information must be an object.");
  }

  const entries = Object.entries(value);
  if (entries.length > CUSTOM_INFO_MAX_FIELDS) {
    throw new SharedEventInputError("invalid-argument", "Too many custom event fields.");
  }

  const normalizedEntries = [];
  const normalizedKeys = new Set();
  for (const [key, fieldValue] of entries) {
    const normalizedKey = requiredString(
      key,
      "invalid-argument",
      "Custom event field names must not be blank.",
      CUSTOM_INFO_KEY_MAX_LENGTH
    );
    if (
      !CUSTOM_INFO_KEY_PATTERN.test(normalizedKey)
      || normalizedKey.startsWith("__")
      || RESERVED_CUSTOM_INFO_KEYS.has(normalizedKey)
      || normalizedKeys.has(normalizedKey)
    ) {
      throw new SharedEventInputError(
        "invalid-argument",
        "Custom event field name is not supported or is duplicated."
      );
    }
    normalizedKeys.add(normalizedKey);
    const normalizedValue = optionalString(fieldValue, CUSTOM_INFO_VALUE_MAX_LENGTH);
    normalizedEntries.push([normalizedKey, normalizedValue]);
  }
  return Object.fromEntries(normalizedEntries);
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

function optionalString(value, maxLength) {
  if (value == null) return null;
  if (typeof value !== "string") {
    throw new SharedEventInputError("invalid-argument", "Text fields must contain text values.");
  }
  const normalized = value.trim();
  if (normalized.length > maxLength) {
    throw new SharedEventInputError("invalid-argument", "Text field exceeds its supported length.");
  }
  return normalized || null;
}

function canonicalJson(value) {
  if (Array.isArray(value)) {
    return `[${value.map(canonicalJson).join(",")}]`;
  }
  if (value && typeof value === "object") {
    return `{${Object.keys(value)
      .sort()
      .map((key) => `${JSON.stringify(key)}:${canonicalJson(value[key])}`)
      .join(",")}}`;
  }
  return JSON.stringify(value);
}

function sha256(value) {
  return createHash("sha256").update(value, "utf8").digest("hex");
}