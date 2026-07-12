# Architecture Overview

Community Ledger is a single-module, local-first Android app. It has no backend, account system, or multi-device synchronization. Every installed APK owns an independent Room database and app-private receipt store.

## Product Boundary

The implemented product is a trustworthy ledger on one device. An invitation link copies event metadata to another installation; it does not transfer members, transactions, balances, or receipt evidence.

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
       -> app-private receipt JSON files
  -> Compose AppContent
       -> DashboardScreen
       -> CreateEventScreen
       -> EventDetailsScreen
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
| Room entities | Events, members, transactions and foreign-key ownership | Version 5 with explicit migrations |

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
  -> JSON-only user review
  -> positive/confidence/duplicate save gates
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

A real shared ledger still requires a server-issued identifier, verified accounts, authorization, synchronization, conflict handling, and audit protocol. Opaque local-copy keys fix collisions; they are not server identity or access tokens.

## Data Model

```text
EventEntity 1 --* MemberEntity
EventEntity 1 --* TransactionEntity
MemberEntity 1 --* TransactionEntity (nullable memberId)
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
- No export/restore exists. Uninstalling the app loses the ledger.

## Trust Boundaries

- Shared image/text intents and deep links are attacker-controlled input.
- Local email is self-declared identity, not authentication.
- Invite checksum detects accidental changes only; it has no secret key.
- Public/private does not enforce access.
- OCR output is evidence requiring review, not truth.

## Core Invariants

- A transaction belongs to one valid event.
- Persisted transaction amounts are finite and greater than zero.
- Transaction type is one of `Donated`, `Credit`, `Debit`, or `Expense`.
- Receipt saves require positive amount, confidence threshold, and no duplicate match.
- A receipt transaction links to a persisted member when possible.
- Event-copy identity conflicts must never replace an existing local ledger.
- Saved ledger calculations must be explainable from Room and receipt JSON.

## Known Architecture Risks

1. No shared source of truth: installations diverge immediately.
2. Opaque event-copy keys prevent local Room-ID collisions but do not create synchronized identity.
3. `Screens.kt` and `EventViewModel` own too many responsibilities.
4. Active navigation is not restored after process death; interrupted OCR/review is detected but must be restarted by the user.
5. Unit-test infrastructure can stall before executing tests.
6. No user-controlled export, restore, or recovery path exists.
