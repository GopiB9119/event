import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import { after, before, beforeEach, describe, test } from "node:test";
import { deleteApp, initializeApp } from "firebase-admin/app";
import { getDatabaseWithUrl } from "firebase-admin/database";
import { getFirestore } from "firebase-admin/firestore";
import {
  assertFails,
  assertSucceeds,
  initializeTestEnvironment
} from "@firebase/rules-unit-testing";
import {
  presenceDatabaseUrl,
  refreshPresenceAccessForActor,
  syncPresenceAccessFromCurrentMembership,
  syncPresenceAccessForMembership
} from "../src/sharedPresence.js";

const PROJECT_ID = "demo-community-ledger";
const MEMBER_UID = "presence-member";
const OTHER_UID = "presence-other";
const DATABASE_URL = `http://127.0.0.1:9000?ns=${PROJECT_ID}-default-rtdb`;
const DATABASE_RULES_PATH = new URL("../../database.rules.json", import.meta.url);
const FIRESTORE_RULES_PATH = new URL("../../firestore.rules", import.meta.url);
const FUNCTIONS_BASE_URL = `http://127.0.0.1:5001/${PROJECT_ID}/us-central1`;

let testEnvironment;
let adminApp;
let adminDatabase;
let adminFirestore;
let currentEventId;
let eventSequence = 0;

before(async () => {
  if (!process.env.FIRESTORE_EMULATOR_HOST) {
    throw new Error("Run presence tests through the complete Firebase emulator suite.");
  }
  testEnvironment = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    database: {
      host: "127.0.0.1",
      port: 9000,
      rules: await readFile(DATABASE_RULES_PATH, "utf8")
    },
    firestore: {
      rules: await readFile(FIRESTORE_RULES_PATH, "utf8")
    }
  });
  adminApp = initializeApp({ projectId: PROJECT_ID }, "shared-presence-emulator-tests");
  adminDatabase = getDatabaseWithUrl(DATABASE_URL, adminApp);
  adminFirestore = getFirestore(adminApp);
});

beforeEach(async () => {
  eventSequence += 1;
  currentEventId = `presence-event-${eventSequence}`;
  await testEnvironment.clearDatabase();
  await testEnvironment.clearFirestore();
});

after(async () => {
  if (testEnvironment) await testEnvironment.cleanup();
  if (adminApp) await deleteApp(adminApp);
});

describe("member-only approximate presence", () => {
  beforeEach(async () => {
    await seedAccess(MEMBER_UID);
  });

  test("active member reads event presence while outsiders and anonymous users fail", async () => {
    await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set(validPresence("active"));
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);
    const otherDatabase = testEnvironment.authenticatedContext(OTHER_UID).database(DATABASE_URL);
    const anonymousDatabase = testEnvironment.unauthenticatedContext().database(DATABASE_URL);

    const target = `presence/${currentEventId}/${MEMBER_UID}`;
    const snapshot = await assertSucceeds(memberDatabase.ref(target).once("value"));
    await assertFails(otherDatabase.ref(target).once("value"));
    await assertFails(anonymousDatabase.ref(target).once("value"));
    assert.equal(snapshot.val().state, "active");
  });

  test("reader sees another member only while the target lease is active", async () => {
    await seedAccess(OTHER_UID);
    await adminDatabase.ref(`presence/${currentEventId}/${OTHER_UID}`).set(validPresence("active"));
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);
    const target = memberDatabase.ref(`presence/${currentEventId}/${OTHER_UID}`);

    await assertSucceeds(target.once("value"));
    await adminDatabase.ref(`eventAccess/${currentEventId}/${OTHER_UID}`).update({ expiresAt: 0 });
    await assertFails(target.once("value"));
  });

  test("member writes only their own bounded presence record", async () => {
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);

    await assertSucceeds(
      memberDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set(validPresence("active"))
    );
    await assertFails(
      memberDatabase.ref(`presence/${currentEventId}/${OTHER_UID}`).set(validPresence("active"))
    );
  });

  test("invalid state, extra fields, and stale timestamps fail", async () => {
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);
    const reference = memberDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`);

    await assertFails(reference.set(validPresence("online")));
    await assertFails(reference.set({ ...validPresence("active"), preciseLastSeen: Date.now() }));
    await assertFails(reference.set({ state: "active", updatedAt: Date.now() - 120000 }));
  });

  test("client cannot grant itself event access", async () => {
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);

    await assertFails(memberDatabase.ref(`eventAccess/${currentEventId}/${OTHER_UID}`).set(true));
  });

  test("expired access lease and stale presence fail closed", async () => {
    await adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`).set({
      generation: 1,
      revoked: false,
      expiresAt: Date.now() - 1
    });
    await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set({
      state: "active",
      updatedAt: Date.now() - 180000
    });
    const memberDatabase = testEnvironment.authenticatedContext(MEMBER_UID).database(DATABASE_URL);

    await assertFails(memberDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).once("value"));
    await assertFails(
      memberDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set(validPresence("active"))
    );
  });
});

describe("membership access mirror", () => {
  test("helper grants active role and revocation removes access plus stale presence", async () => {
    await syncPresenceAccessForMembership(adminDatabase, currentEventId, MEMBER_UID, {
      uid: MEMBER_UID,
      role: "contributor",
      status: "active",
      presenceGeneration: 1
    });
    await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set(validPresence("active"));

    const granted = (await adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`).once("value")).val();
    assert.ok(granted.expiresAt > Date.now());

    await syncPresenceAccessForMembership(adminDatabase, currentEventId, MEMBER_UID, {
      uid: MEMBER_UID,
      role: "contributor",
      status: "revoked",
      presenceGeneration: 2
    });

    assert.deepEqual(
      (await adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`).once("value")).val(),
      { generation: 2, revoked: true, expiresAt: 0 }
    );
    assert.equal((await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).once("value")).val(), null);
  });

  test("delayed active generation cannot overwrite newer revocation tombstone", async () => {
    await syncPresenceAccessForMembership(adminDatabase, currentEventId, MEMBER_UID, {
      uid: MEMBER_UID,
      role: "viewer",
      status: "active",
      presenceGeneration: 1
    });
    await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).set(validPresence("active"));
    await syncPresenceAccessForMembership(adminDatabase, currentEventId, MEMBER_UID, {
      uid: MEMBER_UID,
      role: "viewer",
      status: "revoked",
      presenceGeneration: 2
    });

    const staleResult = await syncPresenceAccessForMembership(adminDatabase, currentEventId, MEMBER_UID, {
      uid: MEMBER_UID,
      role: "viewer",
      status: "active",
      presenceGeneration: 1
    });

    assert.equal(staleResult, null);
    assert.deepEqual(
      (await adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`).once("value")).val(),
      { generation: 2, revoked: true, expiresAt: 0 }
    );
    assert.equal((await adminDatabase.ref(`presence/${currentEventId}/${MEMBER_UID}`).once("value")).val(), null);
  });

  test("Firestore membership trigger grants and revokes RTDB access", async () => {
    const membershipRef = adminFirestore
      .collection("events")
      .doc(currentEventId)
      .collection("members")
      .doc(MEMBER_UID);

    await membershipRef.set({
      uid: MEMBER_UID,
      role: "viewer",
      status: "active",
      presenceGeneration: 1
    });
    await waitForActiveLease(adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`));

    await membershipRef.update({ status: "revoked", presenceGeneration: 2 });
    await waitForRevokedTombstone(adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`), 2);
  });

  test("current membership recheck cannot restore a revoked lease from stale delivery", async () => {
    const membershipRef = adminFirestore
      .collection("events")
      .doc(currentEventId)
      .collection("members")
      .doc(MEMBER_UID);
    await membershipRef.set({
      uid: MEMBER_UID,
      role: "viewer",
      status: "active",
      presenceGeneration: 1
    });
    await syncPresenceAccessFromCurrentMembership(
      adminFirestore,
      adminDatabase,
      currentEventId,
      MEMBER_UID
    );
    await membershipRef.update({ status: "revoked", presenceGeneration: 2 });
    await syncPresenceAccessFromCurrentMembership(
      adminFirestore,
      adminDatabase,
      currentEventId,
      MEMBER_UID
    );
    await syncPresenceAccessFromCurrentMembership(
      adminFirestore,
      adminDatabase,
      currentEventId,
      MEMBER_UID
    );

    assert.deepEqual(
      (await adminDatabase.ref(`eventAccess/${currentEventId}/${MEMBER_UID}`).once("value")).val(),
      { generation: 2, revoked: true, expiresAt: 0 }
    );
  });

  test("refresh helper and callable recheck Firestore membership", async () => {
    const membershipRef = adminFirestore
      .collection("events")
      .doc(currentEventId)
      .collection("members")
      .doc(MEMBER_UID);
    await membershipRef.set({
      uid: MEMBER_UID,
      role: "contributor",
      status: "active",
      presenceGeneration: 1
    });

    const direct = await refreshPresenceAccessForActor(
      adminFirestore,
      adminDatabase,
      { uid: MEMBER_UID },
      currentEventId
    );
    assert.ok(direct.expiresAt > Date.now());

    const callable = await callFunction("refreshSharedPresence", { eventId: currentEventId }, {
      authToken: emulatorJwt(MEMBER_UID),
      appCheckToken: emulatorJwt("demo-app")
    });
    assert.equal(callable.status, 200, JSON.stringify(callable.body));
    assert.ok(callable.body.result.expiresAt > Date.now());

    await membershipRef.update({ status: "revoked", presenceGeneration: 2 });
    await assert.rejects(
      refreshPresenceAccessForActor(
        adminFirestore,
        adminDatabase,
        { uid: MEMBER_UID },
        currentEventId
      ),
      (error) => error.code === "permission-denied"
    );
  });

  test("production presence URL cannot be guessed", () => {
    assert.throws(() => presenceDatabaseUrl({}), /not configured/);
    assert.equal(
      presenceDatabaseUrl({
        FUNCTIONS_EMULATOR: "true",
        FIREBASE_DATABASE_EMULATOR_HOST: "127.0.0.1:9000",
        GCLOUD_PROJECT: PROJECT_ID
      }),
      DATABASE_URL
    );
    assert.throws(
      () => presenceDatabaseUrl({
        FUNCTIONS_EMULATOR: "true",
        FIREBASE_DATABASE_EMULATOR_HOST: "127.0.0.1:9000",
        GCLOUD_PROJECT: PROJECT_ID,
        PRESENCE_DATABASE_URL: "https://project.asia-south1.firebasedatabase.app"
      }),
      /cannot use a configured production database URL/
    );
    assert.throws(
      () => presenceDatabaseUrl({
        FUNCTIONS_EMULATOR: "true",
        FIREBASE_DATABASE_EMULATOR_HOST: "127.0.0.1:9000",
        GCLOUD_PROJECT: "production-project"
      }),
      /isolated demo-community-ledger project/
    );
    assert.equal(
      presenceDatabaseUrl({
        PRESENCE_DATABASE_URL: "https://community-ledger-prod.asia-south1.firebasedatabase.app/"
      }),
      "https://community-ledger-prod.asia-south1.firebasedatabase.app"
    );
    assert.throws(
      () => presenceDatabaseUrl({ PRESENCE_DATABASE_URL: "http://example.com" }),
      /root HTTPS Firebase Realtime Database URL/
    );
  });
});

async function seedAccess(uid) {
  await testEnvironment.withSecurityRulesDisabled(async (context) => {
    await context.database(DATABASE_URL).ref(`eventAccess/${currentEventId}/${uid}`).set({
      generation: 1,
      revoked: false,
      expiresAt: Date.now() + 120000
    });
  });
}

function validPresence(state) {
  return {
    state,
    updatedAt: Date.now()
  };
}

async function waitForValue(reference, expected, timeoutMillis = 10000) {
  const deadline = Date.now() + timeoutMillis;
  while (Date.now() < deadline) {
    const value = (await reference.once("value")).val();
    if (value === expected) return;
    await new Promise((resolve) => setTimeout(resolve, 50));
  }
  assert.fail(`Timed out waiting for ${reference.toString()} to become ${expected}.`);
}

async function waitForActiveLease(reference, timeoutMillis = 10000) {
  const deadline = Date.now() + timeoutMillis;
  while (Date.now() < deadline) {
    const value = (await reference.once("value")).val();
    if (value?.expiresAt > Date.now()) return;
    await new Promise((resolve) => setTimeout(resolve, 50));
  }
  assert.fail(`Timed out waiting for an active lease at ${reference.toString()}.`);
}

async function waitForRevokedTombstone(reference, generation, timeoutMillis = 10000) {
  const deadline = Date.now() + timeoutMillis;
  while (Date.now() < deadline) {
    const value = (await reference.once("value")).val();
    if (value?.revoked === true && value?.generation === generation && value?.expiresAt === 0) return;
    await new Promise((resolve) => setTimeout(resolve, 50));
  }
  assert.fail(`Timed out waiting for a generation ${generation} revocation tombstone at ${reference.toString()}.`);
}

async function callFunction(functionName, data, { authToken, appCheckToken }) {
  const headers = { "Content-Type": "application/json" };
  if (authToken) headers.Authorization = `Bearer ${authToken}`;
  if (appCheckToken) headers["X-Firebase-AppCheck"] = appCheckToken;
  const response = await fetch(`${FUNCTIONS_BASE_URL}/${functionName}`, {
    method: "POST",
    headers,
    body: JSON.stringify({ data })
  });
  return {
    status: response.status,
    body: await response.json()
  };
}

function emulatorJwt(subject) {
  const header = Buffer.from(JSON.stringify({ alg: "none", typ: "JWT" })).toString("base64url");
  const payload = Buffer.from(JSON.stringify({
    sub: subject,
    iat: Math.floor(Date.now() / 1000),
    exp: Math.floor(Date.now() / 1000) + 3600
  })).toString("base64url");
  return `${header}.${payload}.emulator-signature`;
}
