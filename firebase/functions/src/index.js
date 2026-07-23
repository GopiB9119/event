import { getApps, initializeApp } from "firebase-admin/app";
import { getDatabaseWithUrl } from "firebase-admin/database";
import { getFirestore } from "firebase-admin/firestore";
import { defineSecret, defineString, select } from "firebase-functions/params";
import { setGlobalOptions } from "firebase-functions/v2";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { onDocumentWritten } from "firebase-functions/v2/firestore";
import { createEventForActor, SharedEventInputError } from "./sharedEvents.js";
import {
  acceptInviteForActor,
  createInviteForActor,
  joinPublicEventForActor
} from "./sharedMembership.js";
import { reviewPendingEntryForActor, submitReviewedEntryForActor } from "./sharedEntries.js";
import {
  presenceDatabaseUrl,
  refreshPresenceAccessForActor,
  syncPresenceAccessFromCurrentMembership
} from "./sharedPresence.js";

const functionRegion = defineString("FUNCTIONS_REGION", {
  label: "Permanent Cloud Functions region",
  description: "Select the owner-approved permanent region. Changing this later creates migration and data-residency work.",
  input: select({
    "Mumbai, India (asia-south1)": "asia-south1",
    "Singapore (asia-southeast1)": "asia-southeast1",
    "Belgium (europe-west1)": "europe-west1",
    "London (europe-west2)": "europe-west2",
    "Iowa, US (us-central1)": "us-central1",
    "South Carolina, US (us-east1)": "us-east1"
  })
});
const inviteTokenSecret = defineSecret("INVITE_TOKEN_KEY", {
  description: "At least 32 random bytes used only to derive private invite tokens."
});
const duplicateHashSecret = defineSecret("DUPLICATE_HASH_KEY", {
  description: "At least 32 random bytes used only to reserve receipt duplicate hashes."
});

setGlobalOptions({ region: functionRegion });

if (getApps().length === 0) {
  initializeApp();
}

export const createSharedEvent = onCall(
  {
    enforceAppCheck: true
  },
  async (request) => {
    if (!request.auth) {
      throw new HttpsError("unauthenticated", "An authenticated account is required.");
    }

    try {
      return await createEventForActor(
        getFirestore(),
        {
          uid: request.auth.uid,
          displayName: request.auth.token.name || "Organizer"
        },
        request.data
      );
    } catch (error) {
      if (error instanceof SharedEventInputError) {
        const callableCode = toCallableErrorCode(error.code);
        throw new HttpsError(callableCode, error.message);
      }
      throw error;
    }
  }
);

export const createSharedInvite = onCall(
  { enforceAppCheck: true, secrets: [inviteTokenSecret] },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    createInviteForActor(getFirestore(), actor, request.data, inviteTokenKey())
  )
);

export const acceptSharedInvite = onCall(
  { enforceAppCheck: true },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    acceptInviteForActor(getFirestore(), actor, request.data)
  )
);

export const joinPublicSharedEvent = onCall(
  { enforceAppCheck: true },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    joinPublicEventForActor(getFirestore(), actor, request.data)
  )
);

export const submitSharedEntry = onCall(
  { enforceAppCheck: true, secrets: [duplicateHashSecret] },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    submitReviewedEntryForActor(getFirestore(), actor, request.data, duplicateHashKey())
  )
);

export const reviewSharedEntry = onCall(
  { enforceAppCheck: true },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    reviewPendingEntryForActor(getFirestore(), actor, request.data)
  )
);

export const refreshSharedPresence = onCall(
  { enforceAppCheck: true },
  async (request) => runAuthenticatedMutation(request, (actor) =>
    refreshPresenceAccessForActor(
      getFirestore(),
      getDatabaseWithUrl(presenceDatabaseUrl()),
      actor,
      request.data?.eventId
    )
  )
);

export const syncSharedPresenceAccess = onDocumentWritten(
  "events/{eventId}/members/{uid}",
  async (event) => {
    await syncPresenceAccessFromCurrentMembership(
      getFirestore(),
      getDatabaseWithUrl(presenceDatabaseUrl()),
      event.params.eventId,
      event.params.uid
    );
  }
);

async function runAuthenticatedMutation(request, mutation) {
  if (!request.auth) {
    throw new HttpsError("unauthenticated", "An authenticated account is required.");
  }
  try {
    return await mutation({
      uid: request.auth.uid,
      displayName: request.auth.token.name || "Member"
    });
  } catch (error) {
    if (error instanceof SharedEventInputError) {
      throw new HttpsError(toCallableErrorCode(error.code), error.message);
    }
    throw error;
  }
}

function inviteTokenKey() {
  return requireSecretKey(inviteTokenSecret.value(), "Invite token key");
}

function duplicateHashKey() {
  return requireSecretKey(duplicateHashSecret.value(), "Duplicate hash key");
}

function requireSecretKey(value, label) {
  if (Buffer.byteLength(value, "utf8") < 32) {
    throw new HttpsError("failed-precondition", `${label} must contain at least 32 bytes.`);
  }
  return value;
}

function toCallableErrorCode(code) {
  switch (code) {
    case "unauthenticated":
    case "invalid-argument":
    case "failed-precondition":
    case "permission-denied":
    case "not-found":
    case "deadline-exceeded":
    case "already-exists":
    case "aborted":
    case "resource-exhausted":
      return code;
    case "invalid-identity":
      return "failed-precondition";
    case "idempotency-conflict":
      return "already-exists";
    default:
      return "internal";
  }
}