import { createHash, createHmac } from "node:crypto";
import { FieldValue } from "firebase-admin/firestore";
import { SharedEventInputError } from "./sharedEvents.js";

const IDEMPOTENCY_KEY_PATTERN = /^[0-9a-f]{32}$/;
const EVENT_ID_PATTERN = /^[A-Za-z0-9]{20}$/;
const ENTRY_ID_PATTERN = /^[A-Za-z0-9]{20}$/;
const LEDGER_TYPES = new Set(["Donated", "Credit", "Debit", "Expense"]);
const MUTATING_ROLES = new Set(["organizer", "contributor"]);
const PAYMENT_DATE_PATTERN = /^\d{4}-\d{2}-\d{2}$/;
const DISPLAY_NAME_MAX_LENGTH = 80;
const EVIDENCE_TEXT_MAX_LENGTH = 160;
const PAYMENT_REFERENCE_MAX_LENGTH = 128;
const STRONG_REFERENCE_PATTERN = /^[A-Z0-9]{6,64}$/;
const MIN_RELIABLE_AMOUNT_EVIDENCE = 65;
const RELIABLE_AMOUNT_EVIDENCE_SOURCES = new Set([
  "CURRENCY_MARKED",
  "AMOUNT_LABEL",
  "NEAR_AMOUNT_LABEL",
  "PARTY_LINE_AMOUNT",
  "TOP_RECEIPT_VALUE"
]);
const WARNING_MAX_COUNT = 20;
const WARNING_MAX_LENGTH = 200;
const REVIEW_REASON_MAX_LENGTH = 500;

export async function submitReviewedEntryForActor(database, actor, input, duplicateHashKey) {
  const normalized = normalizeSubmitInput(actor, input, duplicateHashKey);
  const payloadHash = hmacHex(duplicateHashKey, canonicalJson(normalized.payload));
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);
  const eventRef = database.collection("events").doc(normalized.payload.eventId);
  const actorMembershipRef = eventRef.collection("members").doc(normalized.actorUid);
  const ledgerPersonRef = eventRef.collection("members").doc(normalized.payload.ledgerPersonUid);
  const entryRef = eventRef.collection("entries").doc();
  const privateEvidenceRef = eventRef.collection("privateEvidence").doc(entryRef.id);
  const auditRef = eventRef.collection("audit").doc();
  const activityRef = eventRef.collection("activity").doc();
  const duplicateKeyIds = duplicateKeyIdsForPayload(normalized.payload, duplicateHashKey);
  const duplicateRefs = duplicateKeyIds.map((keyId) => eventRef.collection("duplicateKeys").doc(keyId));

  return database.runTransaction(async (transaction) => {
    const existingOperation = await transaction.get(operationRef);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      assertIdempotentOperation(data, "submitReviewedEntry", payloadHash);
      return data.result;
    }

    const reads = [
      transaction.get(eventRef),
      transaction.get(actorMembershipRef),
      transaction.get(ledgerPersonRef)
    ];
    reads.push(...duplicateRefs.map((reference) => transaction.get(reference)));
    const snapshots = await Promise.all(reads);
    const [eventSnapshot, actorMembershipSnapshot, ledgerPersonSnapshot] = snapshots;
    const duplicateSnapshots = snapshots.slice(3);

    assertActiveEvent(eventSnapshot);
    assertExpectedRevision(eventSnapshot.data(), normalized.payload.expectedRevision);
    const actorMembership = assertActiveMembership(actorMembershipSnapshot, normalized.actorUid);
    if (!MUTATING_ROLES.has(actorMembership.role)) {
      throw new SharedEventInputError("permission-denied", "Viewer role cannot submit ledger entries.");
    }
    assertActiveMembership(ledgerPersonSnapshot, normalized.payload.ledgerPersonUid);
    if (actorMembership.role !== "organizer" && normalized.payload.ledgerPersonUid !== normalized.actorUid) {
      throw new SharedEventInputError("permission-denied", "Contributors can submit entries only for their own membership.");
    }
    if (duplicateSnapshots.some((snapshot) => snapshot.exists)) {
      throw new SharedEventInputError(
        "already-exists",
        "A confirmed or pending entry already reserves matching receipt evidence."
      );
    }

    const status = "pending";
    const revision = eventSnapshot.data().revision;
    const timestamp = FieldValue.serverTimestamp();
    const result = {
      eventId: eventRef.id,
      entryId: entryRef.id,
      status,
      revision
    };

    transaction.create(entryRef, {
      eventId: eventRef.id,
      revision: null,
      status,
      ledgerType: normalized.payload.ledgerType,
      amountMinor: normalized.payload.amountMinor,
      amountEvidenceSource: normalized.payload.amountEvidenceSource,
      amountEvidenceConfidence: normalized.payload.amountEvidenceConfidence,
      currency: "INR",
      ledgerPersonUid: normalized.payload.ledgerPersonUid,
      ledgerPersonDisplayNameSnapshot: ledgerPersonSnapshot.data().displayName,
      submittedByUid: normalized.actorUid,
      submittedByDisplayNameSnapshot: normalized.displayName,
      reviewedByUid: null,
      idempotencyKey: normalized.idempotencyKey,
      duplicateKeyIds,
      createdAtServer: timestamp,
      reviewedAtServer: null,
      supersedesEntryId: null
    });
    transaction.create(privateEvidenceRef, {
      eventId: eventRef.id,
      entryId: entryRef.id,
      submittedByUid: normalized.actorUid,
      paymentApp: normalized.payload.paymentApp,
      paymentDate: normalized.payload.paymentDate,
      counterparty: normalized.payload.counterparty,
      paymentReference: normalized.payload.paymentReference,
      confidence: normalized.payload.confidence,
      amountEvidenceSource: normalized.payload.amountEvidenceSource,
      amountEvidenceConfidence: normalized.payload.amountEvidenceConfidence,
      warnings: normalized.payload.warnings,
      createdAtServer: timestamp
    });
    for (const duplicateRef of duplicateRefs) {
      transaction.create(duplicateRef, {
        eventId: eventRef.id,
        entryId: entryRef.id,
        status: "reserved",
        createdAtServer: timestamp
      });
    }

    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: "entry.submitted",
      subjectId: entryRef.id,
      beforeRevision: eventSnapshot.data().revision,
      afterRevision: revision,
      reason: "organizer review required",
      createdAtServer: timestamp
    });
    transaction.create(activityRef, {
      actorUid: normalized.actorUid,
      submittedByUid: normalized.actorUid,
      action: "entry.pending",
      visibility: "restricted",
      subjectId: entryRef.id,
      ledgerType: normalized.payload.ledgerType,
      amountMinor: normalized.payload.amountMinor,
      ledgerPersonDisplayNameSnapshot: ledgerPersonSnapshot.data().displayName,
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "submitReviewedEntry",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return result;
  });
}

export async function reviewPendingEntryForActor(database, actor, input) {
  const normalized = normalizeReviewInput(actor, input);
  const payloadHash = sha256(canonicalJson(normalized.payload));
  const operationRef = database
    .collection("users")
    .doc(normalized.actorUid)
    .collection("operations")
    .doc(normalized.idempotencyKey);
  const eventRef = database.collection("events").doc(normalized.payload.eventId);
  const actorMembershipRef = eventRef.collection("members").doc(normalized.actorUid);
  const entryRef = eventRef.collection("entries").doc(normalized.payload.entryId);
  const privateEvidenceRef = eventRef.collection("privateEvidence").doc(normalized.payload.entryId);
  const auditRef = eventRef.collection("audit").doc();
  const activityRef = eventRef.collection("activity").doc();

  return database.runTransaction(async (transaction) => {
    const existingOperation = await transaction.get(operationRef);
    if (existingOperation.exists) {
      const data = existingOperation.data();
      assertIdempotentOperation(data, "reviewPendingEntry", payloadHash);
      return data.result;
    }

    const [eventSnapshot, actorMembershipSnapshot, entrySnapshot, privateEvidenceSnapshot] = await Promise.all([
      transaction.get(eventRef),
      transaction.get(actorMembershipRef),
      transaction.get(entryRef),
      transaction.get(privateEvidenceRef)
    ]);
    assertActiveEvent(eventSnapshot);
    assertExpectedRevision(eventSnapshot.data(), normalized.payload.expectedRevision);
    const actorMembership = assertActiveMembership(actorMembershipSnapshot, normalized.actorUid);
    if (actorMembership.role !== "organizer") {
      throw new SharedEventInputError("permission-denied", "Only an active organizer can review entries.");
    }
    if (!entrySnapshot.exists || entrySnapshot.data().status !== "pending") {
      throw new SharedEventInputError("failed-precondition", "Entry is not pending organizer review.");
    }

    const entry = entrySnapshot.data();
    const ledgerPersonRef = eventRef.collection("members").doc(entry.ledgerPersonUid);
    const duplicateRefs = Array.isArray(entry.duplicateKeyIds)
      ? entry.duplicateKeyIds.map((keyId) => eventRef.collection("duplicateKeys").doc(keyId))
      : [];
    const followUpReads = [transaction.get(ledgerPersonRef), ...duplicateRefs.map((reference) => transaction.get(reference))];
    const [ledgerPersonSnapshot, ...duplicateSnapshots] = await Promise.all(followUpReads);
    if (duplicateSnapshots.some((snapshot) => !snapshot.exists)) {
      throw new SharedEventInputError("failed-precondition", "Entry duplicate reservation is missing.");
    }

    const revision = eventSnapshot.data().revision + 1;
    const timestamp = FieldValue.serverTimestamp();
    const status = normalized.payload.decision === "confirm" ? "confirmed" : "rejected";
    const result = {
      eventId: eventRef.id,
      entryId: entryRef.id,
      status,
      revision
    };

    transaction.update(entryRef, {
      status,
      revision,
      reviewedByUid: normalized.actorUid,
      reviewedAtServer: timestamp
    });
    if (status === "confirmed") {
      assertStoredPrivateEvidence(privateEvidenceSnapshot, entry, entryRef.id, eventRef.id);
      assertReliableAmountEvidence(
        entry.amountEvidenceSource,
        entry.amountEvidenceConfidence,
        "failed-precondition"
      );
      assertActiveMembership(ledgerPersonSnapshot, entry.ledgerPersonUid);
      transaction.update(
        eventRef,
        confirmedEventUpdates(eventSnapshot.data(), entry.ledgerType, entry.amountMinor, revision, timestamp)
      );
      transaction.update(
        ledgerPersonRef,
        confirmedMemberUpdates(ledgerPersonSnapshot.data(), entry.ledgerType, entry.amountMinor)
      );
      duplicateRefs.forEach((reference) => transaction.update(reference, { status: "confirmed" }));
    } else {
      transaction.update(eventRef, { revision, updatedAtServer: timestamp });
      duplicateRefs.forEach((reference) => transaction.delete(reference));
    }
    transaction.create(auditRef, {
      actorUid: normalized.actorUid,
      action: status === "confirmed" ? "entry.confirmed" : "entry.rejected",
      subjectId: entryRef.id,
      beforeRevision: eventSnapshot.data().revision,
      afterRevision: revision,
      reason: normalized.payload.reason,
      createdAtServer: timestamp
    });
    transaction.create(activityRef, {
      actorUid: normalized.actorUid,
      submittedByUid: entry.submittedByUid,
      action: status === "confirmed" ? "entry.confirmed" : "entry.rejected",
      visibility: status === "confirmed" ? "member" : "restricted",
      subjectId: entryRef.id,
      ledgerType: entry.ledgerType,
      amountMinor: entry.amountMinor,
      ledgerPersonDisplayNameSnapshot: entry.ledgerPersonDisplayNameSnapshot,
      createdAtServer: timestamp
    });
    transaction.create(operationRef, {
      operationType: "reviewPendingEntry",
      eventId: eventRef.id,
      payloadHash,
      result,
      createdAtServer: timestamp
    });

    return result;
  });
}

function assertStoredPrivateEvidence(snapshot, entry, entryId, eventId) {
  if (!snapshot.exists) {
    throw new SharedEventInputError("failed-precondition", "Receipt evidence is missing.");
  }
  const evidence = snapshot.data();
  const validWarnings = Array.isArray(evidence.warnings)
    && evidence.warnings.length <= WARNING_MAX_COUNT
    && evidence.warnings.every((warning) => typeof warning === "string" && warning.length <= WARNING_MAX_LENGTH);
  if (evidence.eventId !== eventId
    || evidence.entryId !== entryId
    || evidence.submittedByUid !== entry.submittedByUid
    || evidence.amountEvidenceSource !== entry.amountEvidenceSource
    || evidence.amountEvidenceConfidence !== entry.amountEvidenceConfidence
    || !Number.isInteger(evidence.confidence)
    || evidence.confidence < 0
    || evidence.confidence > 100
    || !validWarnings) {
    throw new SharedEventInputError("failed-precondition", "Receipt evidence does not match its pending entry.");
  }
}

function normalizeSubmitInput(actor, input, duplicateHashKey) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "An authenticated account is required.", 128);
  const displayName = requiredString(
    actor?.displayName || "Member",
    "invalid-identity",
    "Account display name is invalid.",
    DISPLAY_NAME_MAX_LENGTH
  );
  requiredString(duplicateHashKey, "failed-precondition", "Duplicate hash key is not configured.", 512);
  const eventId = normalizeDocumentId(input?.eventId, EVENT_ID_PATTERN, "Event ID is invalid.");
  const ledgerPersonUid = requiredString(
    input?.ledgerPersonUid,
    "invalid-argument",
    "Ledger person membership is required.",
    128
  );
  const ledgerType = requiredString(input?.ledgerType, "invalid-argument", "Ledger type is required.", 16);
  if (!LEDGER_TYPES.has(ledgerType)) {
    throw new SharedEventInputError("invalid-argument", "Ledger type is not supported.");
  }
  if (!Number.isSafeInteger(input?.amountMinor) || input.amountMinor <= 0) {
    throw new SharedEventInputError("invalid-argument", "Amount must be a positive integer in minor units.");
  }
  const amountEvidenceSource = requiredString(
    input?.amountEvidenceSource,
    "invalid-argument",
    "Receipt-derived amount evidence source is required.",
    32
  );
  const amountEvidenceConfidence = input?.amountEvidenceConfidence;
  assertReliableAmountEvidence(amountEvidenceSource, amountEvidenceConfidence, "invalid-argument");
  if (!Number.isSafeInteger(input?.expectedRevision) || input.expectedRevision <= 0) {
    throw new SharedEventInputError("invalid-argument", "Expected event revision is required.");
  }
  const paymentReference = optionalString(input?.paymentReference, PAYMENT_REFERENCE_MAX_LENGTH);
  if (paymentReference != null && !STRONG_REFERENCE_PATTERN.test(normalizePaymentReference(paymentReference))) {
    throw new SharedEventInputError(
      "invalid-argument",
      "Payment reference must contain at least six letters or digits after normalization."
    );
  }
  const paymentDate = optionalString(input?.paymentDate, 10);
  if (paymentDate != null && !PAYMENT_DATE_PATTERN.test(paymentDate)) {
    throw new SharedEventInputError("invalid-argument", "Payment date must use YYYY-MM-DD.");
  }
  const confidence = input?.confidence;
  if (!Number.isInteger(confidence) || confidence < 0 || confidence > 100) {
    throw new SharedEventInputError("invalid-argument", "Confidence must be an integer from 0 to 100.");
  }
  const paymentApp = optionalString(input?.paymentApp, EVIDENCE_TEXT_MAX_LENGTH);
  const counterparty = optionalString(input?.counterparty, EVIDENCE_TEXT_MAX_LENGTH);
  if (paymentReference == null && [paymentDate, paymentApp, counterparty].filter(Boolean).length < 2) {
    throw new SharedEventInputError(
      "invalid-argument",
      "A receipt without a payment reference needs at least two fallback evidence fields."
    );
  }
  return {
    actorUid,
    displayName,
    idempotencyKey: normalizeIdempotencyKey(input?.idempotencyKey),
    payload: {
      eventId,
      expectedRevision: input.expectedRevision,
      ledgerType,
      amountMinor: input.amountMinor,
      amountEvidenceSource,
      amountEvidenceConfidence,
      ledgerPersonUid,
      paymentApp,
      paymentDate,
      counterparty,
      paymentReference,
      confidence,
      warnings: normalizeWarnings(input?.warnings)
    }
  };
}

function assertReliableAmountEvidence(source, confidence, errorCode) {
  if (!RELIABLE_AMOUNT_EVIDENCE_SOURCES.has(source)
    || !Number.isInteger(confidence)
    || confidence < MIN_RELIABLE_AMOUNT_EVIDENCE
    || confidence > 100) {
    throw new SharedEventInputError(
      errorCode,
      "Amount must come from reliable receipt-derived evidence."
    );
  }
}

function normalizeReviewInput(actor, input) {
  const actorUid = requiredString(actor?.uid, "unauthenticated", "An authenticated account is required.", 128);
  const decision = requiredString(input?.decision, "invalid-argument", "Review decision is required.", 16);
  if (decision !== "confirm" && decision !== "reject") {
    throw new SharedEventInputError("invalid-argument", "Review decision must be confirm or reject.");
  }
  if (!Number.isSafeInteger(input?.expectedRevision) || input.expectedRevision <= 0) {
    throw new SharedEventInputError("invalid-argument", "Expected event revision is required.");
  }
  const reason = optionalString(input?.reason, REVIEW_REASON_MAX_LENGTH);
  if (decision === "reject" && reason == null) {
    throw new SharedEventInputError("invalid-argument", "A rejection reason is required.");
  }
  return {
    actorUid,
    idempotencyKey: normalizeIdempotencyKey(input?.idempotencyKey),
    payload: {
      eventId: normalizeDocumentId(input?.eventId, EVENT_ID_PATTERN, "Event ID is invalid."),
      entryId: normalizeDocumentId(input?.entryId, ENTRY_ID_PATTERN, "Entry ID is invalid."),
      expectedRevision: input.expectedRevision,
      decision,
      reason
    }
  };
}

function assertActiveEvent(snapshot) {
  if (!snapshot.exists || snapshot.data().status !== "active") {
    throw new SharedEventInputError("not-found", "Active shared event was not found.");
  }
}

function assertExpectedRevision(event, expectedRevision) {
  if (event.revision !== expectedRevision) {
    throw new SharedEventInputError("aborted", "Event changed; refresh before retrying this operation.");
  }
}

function assertActiveMembership(snapshot, expectedUid) {
  if (!snapshot.exists
    || snapshot.data().uid !== expectedUid
    || snapshot.data().status !== "active") {
    throw new SharedEventInputError("permission-denied", "Active event membership is required.");
  }
  return snapshot.data();
}

function confirmedEventUpdates(event, ledgerType, amountMinor, revision, timestamp) {
  const collected = ledgerType === "Donated" || ledgerType === "Credit";
  const totalCollectedMinor = safeAdd(event.totalCollectedMinor, collected ? amountMinor : 0);
  const totalSpentMinor = safeAdd(event.totalSpentMinor, collected ? 0 : amountMinor);
  safeSubtract(totalCollectedMinor, totalSpentMinor);
  return {
    revision,
    totalCollectedMinor,
    totalSpentMinor,
    confirmedReceiptCount: safeAdd(event.confirmedReceiptCount, 1),
    updatedAtServer: timestamp
  };
}

function confirmedMemberUpdates(member, ledgerType, amountMinor) {
  const collected = ledgerType === "Donated" || ledgerType === "Credit";
  return {
    confirmedReceiptCount: safeAdd(member.confirmedReceiptCount, 1),
    confirmedMoneyInMinor: safeAdd(member.confirmedMoneyInMinor, collected ? amountMinor : 0),
    confirmedMoneyOutMinor: safeAdd(member.confirmedMoneyOutMinor, collected ? 0 : amountMinor)
  };
}

function safeAdd(left, right) {
  if (!Number.isSafeInteger(left) || !Number.isSafeInteger(right) || !Number.isSafeInteger(left + right)) {
    throw new SharedEventInputError("resource-exhausted", "Ledger aggregate exceeds the supported integer range.");
  }
  return left + right;
}

function safeSubtract(left, right) {
  if (!Number.isSafeInteger(left - right)) {
    throw new SharedEventInputError("resource-exhausted", "Ledger balance exceeds the supported integer range.");
  }
  return left - right;
}

function normalizeWarnings(value) {
  if (!Array.isArray(value) || value.length > WARNING_MAX_COUNT) {
    throw new SharedEventInputError("invalid-argument", "Warnings must be a bounded array.");
  }
  return value.map((warning) => requiredString(
    warning,
    "invalid-argument",
    "Warnings must contain bounded text values.",
    WARNING_MAX_LENGTH
  ));
}

function normalizePaymentReference(value) {
  return value.trim().toUpperCase().replace(/[^A-Z0-9]/g, "");
}

function duplicateKeyIdsForPayload(payload, duplicateHashKey) {
  if (payload.paymentReference != null) {
    return [hmacHex(
      duplicateHashKey,
      `strong:${payload.eventId}:${normalizePaymentReference(payload.paymentReference)}`
    )];
  }

  const fallbackFields = [
    ["date", payload.paymentDate],
    ["app", normalizeFallbackText(payload.paymentApp)],
    ["counterparty", normalizeFallbackText(payload.counterparty)]
  ].filter(([, value]) => value != null);
  const keys = [];
  for (let left = 0; left < fallbackFields.length; left++) {
    for (let right = left + 1; right < fallbackFields.length; right++) {
      const [leftName, leftValue] = fallbackFields[left];
      const [rightName, rightValue] = fallbackFields[right];
      keys.push(hmacHex(
        duplicateHashKey,
        `fallback:${payload.eventId}:${payload.amountMinor}:${leftName}:${leftValue}:${rightName}:${rightValue}`
      ));
    }
  }
  return keys.sort();
}

function normalizeFallbackText(value) {
  if (value == null) return null;
  return value.trim().toUpperCase().replace(/[^A-Z0-9]+/g, " ").trim();
}

function normalizeDocumentId(value, pattern, message) {
  const normalized = requiredString(value, "invalid-argument", message, 20);
  if (!pattern.test(normalized)) throw new SharedEventInputError("invalid-argument", message);
  return normalized;
}

function normalizeIdempotencyKey(value) {
  const normalized = requiredString(value, "invalid-argument", "A valid idempotency key is required.", 32).toLowerCase();
  if (!IDEMPOTENCY_KEY_PATTERN.test(normalized)) {
    throw new SharedEventInputError("invalid-argument", "Idempotency key must be 32 lowercase hexadecimal characters.");
  }
  return normalized;
}

function requiredString(value, code, message, maxLength) {
  if (typeof value !== "string") throw new SharedEventInputError(code, message);
  const normalized = value.trim();
  if (!normalized || normalized.length > maxLength) throw new SharedEventInputError(code, message);
  return normalized;
}

function optionalString(value, maxLength) {
  if (value == null) return null;
  if (typeof value !== "string") {
    throw new SharedEventInputError("invalid-argument", "Evidence text must contain text values.");
  }
  const normalized = value.trim();
  if (normalized.length > maxLength) {
    throw new SharedEventInputError("invalid-argument", "Evidence text exceeds its supported length.");
  }
  return normalized || null;
}

function assertIdempotentOperation(data, operationType, payloadHash) {
  if (data.operationType !== operationType || data.payloadHash !== payloadHash) {
    throw new SharedEventInputError("idempotency-conflict", "This operation key was already used for different data.");
  }
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

function sha256(value) {
  return createHash("sha256").update(value, "utf8").digest("hex");
}
