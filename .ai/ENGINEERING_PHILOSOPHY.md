# Engineering Philosophy

Community Ledger should be boring, explicit, and hard to corrupt.

## Principles

- Prefer data integrity over speed of implementation.
- Prefer explicit state over hidden magic.
- Prefer small migrations over destructive resets.
- Prefer structured JSON over free-form notes.
- Prefer deterministic parsing over model-generated guesses.
- Prefer blocking unsafe saves over allowing wrong totals.
- Prefer app-private storage over public files.
- Prefer observable warnings over silent failure.

## Architecture Rules

- OCR reads text; it does not decide truth.
- Parser extracts candidates; confidence gates decide whether they are usable.
- Review UI shows evidence; save path persists only accepted data.
- Room stores current ledger state.
- App-private JSON stores receipt proof records.
- Members are first-class persisted entities.
- Transactions should link to `memberId` whenever possible.

## Code Review Questions

Before approving receipt-related code, ask:

1. Can this create a transaction from non-receipt data?
2. Can this count the same receipt twice?
3. Can this attach a receipt to the wrong member?
4. Can this wipe existing user data?
5. Can this leak receipt data through backup or public storage?
6. Does the user see uncertainty clearly?
7. Does compile pass?
