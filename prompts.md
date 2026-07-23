# Community Ledger Product And Engineering Doctrine

Community Ledger is a local-first event finance ledger. Its job is to turn payment receipts into trustworthy, structured ledger entries for community events.

This is not a demo OCR toy. This is a receipt integrity system.

The app must extract, validate, score, link, store, and explain every rupee before it affects an event balance.

## Product Thesis

Community events run on trust. Money comes in from many people, payments happen across many apps, and screenshots become the proof trail. The product wins only if the ledger is harder to pollute than a spreadsheet and easier to verify than a chat thread.

The core promise:

> Upload or share a payment receipt. Community Ledger extracts the receipt data, validates it, links it to the right event and person, stores a JSON proof record, and updates the ledger only when the evidence is strong enough.

## Target User

- Event organizers
- Festival committees
- Local community groups
- Apartment or village associations
- Small teams collecting and spending shared money

They need clarity, not complexity. They need proof, not guesses.

## Non-Negotiable Rules

1. No dummy receipt data.
2. No random receipt values.
3. No filename-based receipt extraction.
4. No Gemini or cloud OCR fallback.
5. No hallucinated amount, UPI ID, transaction ID, receiver, date, or app name.
6. Missing data must remain `null`.
7. Low-confidence OCR must be blocked or clearly flagged.
8. A receipt must never affect totals unless a positive amount is extracted and accepted.
9. Duplicate receipts must not be counted twice.
10. Every saved receipt must produce structured JSON.
11. Every saved receipt must belong to one event.
12. Every saved receipt must link to a persisted member when possible.
13. Every ledger calculation must be explainable from stored data.
14. Do not describe local invite links as real security.
15. Do not store developer identity as default user identity.

## Current Architecture

- Platform: Android
- Language: Kotlin
- UI: Jetpack Compose + Material 3
- Database: Room
- OCR: Google ML Kit on-device text recognition
- Receipt OCR scripts:
  - Latin
  - Devanagari
- Storage:
  - Room transactions for ledger state
  - App-private JSON receipt files for receipt proof records
- Share import:
  - `ACTION_SEND` image
  - `ACTION_SEND_MULTIPLE` image
  - `ACTION_SEND` text

## Receipt Integrity Pipeline

Input sources:

- Shared receipt image from a payment app
- Uploaded receipt image from device storage
- Shared receipt text

Processing stages:

1. Clear stale receipt state.
2. Run ML Kit OCR only.
3. Run multi-pass OCR:
   - direct URI image
   - decoded bitmap
   - scaled bitmap
   - contrast-processed bitmap
   - scaled processed bitmap
4. Run both Latin and Devanagari recognizers.
5. Merge unique OCR lines.
6. Parse receipt fields.
7. Score confidence.
8. Generate validation warnings.
9. Check duplicates.
10. Show a compact amount/details review; keep JSON as app-private evidence.
11. Save only if the result is safe enough.
12. Store JSON under event/person/uploader folders.
13. Insert or update Room transaction.
14. Update event totals.

## OCR Policy

Use only Google ML Kit on-device OCR for receipt images.

Allowed:

- `com.google.mlkit:text-recognition`
- `com.google.mlkit:text-recognition-devanagari`

Not allowed:

- Gemini extraction
- cloud OCR
- API-key OCR
- filename heuristics
- random values
- sample receipt fixtures in runtime flow

If OCR cannot read the receipt, the app must say so. It must not invent values.

## JSON Contract

Every saved receipt JSON should follow this shape:

```json
{
  "amount": 5000.0,
  "currency": "INR",
  "calculationAmount": 5000.0,
  "calculationBucket": "Total Collected",
  "calculationOperation": "add",
  "paidTo": "SAMPLE MERCHANT",
  "upiId": "sample.merchant@ybl",
  "upiReferenceOrTransactionId": null,
  "paymentApp": "PhonePe",
  "date": "18 June 2026",
  "phone": null,
  "email": null,
  "ledgerType": "Donated",
  "uploaderEmail": "user@example.com",
  "extractionMethod": "On-device OCR (ML Kit Latin + Devanagari)",
  "confidence": 75,
  "warnings": [
    "UPI reference or transaction ID not detected."
  ],
  "duplicateCheck": {
    "status": "clear",
    "matchedTransactionRowId": null,
    "reason": null
  },
  "eventId": 12,
  "storedAt": 1780000000000,
  "receiptFilePath": ".../files/receipts/event_12/person_sample_merchant/uploader_user_example.com/receipt_1780000000000.json"
}
```

Rules:

- `amount` must be a real parsed positive number or `null`.
- `calculationAmount` must match `amount`.
- `calculationBucket` must be `Total Collected` or `Total Spent`.
- `calculationOperation` must be `add` or `subtract`.
- `upiReferenceOrTransactionId` must be labelled in OCR text or stay `null`.
- Raw OCR text must not be stored as proof data unless specifically needed for debugging.
- Warnings must explain missing or uncertain fields.

## Ledger Calculation Rules

Donation-like types:

- `Donated`
- `Credit`

These add to `Total Collected`.

Expense-like types:

- `Expense`
- `Debit`

These add to `Total Spent`.

Balance formula:

```text
Available Balance = Total Collected - Total Spent
```

Rules:

- Do not use negative OCR amounts.
- Do not save amount `0`.
- Do not save when confidence is below the current threshold.
- Do not save duplicates.
- Do not let raw OCR numbers like balance, card tail, account number, or unrelated app UI numbers affect calculations.

## Duplicate Protection

Strong duplicate check:

- Same event
- Same UPI reference or transaction ID

Fallback duplicate check:

- Same event
- Same amount
- At least two of:
  - same date
  - same payment app
  - same receiver name or UPI ID

If duplicate is detected:

- Disable save.
- Show clear warning.
- Include duplicate status in JSON.
- Do not update ledger totals.

## Member Linking Rules

Members are persisted in Room. Transactions should link to `memberId` when possible.

Matching order:

1. Exact phone match when present.
2. Exact email match when present.
3. Normalized name match.
4. Create new member only when no match exists.

Avoid relying only on name for new data. Old transactions may still have `memberId = null`, but the repair job should backfill them when an event opens.

## Receipt Storage Rules

Every confirmed receipt JSON must be written to app-private storage:

```text
files/receipts/event_<eventId>/person_<person>/uploader_<email>/receipt_<id>.json
```

Rules:

- Do not write receipts to public storage.
- Do not store fake receipts.
- Do not store raw PII dumps unnecessarily.
- Exclude receipt files from backup and device transfer.
- Keep `receiptFilePath` in transaction JSON when file storage succeeds.

## Payment App Coverage

The parser should recognize receipts from:

- PhonePe
- Google Pay / GPay
- Paytm
- Amazon Pay
- BHIM
- Samsung Pay / Samsung Wallet
- CRED
- WhatsApp Pay
- MobiKwik
- Freecharge
- Super.money
- Ping Pay

Do not hardcode behavior that only works for one app. Use labelled fields and confidence scoring.

## Safe Parsing Rules

Amounts:

- Prefer currency-labelled values.
- Prefer values near labels like `Paid`, `Sent`, `Amount`, `Total`, `Received`, `Debited`, `Credited`, `Purchase`, `Payment`, `Transaction Amount`.
- Allow standalone amount lines near the top of receipt OCR.
- Reject obvious account numbers or transaction IDs.
- Reject year-like values when they are likely dates.

Transaction IDs:

- Extract only when near labels like:
  - `UPI Ref No`
  - `UPI Transaction ID`
  - `Transaction ID`
  - `Txn ID`
  - `UTR`
  - `Bank Reference`
  - `Approval Code`
  - `Authorization Code`
  - `Order ID`
  - `Receipt Number`
  - `Reference Number`

Never scan the whole OCR text and accept a random ID-looking string.

Receiver fields:

- Extract `paidTo` from labelled receiver context.
- Extract `upiId` from valid UPI handle format.
- If receiver is unclear, leave it null.

## Security Language Rules

Do not overclaim security.

Current invite links are convenience links with checksum validation. They are not cryptographic access control.

Allowed wording:

- shareable invite link
- checksum-protected link
- link integrity check
- invitation link

Avoid wording:

- secure link
- tamper-proof link
- advanced privacy guard
- cryptographically secure invite
- access granted by security verification

## Threat Model

The app must defend against:

- OCR hallucination or misread data
- duplicate receipt upload
- stale shared receipt state
- fake filename-based extraction
- same receipt counted twice
- wrong person attribution
- local data loss from destructive migration
- orphan transactions after event deletion
- backup leakage of receipt and identity data

The app currently does not defend against:

- malicious users with full device access
- server-validated invite tokens
- multi-device identity proof
- cryptographic membership authorization

Do not claim those protections exist.

## Validation Checklist Before Shipping Receipt Changes

Run:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
```

Check:

- No Gemini references.
- No filename receipt extraction.
- No dummy receipt data.
- No random receipt data.
- No stale Google Services warning.
- ML Kit dependency is present.
- Receipt JSON saves only after confidence and duplicate checks.
- Receipt amount changes event totals exactly once.
- Member upload count uses persisted members and transaction JSON.

## Agent Instructions For Future Work

When editing this project:

1. Read current code before changing.
2. Keep changes small and behavior-scoped.
3. Do not add cloud OCR without explicit user approval.
4. Do not reintroduce dummy receipt paths.
5. Do not add sample receipt values to runtime code.
6. Do not claim security that is not enforced.
7. Prefer null over guessed data.
8. Add warnings for missing fields.
9. Validate with compile after edits.
10. Explain remaining risks honestly.

## Current Highest Priority Remaining Work

1. Run Room invite-collision, receipt OCR, share-intent, cancellation, and restart checks on real devices.
2. Push the bounded Android CI workflow and record a green hosted run of the full 39-test unit suite.
3. Design and implement user-controlled export/restore before broader beta distribution.
4. If multi-device sharing is the product goal, complete authentication, server-issued event identity, authorization, sync, and conflict-resolution design before implementation.