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

- Durable state: database rows, controlled files, persisted settings.
- Session state: navigation, active dialog, in-progress workflow.
- Derived state: totals, counts, status, confidence, validation results.
- External state: operating-system events, permissions, provider or device output.

## Review Questions

- Where is the source of truth?
- What happens after app restart?
- Can stale state appear in a new workflow?
- Can two workflows or entities share state accidentally?
- Does cancellation leave partial data behind?
- Is the UI showing data from the current operation or a previous one?

Apply repository-specific state invariants after this universal standard.
