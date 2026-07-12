# Changelog

All notable public release changes will be recorded here. This file does not make an artifact publicly released; GitHub Releases and the signed release manifest are authoritative for distribution.

## 0.2.0-beta.2 - 2026-07-13

### Changed

- Receipt review now shows a compact amount and four human-readable details instead of raw JSON or an overall confidence percentage.
- OCR processing now uses a neutral progress indicator without terminal-like scripts or staged fake delays.
- Clearly labelled amounts can be saved when optional app/reference details are missing; unlabelled amounts require explicit confirmation against the original receipt.
- The parser no longer applies an undocumented ₹150,000 ceiling and rejects split available-balance values when selecting the payment amount.
- Event-copy links use opaque cross-device keys instead of reusing one device's Room integer ID.
- Legacy event-copy links remain accepted; they never attach to an existing row by ambiguous numeric ID/title/organizer label. Reopening a link for a shell imported before migration can create one additional empty shell.
- Reopening one opaque-key link returns to the same local shell without duplicating it.
- Share-copy controls no longer use add-member imagery and explicitly state that sharing does not create membership.

### Data Integrity

- Receipt JSON remains app-private and must be written before the ledger transaction; evidence-file failure leaves the ledger unchanged.
- Receipt evidence and Room persistence run through one awaited operation; database failure removes the newly written evidence file.
- Evidence filenames include a full UUID so receipt replacement cannot overwrite prior proof with the same payment reference.
- Private receipt JSON records both the OCR amount and any amount confirmed or changed during review.
- Room migration 4 to 5 assigns unique event keys while preserving existing events, members, and transactions.
- The browser fallback accepts both new `eventKey` links and legacy `eventId` links.

### Still Not Implemented

- Event copies do not synchronize members, receipts, uploader activity, transactions, balances, utilization, event details, or custom information.
- Real shared events remain gated on accounts, authorization, revocable invitations, server-confirmed revisions, conflict handling, audit history, recovery, and privacy operations.

## 0.2.0-beta.1 - 2026-07-12

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
- Permanent release signing and an independently backed-up key lineage are established.
- The official GitHub prerelease and HTTPS launch site provide the APK, checksum, Privacy, Terms, Contact, and fail-closed release metadata.

### Known Limits

- No account, authentication, authorization, cross-device synchronization, export, restore, or recovery.
- OCR can be wrong and requires review.
- Physical-device breadth and qualified legal review remain incomplete; this release is a limited beta, not a production-readiness claim.

## Releases

- [0.2.0-beta.2](https://github.com/GopiB9119/event/releases/tag/v0.2.0-beta.2) - limited public beta prerelease
- [0.2.0-beta.1](https://github.com/GopiB9119/event/releases/tag/v0.2.0-beta.1) - limited public beta prerelease