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
- In debug development builds, create or join server-authoritative shared events against isolated local Firebase emulators.

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
8. A compact human-readable review shows the detected amount, payment mode, from, to, date, and reference; the amount is read-only.
9. The app-private JSON proof file must be written successfully before save can continue.
10. Save is allowed only when reliable receipt-derived amount evidence, receipt context, duplicate clearance, attribution, and evidence persistence pass.
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

An internal [encrypted backup codec, transactional source snapshot, logical manifest, and canonical receipt package](docs/Architecture/ENCRYPTED_BACKUP_FOUNDATION.md) are under test, but no export coordinator, Storage Access Framework flow, restore coordinator, conflict handler, or recovery UI invokes them. Uninstalling the current app still deletes the local ledger.

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

## Shared Event Development Status

Shared events are separate from event-copy links. Current debug builds use silent anonymous Firebase Auth, server-issued membership, Cloud Functions mutations, authorized Firestore projections, approximate Realtime Database presence, and Room-backed pending-operation replay. Receipt images remain on the originating phone; only reviewed structured evidence is submitted.

The local backend contract passes 51 emulator tests. One API 36 convergence test passes for two isolated Android clients covering invite/join, joined members, presence, pending submissions, evidence review, confirmation, and equal final history/totals. The debug package is `com.communityledger.app.dev`, and the test runner refuses to install on physical-device serials.

No production Firebase project, billing, managed secrets, permanent region, production App Check, recovery policy, or deployment is configured. Release builds keep shared mode disabled, and the published `0.2.0-beta.2` remains local-only.

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
- [Shared ledger implementation status](docs/Architecture/SHARED_LEDGER_IMPLEMENTATION_PLAN.md)
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
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
```

Build a debug APK:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDirectDebug :app:assemblePlayDebug
```

Install on a connected device:

```powershell
adb install -r app\build\outputs\apk\direct\debug\app-direct-debug.apk
```

Launch:

```powershell
adb shell monkey -p com.communityledger.app 1
```

## Testing Notes

Both flavored debug unit/Robolectric suites pass 177 tests across 26 suites per flavor, with zero failures, errors, or skips. This includes a synthetic 10,000-transaction correctness fixture, constant-memory prospective-total guards, cross-event replacement rejection, transactional backup-source capture, strict bounded manifest encoding, canonical receipt packaging, encrypted private-cache staging, the original version-1/schema-5 package compatibility hash and schema-6 decoding, direct/Play update-channel UI coverage, and focused EventCard interaction/accessibility/screenshot coverage. The large fixture proves exact aggregation and matching behavior, not frame time or 10,000-user concurrency. The Firebase backend passes 51 emulator tests, and the focused API 36 shared convergence test passes exactly one end-to-end two-client scenario. The current seven non-Firebase instrumentation classes pass all 18 tests together on API 36, including Room migration 5 to 6, private-image OCR, encrypted backup, receipt intent, and ledger-safety UI behavior. Direct/Play debug app and instrumentation APKs build, align, and verify; the Play debug AAB builds; both release variants compile and pass lint with no error/fatal findings. Release manifests keep `com.communityledger.app`, disable shared mode, blank the emulator host, reject cleartext, and remove `INTERNET` from Play; Firebase Auth, Firestore, and RTDB SDKs are absent from release runtime classpaths. Signed release packaging remains owner-gated because signing inputs are intentionally absent. Current receipt tests enforce a compact read-only detected amount, block weak, manually sourced, and non-finite amounts, assert Money in/out endpoint direction, and retain private evidence persistence and acknowledged deletion coverage. First-use disclosure, local identity gating, Trust Center navigation, and an honest receipt-interruption notice after process death have also been verified through runtime interaction. The bounded workflow at `.github/workflows/android-ci.yml` compiles, tests, and assembles both flavors with a 30-minute timeout; current local gates pass, while the current source still needs its hosted run.

## Launch Website

The static launch package lives in [site](site). It includes the product page, real privacy-safe app screenshots, Privacy, Terms, Contact, event-copy fallback, the historical old-package release manifest, and an independent initially-unpublished `com.communityledger.app` direct-update manifest. `.github/workflows/pages.yml` requires an explicit deployment; publishing a GitHub prerelease alone does not silently change either package-bound update channel.

The current signed [0.2.0-beta.2 limited public prerelease](https://github.com/GopiB9119/event/releases/tag/v0.2.0-beta.2) is documented in the [repository release notes](docs/Release/0.2.0-beta.2.md). Verify SHA-256 before installation. Physical-device breadth and qualified legal review remain incomplete beta risks.

Next recommended test work:

- Add more JVM-only parser tests for Amazon Pay, Paytm, BHIM, WhatsApp Pay, and noisier low-light receipts.
- For image OCR checks, use real private receipt screenshots only. Do not commit them. The focused instrumentation test can read images from either `app/src/androidTest/assets/receipt-images-private/` or the app-specific device folder `Android/data/com.communityledger.app/files/receipt-images-private/`.
- Add physical-device coverage for image sharing, interruption, and restart behavior.

Run the private image OCR test on a connected device:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:connectedDirectDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=com.communityledger.app.receipt.ReceiptImageOcrInstrumentedTest'
```

The test writes OCR/parse JSON reports to the app-specific external files directory and prints the report path in the test output. If no private receipt images exist, the test is skipped instead of using dummy data. In PowerShell, keep the `-P...` argument quoted.

## Engineering Doctrine

Read [prompts.md](prompts.md) before major changes. It contains the current product and engineering rules for receipt integrity, OCR, JSON storage, duplicate protection, member linking, and security language.
