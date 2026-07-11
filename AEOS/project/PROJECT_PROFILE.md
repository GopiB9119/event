# Project Profile: Community Ledger

## Product

Community Ledger is a local-first Android event finance ledger for shared community money.

Publisher: **Gopi Banoth**, publishing Community Ledger independently. Private support and privacy email: `banothgopikrishna19@gmail.com`.

Each installation owns an independent ledger. Event-copy links copy metadata but do not synchronize transactions, members, balances, or receipts.

## Current Stack

- Kotlin
- Jetpack Compose
- Material 3
- Room
- Google ML Kit Latin OCR
- Google ML Kit Devanagari OCR
- Static GitHub Pages launch package
- Manual HTTPS update manifest backed by GitHub Releases

## Critical Invariants

- No receipt data is invented.
- Receipt amount must be positive before save.
- Duplicate receipts cannot affect totals twice.
- Receipt transactions link to events and persisted members.
- Receipt JSON is stored in app-private storage.
- Public/private event visibility is a local marker; event-copy links are convenience links, not real authorization.
- A valid normalized local identity label is required for event ownership and receipt uploader writes; it is not authentication.
- Persisted transaction amounts are finite, positive, and use a supported ledger type.
- Event-copy link ID conflicts cannot replace an existing local ledger.

## Current Milestone

**Public Launch Trust Package**

Done when branding, in-app trust disclosures, legal/data inventory, responsive public website, release-gated downloads, and manual update checks are verified; broad publication still requires permanent signing, hosted deployment, and physical-device launch checks.

Current decision state: `DEFER` broad public distribution. Debug and API 36 evidence is `VERIFIED`; physical-device, hosted deployment, and permanent signing evidence remains `UNKNOWN`. See the [Mission Brief](MISSION_BRIEF.md).

Current decision and priority records:

- [Mission Brief](MISSION_BRIEF.md)
- [Evidence-Based Backlog](BACKLOG.md)
- [Knowledge Graph](KNOWLEDGE_GRAPH.md)

## Validation

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
```

## Current Risks

- Receipt parser is now a plain Kotlin `ReceiptParser` with focused JVM tests and regressions from six real ML Kit OCR image outputs; coverage still needs more payment apps and edge cases.
- Local identity is validated and required for owner/uploader actions, but remains self-declared and unauthenticated.
- Private events need server-issued invites and real authentication for enforceable multi-device authorization.
- The full unit/Robolectric suite passes and a bounded GitHub Actions workflow exists; repeatability remains unproven until its first hosted run.
- No export/restore exists; uninstall or device loss removes the ledger.
- OCR/review does not resume after process death, but a persisted marker warns that no transaction was saved; active navigation is not restored.
- The current APK is debug-signed; no permanent public update lineage exists until the release keystore is created and backed up.
- Pages deployment, permanent release signing, GitHub Release, legal review for target launch jurisdictions, and physical-device launch evidence remain open.
