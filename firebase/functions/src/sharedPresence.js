import { SharedEventInputError } from "./sharedEvents.js";

const PRESENCE_ACCESS_LEASE_MILLIS = 120000;

export async function syncPresenceAccessForMembership(
  database,
  eventId,
  uid,
  membership,
  nowMillis = Date.now()
) {
  if (!eventId || !uid) {
    throw new Error("Presence access mirror requires event and account identifiers.");
  }
  const accessRef = database.ref(`eventAccess/${eventId}/${uid}`);
  const activeMembership = membership != null
    && membership.uid === uid
    && membership.status === "active"
    && ["organizer", "contributor", "viewer"].includes(membership.role)
    && Number.isSafeInteger(membership.presenceGeneration)
    && membership.presenceGeneration > 0;
  const suppliedGeneration = Number.isSafeInteger(membership?.presenceGeneration)
    && membership.presenceGeneration > 0
    ? membership.presenceGeneration
    : null;
  const expiresAt = activeMembership ? nowMillis + PRESENCE_ACCESS_LEASE_MILLIS : 0;

  const result = await accessRef.transaction((current) => {
    const currentGeneration = Number.isSafeInteger(current?.generation)
      && current.generation > 0
      ? current.generation
      : 0;
    const nextGeneration = suppliedGeneration ?? currentGeneration + 1;
    if (nextGeneration < currentGeneration) return;
    if (nextGeneration === currentGeneration && current?.revoked === true && activeMembership) {
      return;
    }
    return activeMembership
      ? { generation: nextGeneration, revoked: false, expiresAt }
      : { generation: nextGeneration, revoked: true, expiresAt: 0 };
  }, undefined, false);
  const mirrored = result.snapshot.val();
  if (mirrored?.revoked === true) {
    await database.ref(`presence/${eventId}/${uid}`).remove();
    return null;
  }
  return mirrored?.expiresAt ?? null;
}

export async function refreshPresenceAccessForActor(
  firestore,
  database,
  actor,
  eventId,
  nowMillis = Date.now()
) {
  if (typeof actor?.uid !== "string" || !actor.uid || typeof eventId !== "string" || !eventId) {
    throw new SharedEventInputError("invalid-argument", "Event and account identifiers are required.");
  }
  const membershipSnapshot = await firestore
    .collection("events")
    .doc(eventId)
    .collection("members")
    .doc(actor.uid)
    .get();
  const membership = membershipSnapshot.exists ? membershipSnapshot.data() : null;
  const active = membership != null
    && membership.uid === actor.uid
    && membership.status === "active"
    && ["organizer", "contributor", "viewer"].includes(membership.role);
  if (!active) {
    await syncPresenceAccessForMembership(database, eventId, actor.uid, null, nowMillis);
    throw new SharedEventInputError("permission-denied", "Active event membership is required.");
  }
  const expiresAt = await syncPresenceAccessForMembership(
    database,
    eventId,
    actor.uid,
    membership,
    nowMillis
  );
  if (expiresAt == null) {
    throw new SharedEventInputError("permission-denied", "Presence access was superseded by a newer membership state.");
  }
  return { eventId, expiresAt };
}

export async function syncPresenceAccessFromCurrentMembership(
  firestore,
  database,
  eventId,
  uid,
  nowMillis = Date.now()
) {
  if (!eventId || !uid) {
    throw new Error("Presence access mirror requires event and account identifiers.");
  }
  const membershipSnapshot = await firestore
    .collection("events")
    .doc(eventId)
    .collection("members")
    .doc(uid)
    .get();
  return syncPresenceAccessForMembership(
    database,
    eventId,
    uid,
    membershipSnapshot.exists ? membershipSnapshot.data() : null,
    nowMillis
  );
}

export function presenceDatabaseUrl(environment = process.env) {
  if (environment.FUNCTIONS_EMULATOR === "true") {
    const projectId = environment.GCLOUD_PROJECT || environment.GOOGLE_CLOUD_PROJECT;
    const emulatorHost = environment.FIREBASE_DATABASE_EMULATOR_HOST;
    if (projectId !== TEST_PROJECT_ID) {
      throw new Error(`Presence emulator requires the isolated ${TEST_PROJECT_ID} project.`);
    }
    if (!TEST_EMULATOR_HOSTS.has(emulatorHost)) {
      throw new Error("Presence emulator requires the local Realtime Database emulator host.");
    }
    const emulatorUrl = `http://${emulatorHost}?ns=${projectId}-default-rtdb`;
    if (environment.PRESENCE_DATABASE_URL && environment.PRESENCE_DATABASE_URL !== emulatorUrl) {
      throw new Error("Presence emulator cannot use a configured production database URL.");
    }
    return emulatorUrl;
  }

  const configuredUrl = environment.PRESENCE_DATABASE_URL;
  if (!configuredUrl) throw new Error("Presence database URL is not configured.");
  let parsed;
  try {
    parsed = new URL(configuredUrl);
  } catch (error) {
    throw new Error("Presence database URL is invalid.", { cause: error });
  }
  const trustedHost = parsed.hostname.endsWith(".firebasedatabase.app")
    || parsed.hostname.endsWith(".firebaseio.com");
  if (
    parsed.protocol !== "https:"
    || !trustedHost
    || parsed.username
    || parsed.password
    || parsed.port
    || parsed.search
    || parsed.hash
    || (parsed.pathname !== "" && parsed.pathname !== "/")
  ) {
    throw new Error("Presence database URL must be a root HTTPS Firebase Realtime Database URL.");
  }
  return configuredUrl.replace(/\/$/u, "");
}

const TEST_PROJECT_ID = "demo-community-ledger";
const TEST_EMULATOR_HOSTS = new Set(["127.0.0.1:9000", "localhost:9000"]);
