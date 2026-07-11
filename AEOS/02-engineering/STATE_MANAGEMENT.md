# State Management

State management defines how facts move through the system.

## Principles

- Keep durable state in a durable store.
- Keep UI state local and temporary.
- Do not duplicate source-of-truth state unless derived.
- Clear stale state before starting a new high-risk flow.
- Make loading, success, empty, and error states explicit.
- Prefer observable flows over hidden callbacks.

## State Categories

- Durable state: database rows, app-private files, persisted preferences.
- Session state: selected screen, active dialog, in-progress OCR.
- Derived state: totals, counts, confidence display, duplicate status.
- External state: Android share intents, URI permissions, ML Kit output.

## Review Questions

- Where is the source of truth?
- What happens after app restart?
- Can stale state appear in a new workflow?
- Can two events share state accidentally?
- Does cancellation leave partial data behind?
- Is the UI showing data from the current operation or a previous one?

## Community Ledger Rules

- Clear old receipt review state before every new upload/share.
- Force OCR reprocessing even when share URI is reused.
- Event totals derive from Room transactions, not UI cache.
- Member counts derive from persisted members and transactions.
- Receipt JSON review must represent the current extraction only.
