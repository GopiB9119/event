# Architecture Overview

Community Ledger is a single-module, local-first Android app. Every installation owns a Room database and app-private receipt store. The published release remains local-only; debug builds additionally provide a separately identified shared-event mode backed only by the isolated Firebase Emulator Suite.

## Product Boundary

Local events remain trustworthy ledgers on one device. An event-copy link copies metadata to another installation; it does not transfer members, transactions, balances, or receipt evidence.

Shared events are a separate debug/emulator mode. Silent anonymous Firebase Auth identifies installations, Cloud Functions own mutations, Firestore owns membership/history/totals, Realtime Database supplies approximate presence, and Room stores local mappings plus pending operations. No production Firebase service is configured or deployed.

Public/private is a local visibility marker carried by invitation links. It is not authorization.

## Component Map

```text
Android intents
  -> MainActivity
  -> EventViewModel
       -> ReceiptParser (pure Kotlin)
       -> ML Kit Latin + Devanagari OCR
       -> EventRepository
            -> EventDao
                 -> Room database
        -> SharedWorkspaceController
          -> SharedBackend
            -> Firebase Auth / Functions / Firestore / RTDB emulators
       -> app-private receipt JSON files
  -> Compose AppContent
       -> DashboardScreen
       -> CreateEventScreen
       -> EventDetailsScreen
        -> SharedEventsScreen / SharedEventDetailsScreen
```

## Ownership

| Component | Responsibility | Current pressure |
|---|---|---|
| `MainActivity` | Receives launch, share, and deep-link intents | Thin and appropriate |
| `Screens.kt` | All Compose screens, dialogs, receipt review, duplicate detection | Large UI file with mixed product and domain rules |
| `EventViewModel` | Navigation, state, persistence coordination, deep links, image/OCR orchestration | Coordinator is broad and tightly coupled to Android |
| `ReceiptParser` | Deterministic OCR-text parsing and confidence/warnings | Pure and JVM-testable |
| `LedgerTransactionPolicy` | Positive finite amount, valid event, supported type | Pure and JVM-testable |
| `Repository` / `EventDao` | Room access and conflict-safe event insertion | Thin persistence boundary |
| `SharedWorkspaceController` | Shared auth, durable mutation replay, listener lifecycle, role reconciliation, and presence | Debug/emulator integration; production provider disabled |
| `SharedBackend` | Typed boundary for server-authoritative shared data | Firebase emulator implementation in debug; explicit disabled release implementation |
| Room entities | Local events/members/transactions plus shared mappings and pending operations | Version 6 with explicit migrations |

## State Flow

```text
Room Flow<List<EventEntity>> -> Dashboard
selectedEventId
  -> selected event Flow
  -> event transactions Flow
  -> event members Flow

pendingSharedReceipt StateFlow
  -> selected event OCR/review pipeline
  -> cleared only when review data is ready

SharedWorkspaceController flows
  -> authenticated installation and public discovery
  -> server event/member/entry/presence listeners
  -> local shared-event mapping and durable pending-operation replay
```

Compose uses a manual navigation stack held by `EventViewModel`. Process death does not preserve the active navigation route or resume in-progress OCR review. A persisted non-sensitive marker detects an interrupted receipt flow on the next launch, states that no transaction was saved, and remains until acknowledged.

## Receipt Integrity Flow

```text
Image URI / shared text
  -> retain pending input
  -> ML Kit multi-pass OCR (image only)
  -> merge unique OCR lines
  -> ReceiptParser
  -> amount/reference/app/receiver/date + confidence + warnings
  -> duplicate check against selected event
  -> compact details review with a read-only receipt-derived amount
  -> reliable amount evidence/duplicate save gates
  -> app-private JSON evidence file
  -> validated Room transaction + member link
  -> collected/spent/balance calculations
```

No Gemini, cloud OCR, random data, filename extraction, or unlabelled arbitrary reference scan is allowed.

## Event And Invite Flow

```text
Create event
  -> Room auto-generated local integer ID
  -> opaque event-copy key independent of the Room ID
  -> share link carries key, title, creator, expiry, visibility marker, checksum

Open invite on another installation
  -> validate link shape/checksum/expiry
  -> find an existing shell by opaque event key
  -> otherwise insert a metadata shell with a new local Room ID
  -> open independent local event
```

Legacy links with numeric source IDs remain accepted through a deterministic compatibility identity, but never match an existing row by the ambiguous numeric ID/title/organizer label. A pre-v5 imported shell can therefore gain one additional empty shell when its old link is reopened after upgrade. `insertEventIfAbsent` uses conflict-ignore semantics, regular event insertion aborts on conflict, and the unique event-key index prevents later duplicates without replacing a local event or cascading deletion to members/transactions. See [ADR-0002](../Decisions/ADR-0002-EVENT-IDENTITY-AND-SHARED-LEDGER-BOUNDARY.md).

Opaque local-copy keys fix collisions; they are not server identity or access tokens. Debug shared events instead use server-issued event IDs, authenticated memberships, server-authorized projections, idempotent mutations, revisions, and server-confirmed totals. Production still requires an owner-controlled Firebase project, recovery/privacy/operations decisions, deployment, and physical two-phone validation.

## Data Model

```text
EventEntity 1 --* MemberEntity
EventEntity 1 --* TransactionEntity
MemberEntity 1 --* TransactionEntity (nullable memberId)
EventEntity 0..1 -- 1 SharedEventLinkEntity
SharedPendingOperationEntity -> optional local EventEntity + remote operation key
```

- Event deletion cascades to members and transactions.
- Member deletion sets transaction `memberId` to null.
- Transactions retain person snapshot fields for explainability.
- Receipt JSON paths are stored in transaction JSON notes when persistence succeeds.

## Storage And Privacy

- Room database: `community_ledger_db`
- Identity/theme/link counters: `app_prefs` SharedPreferences
- Receipt evidence: `files/receipts/event_<id>/.../*.json`
- Database, receipts, and preferences are excluded from Android backup and device transfer.
- Linked shared-event shells and their child rows are excluded from portable local backup snapshots because the server remains their authority.
- No export/restore exists. Uninstalling the app loses the ledger.
- An [encrypted backup codec, transactional source snapshot, canonical receipt package, and logical manifest codec](ENCRYPTED_BACKUP_FOUNDATION.md) exist internally, but no source/export coordinator, Storage Access Framework flow, restore coordinator, conflict handler, or recovery UI uses them yet.

## Trust Boundaries

- Shared image/text intents and deep links are attacker-controlled input.
- Local email is self-declared identity, not authentication.
- Invite checksum detects accidental changes only; it has no secret key.
- Public/private does not enforce access.
- OCR output is evidence requiring review, not truth.
- Shared-mode Auth tokens, App Check assertions, callable payloads, Firestore documents, and RTDB presence records cross a separate debug network boundary and are treated as untrusted until validated.

## Core Invariants

- A transaction belongs to one valid event.
- Persisted transaction amounts are finite and greater than zero.
- Transaction type is one of `Donated`, `Credit`, `Debit`, or `Expense`.
- Receipt saves require positive amount, confidence threshold, and no duplicate match.
- A receipt transaction links to a persisted member when possible.
- Event-copy identity conflicts must never replace an existing local ledger.
- Saved ledger calculations must be explainable from Room and receipt JSON.
- Shared confirmed totals and membership come only from server-authoritative records; local pending operations cannot change confirmed totals.
- Shared receipt submissions remain pending until evidence-authorized organizer review.

## Known Architecture Risks

1. Production shared mode remains disabled; the verified server authority currently exists only in disposable local emulators.
2. Anonymous installation identity has no recovery after app-data clearing unless the member can rejoin or a future account-linking policy is added.
3. Opaque event-copy keys prevent local Room-ID collisions but do not create synchronized identity; event-copy links remain separate from shared events.
4. `Screens.kt` and `EventViewModel` own too many responsibilities.
5. Active navigation is not restored after process death; interrupted OCR/review is detected but must be restarted by the user.
6. No user-controlled export, restore, or recovery path exists.
