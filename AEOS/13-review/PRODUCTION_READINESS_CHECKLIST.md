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
- Logs do not expose secrets or private user data.

## High-Stakes Domain Readiness When Applicable

- Intended use, safety boundary, qualified domain owner, jurisdictions, and supported platforms are explicit.
- Degraded/offline/permission/reboot/time-change behavior is visible and tested without guarantee language.
- Sensitive-data purpose, access, retention, withdrawal, deletion, backup, processor, and incident paths have owners.
- Platform, store-policy, cryptographic, security-standard, and legal claims cite current applicable evidence.
- Independent domain, security/privacy, physical-device, and recovery gates pass before human risk acceptance.

## Performance Readiness

- Expensive work is off the UI thread.
- Image/bitmap processing is bounded.
- Database queries are indexed where needed.
- Slow paths have user-visible progress.

Apply repository-specific release overlays after this universal checklist.

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
