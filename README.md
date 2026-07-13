# Community Ledger

Community Ledger is a local-first Android event finance ledger. It turns payment receipts into structured, reviewable ledger entries for community events.

The app is built around receipt integrity: extract the receipt, validate the data, score confidence, check duplicates, link the entry to an event/member, save JSON proof, and only then update totals.

## What It Does

- Create event ledgers.
- Share an event-copy link that adds an independent local event shell on another device.
- Upload receipt screenshots from device storage.
- Receive shared receipt images/text from payment apps.
- Extract receipt text using on-device Google ML Kit OCR.
- Parse receipt data into JSON.
- Block low-confidence or duplicate receipts.
- Link transactions to persisted members.
- Store receipt JSON in app-private event/person folders.
- Calculate collected, spent, and available balance from saved transactions.

## Receipt OCR

Receipt OCR is on-device only.

The app currently uses:

- Google ML Kit Latin text recognition
- Google ML Kit Devanagari text recognition

The receipt flow does not use:

- Gemini
- cloud OCR
- API-key OCR
- filename-based receipt extraction
- random or dummy receipt data

## Receipt Pipeline

1. User uploads or shares a receipt image/text.
2. The app clears stale receipt state.
3. ML Kit runs multi-pass OCR:
	 - direct URI image
	 - decoded bitmap
	 - scaled bitmap
	 - contrast-processed bitmap
	 - scaled processed bitmap
4. Latin and Devanagari OCR outputs are merged.
5. Receipt fields are parsed.
6. Amount evidence and optional-detail completeness are evaluated separately.
7. Duplicate detection runs.
8. A compact human-readable review shows the amount, app, counterparty, date, and reference; users can correct or explicitly confirm the amount.
9. The app-private JSON proof file must be written successfully before save can continue.
10. Save is allowed only when amount evidence or explicit user confirmation, receipt context, duplicate clearance, attribution, and evidence persistence pass.
11. The Room transaction references the private JSON proof record.

## Saved Receipt JSON

Example shape:

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
	}
}
```

## Storage

Receipt JSON files are stored in app-private storage:

```text
files/receipts/event_<eventId>/person_<person>/uploader_<email>/receipt_<id>.json
```

These files are excluded from backup and device transfer rules.

## Database

The app uses Room with:

- `events`
- `members`
- `transactions`

Transactions link to events and, when possible, persisted members.

Important safeguards already implemented:

- No destructive Room migration.
- Event delete cascades to transactions and members.
- Receipt transactions use `memberId`.
- Legacy transactions can be repaired/backfilled to members when opening an event.

## Event-Copy Links

Event-copy links use an opaque cross-device copy key plus checksum validation. Room integer IDs remain device-local, so unrelated ledgers with the same local ID do not conflict. Legacy numeric-ID links remain accepted.

These links still create independent metadata shells. They are not cryptographic access control or synchronized membership, and they do not carry members, receipts, transactions, balances, or later changes.

Do not describe event-copy links as secure, authenticated, or tamper-proof unless server-issued tokens are added later.

## Public And Private Events

Events can be marked public or private. The dashboard can filter by this marker, event cards show it, and event-copy links preserve it when another device adds the local shell.

This is a local visibility marker, not server authorization. Share private-event links only with trusted people until real account-based access control exists.

## Local Identity

A valid email label is stored on the device and is required before creating events, sharing event copies, or saving/replacing receipt transactions. It is normalized to lowercase and used for local ownership/uploader checks. It is not sign-in or authentication.

## Publisher And Support

Community Ledger is independently published by **Gopi Banoth**.

- Private support and privacy: [banothgopikrishna19@gmail.com](mailto:banothgopikrishna19@gmail.com)
- GitHub: [GopiB9119](https://github.com/GopiB9119)
- LinkedIn: [Gopi Banoth](https://www.linkedin.com/in/gopib-960965243/)
- Public non-sensitive bug reports: [project issues](https://github.com/GopiB9119/event/issues)

Never post receipt images, personal or financial data, passwords, signing keys, or verification codes in a public issue.

## Repository Governance

- [Security policy](SECURITY.md)
- [Incident response](docs/Security/INCIDENT_RESPONSE.md)
- [Support](SUPPORT.md)
- [Code of conduct](CODE_OF_CONDUCT.md)
- [Contributing](CONTRIBUTING.md)
- [Changelog](CHANGELOG.md)
- [GitHub repository operations](docs/GitHub/REPOSITORY_OPERATIONS.md)

## License

Community Ledger is dual-licensed under the [MIT License](LICENSE-MIT) or the [Apache License 2.0](LICENSE-APACHE), at your option. See [LICENSE](LICENSE) for the combined terms and contribution notice.

External contributions remain subject to review and acceptance by the publisher. Contributions intentionally submitted for inclusion are licensed under the same `MIT OR Apache-2.0` terms unless explicitly stated otherwise.

## Project Documentation

- [Architecture overview](docs/Architecture/OVERVIEW.md)
- [Future shared-event architecture](docs/Architecture/SHARED_EVENTS_FUTURE.md)
- [Product overview](docs/Product/PRODUCT_OVERVIEW.md)
- [Post-beta.2 product program](docs/Product/POST_BETA2_PROGRAM.md)
- [Four-month execution plan](docs/Product/FOUR_MONTH_EXECUTION_PLAN.md)
- [Test strategy](docs/Testing/TEST_STRATEGY.md)
- [Physical-device launch matrix](docs/Testing/PHYSICAL_DEVICE_LAUNCH_MATRIX.md)
- [Friend beta readiness](docs/Release/FRIEND_BETA_READINESS.md)
- [Privacy policy](docs/Legal/PRIVACY_POLICY.md)
- [Beta terms](docs/Legal/TERMS_OF_USE.md)
- [Data and permissions](docs/Legal/DATA_AND_PERMISSIONS.md)
- [Public launch checklist](docs/Release/PUBLIC_LAUNCH_CHECKLIST.md)
- [Signing and distribution](docs/Release/SIGNING_AND_DISTRIBUTION.md)
- [Receipt OCR validation](docs/Research/RECEIPT_OCR_VALIDATION.md)
- [Receipt integrity ADR](docs/Decisions/ADR-0001-RECEIPT-INTEGRITY.md)

## Validation

Run Kotlin/Room compile validation:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
```

Build a debug APK:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDebug
```

Install on a connected device:

```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

Launch:

```powershell
adb shell monkey -p com.aistudio.communityledger.yrtqwx 1
```

## Testing Notes

The complete debug unit/Robolectric suite currently passes 54 tests across nine suites. The last complete API 36 Android instrumentation suite passes 14 tests across six classes, covering compact receipt review, detected/edited amount confirmation, private evidence persistence, Room 4-to-5 migration, fail-closed numeric-ID and opaque-key collision safety, file-backed database reopen, shared receipt state, app context, six private real-image OCR fixtures, receipt attribution, and acknowledged deletion. First-use disclosure, local identity gating, Trust Center navigation, and an honest receipt-interruption notice after process death have also been verified through runtime interaction. The bounded workflow at `.github/workflows/android-ci.yml` runs compile, unit tests, and APK assembly with a 30-minute timeout; its exact command passes locally and on hosted GitHub runners.

## Launch Website

The static launch package lives in [site](site). It includes the product page, real privacy-safe app screenshots, Privacy, Terms, Contact, event-copy fallback, and a release manifest used by the manual in-app update check. `.github/workflows/pages.yml` requires an explicit deployment; publishing a GitHub prerelease alone does not silently change the in-app update channel.

The current signed [0.2.0-beta.2 limited public prerelease](https://github.com/GopiB9119/event/releases/tag/v0.2.0-beta.2) is documented in the [repository release notes](docs/Release/0.2.0-beta.2.md). Verify SHA-256 before installation. Physical-device breadth and qualified legal review remain incomplete beta risks.

Next recommended test work:

- Add more JVM-only parser tests for Amazon Pay, Paytm, BHIM, WhatsApp Pay, and noisier low-light receipts.
- For image OCR checks, use real private receipt screenshots only. Do not commit them. The focused instrumentation test can read images from either `app/src/androidTest/assets/receipt-images-private/` or the app-specific device folder `Android/data/com.aistudio.communityledger.yrtqwx/files/receipt-images-private/`.
- Add physical-device coverage for image sharing, interruption, and restart behavior.

Run the private image OCR test on a connected device:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=com.example.receipt.ReceiptImageOcrInstrumentedTest'
```

The test writes OCR/parse JSON reports to the app-specific external files directory and prints the report path in the test output. If no private receipt images exist, the test is skipped instead of using dummy data. In PowerShell, keep the `-P...` argument quoted.

## Engineering Doctrine

Read [prompts.md](prompts.md) before major changes. It contains the current product and engineering rules for receipt integrity, OCR, JSON storage, duplicate protection, member linking, and security language.
