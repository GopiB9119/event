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
- Opaque event-copy keys cannot replace an existing local ledger; Room integer IDs remain device-local.

## Current Milestone

**Event-Copy Reliability And Shared-Ledger Decision**

The signed `0.2.0-beta.2` limited GitHub prerelease is public and anonymously verified. The currently deployed Pages manifest still points to beta.1 until the reviewed beta.2 manifest source is merged and manually deployed. Broader reliance still requires physical-device breadth, qualified legal review, and user-value evidence.

Current decision state: limited public prerelease published with explicit beta limits; `BUILD` collision-safe event-copy identity; `TEST` the first account/server-identity shared-ledger slice after owner inputs; `DEFER` Play production and synchronized-ledger claims. Signing, emulator, hosted CI, Pages, release, and public-download integrity are `VERIFIED`; complete physical-device, legal, backend ownership, and market-demand evidence remain incomplete. See the [Mission Brief](MISSION_BRIEF.md).

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
- The full unit/Robolectric suite and hosted Android CI pass; required Android CI is enforced on the default branch.
- No export/restore exists; uninstall or device loss removes the ledger.
- OCR/review does not resume after process death, but a persisted marker warns that no transaction was saved; active navigation is not restored.
- The permanent release key is verified and independently backed up; `v0.2.0-beta.1` established the public signing lineage and beta.2 preserves it.
- The beta.2 GitHub prerelease is public. Pages/update-channel deployment is the current release operation; legal review and complete physical-device launch evidence remain open.
