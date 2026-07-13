# Encrypted Backup Foundation

Last reviewed: 13 July 2026

## Status

This is an internal codec foundation, not a user-facing recovery feature.

| Claim | State | Evidence or blocker |
|---|---|---|
| Version-1 archives round-trip and reject tested corruption on the JVM | `VERIFIED` | `EncryptedBackupCodecTest` covers 17 round-trip, boundary, wrong-password, tamper, truncation, invalid-compression, staging-path, and size-limit cases. |
| A failed authentication or decompression writes no plaintext to caller output | `VERIFIED` | Decryption authenticates and decompresses into private staging files before copying validated plaintext. |
| Required providers and a private-staging round trip work on Android API 36 | `VERIFIED` | `EncryptedBackupCodecInstrumentedTest` passed on a clean API 36 emulator with one test, zero skipped, and zero failed. |
| Required providers work on Android API 24 and 25 | `UNKNOWN` | This workspace has no API 24/25 runtime. Future UI must call `EncryptedBackupCodec.isSupported()` and a release must test the lowest supported Android runtime. |
| Users can export, restore, or recover after uninstall | `NOT IMPLEMENTED` | No payload bundle, Storage Access Framework flow, restore coordinator, or UI exists. |
| Reinstall restores data automatically | `NOT IMPLEMENTED` | Android backup exclusions remain active and there is no account or cloud recovery service. |

Uninstalling the current app still deletes its Room database, preferences, and receipt evidence. The codec does not change that behavior.

## Ownership Boundary

`EncryptedBackupCodec` transforms one caller-supplied byte stream into an encrypted archive and validates that archive on decryption. It owns:

- the versioned binary envelope;
- password-based key derivation;
- authenticated encryption and compression;
- input bounds;
- fail-closed validation before caller-visible plaintext;
- best-effort cleanup of private staging files and derived key bytes.

It does not define which Room rows, receipt files, or preferences belong in a backup. It does not take a consistent database snapshot, select a destination, store a password, validate the decrypted application payload, resolve restore conflicts, replace live state, or make a restore atomic.

## Version-1 Format

All integers are big-endian. The 43-byte header is authenticated as AES-GCM additional authenticated data.

| Offset | Size | Field |
|---:|---:|---|
| 0 | 4 | ASCII magic `CLBK` |
| 4 | 4 | Format version `1` |
| 8 | 1 | KDF identifier `1` for PBKDF2-HMAC-SHA256 |
| 9 | 4 | PBKDF2 iteration count |
| 13 | 1 | Salt length `16` |
| 14 | 1 | Nonce length `12` |
| 15 | 16 | Random salt |
| 31 | 12 | Random AES-GCM nonce |
| 43 | variable | AES-GCM ciphertext followed by its 16-byte authentication tag |

Version 1 uses:

- PBKDF2-HMAC-SHA256 with 600,000 iterations and a 256-bit derived key;
- AES-256-GCM with a 96-bit random nonce and 128-bit authentication tag;
- GZIP before encryption;
- a minimum 12-character password;
- a default 64 MiB plaintext limit;
- accepted iteration counts from 600,000 through 2,000,000 to bound attacker-controlled key-derivation work;
- an encrypted-payload limit of the selected plaintext limit plus 1 MiB.

The 12-character rule is only a minimum input check, not an entropy guarantee. The caller must clear the password `CharArray` after use. The app must never persist, log, sync, or include the password in the archive. Losing the password means losing access to that backup; there is no password reset path.

## Failure Behavior

Decryption uses two files inside a caller-provided private staging directory:

1. authenticated compressed bytes;
2. bounded, decompressed plaintext.

Nothing is copied to caller output until authentication and decompression both finish. Wrong passwords, modified headers or ciphertext, unsupported parameters, truncation, malformed compression, and configured size-limit violations fail closed: no invalid plaintext is written to caller output. Registered staging files receive best-effort deletion in `finally` blocks; if normal deletion fails, the codec attempts to truncate the file and delete it again. Files can remain after filesystem failure, abrupt process death, or power loss, and flash storage does not provide a portable secure-overwrite guarantee.

Every caller must explicitly pass a staging directory. Android production code must use an app-private cache directory such as `context.cacheDir`; shared or world-readable directories are forbidden. Production export and restore coordinators must also write to private temporary destinations first because an output-stream failure can still leave a partially copied destination. Integration must remove stale codec staging files during startup before enabling restore.

PBKDF2 key derivation necessarily runs before AES-GCM can authenticate an archive. A selected file may therefore consume the accepted 600,000-to-2,000,000-iteration work factor even when its ciphertext is invalid. Restore must require an explicit user action, serialize attempts, apply a bounded cooldown after failures, and never automatically process untrusted files in the background.

## Required Export Integration

Before export can be built:

1. Define a versioned logical payload manifest for events, members, transactions, preferences, and receipt evidence.
2. Take a consistent snapshot without copying an open Room database file.
3. Validate every amount, relationship, identifier, evidence reference, size, and payload checksum before encryption.
4. Call `isSupported()` before presenting the action. Show an honest unsupported-device state; do not add a weaker fallback format.
5. Build and encrypt the complete archive in app-private temporary storage.
6. Use Android's Storage Access Framework `ACTION_CREATE_DOCUMENT` flow to let the user choose the destination. Do not request broad storage access.
7. Remove private temporary files on success, cancellation, process interruption, and failure.

## Required Restore Integration

Before restore can be described as recovery:

1. Use `ACTION_OPEN_DOCUMENT`; treat the selected document and all decoded fields as untrusted input.
2. Decrypt only into app-private staging with strict compressed, plaintext, record-count, and per-file limits.
3. Validate manifest version, checksums, paths, schema, finite positive transaction amounts, ownership, foreign keys, duplicate identifiers, receipt evidence, and recalculated totals.
4. Construct and validate a temporary Room database and staged receipt tree. Never copy an imported database over an open live Room database.
5. Show the user what will be restored and use a separately reviewed, rollback-capable reconciliation operation. No imported row may silently replace unrelated live ledger data.
6. Keep current durable state unchanged on wrong password, corruption, cancellation, low storage, process death, validation failure, or reconciliation failure.
7. Delete decrypted staging data on every terminal path.

Release evidence must include the lowest supported Android runtime, API 36, a physical device, wrong password, tampering, cancellation, process death, low storage, duplicate/live-data conflicts, export followed by uninstall/reinstall, and exact ledger/evidence reconciliation. A passing codec unit test alone is not recovery evidence.

## Format Evolution

New code must continue reading supported older formats long enough to migrate valid user archives, while new exports use only the latest approved format. Any change to fields, KDF semantics, cipher semantics, or payload framing requires a new format version and cross-version fixtures. Removing old-format read support requires a separately approved migration and user-notice decision.
