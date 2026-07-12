# Changelog

All notable public release changes will be recorded here. This file does not make an artifact publicly released; GitHub Releases and the signed release manifest are authoritative for distribution.

## Unreleased - 0.2.0-beta.1 Candidate

### Added

- On-device ML Kit Latin and Devanagari receipt OCR with human review.
- Receipt confidence, duplicate, positive-amount, and ledger-integrity gates.
- Local event/member/transaction persistence with explicit Room migrations.
- First-use local-only disclosure and receipt process-interruption notice.
- Trust Center with About, Privacy, Terms, publisher identity, and manual update checks.
- Original Community Ledger launcher mark and privacy-safe launch-site screenshots.
- Static launch site with Privacy, Terms, Contact, event-copy fallback, and fail-closed release manifest.
- AEOS v0.3 decision intelligence, bounded loop contracts, evidence-based backlog, mission brief, and knowledge graph.

### Changed

- Event links are described as independent event copies, not synchronized membership.
- Public/private is described as a local marker, not access control.
- Manual update metadata accepts only validated official GitHub Release APK asset URLs.
- Receipt review separates the ledger contributor/payer or vendor from OCR Paid to/counterparty evidence.
- New receipt review labels Money in versus Money out and explains which total changes.
- Authorized transaction deletion now requires a permanent-deletion warning and explicit acknowledgement.

### Security And Privacy

- App-private database, receipt evidence, and preferences remain excluded from Android backup/device transfer.
- Runtime paths contain no dummy/random/filename-derived receipt values or cloud OCR fallback.
- Permanent release signing and an independently backed-up key lineage are established; public download remains disabled until hosted and device gates pass.

### Known Limits

- No account, authentication, authorization, cross-device synchronization, export, restore, or recovery.
- OCR can be wrong and requires review.
- The current candidate is release-signed but remains unpublished until hosted and physical-device gates pass.

## Releases

No GitHub Release has been published yet.