## Purpose

Issue/decision:

One-sentence change:

## Scope

In scope:

Out of scope:

## Evidence And Risk

- Decision state: `STOP` / `DEFER` / `TEST` / `BUILD` / narrow verified defect
- User/data behavior changed:
- Security/privacy impact:
- Migration/rollback impact:
- Residual risk:

## Validation

- [ ] Focused test for the changed behavior passed.
- [ ] `:app:compileDirectDebugKotlin :app:compilePlayDebugKotlin` passed when Android source/config changed.
- [ ] `:app:testDirectDebugUnitTest` passed when behavior or tests changed.
- [ ] `:app:assembleDirectDebug :app:assemblePlayDebug` passed for release-affecting Android changes.
- [ ] Applicable device, restart, permission, offline, duplicate, and time-boundary checks passed or are documented as blocked.
- [ ] Documentation and project memory match verified behavior.

Commands/evidence:

## Data Protection

- [ ] No real receipt images, member details, phone numbers, emails, UPI IDs, transaction references, secrets, passwords, signing keys, or verification codes are included.
- [ ] No dummy/random/filename-derived financial runtime data or cloud OCR path was added.
- [ ] Public copy makes no authentication, synchronization, access-control, payment, investment, or security claim beyond implementation.

## Reviewer Challenge

What evidence would prove this change wrong or incomplete?