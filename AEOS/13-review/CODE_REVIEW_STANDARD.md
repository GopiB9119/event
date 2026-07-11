# Code Review Standard

Code review exists to protect correctness, maintainability, security, and user trust.

The reviewer’s job is not to make code look like their preferred style. The reviewer’s job is to find the risks that matter.

## Review Order

1. Understand the user-facing behavior.
2. Identify the owning system boundary.
3. Check data flow and state mutation.
4. Check invariants and failure modes.
5. Check security and privacy implications.
6. Check tests and validation evidence.
7. Check maintainability and naming.
8. Check documentation impact.
9. Approve only when remaining risk is acceptable.

## Severity Levels

### Blocking

- Data corruption risk.
- Security or privacy regression.
- Silent failure in critical path.
- Missing migration for schema change.
- Unverified code touching money, auth, persistence, or security.
- Runtime dummy data or guessed financial values.

### Important

- Hard-to-test logic.
- Ambiguous naming.
- Duplicated domain rules.
- Incomplete error handling.
- Missing documentation for behavior change.

### Optional

- Local readability improvements.
- Formatting not handled by tools.
- Minor refactor suggestions outside the requested behavior.

## Review Questions

- Can this write the wrong data?
- Can this count something twice?
- Can this leak sensitive data?
- Can this fail silently?
- Can this break after app restart?
- Does this preserve existing user data?
- Is the validation strong enough for the risk?
- Would a future engineer understand the decision?

## Community Ledger Critical Review Areas

- Receipt OCR and parsing.
- Receipt JSON storage.
- Duplicate detection.
- Member linking.
- Room migrations.
- Event totals.
- Share intents.
- Backup/data extraction rules.
- Invite link wording and behavior.

## Review Output Format

Lead with findings.

```text
Findings
1. [Severity] File/area: issue and impact.

Required fixes
- ...

Validation gaps
- ...

Summary
- ...
```

If there are no findings, say so directly and name residual risk.
