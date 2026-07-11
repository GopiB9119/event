# Community Ledger Receipt Quality Gates

Every receipt-related change must pass these gates before being considered done.

These are project-specific receipt gates layered under [AEOS universal quality gates](../AEOS/13-review/QUALITY_GATES.md). They do not replace the AEOS decision, readiness, documentation, or release gates.

## Build Gate

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
```

## Search Gate

Confirm runtime source does not contain prohibited paths:

- `Gemini`
- `GEMINI_API_KEY`
- `extractHeuristicsFromUri`
- `Local Heuristic`
- `simulatedReceipts`
- `dummy`
- `fake`
- filename receipt extraction

## Data Gate

Saved receipt must include:

- event id
- amount or null
- calculation amount or null
- calculation bucket
- calculation operation
- payment app or null
- receiver evidence when available
- confidence
- warnings
- duplicate status
- receipt file path when persisted

## UX Gate

The user must be able to tell:

- what was extracted
- what is missing
- why save is blocked
- how the amount affects totals
- whether a duplicate was found

## Security Gate

- No hardcoded developer identity.
- No hardcoded cryptographic invite secrets.
- No public receipt file writes.
- Receipt files excluded from backup.
- Local invite checksum not described as security.
