# Receipt OCR Validation Plan

The goal is to validate OCR against real payment-app receipt layouts without collecting or storing random people’s private payment data.

## Data Policy

Do not scrape random internet receipts. They may contain names, phone numbers, UPI IDs, transaction IDs, bank details, or other personal data.

Allowed validation sources:

- User-provided receipts with consent.
- Redacted real receipts.
- Official app-store screenshots that contain sample/non-real data.
- Synthetic test images clearly marked as test fixtures, used only in tests and never in runtime extraction.

## Coverage Matrix

Test at least one receipt-like sample for:

- PhonePe
- Google Pay
- Paytm
- Amazon Pay
- BHIM
- Samsung Pay / Samsung Wallet
- CRED
- WhatsApp Pay
- MobiKwik
- Freecharge
- Super.money

## Fields To Verify

- amount
- currency
- paid-to name
- UPI ID
- UPI reference / transaction ID
- payment app
- date
- confidence
- warnings
- duplicate status
- calculation bucket
- calculation operation

## Acceptance Criteria

- Correct amount for clear receipts.
- No unrelated balance/account/card number stored as amount.
- Transaction/reference ID stays null unless labelled.
- Duplicate receipt cannot be saved twice.
- Low-confidence receipt is blocked or flagged.
- JSON output is stable and reviewable.

## Manual Device Test

1. Install latest debug APK.
2. Open or create an event.
3. Share a receipt image from a payment app into Community Ledger.
4. Review and confirm the extracted amount and receipt details; JSON remains private evidence.
5. Save if confidence is valid.
6. Verify event total changes exactly once.
7. Re-share the same receipt and confirm duplicate blocking.
