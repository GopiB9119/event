# ADR-0002: Separate Local Row IDs From Shared Event Identity

## Status

Accepted for event-copy identity. Real-time shared-ledger implementation remains gated on backend ownership, authentication, privacy, and authorization decisions.

## Context

Event-copy links previously carried the source device's Room integer event ID. Two unrelated devices commonly generate the same local integer, so opening a valid link could produce this error:

```text
Cannot add this event copy: its link ID conflicts with a different ledger already stored on this device.
```

The link carries event metadata only. It has no members, transactions, receipt evidence, contributor activity, balances, update feed, account identity, or server authority. Fixing the integer collision cannot make the copied ledger live.

## Decision

- Room integer IDs remain local foreign keys only.
- Every event has a unique opaque `eventKey` used by new event-copy links.
- An imported event receives a new local Room ID and retains the shared `eventKey`.
- Reopening the same opaque-key link opens the same local shell.
- Legacy numeric-ID links remain accepted and receive a deterministic compatibility key. They never attach to an existing row using the ambiguous numeric ID, title, or self-declared organizer label.
- Room migration 4 to 5 backfills existing events with unique opaque keys without changing event/member/transaction ownership.
- Both regular event creation and event-copy insertion fail closed on identity conflicts; neither may use replacement semantics that could cascade-delete ledger rows.
- The static `/join/` fallback accepts both `eventKey` and legacy `eventId` links.
- Event-copy links remain independent local shells. They must not be described as synchronized membership or access control.

## Live Shared-Ledger Boundary

The requested multi-device behavior is a different product architecture. It requires:

- verified accounts and session/device recovery
- server-issued event identity
- organizer, contributor, and viewer authorization on every operation
- revocable invite acceptance
- idempotent receipt/transaction submissions
- server-confirmed revisions and explicit offline conflict handling
- append-only audit history for financial corrections
- privacy, retention, deletion, export, incident-response, and data-region decisions

No client-only link format can safely provide those controls.

## Consequences

- Unrelated local ledgers no longer block valid event-copy imports merely because two devices reused one Room ID.
- Existing version-3 beta data needs an explicit Room migration in the next APK/AAB.
- Legacy links cannot prove global identity because their old payload never contained one. A shell imported before migration 4 to 5 receives a new random key, so reopening its old link can create one additional empty compatibility shell. This is safer than opening or mutating a possibly unrelated ledger with the same local ID/title/label.
- Play production remains deferred if the intended product promise is a live shared ledger.
- Data safety and privacy declarations must be reassessed before any backend-enabled build because member, receipt, and financial evidence would leave the device.

## Rejected Alternatives

- Keep using Room integer IDs across devices.
- Replace an existing local event on ID conflict.
- Put complete ledger or receipt data inside a share URL.
- Treat the current checksum or self-declared email as authentication.
- Add client-to-client synchronization without a server authority, audit history, or conflict protocol.
