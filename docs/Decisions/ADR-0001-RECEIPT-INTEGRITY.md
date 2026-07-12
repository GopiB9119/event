# ADR-0001: Receipt Integrity Over Blind OCR

## Status

Accepted

## Context

Community Ledger uses receipt screenshots as proof for event financial transactions. OCR can misread values, payment apps show unrelated numbers, and users can accidentally upload the same receipt multiple times.

## Decision

Receipt OCR must not directly mutate ledger totals. OCR output must pass through parsing, amount-evidence evaluation, duplicate detection, compact human review, member linking, and app-private JSON storage before it affects Room transactions.

## Consequences

- More code in the receipt pipeline.
- More explicit warnings shown to the user.
- Less risk of wrong event totals.
- Future parser work can be tested independently.

## Rejected Alternatives

- Save the first OCR amount found.
- Use filename parsing when OCR fails.
- Use cloud/LLM extraction without explicit security review.
- Trust transaction IDs found anywhere in OCR text.
