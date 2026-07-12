# Community Ledger Knowledge Graph

Last evidence sync: 12 July 2026

This is a routing map, not a substitute for source inspection. Update it only when verified behavior or ownership changes. Preserve unknowns as unknowns.

## Product And Maturity

```text
Gopi Banoth (independent publisher)
  -> Community Ledger 0.2.0-beta.1
  -> local organizer ledger
  -> money in / money out / reviewed receipt evidence
  -> independent installation per device
  -> no account, authorization, synchronized ledger, export, or recovery
```

Current milestone: **Beta Reliability Validation**. See [Project Profile](PROJECT_PROFILE.md) and [Product Overview](../../docs/Product/PRODUCT_OVERVIEW.md).

## Runtime Entry And Navigation

```text
Android launch/share/event-copy intent
  -> MainActivity
  -> EventViewModel intent handling
  -> AppContent
  -> manual Screen stack
       -> Dashboard
       -> CreateEvent
       -> EventDetails(eventId)
       -> TrustCenter
```

Ownership:

- Android entrypoint: [MainActivity](../../app/src/main/java/com/example/MainActivity.kt)
- navigation, UI flows, and review dialogs: [Screens](../../app/src/main/java/com/example/ui/Screens.kt)
- privacy/about/terms/update UI: [Trust Center](../../app/src/main/java/com/example/ui/TrustCenterScreen.kt)
- application state and orchestration: [EventViewModel](../../app/src/main/java/com/example/ui/EventViewModel.kt)

## Data And State

```text
EventEntity 1 -> many MemberEntity
EventEntity 1 -> many TransactionEntity
MemberEntity 1 -> many TransactionEntity (nullable memberId)

Room
  -> events / members / transactions
  -> explicit migrations 2 -> 3 -> 4 -> 5

SharedPreferences app_prefs
  -> local identity
  -> beta acknowledgement
  -> theme/link counters
  -> non-sensitive receipt interruption marker

App-private files
  -> reviewed receipt JSON under files/receipts/
```

Ownership:

- entities, DAO, migrations, and constraints: [Database](../../app/src/main/java/com/example/data/Database.kt)
- persistence boundary: [Repository](../../app/src/main/java/com/example/data/Repository.kt)
- transaction write invariant: [Ledger Transaction Policy](../../app/src/main/java/com/example/data/LedgerTransactionPolicy.kt)
- local identity invariant: [Local Identity Policy](../../app/src/main/java/com/example/data/LocalIdentityPolicy.kt)
- architecture detail: [Architecture Overview](../../docs/Architecture/OVERVIEW.md)

## Critical Flows

### Event Creation

```text
Dashboard create action
  -> valid local identity required
  -> CreateEvent form
  -> EventViewModel.createEvent
  -> Room event row
  -> local Dashboard list
```

### Receipt Evidence

```text
picker or Android share intent
  -> selected/shared URI or text
  -> ML Kit Latin + Devanagari OCR for images
  -> ReceiptParser
  -> amount/reference/receiver/date + confidence/warnings
  -> duplicate and positive-amount gates
  -> compact human review of amount and receipt details
  -> app-private JSON evidence
  -> validated Room transaction
  -> collected/spent/balance totals
```

Ownership:

- parser: [Receipt Parser](../../app/src/main/java/com/example/receipt/ReceiptParser.kt)
- OCR rules: [Receipt OCR Playbook](../../.ai/RECEIPT_OCR_PLAYBOOK.md)
- decision record: [Receipt Integrity ADR](../../docs/Decisions/ADR-0001-RECEIPT-INTEGRITY.md)

### Event-Copy Link

```text
organizer shares expiring checksum link
  -> recipient app validates shape/checksum/expiry
  -> find by opaque event-copy key
  -> add metadata shell with a new local Room ID when absent
  -> open independent local event
```

The link does not authenticate people or synchronize members, receipts, transactions, or balances. Future shared-event requirements are in [Future Shared Events](../../docs/Architecture/SHARED_EVENTS_FUTURE.md).

### Manual Update Check

```text
Trust Center button
  -> HTTPS GitHub Pages release manifest
  -> schema/version/hash/official GitHub Release APK URL validation
  -> show unpublished/current/available/failure state
  -> browser opens official release only after user action
```

Ownership:

- update contract: [Update Checker](../../app/src/main/java/com/example/update/UpdateChecker.kt)
- static manifest: [Latest Release Manifest](../../site/releases/latest.json)
- distribution rules: [Signing And Distribution](../../docs/Release/SIGNING_AND_DISTRIBUTION.md)

## Website And Release

```text
GitHub Issues
  -> bug form / product evidence form
  -> private security reports bypass public issues
  -> decision state and validation evidence
  -> Pull Request
  -> Android CI / Site CI

site/
  -> product page
  -> Privacy / Terms / Contact
  -> event-copy fallback
  -> real privacy-safe app screenshots
  -> release manifest points to the verified v0.2.0-beta.1 GitHub asset

manual Pages workflow (deployed)
  -> static-site validation preflight
  -> explicit workflow_dispatch only

GitHub prerelease v0.2.0-beta.1 (published)
  -> permanent release-signed APK
  -> checksum + certificate fingerprint + notes
```

Ownership:

- website source: [Launch Site](../../site/index.html)
- repository governance: [GitHub Repository Operations](../../docs/GitHub/REPOSITORY_OPERATIONS.md)
- security reporting: [Security Policy](../../SECURITY.md)
- contribution status: [Contributing](../../CONTRIBUTING.md)
- non-deploying site validation: [Site CI](../../.github/workflows/site-ci.yml)
- Pages gate: [Pages Workflow](../../.github/workflows/pages.yml)
- public launch gates: [Public Launch Checklist](../../docs/Release/PUBLIC_LAUNCH_CHECKLIST.md)
- privacy source: [Privacy Policy](../../docs/Legal/PRIVACY_POLICY.md)
- data inventory: [Data And Permissions](../../docs/Legal/DATA_AND_PERMISSIONS.md)

## Trust Boundaries And Invariants

- Receipt OCR is evidence, not truth; no save without human review and policy gates.
- Runtime receipt values must come from user input or real ML Kit output; no dummy, random, filename, Gemini, or cloud fallback.
- Transaction amounts are finite, positive, and use a supported type.
- Event-copy key conflicts cannot replace an existing local ledger; Room IDs remain local.
- Local email is a self-declared label, not authentication.
- Public/private is a local marker, not access control.
- Receipt/database/preferences are app-private and excluded from Android backup/device transfer.
- Manual update checks are user-triggered and accept only official GitHub Release APK asset URLs.
- Website and update manifest accept only the published release-signed GitHub APK asset.
- The public repository has no open-source license; external code merging remains closed until the publisher chooses explicit terms.

See [Security Playbook](../../.ai/SECURITY_PLAYBOOK.md) and [Data And Permissions](../../docs/Legal/DATA_AND_PERMISSIONS.md).

## Verification Graph

```text
source change
  -> compileDebugKotlin
  -> testDebugUnitTest
  -> assembleDebug + assembleDebugAndroidTest
  -> API 36 instrumentation for Room/share/OCR
  -> APK hash/signature/alignment
  -> docs and project memory sync
```

Current verified evidence is recorded in [Test Strategy](../../docs/Testing/TEST_STRATEGY.md) and [Friend Beta Readiness](../../docs/Release/FRIEND_BETA_READINESS.md).

## Known Unknowns And Open Gates

- `UNKNOWN`: demand and retention evidence beyond the current trusted beta.
- Permanent signing lineage, hosted CI, Pages, GitHub prerelease, ruleset, and security-alert evidence are recorded.
- Project fields/views and license selection require owner action.
- Final flows still require a physical phone and a second device.
- Legal text has not been reviewed for target launch jurisdictions.
- Export/restore, authentication, authorization, sync, revocation, and conflict resolution do not exist.
- OCR coverage needs more payment apps and poor-image conditions.

Do not convert these items into completed capability without new evidence.