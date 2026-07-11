# Changelog

All notable public release changes will be recorded here. This file does not make an artifact publicly released; GitHub Releases and the signed release manifest are authoritative for distribution.

## Unreleased - 0.2.0-beta Candidate

### Added

- On-device ML Kit Latin and Devanagari receipt OCR with human review.
- Receipt confidence, duplicate, positive-amount, and ledger-integrity gates.
- Local event/member/transaction persistence with explicit Room migrations.
- First-use local-only disclosure and receipt process-interruption notice.
- Trust Center with About, Privacy, Terms, publisher identity, and manual update checks.
- Original Community Ledger launcher mark and privacy-safe launch-site screenshots.
- Static launch site with Privacy, Terms, Contact, event-copy fallback, and fail-closed release manifest.
- AEOS v0.2 decision intelligence, evidence-based backlog, mission brief, and knowledge graph.

### Changed

- Event links are described as independent event copies, not synchronized membership.
- Public/private is described as a local marker, not access control.
- Manual update metadata accepts only validated official GitHub Release APK asset URLs.

### Security And Privacy

- App-private database, receipt evidence, and preferences remain excluded from Android backup/device transfer.
- Runtime paths contain no dummy/random/filename-derived receipt values or cloud OCR fallback.
- Public download remains disabled until a permanent release-signed APK passes all gates.

### Known Limits

- No account, authentication, authorization, cross-device synchronization, export, restore, or recovery.
- OCR can be wrong and requires review.
- Current candidate is debug-signed and must not be used as the permanent public release channel.

## Releases

No GitHub Release has been published yet.