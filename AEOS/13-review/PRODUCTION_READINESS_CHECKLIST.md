# Production Readiness Checklist

Use this before shipping a meaningful release or enabling a feature for real users.

## Product Readiness

- Originating decision state and evidence are recorded; the release scope is permitted by that decision.
- Irreversible/high-risk scope has an independent Decision Challenge with no unresolved blocker.
- User problem is clear.
- Primary workflow is complete.
- Empty, loading, error, and success states exist.
- Copy is accurate and not exaggerated.
- Known limitations are visible or documented.
- Material assumptions and unknowns remain labeled and have owners/review triggers.

## Engineering Readiness

- Build passes.
- Tests pass or known test gaps are documented.
- No unrelated refactors are bundled with release changes.
- Data migrations are explicit and non-destructive.
- Runtime feature flags or rollback path exist when needed.

## Data Readiness

- Source of truth is clear.
- Writes are idempotent where needed.
- Duplicate saves are blocked or detected.
- Sensitive data is stored only where necessary.
- Backup and export behavior are intentional.

## Security Readiness

- Trust boundaries are documented.
- Secrets are not stored in source.
- Auth/authz claims match implementation.
- Sensitive actions require appropriate checks.
- Logs do not expose secrets or private receipt data.

## Performance Readiness

- Expensive work is off the UI thread.
- Image/bitmap processing is bounded.
- Database queries are indexed where needed.
- Slow paths have user-visible progress.

## Community Ledger Release Checks

- Receipt extraction is ML Kit only.
- No dummy, random, filename, or cloud fallback receipt data.
- Receipt save requires valid amount, confidence, and duplicate clearance.
- Receipt JSON is persisted in app-private storage.
- Event/member/transaction schema compiles with Room.
- Event deletion cascades correctly.
- Member upload counts use persisted members where possible.
- Invite links are described as convenience links, not security boundaries.

## Release Decision

```text
Ready: yes/no
Blocking issues:
Known risks:
Rollback plan:
Validation evidence:
Owner:
Date:
```
