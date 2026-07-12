# Future Shared Events

## Decision State

`BUILD` the bounded architecture/authentication prototype after the accountable owner selects the backend owner, data region, sign-in method, and privacy scope. `DEFER` production synchronization and Play production claims until authorization, convergence, conflict, audit, recovery, and privacy gates pass.

User-observed problem on 12 July 2026: a recipient can add an event shell, but later members, receipts, uploader activity, totals, utilization, and event changes do not appear on other devices. This is `VERIFIED` current behavior and a stated product requirement. Frequency and broader market demand remain `UNKNOWN`.

## Current Product Truth

Community Ledger does not currently connect phones to one live event.

- Dashboard filtering searches only events stored on that device.
- An event-copy link adds an independent metadata shell to another device.
- Members, receipts, transactions, balances, and edits do not synchronize.
- Local email labels are not accounts or verified identities.
- Public/private is not server-enforced access control.

The first public beta should launch as a **local organizer ledger**. Do not add UI that implies live membership until the system below exists and is tested.

## Recommended Shared Product

If multi-device collaboration becomes the next product direction, use three explicit roles:

| Role | Allowed actions |
|---|---|
| Organizer | Create event, invite/revoke people, review entries, manage roles, close event |
| Contributor | Submit donation/expense evidence and view permitted event records |
| Viewer | Read organizer-approved totals and evidence summaries only |

Community Ledger should still never collect, hold, transfer, or invest money. A contribution is paid through an external payment method; the app records reviewed evidence afterward.

## Required Architecture

1. **Authentication**
   - Verified account identity, session expiry, account recovery, and device revocation.
   - Local email labels must not be upgraded silently into accounts.

2. **Server-issued event identity**
   - Globally unique opaque event IDs, never Room integer IDs.
   - The server is the authority for event existence, status, and ownership.

3. **Authorization**
   - Role checks on every server read and write.
   - Private events are undiscoverable without authorized membership.
   - Organizer transfer and role changes require explicit confirmation and audit records.

4. **Invite acceptance**
   - Random, single-purpose, expiring invite tokens stored hashed on the server.
   - Tokens can be revoked and cannot encode private member or ledger data.
   - Accepting a token creates server-side membership; it does not copy a ledger.

5. **Synchronization and conflicts**
   - Server version numbers or append-only event revisions.
   - Idempotency keys for transaction and receipt submissions.
   - Explicit conflict UI; never silently overwrite financial records.
   - Offline writes remain pending until the server confirms them.

6. **Auditability**
   - Immutable audit events for create, edit, delete, role, invite, and receipt decisions.
   - Corrections create new revisions instead of erasing prior financial history.
   - Users can see who changed what and when.

7. **Receipt privacy**
   - Decide whether images ever leave the device before implementation.
   - Prefer uploading reviewed structured evidence, with the minimum personal data required.
   - Define retention, deletion, encryption, access logs, incident response, and export.

## Public Search

Public search is a later feature, not a shortcut around authorization.

- Events opt in explicitly and expose only a reviewed public profile.
- Search results must not expose member contacts, payment identifiers, receipts, or balances by default.
- Organizer verification, abuse reporting, moderation, rate limits, and anti-impersonation controls are required.
- Search ranking must not imply that an organizer, event, payment request, or investment is endorsed.

## Delivery Milestones

1. Account and session security.
2. Server-issued events and organizer-only access.
3. Role authorization and revocable invite acceptance.
4. Transaction synchronization, idempotency, and audit history.
5. Offline queue and conflict-resolution UX.
6. Export, recovery, privacy operations, and security review.
7. Optional public discovery with moderation and verified organizer controls.

Each milestone needs backend, Android, security, privacy, migration, and device tests. Do not ship a partially synchronized money ledger where two users can see different totals without an explicit warning.

## Shared-Ledger Acceptance Contract

The first real shared-event release is complete only when two clean devices prove all of the following against one server-authoritative event:

1. A verified organizer creates the event and accepts or revokes a contributor.
2. Both devices show the same event title, duration, visibility policy, and custom fields after a confirmed revision.
3. A contributor submits reviewed receipt evidence with a unique idempotency key; retrying cannot double-count it.
4. The server records who submitted and who reviewed the evidence without treating OCR counterparty data as account identity.
5. Authorized devices converge on the same members, per-person receipt counts, transaction history, Total Collected, Total Spent, Available Balance, and utilization value.
6. Unauthorized and revoked accounts cannot read or write the event.
7. Concurrent edit/delete/correction conflicts are visible; financial history is revised through audit records rather than silently overwritten.
8. Offline work remains visibly pending until server confirmation and survives restart without falsely changing confirmed totals.
9. Receipt-image transfer, if ever enabled, follows an approved minimum-data, retention, deletion, encryption, and access-log policy.
10. Account recovery, export, deletion, incident response, and Play Data safety declarations match the implemented service.

## Owner Inputs Required Before Backend Code

- backend/cloud account owner and billing owner
- initial hosting/data region and intended countries
- sign-in method and recovery policy
- whether receipt images remain local or are uploaded
- organizer/contributor/viewer permissions and approval workflow
- retention, deletion, export, support, and incident-response owner

These choices change legal disclosures, operating cost, abuse controls, and irreversible service configuration. They cannot be inferred from the current local beta.

## Definition Of Done

Shared events are real only when two clean devices can authenticate, accept/revoke membership, submit and review entries, converge on the same server-confirmed totals, recover after offline conflicts, and show a complete audit history without exposing unauthorized event or receipt data.

The concrete proposed delivery design is in [Shared Ledger Implementation Plan](SHARED_LEDGER_IMPLEMENTATION_PLAN.md). It creates no cloud resources and keeps backend ownership, billing, permanent region, retention, and production deployment as human approval gates.