# Community Ledger Receipt Quality Gates

Every receipt-related change must pass these gates before being considered done.

These are project-specific receipt gates layered under [AEOS universal quality gates](../AEOS/13-review/QUALITY_GATES.md). They do not replace the AEOS decision, readiness, documentation, or release gates.

## Build Gate

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
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

## Boundary And Persistence Gate

- Android share intents and receipt images are untrusted input.
- OCR text is evidence, not truth; parser output must pass confidence and duplicate gates.
- DAO writes preserve event/member ownership.
- Events own members and transactions; event deletion cascades intentionally.
- Receipt transactions link to `memberId` when possible.
- Receipt JSON files remain outside Room and are referenced from transaction evidence.
- Destructive migrations are forbidden for production ledger data.

## UX Gate

The user must be able to tell:

- what was extracted
- what is missing
- why save is blocked
- how the amount affects totals
- whether a duplicate was found
- a compact human-readable review and exact ledger impact

Unsafe save remains disabled and the UI explains the blocking reason.

The full structured JSON remains app-private evidence and must be persisted before the Room transaction. Raw JSON and terminal-like processing logs are not user-facing review UI.

## State And Performance Gate

- Clear stale receipt-review state before each new upload/share and reprocess reused input deliberately.
- Event/member totals derive from persisted rows, not UI cache.
- Receipt review represents only the current extraction.
- Multi-pass OCR and bitmap scaling remain bounded and are measured on target devices when changed.

## Documentation Gate

- README states ML Kit-only OCR and the prohibited cloud/filename/dummy paths.
- README links the receipt JSON contract, Room entities, validation commands, current test evidence, and known instability honestly.

## Security Gate

- No hardcoded developer identity.
- No hardcoded cryptographic invite secrets.
- No public receipt file writes.
- Receipt files excluded from backup.
- Local invite checksum not described as security.

## Critical Review And Release Gate

- Review OCR/parsing, JSON storage, duplicate detection, member linking, migrations, totals, share intents, backup rules, and invite wording.
- Receipt extraction remains ML Kit only, with no dummy, random, filename, or cloud fallback data.
- Receipt save requires a positive amount, sufficient confidence, duplicate clearance, and app-private JSON evidence.
- Event/member/transaction schema compiles with Room and ownership/cascade behavior remains verified.
