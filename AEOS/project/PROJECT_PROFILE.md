# Project Profile: Community Ledger

## Product

Community Ledger is a local-first Android event finance ledger for shared community money.

Publisher: **Gopi Banoth**, publishing Community Ledger independently. Private support and privacy email: `banothgopikrishna19@gmail.com`.

The published beta remains local-only: each installation owns an independent ledger, and event-copy links copy metadata without synchronizing transactions, members, balances, or receipts. Current debug source also contains a separate Firebase-emulator shared-event mode; it is not enabled in release builds.

## Current Stack

- Kotlin
- Jetpack Compose
- Material 3
- Room
- Google ML Kit Latin OCR
- Google ML Kit Devanagari OCR
- Static GitHub Pages launch package
- Direct-build manual HTTPS update manifest backed by GitHub Releases
- Google Play distribution flavor without the external APK checker
- Debug-only Firebase Auth, Firestore, Functions transport, and Realtime Database integration against `demo-community-ledger` emulators

## Critical Invariants

- No receipt data is invented.
- Receipt amount must be positive, reliable, receipt-derived, and read-only during review.
- Duplicate receipts cannot affect totals twice.
- Receipt transactions link to events and persisted members.
- Receipt JSON is stored in app-private storage.
- Public/private event visibility is a local marker; event-copy links are convenience links, not real authorization.
- A valid normalized local identity label is required for event ownership and receipt uploader writes; it is not authentication.
- Persisted transaction amounts are finite, positive, and use a supported ledger type.
- Opaque event-copy keys cannot replace an existing local ledger; Room integer IDs remain device-local.

## Current Milestone

**Post-Beta.2 Shared-Event Emulator Acceptance**

The signed `0.2.0-beta.2` limited GitHub prerelease remains local-only. In current unreleased source, the server-authority contract passes 51 emulator tests and the Android debug client passes API 36 two-client convergence for membership, presence, pending evidence review, history, and totals. Release shared mode remains disabled.

Current decision state: debug/emulator shared integration is `VERIFIED`; `DEFER` cloud creation, billing, production deployment, Play production, and public synchronized-ledger claims. Physical two-phone acceptance, account recovery, legal/privacy updates, backend ownership, operations, and market-demand evidence remain incomplete. See the [Shared Ledger Implementation Plan](../../docs/Architecture/SHARED_LEDGER_IMPLEMENTATION_PLAN.md) and [ADR-0003](../../docs/Decisions/ADR-0003-SHARED-EVENT-AUTHORITY-AND-PRESENCE.md).

Current decision and priority records:

- [Mission Brief](MISSION_BRIEF.md)
- [Evidence-Based Backlog](BACKLOG.md)
- [Knowledge Graph](KNOWLEDGE_GRAPH.md)

## Validation

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
```

## Current Risks

- Receipt parser is now a plain Kotlin `ReceiptParser` with focused JVM tests and regressions from six real ML Kit OCR image outputs; coverage still needs more payment apps and edge cases.
- Local-event identity remains self-declared and unauthenticated. Shared debug events use a separate silent anonymous Firebase identity and server-issued membership.
- Anonymous shared identity is installation-scoped; uninstall/data clearing has no recovery unless the user can rejoin or a future account-linking flow exists.
- The full unit/Robolectric suite and hosted Android CI pass; required Android CI is enforced on the default branch.
- No export/restore exists; uninstall or device loss removes the ledger.
- OCR/review does not resume after process death, but a persisted marker warns that no transaction was saved; active navigation is not restored.
- The permanent release key is verified and independently backed up; `v0.2.0-beta.1` established the public signing lineage and beta.2 preserves it.
- The beta.2 GitHub prerelease and Pages update channel are live; an installed beta.2 build reported itself current after an explicit Trust Center check. Legal review and complete physical-device launch evidence remain open.
