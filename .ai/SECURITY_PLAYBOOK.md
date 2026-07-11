# Security Playbook

Community Ledger handles event money records and personal receipt data. Treat this as sensitive local data.

## Protected Assets

- Event ledgers
- Receipt JSON files
- Member names
- Phone numbers
- Email addresses
- UPI IDs
- Transaction/reference IDs
- Local identity setting

## Implemented Controls

- App-private receipt file storage.
- Backup exclusion for database, receipt files, and identity preferences.
- Room migrations instead of destructive migration.
- Event cascade delete.
- Persisted members with `memberId` transaction links.
- Duplicate receipt save blocking.
- Confidence gate before ledger mutation.
- Positive finite transaction policy at coordinator and repository boundaries.
- Conflict-safe invite event insertion that cannot replace an existing local ledger.
- Valid normalized local identity required at ownership/uploader write boundaries.
- Non-sensitive persisted marker detects receipt OCR/review interruption without storing receipt content or image URIs.
- Manual update checks use HTTPS without redirects and accept only validated APK assets from the official GitHub repository release path.
- Website downloads remain gated while the release manifest is unpublished; no debug APK is presented as the permanent public channel.
- Local invite checksum with honest wording.

## Known Limits

- No real authentication.
- Local email identity is self-declared and can be changed by the device user.
- Invite links are not server-authorized.
- Private/public event visibility is a local marker, not access control.
- Device owner can access local app data on rooted/debuggable devices.
- OCR can still misread receipts.
- Parser coverage still needs more payment apps and poor-image fixtures.
- No user-controlled export or recovery after uninstall/device loss.

## Required Posture

Do not claim the app is secure beyond the controls above. Say what is enforced, what is detected, and what remains a risk.
