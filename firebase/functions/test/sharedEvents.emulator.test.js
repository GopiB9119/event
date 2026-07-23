import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import { after, before, beforeEach, describe, test } from "node:test";
import { deleteApp, initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import {
  assertFails,
  assertSucceeds,
  initializeTestEnvironment
} from "@firebase/rules-unit-testing";
import { createEventForActor, SharedEventInputError } from "../src/sharedEvents.js";
import {
  acceptInviteForActor,
  createInviteForActor,
  joinPublicEventForActor
} from "../src/sharedMembership.js";

const PROJECT_ID = "demo-community-ledger";
const RULES_PATH = new URL("../../firestore.rules", import.meta.url);
const ORGANIZER_UID = "organizer-user";
const CONTRIBUTOR_UID = "contributor-user";
const REVOKED_UID = "revoked-user";
const NON_MEMBER_UID = "outside-user";
const VIEWER_UID = "viewer-user";
const MALFORMED_UID = "malformed-user";
const FUNCTIONS_URL = `http://127.0.0.1:5001/${PROJECT_ID}/us-central1/createSharedEvent`;
const AUTH_URL = "http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=demo-key";
const INVITE_TOKEN_KEY = "emulator-membership-test-token-key";

let testEnvironment;
let adminApp;
let adminDatabase;

before(async () => {
  if (!process.env.FIRESTORE_EMULATOR_HOST) {
    throw new Error("Run tests through firebase emulators:exec so no production Firestore can be reached.");
  }
  testEnvironment = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules: await readFile(RULES_PATH, "utf8")
    }
  });
  adminApp = initializeApp({ projectId: PROJECT_ID }, "shared-events-emulator-tests");
  adminDatabase = getFirestore(adminApp);
});

beforeEach(async () => {
  await testEnvironment.clearFirestore();
});

after(async () => {
  if (testEnvironment) await testEnvironment.cleanup();
  if (adminApp) await deleteApp(adminApp);
});

describe("shared event creation", () => {
  test("creates event, organizer membership, audit, and operation atomically", async () => {
    const result = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );

    const eventSnapshot = await adminDatabase.collection("events").doc(result.eventId).get();
    const membershipSnapshot = await eventSnapshot.ref.collection("members").doc(ORGANIZER_UID).get();
    const auditSnapshot = await eventSnapshot.ref.collection("audit").get();
    const operationSnapshot = await adminDatabase
      .collection("users")
      .doc(ORGANIZER_UID)
      .collection("operations")
      .doc(validCreateInput().idempotencyKey)
      .get();

    assert.equal(eventSnapshot.data().ownerUid, ORGANIZER_UID);
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
    assert.equal(eventSnapshot.data().activeMemberCount, 1);
    assert.equal(membershipSnapshot.data().role, "organizer");
    assert.equal(membershipSnapshot.data().status, "active");
    assert.equal(auditSnapshot.size, 1);
    assert.equal(operationSnapshot.data().result.eventId, result.eventId);
  });

  test("same idempotency key and payload return the original event", async () => {
    const first = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );
    const second = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );

    assert.deepEqual(second, first);
    assert.equal((await adminDatabase.collection("events").get()).size, 1);
  });

  test("same idempotency key with changed payload fails closed", async () => {
    await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );

    await assert.rejects(
      createEventForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Test Organizer" },
        { ...validCreateInput(), title: "Changed Event" }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "idempotency-conflict"
    );
    assert.equal((await adminDatabase.collection("events").get()).size, 1);
  });

  test("unsafe custom keys fail before mutation", async () => {
    await assert.rejects(
      createEventForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Test Organizer" },
        { ...validCreateInput(), customInfo: { " venue ": "A", venue: "B" } }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "invalid-argument"
    );
    await assert.rejects(
      createEventForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Test Organizer" },
        { ...validCreateInput(), customInfo: { constructor: "blocked" } }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "invalid-argument"
    );
    assert.equal((await adminDatabase.collection("events").get()).size, 0);
  });
});

describe("callable authentication and App Check boundary", () => {
  test("missing authentication is rejected", async () => {
    const response = await callCreateSharedEvent({ appCheckToken: emulatorJwt("demo-app") });

    assert.equal(response.status, 401);
    assert.equal(response.body.error.status, "UNAUTHENTICATED");
  });

  test("missing App Check is rejected even with Auth emulator identity", async () => {
    const authToken = await createAuthEmulatorUser("missing-app-check@example.com");
    const response = await callCreateSharedEvent({ authToken });

    assert.equal(response.status, 401);
    assert.equal(response.body.error.status, "UNAUTHENTICATED");
  });

  test("authenticated callable with App Check creates one idempotent event", async () => {
    const authToken = await createAuthEmulatorUser("callable-organizer@example.com");
    const appCheckToken = emulatorJwt("demo-app");
    const first = await callCreateSharedEvent({ authToken, appCheckToken });
    const second = await callCreateSharedEvent({ authToken, appCheckToken });

    assert.equal(first.status, 200, JSON.stringify(first.body));
    assert.deepEqual(second.body.result, first.body.result);
    assert.equal((await adminDatabase.collection("events").get()).size, 1);
  });

  test("invalid account profile maps to a valid callable error", async () => {
    const authToken = emulatorJwt("profile-user", { name: "x".repeat(81) });
    const response = await callCreateSharedEvent({
      authToken,
      appCheckToken: emulatorJwt("demo-app")
    });

    assert.equal(response.status, 400);
    assert.equal(response.body.error.status, "FAILED_PRECONDITION");
    assert.equal((await adminDatabase.collection("events").get()).size, 0);
  });

  test("callable organizer creates invite and authenticated recipient accepts it", async () => {
    const appCheckToken = emulatorJwt("demo-app");
    const organizerToken = await createAuthEmulatorUser("invite-organizer@example.com");
    const recipientToken = await createAuthEmulatorUser("invite-recipient@example.com");
    const created = await callFunction("createSharedEvent", validCreateInput(), {
      authToken: organizerToken,
      appCheckToken
    });
    assert.equal(created.status, 200, JSON.stringify(created.body));

    const invitation = await callFunction(
      "createSharedInvite",
      validInviteInput(created.body.result.eventId),
      { authToken: organizerToken, appCheckToken }
    );
    assert.equal(invitation.status, 200, JSON.stringify(invitation.body));

    const accepted = await callFunction(
      "acceptSharedInvite",
      {
        idempotencyKey: "22222222222222222222222222222222",
        inviteToken: invitation.body.result.inviteToken
      },
      { authToken: recipientToken, appCheckToken }
    );

    assert.equal(accepted.status, 200, JSON.stringify(accepted.body));
    assert.equal(accepted.body.result.role, "contributor");
    const eventSnapshot = await adminDatabase.collection("events").doc(created.body.result.eventId).get();
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
  });

  test("callable public event requires explicit authenticated join", async () => {
    const appCheckToken = emulatorJwt("demo-app");
    const organizerToken = await createAuthEmulatorUser("public-organizer@example.com");
    const viewerToken = await createAuthEmulatorUser("public-viewer@example.com");
    const created = await callFunction("createSharedEvent", validPublicCreateInput(), {
      authToken: organizerToken,
      appCheckToken
    });
    assert.equal(created.status, 200, JSON.stringify(created.body));

    const joined = await callFunction("joinPublicSharedEvent", {
      idempotencyKey: "55555555555555555555555555555555",
      eventId: created.body.result.eventId
    }, {
      authToken: viewerToken,
      appCheckToken
    });

    assert.equal(joined.status, 200, JSON.stringify(joined.body));
    assert.equal(joined.body.result.role, "viewer");
    const eventSnapshot = await adminDatabase.collection("events").doc(created.body.result.eventId).get();
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
  });
});

describe("private invite membership", () => {
  test("organizer creates hash-only invite and acceptance updates shared counts", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );
    const invitation = await createInviteForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      validInviteInput(event.eventId),
      INVITE_TOKEN_KEY
    );
    const accepted = await acceptInviteForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
      {
        idempotencyKey: "22222222222222222222222222222222",
        inviteToken: invitation.inviteToken
      }
    );

    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    const memberSnapshot = await eventSnapshot.ref.collection("members").doc(CONTRIBUTOR_UID).get();
    const inviteSnapshot = await adminDatabase.collection("invites").doc(invitation.inviteId).get();
    const activitySnapshot = await eventSnapshot.ref.collection("activity").get();
    const auditSnapshot = await eventSnapshot.ref.collection("audit").get();

    assert.equal(accepted.alreadyMember, false);
    assert.equal(accepted.role, "contributor");
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
    assert.equal(eventSnapshot.data().contributorCount, 1);
    assert.equal(memberSnapshot.data().status, "active");
    assert.equal(inviteSnapshot.data().status, "consumed");
    assert.equal(inviteSnapshot.data().useCount, 1);
    assert.equal(Object.hasOwn(inviteSnapshot.data(), "inviteToken"), false);
    assert.equal(Object.hasOwn(inviteSnapshot.data(), "secret"), false);
    assert.equal(activitySnapshot.size, 1);
    assert.equal(auditSnapshot.size, 3);

    const contributorDatabase = testEnvironment.authenticatedContext(CONTRIBUTOR_UID).firestore();
    await assertSucceeds(contributorDatabase.collection("events").doc(event.eventId).get());
  });

  test("accept retry cannot double-count membership", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );
    const invitation = await createInviteForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      validInviteInput(event.eventId),
      INVITE_TOKEN_KEY
    );
    const input = {
      idempotencyKey: "22222222222222222222222222222222",
      inviteToken: invitation.inviteToken
    };

    const first = await acceptInviteForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
      input
    );
    const second = await acceptInviteForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
      input
    );

    assert.deepEqual(second, first);
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
    assert.equal(eventSnapshot.data().contributorCount, 1);
  });

  test("invite acceptance into public event keeps public joined count synchronized", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validPublicCreateInput()
    );
    const invitation = await createInviteForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      validInviteInput(event.eventId),
      INVITE_TOKEN_KEY
    );
    const input = {
      idempotencyKey: "22222222222222222222222222222222",
      inviteToken: invitation.inviteToken
    };

    await acceptInviteForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
      input
    );
    await acceptInviteForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
      input
    );

    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    const publicSnapshot = await adminDatabase.collection("publicEvents").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
    assert.equal(publicSnapshot.data().activeMemberCount, 2);
  });

  test("non-organizer cannot create invite", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );
    await adminDatabase.collection("events").doc(event.eventId).collection("members").doc(CONTRIBUTOR_UID).set({
      uid: CONTRIBUTOR_UID,
      role: "contributor",
      status: "active"
    });

    await assert.rejects(
      createInviteForActor(
        adminDatabase,
        { uid: CONTRIBUTOR_UID },
        {
          ...validInviteInput(event.eventId),
          idempotencyKey: "33333333333333333333333333333333"
        },
        INVITE_TOKEN_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "permission-denied"
    );
  });

  test("wrong and expired invite tokens fail without membership", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );
    const invitation = await createInviteForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      validInviteInput(event.eventId),
      INVITE_TOKEN_KEY
    );
    const [inviteId] = invitation.inviteToken.split(".");
    const wrongToken = `${inviteId}.${"A".repeat(43)}`;

    await assert.rejects(
      acceptInviteForActor(
        adminDatabase,
        { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
        {
          idempotencyKey: "22222222222222222222222222222222",
          inviteToken: wrongToken
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "permission-denied"
    );

    await adminDatabase.collection("invites").doc(inviteId).update({
      expiresAtMillis: Date.now() - 1
    });
    await assert.rejects(
      acceptInviteForActor(
        adminDatabase,
        { uid: CONTRIBUTOR_UID, displayName: "Test Contributor" },
        {
          idempotencyKey: "44444444444444444444444444444444",
          inviteToken: invitation.inviteToken
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "deadline-exceeded"
    );
    const memberSnapshot = await adminDatabase
      .collection("events")
      .doc(event.eventId)
      .collection("members")
      .doc(CONTRIBUTOR_UID)
      .get();
    assert.equal(memberSnapshot.exists, false);
  });
});

describe("public profile and explicit join", () => {
  test("anonymous user reads only the strict public profile before joining", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validPublicCreateInput()
    );
    const unauthenticatedDatabase = testEnvironment.unauthenticatedContext().firestore();

    const profile = await assertSucceeds(
      unauthenticatedDatabase.collection("publicEvents").doc(event.eventId).get()
    );
    await assertFails(unauthenticatedDatabase.collection("events").doc(event.eventId).get());
    assert.deepEqual(
      Object.keys(profile.data()).sort(),
      [
        "activeMemberCount",
        "createdAtServer",
        "duration",
        "eventId",
        "status",
        "title",
        "updatedAtServer",
        "visibilityPolicy"
      ]
    );
  });

  test("Android public-event query matches the rules contract", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validPublicCreateInput()
    );
    const unauthenticatedDatabase = testEnvironment.unauthenticatedContext().firestore();

    const snapshot = await assertSucceeds(
      unauthenticatedDatabase
        .collection("publicEvents")
        .where("visibilityPolicy", "==", "public")
        .where("status", "==", "active")
        .limit(100)
        .get()
    );

    assert.equal(snapshot.docs.some((document) => document.id === event.eventId), true);
  });

  test("public profile with an extra field fails closed", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validPublicCreateInput()
    );
    await adminDatabase.collection("publicEvents").doc(event.eventId).update({
      leakedBalance: 50000
    });
    const unauthenticatedDatabase = testEnvironment.unauthenticatedContext().firestore();

    await assertFails(unauthenticatedDatabase.collection("publicEvents").doc(event.eventId).get());
  });

  test("explicit public join creates one idempotent viewer membership and shared count", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validPublicCreateInput()
    );
    const input = {
      idempotencyKey: "55555555555555555555555555555555",
      eventId: event.eventId
    };

    const first = await joinPublicEventForActor(
      adminDatabase,
      { uid: NON_MEMBER_UID, displayName: "Public Viewer" },
      input
    );
    const second = await joinPublicEventForActor(
      adminDatabase,
      { uid: NON_MEMBER_UID, displayName: "Public Viewer" },
      input
    );

    assert.deepEqual(second, first);
    assert.equal(first.role, "viewer");
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    const profileSnapshot = await adminDatabase.collection("publicEvents").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().activeMemberCount, 2);
    assert.equal(eventSnapshot.data().viewerCount, 1);
    assert.equal(profileSnapshot.data().activeMemberCount, 2);
    const viewerDatabase = testEnvironment.authenticatedContext(NON_MEMBER_UID).firestore();
    await assertSucceeds(viewerDatabase.collection("events").doc(event.eventId).get());
  });

  test("private event cannot be joined through the public path", async () => {
    const event = await createEventForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Test Organizer" },
      validCreateInput()
    );

    await assert.rejects(
      joinPublicEventForActor(
        adminDatabase,
        { uid: NON_MEMBER_UID, displayName: "Outside User" },
        {
          idempotencyKey: "55555555555555555555555555555555",
          eventId: event.eventId
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "not-found"
    );
  });
});

describe("shared event read authorization", () => {
  beforeEach(async () => {
    await seedAuthorizedEvent();
  });

  test("active organizer and contributor can read server projections", async () => {
    const organizerDatabase = testEnvironment.authenticatedContext(ORGANIZER_UID).firestore();
    const contributorDatabase = testEnvironment.authenticatedContext(CONTRIBUTOR_UID).firestore();

    await assertSucceeds(organizerDatabase.collection("events").doc("event-one").get());
    await assertSucceeds(contributorDatabase.collection("events").doc("event-one").get());
    await assertSucceeds(contributorDatabase.collection("events").doc("event-one").collection("members").get());
  });

  test("active viewer can read but malformed membership fails closed", async () => {
    const viewerDatabase = testEnvironment.authenticatedContext(VIEWER_UID).firestore();
    const malformedDatabase = testEnvironment.authenticatedContext(MALFORMED_UID).firestore();

    await assertSucceeds(viewerDatabase.collection("events").doc("event-one").get());
    await assertFails(malformedDatabase.collection("events").doc("event-one").get());
  });

  test("unauthenticated, non-member, and revoked reads fail", async () => {
    const unauthenticatedDatabase = testEnvironment.unauthenticatedContext().firestore();
    const outsideDatabase = testEnvironment.authenticatedContext(NON_MEMBER_UID).firestore();
    const revokedDatabase = testEnvironment.authenticatedContext(REVOKED_UID).firestore();

    await assertFails(unauthenticatedDatabase.collection("events").doc("event-one").get());
    await assertFails(outsideDatabase.collection("events").doc("event-one").get());
    await assertFails(revokedDatabase.collection("events").doc("event-one").get());
  });

  test("all direct client writes to protected data fail", async () => {
    const organizerDatabase = testEnvironment.authenticatedContext(ORGANIZER_UID).firestore();
    const eventRef = organizerDatabase.collection("events").doc("event-one");
    const protectedReferences = [
      {
        existing: eventRef,
        created: organizerDatabase.collection("events").doc("forged-event")
      },
      {
        existing: eventRef.collection("members").doc(ORGANIZER_UID),
        created: eventRef.collection("members").doc("forged-member")
      },
      {
        existing: eventRef.collection("entries").doc("entry-one"),
        created: eventRef.collection("entries").doc("forged-entry")
      },
      {
        existing: eventRef.collection("activity").doc("activity-one"),
        created: eventRef.collection("activity").doc("forged-activity")
      },
      {
        existing: eventRef.collection("privateEvidence").doc("evidence-one"),
        created: eventRef.collection("privateEvidence").doc("forged-evidence")
      },
      {
        existing: eventRef.collection("audit").doc("audit-one"),
        created: eventRef.collection("audit").doc("forged-audit")
      },
      {
        existing: eventRef.collection("invites").doc("invite-one"),
        created: eventRef.collection("invites").doc("forged-invite")
      },
      {
        existing: organizerDatabase.collection("publicEvents").doc("event-one"),
        created: organizerDatabase.collection("publicEvents").doc("forged-public-event")
      },
      {
        existing: organizerDatabase.collection("users").doc(ORGANIZER_UID).collection("operations").doc("operation-one"),
        created: organizerDatabase.collection("users").doc(ORGANIZER_UID).collection("operations").doc("forged-operation")
      }
    ];

    for (const reference of protectedReferences) {
      await assertFails(reference.created.set({ forged: true }));
      await assertFails(reference.existing.update({ forged: true }));
      await assertFails(reference.existing.delete());
    }
  });
});

function validCreateInput() {
  return {
    idempotencyKey: "0123456789abcdef0123456789abcdef",
    title: "Community Festival",
    duration: "Two days",
    visibilityPolicy: "private",
    customInfo: {
      venue: "Community Hall"
    }
  };
}

function validPublicCreateInput() {
  return {
    ...validCreateInput(),
    visibilityPolicy: "public"
  };
}

function validInviteInput(eventId) {
  return {
    idempotencyKey: "11111111111111111111111111111111",
    eventId,
    role: "contributor",
    expiresInSeconds: 3600
  };
}

async function seedAuthorizedEvent() {
  await testEnvironment.withSecurityRulesDisabled(async (context) => {
    const database = context.firestore();
    const eventRef = database.collection("events").doc("event-one");
    await eventRef.set({
      ownerUid: ORGANIZER_UID,
      title: "Authorized Event",
      visibilityPolicy: "public",
      status: "active",
      revision: 1
    });
    await eventRef.collection("members").doc(ORGANIZER_UID).set({
      uid: ORGANIZER_UID,
      role: "organizer",
      status: "active"
    });
    await eventRef.collection("members").doc(CONTRIBUTOR_UID).set({
      uid: CONTRIBUTOR_UID,
      role: "contributor",
      status: "active"
    });
    await eventRef.collection("members").doc(REVOKED_UID).set({
      uid: REVOKED_UID,
      role: "viewer",
      status: "revoked"
    });
    await eventRef.collection("members").doc(VIEWER_UID).set({
      uid: VIEWER_UID,
      role: "viewer",
      status: "active"
    });
    await eventRef.collection("members").doc(MALFORMED_UID).set({
      uid: "different-user",
      role: "viewer",
      status: "active"
    });
    await eventRef.collection("entries").doc("entry-one").set({ status: "confirmed" });
    await eventRef.collection("activity").doc("activity-one").set({ action: "entry.confirmed" });
    await eventRef.collection("privateEvidence").doc("evidence-one").set({ private: true });
    await eventRef.collection("audit").doc("audit-one").set({ action: "event.created" });
    await eventRef.collection("invites").doc("invite-one").set({ status: "active" });
    await database.collection("publicEvents").doc("event-one").set({
      title: "Authorized Event",
      visibilityPolicy: "public",
      status: "active"
    });
    await database.collection("users").doc(ORGANIZER_UID).collection("operations").doc("operation-one").set({
      operationType: "createEvent"
    });
  });
}

async function createAuthEmulatorUser(email) {
  const response = await fetch(AUTH_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      email,
      password: "emulator-test-password",
      returnSecureToken: true
    })
  });
  const body = await response.json();
  assert.equal(response.status, 200, JSON.stringify(body));
  return body.idToken;
}

async function callCreateSharedEvent({ authToken, appCheckToken }) {
  return callFunction("createSharedEvent", validCreateInput(), { authToken, appCheckToken });
}

async function callFunction(functionName, data, { authToken, appCheckToken }) {
  const headers = { "Content-Type": "application/json" };
  if (authToken) headers.Authorization = `Bearer ${authToken}`;
  if (appCheckToken) headers["X-Firebase-AppCheck"] = appCheckToken;

  const functionUrl = FUNCTIONS_URL.replace("createSharedEvent", functionName);
  const response = await fetch(functionUrl, {
    method: "POST",
    headers,
    body: JSON.stringify({ data })
  });
  return {
    status: response.status,
    body: await response.json()
  };
}

function emulatorJwt(subject, claims = {}) {
  const header = Buffer.from(JSON.stringify({ alg: "none", typ: "JWT" })).toString("base64url");
  const payload = Buffer.from(JSON.stringify({
    sub: subject,
    iat: Math.floor(Date.now() / 1000),
    exp: Math.floor(Date.now() / 1000) + 3600,
    ...claims
  })).toString("base64url");
  return `${header}.${payload}.emulator-signature`;
}