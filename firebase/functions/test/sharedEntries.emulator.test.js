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
import { reviewPendingEntryForActor, submitReviewedEntryForActor } from "../src/sharedEntries.js";
import { createEventForActor, SharedEventInputError } from "../src/sharedEvents.js";

const PROJECT_ID = "demo-community-ledger";
const RULES_PATH = new URL("../../firestore.rules", import.meta.url);
const ORGANIZER_UID = "entry-organizer";
const CONTRIBUTOR_UID = "entry-contributor";
const VIEWER_UID = "entry-viewer";
const DUPLICATE_HASH_KEY = "emulator-entry-duplicate-hash-key";
const FUNCTIONS_BASE_URL = `http://127.0.0.1:5001/${PROJECT_ID}/us-central1`;

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
  adminApp = initializeApp({ projectId: PROJECT_ID }, "shared-entries-emulator-tests");
  adminDatabase = getFirestore(adminApp);
});

beforeEach(async () => {
  await testEnvironment.clearFirestore();
});

after(async () => {
  if (testEnvironment) await testEnvironment.cleanup();
  if (adminApp) await deleteApp(adminApp);
});

describe("server-confirmed entries and totals", () => {
  test("organizer entry remains pending until reviewed with private evidence", async () => {
    const event = await seedEventWithRoles();
    const result = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );

    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    const eventSnapshot = await eventRef.get();
    const entrySnapshot = await eventRef.collection("entries").doc(result.entryId).get();
    const evidenceSnapshot = await eventRef.collection("privateEvidence").doc(result.entryId).get();
    const duplicateSnapshot = await eventRef.collection("duplicateKeys").get();

    assert.equal(result.status, "pending");
    assert.equal(eventSnapshot.data().revision, 1);
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
    assert.equal(eventSnapshot.data().totalSpentMinor, 0);
    assert.equal(eventSnapshot.data().confirmedReceiptCount, 0);
    assert.equal(entrySnapshot.data().status, "pending");
    assert.equal(evidenceSnapshot.data().paymentReference, "UPI-ENTRY-001");
    assert.equal(duplicateSnapshot.size, 1);

    const organizerDatabase = testEnvironment.authenticatedContext(ORGANIZER_UID).firestore();
    const contributorDatabase = testEnvironment.authenticatedContext(CONTRIBUTOR_UID).firestore();
    const viewerDatabase = testEnvironment.authenticatedContext(VIEWER_UID).firestore();
    await assertSucceeds(contributorDatabase.collection("events").doc(event.eventId).get());
    await assertFails(viewerDatabase.collection("events").doc(event.eventId).collection("entries").doc(result.entryId).get());
    await assertSucceeds(organizerDatabase.collection("events").doc(event.eventId).collection("privateEvidence").doc(result.entryId).get());
    await assertFails(contributorDatabase.collection("events").doc(event.eventId).collection("privateEvidence").doc(result.entryId).get());
  });

  test("reviewed expense updates only spent and member money out totals", async () => {
    const event = await seedEventWithRoles();
    const result = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      {
        ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
        ledgerType: "Expense"
      },
      DUPLICATE_HASH_KEY
    );

    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    const beforeReview = await eventRef.get();
    assert.equal(result.status, "pending");
    assert.equal(beforeReview.data().totalSpentMinor, 0);
    const reviewed = await reviewPendingEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      {
        idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
        eventId: event.eventId,
        entryId: result.entryId,
        expectedRevision: 1,
        decision: "confirm",
        reason: "Expense evidence reviewed"
      }
    );
    const eventSnapshot = await eventRef.get();
    const memberSnapshot = await eventRef.collection("members").doc(CONTRIBUTOR_UID).get();

    assert.equal(reviewed.status, "confirmed");
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
    assert.equal(eventSnapshot.data().totalSpentMinor, 50000);
    assert.equal(memberSnapshot.data().confirmedMoneyInMinor, 0);
    assert.equal(memberSnapshot.data().confirmedMoneyOutMinor, 50000);
  });

  test("submission retry cannot double-count totals", async () => {
    const event = await seedEventWithRoles();
    const input = validEntryInput(event.eventId, CONTRIBUTOR_UID);

    const first = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      input,
      DUPLICATE_HASH_KEY
    );
    const second = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      input,
      DUPLICATE_HASH_KEY
    );

    assert.deepEqual(second, first);
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
    assert.equal(eventSnapshot.data().confirmedReceiptCount, 0);
    assert.equal((await eventSnapshot.ref.collection("entries").get()).size, 1);
  });

  test("confirmation fails when private evidence is missing", async () => {
    const event = await seedEventWithRoles();
    const submitted = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );
    await adminDatabase
      .collection("events")
      .doc(event.eventId)
      .collection("privateEvidence")
      .doc(submitted.entryId)
      .delete();

    await assert.rejects(
      reviewPendingEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID },
        {
          idempotencyKey: "cccccccccccccccccccccccccccccccc",
          eventId: event.eventId,
          entryId: submitted.entryId,
          expectedRevision: 1,
          decision: "confirm",
          reason: null
        }
      ),
      (error) => error instanceof SharedEventInputError
        && error.code === "failed-precondition"
        && /evidence is missing/u.test(error.message)
    );
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
    assert.equal(eventSnapshot.data().confirmedReceiptCount, 0);
  });

  test("same payment reference with a different operation is rejected", async () => {
    const event = await seedEventWithRoles();
    await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );

    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
        {
          ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
          idempotencyKey: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          expectedRevision: 1
        },
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "already-exists"
    );
    assert.equal((await adminDatabase.collection("events").doc(event.eventId).collection("entries").get()).size, 1);
  });

  test("punctuation variants of one payment reference are duplicates", async () => {
    const event = await seedEventWithRoles();
    await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );

    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
        {
          ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
          idempotencyKey: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          expectedRevision: 1,
          paymentReference: "upi entry 001"
        },
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "already-exists"
    );
  });

  test("no-reference fallback pairs reserve duplicate evidence", async () => {
    const event = await seedEventWithRoles();
    const firstInput = {
      ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
      paymentReference: null
    };
    await submitReviewedEntryForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
      firstInput,
      DUPLICATE_HASH_KEY
    );

    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
        {
          ...firstInput,
          idempotencyKey: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        },
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "already-exists"
    );
    assert.equal((await adminDatabase.collection("events").doc(event.eventId).collection("entries").get()).size, 1);
  });

  test("contributor entry remains pending until organizer confirms it", async () => {
    const event = await seedEventWithRoles();
    const submitted = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );
    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    const beforeReview = await eventRef.get();

    assert.equal(submitted.status, "pending");
    assert.equal(beforeReview.data().revision, 1);
    assert.equal(beforeReview.data().totalCollectedMinor, 0);

    const contributorDatabase = testEnvironment.authenticatedContext(CONTRIBUTOR_UID).firestore();
    const viewerDatabase = testEnvironment.authenticatedContext(VIEWER_UID).firestore();
    const organizerDatabase = testEnvironment.authenticatedContext(ORGANIZER_UID).firestore();
    const eventPath = contributorDatabase.collection("events").doc(event.eventId);
    await assertSucceeds(eventPath.collection("entries").doc(submitted.entryId).get());
    await assertFails(
      viewerDatabase.collection("events").doc(event.eventId).collection("entries").doc(submitted.entryId).get()
    );
    await assertSucceeds(
      organizerDatabase.collection("events").doc(event.eventId).collection("entries").doc(submitted.entryId).get()
    );
    await assertFails(
      viewerDatabase.collection("events").doc(event.eventId).collection("activity").get()
    );
    await assertSucceeds(
      contributorDatabase.collection("events").doc(event.eventId).collection("privateEvidence").doc(submitted.entryId).get()
    );
    await assertFails(
      viewerDatabase.collection("events").doc(event.eventId).collection("privateEvidence").doc(submitted.entryId).get()
    );
    await assertSucceeds(
      organizerDatabase.collection("events").doc(event.eventId).collection("privateEvidence").get()
    );
    await assertSucceeds(
      contributorDatabase
        .collection("events")
        .doc(event.eventId)
        .collection("privateEvidence")
        .where("submittedByUid", "==", CONTRIBUTOR_UID)
        .get()
    );
    await assertFails(
      contributorDatabase.collection("events").doc(event.eventId).collection("privateEvidence").get()
    );

    const reviewed = await reviewPendingEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      {
        idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
        eventId: event.eventId,
        entryId: submitted.entryId,
        expectedRevision: 1,
        decision: "confirm",
        reason: "Receipt reviewed"
      }
    );
    const afterReview = await eventRef.get();
    const memberSnapshot = await eventRef.collection("members").doc(CONTRIBUTOR_UID).get();

    assert.equal(reviewed.status, "confirmed");
    assert.equal(afterReview.data().revision, 2);
    assert.equal(afterReview.data().totalCollectedMinor, 50000);
    assert.equal(memberSnapshot.data().confirmedMoneyInMinor, 50000);
  });

  test("viewer, cross-member contributor, and stale revisions fail closed", async () => {
    const event = await seedEventWithRoles();

    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: VIEWER_UID, displayName: "Entry Viewer" },
        validEntryInput(event.eventId, VIEWER_UID),
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "permission-denied"
    );
    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
        validEntryInput(event.eventId, ORGANIZER_UID),
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "permission-denied"
    );
    await assert.rejects(
      submitReviewedEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
        { ...validEntryInput(event.eventId, CONTRIBUTOR_UID), expectedRevision: 99 },
        DUPLICATE_HASH_KEY
      ),
      (error) => error instanceof SharedEventInputError && error.code === "aborted"
    );
    assert.equal((await adminDatabase.collection("events").doc(event.eventId).collection("entries").get()).size, 0);
  });

  test("weak and manual amount evidence fails before mutation", async () => {
    const event = await seedEventWithRoles();
    const invalidEvidence = [
      ["NOT_DETECTED", 90],
      ["UNLABELLED_NUMBER", 90],
      ["USER_ENTERED", 100],
      ["USER_CONFIRMED", 100],
      ["AMOUNT_LABEL", 64]
    ];

    for (const [index, [amountEvidenceSource, amountEvidenceConfidence]] of invalidEvidence.entries()) {
      await assert.rejects(
        submitReviewedEntryForActor(
          adminDatabase,
          { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
          {
            ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
            idempotencyKey: (index + 1).toString(16).padStart(32, "0"),
            amountEvidenceSource,
            amountEvidenceConfidence
          },
          DUPLICATE_HASH_KEY
        ),
        (error) => error instanceof SharedEventInputError && error.code === "invalid-argument"
      );
    }

    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    assert.equal((await eventRef.collection("entries").get()).size, 0);
    assert.equal((await eventRef.collection("privateEvidence").get()).size, 0);
    assert.equal((await eventRef.get()).data().totalCollectedMinor, 0);
  });

  test("organizer cannot confirm a pending entry with missing amount provenance", async () => {
    const event = await seedEventWithRoles();
    const submitted = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );
    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    await eventRef.collection("entries").doc(submitted.entryId).update({
      amountEvidenceSource: null,
      amountEvidenceConfidence: null
    });

    await assert.rejects(
      reviewPendingEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID },
        {
          idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
          eventId: event.eventId,
          entryId: submitted.entryId,
          expectedRevision: 1,
          decision: "confirm",
          reason: "Receipt reviewed"
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "failed-precondition"
    );

    assert.equal((await eventRef.get()).data().totalCollectedMinor, 0);
    assert.equal((await eventRef.collection("entries").doc(submitted.entryId).get()).data().status, "pending");
  });

  test("organizer entry without strong reference stays pending", async () => {
    const event = await seedEventWithRoles();
    const result = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      {
        ...validEntryInput(event.eventId, CONTRIBUTOR_UID),
        paymentReference: null
      },
      DUPLICATE_HASH_KEY
    );

    assert.equal(result.status, "pending");
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().totalCollectedMinor, 0);
  });

  test("organizer can reject after ledger person revocation and releases duplicate reservations", async () => {
    const event = await seedEventWithRoles();
    const submitted = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: CONTRIBUTOR_UID, displayName: "Entry Contributor" },
      validEntryInput(event.eventId, CONTRIBUTOR_UID),
      DUPLICATE_HASH_KEY
    );
    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    await eventRef.collection("members").doc(CONTRIBUTOR_UID).update({ status: "revoked" });

    await assert.rejects(
      reviewPendingEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID },
        {
          idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
          eventId: event.eventId,
          entryId: submitted.entryId,
          expectedRevision: 1,
          decision: "reject",
          reason: null
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "invalid-argument"
    );

    const rejected = await reviewPendingEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID },
      {
        idempotencyKey: "cccccccccccccccccccccccccccccccc",
        eventId: event.eventId,
        entryId: submitted.entryId,
        expectedRevision: 1,
        decision: "reject",
        reason: "Membership was revoked before review"
      }
    );

    assert.equal(rejected.status, "rejected");
    assert.equal((await eventRef.collection("duplicateKeys").get()).size, 0);
    const viewerDatabase = testEnvironment.authenticatedContext(VIEWER_UID).firestore();
    const organizerDatabase = testEnvironment.authenticatedContext(ORGANIZER_UID).firestore();
    await assertFails(
      viewerDatabase.collection("events").doc(event.eventId).collection("entries").doc(submitted.entryId).get()
    );
    await assertSucceeds(
      organizerDatabase.collection("events").doc(event.eventId).collection("entries").doc(submitted.entryId).get()
    );
  });

  test("aggregate overflow rolls back organizer review and keeps the entry pending", async () => {
    const event = await seedEventWithRoles();
    const eventRef = adminDatabase.collection("events").doc(event.eventId);
    await eventRef.update({ totalCollectedMinor: Number.MAX_SAFE_INTEGER });

    const submitted = await submitReviewedEntryForActor(
      adminDatabase,
      { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
      { ...validEntryInput(event.eventId, CONTRIBUTOR_UID), amountMinor: 1 },
      DUPLICATE_HASH_KEY
    );
    await assert.rejects(
      reviewPendingEntryForActor(
        adminDatabase,
        { uid: ORGANIZER_UID },
        {
          idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
          eventId: event.eventId,
          entryId: submitted.entryId,
          expectedRevision: 1,
          decision: "confirm",
          reason: "Overflow test"
        }
      ),
      (error) => error instanceof SharedEventInputError && error.code === "resource-exhausted"
    );
    assert.equal((await eventRef.collection("entries").doc(submitted.entryId).get()).data().status, "pending");
    assert.equal((await eventRef.collection("privateEvidence").get()).size, 1);
    assert.equal((await eventRef.collection("duplicateKeys").get()).size, 1);
  });
});

describe("entry callable convergence", () => {
  test("authenticated contributor submits and organizer confirms through callables", async () => {
    const event = await seedEventWithRoles();
    const appCheckToken = emulatorJwt("demo-app");
    const submitted = await callFunction("submitSharedEntry", validEntryInput(event.eventId, CONTRIBUTOR_UID), {
      authToken: emulatorJwt(CONTRIBUTOR_UID, { name: "Entry Contributor" }),
      appCheckToken
    });
    assert.equal(submitted.status, 200, JSON.stringify(submitted.body));
    assert.equal(submitted.body.result.status, "pending");

    const reviewed = await callFunction("reviewSharedEntry", {
      idempotencyKey: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
      eventId: event.eventId,
      entryId: submitted.body.result.entryId,
      expectedRevision: 1,
      decision: "confirm",
      reason: "Callable review"
    }, {
      authToken: emulatorJwt(ORGANIZER_UID, { name: "Entry Organizer" }),
      appCheckToken
    });

    assert.equal(reviewed.status, 200, JSON.stringify(reviewed.body));
    assert.equal(reviewed.body.result.status, "confirmed");
    const eventSnapshot = await adminDatabase.collection("events").doc(event.eventId).get();
    assert.equal(eventSnapshot.data().totalCollectedMinor, 50000);
  });
});

function validCreateInput() {
  return {
    idempotencyKey: "0123456789abcdef0123456789abcdef",
    title: "Entry Test Event",
    duration: "Two days",
    visibilityPolicy: "private",
    customInfo: {}
  };
}

function validEntryInput(eventId, ledgerPersonUid) {
  return {
    idempotencyKey: "99999999999999999999999999999999",
    eventId,
    expectedRevision: 1,
    ledgerType: "Donated",
    amountMinor: 50000,
    amountEvidenceSource: "AMOUNT_LABEL",
    amountEvidenceConfidence: 90,
    ledgerPersonUid,
    paymentApp: "Sample Pay",
    paymentDate: "2026-07-14",
    counterparty: "Community Hall",
    paymentReference: "UPI-ENTRY-001",
    confidence: 90,
    warnings: []
  };
}

async function seedEventWithRoles() {
  const event = await createEventForActor(
    adminDatabase,
    { uid: ORGANIZER_UID, displayName: "Entry Organizer" },
    validCreateInput()
  );
  const eventRef = adminDatabase.collection("events").doc(event.eventId);
  await eventRef.collection("members").doc(CONTRIBUTOR_UID).set(membership(CONTRIBUTOR_UID, "contributor", "Entry Contributor"));
  await eventRef.collection("members").doc(VIEWER_UID).set(membership(VIEWER_UID, "viewer", "Entry Viewer"));
  await eventRef.update({
    activeMemberCount: 3,
    contributorCount: 1,
    viewerCount: 1
  });
  return event;
}

function membership(uid, role, displayName) {
  return {
    uid,
    role,
    status: "active",
    displayName,
    confirmedReceiptCount: 0,
    confirmedMoneyInMinor: 0,
    confirmedMoneyOutMinor: 0,
    joinedAtServer: new Date(),
    revokedAtServer: null
  };
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
