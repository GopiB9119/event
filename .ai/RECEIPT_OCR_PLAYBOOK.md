# Receipt OCR Playbook

Receipt extraction is a safety-critical path because it changes event money totals.

## OCR Engine Policy

Use on-device Google ML Kit only.

Current recognizers:

- Latin text recognition
- Devanagari text recognition

Do not add cloud OCR or LLM extraction without an explicit product/security decision.

## Runtime Extraction Flow

1. Receive image or text from upload/share.
2. Clear old review state.
3. Run ML Kit OCR on multiple image variants.
4. Merge unique lines.
5. Parse receipt candidates.
6. Score confidence.
7. Generate warnings.
8. Check duplicate receipt risk.
9. Render a compact human-readable review; keep full JSON as private evidence rather than UI.
10. Save only if the result passes gates.

## Image OCR Evidence

Use real user-provided receipt screenshots for image OCR checks. Do not generate fake receipt images or commit private receipt images.

Private local image fixtures belong under:

```text
app/src/androidTest/assets/receipt-images-private/
```

For receipt images that should never enter the repository, push them to the app-specific device folder instead:

```text
Android/data/com.communityledger.app/files/receipt-images-private/
```

Run only the focused instrumentation test when checking real image OCR output:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:connectedDirectDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=com.communityledger.app.receipt.ReceiptImageOcrInstrumentedTest'
```

If no private images are present, the test must skip. It must never fall back to dummy receipt data.

When private images are present, the focused image OCR test must fail if a receipt does not produce OCR text, a positive amount, save-gate confidence, or at least one reference/UPI/counterparty evidence field.

## Parser Rules

Amount:

- Prefer currency-labelled values.
- Accept standalone top-of-receipt values like `5,000.00`.
- Reject long digit strings likely to be account, card, or reference numbers.
- Reject date-like values.

Reference ID:

- Extract only near explicit labels.
- Never scan all OCR text for an arbitrary ID-shaped token.

Receiver:

- Prefer labelled payee/merchant/recipient context.
- Extract UPI ID only from valid handle format.
- Leave missing data null.

## Supported Payment App Families

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

## Save Gates

Receipt save is blocked when:

- amount is missing or zero
- amount evidence is weak, unlabelled, manually entered, or manually confirmed
- payment receipt context is not detected
- duplicate check detects a likely duplicate
- OCR output is not receipt-like

Missing optional app, reference, date, or counterparty fields must stay missing and be shown as such; they do not independently block a reliably labelled, reviewed amount.

## No-Dummy Guarantee

Runtime code must not contain sample receipt values, fake transactions, random amounts, or filename-derived receipt data.
