# Encrypted Backup Foundation

Last reviewed: 14 July 2026

## Status

This is an internal codec foundation, not a user-facing recovery feature.

| Claim | State | Evidence or blocker |
|---|---|---|
| Version-1 archives round-trip and reject tested corruption on the JVM | `VERIFIED` | `EncryptedBackupCodecTest` covers 17 round-trip, boundary, wrong-password, tamper, truncation, invalid-compression, staging-path, and size-limit cases. |
| A failed authentication or decompression writes no plaintext to caller output | `VERIFIED` | Decryption authenticates and decompresses into private staging files before copying validated plaintext. |
| Required providers and a private-staging round trip work on Android API 36 | `VERIFIED` | `EncryptedBackupCodecInstrumentedTest` passed on a clean API 36 emulator with one test, zero skipped, and zero failed. |
| Required providers work on Android API 24 and 25 | `UNKNOWN` | This workspace has no API 24/25 runtime. Future UI must call `EncryptedBackupCodec.isSupported()` and a release must test the lowest supported Android runtime. |
| The logical manifest rejects tested ownership, identity, financial, and receipt-reference defects | `VERIFIED` | `ReceiptEvidencePolicyTest`, `LedgerBackupPayloadTest`, and `LedgerBackupPayloadBuilderTest` cover classification, validation, and source-mapping defects. |
| A consistent Room source snapshot and canonical logical-manifest encoding exist | `VERIFIED` | `LedgerSourceSnapshotTest` and `LedgerBackupManifestCodecTest` cover count preflight, stable transactional capture, deterministic ordering, exact integer parsing, strict UTF-8/JSON fields, semantic validation, and streaming size limits. |
| A bounded internal encrypted payload package exists | `VERIFIED` | `CanonicalReceiptEvidenceResolverTest`, `LedgerBackupPackageCodecTest`, and `EncryptedLedgerBackupPackageCodecTest` cover canonical receipt bytes, descriptor integrity, deterministic framing, fixed v1 compatibility hash, corruption, package bounds, encrypted round trip, and private-cache cleanup. No production UI calls this path. |
| Users can export, restore, or recover after uninstall | `NOT IMPLEMENTED` | The internal package is not connected to a source coordinator, Storage Access Framework flow, restore coordinator, conflict handling, or UI. |
| Reinstall restores data automatically | `NOT IMPLEMENTED` | Android backup exclusions remain active and there is no account or cloud recovery service. |

Uninstalling the current app still deletes its Room database, preferences, and receipt evidence. The codec does not change that behavior.

## Ownership Boundary

`EncryptedBackupCodec` transforms one caller-supplied byte stream into an encrypted archive and validates that archive on decryption. `LedgerBackupPackageCodec` defines the bounded plaintext package, and `EncryptedLedgerBackupPackageCodec` stages that package under Android's app-private cache before invoking encryption. Together they own:

- the versioned binary envelope;
- password-based key derivation;
- authenticated encryption and compression;
- input bounds;
- canonical manifest-to-evidence length, hash, count, ownership, and ordering checks;
- fail-closed validation before caller-visible plaintext;
- best-effort cleanup of private staging files and derived key bytes.

The foundation does not select a destination, store a password, coordinate source snapshot plus receipt resolution, resolve restore conflicts, rebuild receipt files, replace live Room state, or make a restore atomic.

## Internal Package Foundation

The repository contains a side-effect-free, `TEST`-stage logical manifest, transactional Room source snapshot, deterministic source-entity mapper, canonical JSON manifest codec, portable receipt canonicalizer, bounded package frame, and encrypted bridge. No production UI calls them. This internal foundation establishes these boundaries before export or restore:

- `payloadVersion` identifies logical manifest semantics; `sourceSchemaVersion` is tied to the current Room schema constant.
- `backupId` is a logical archive identifier. It can detect the same archive later, but it is not permission to merge or replace data.
- Event, member, and transaction `sourceId` values are source-database references only. Restore must allocate destination IDs and remap every relationship.
- `eventKey` is preserved as event-copy identity, not authentication or proof that two copies contain the same ledger. A destination conflict must not trigger automatic merge or replacement.
- `paymentReference` preserves the optional payment-app reference. It is not globally unique and must never be used as a restore primary key.
- Source user email and theme are informational `sourcePreferences`. Restore must not silently overwrite destination identity or settings.
- Receipt transactions reference logical evidence IDs. A bounded structured classifier reserves one nonblank top-level `receiptFilePath` for source evidence, preserves non-JSON free-text markers as manual notes, and rejects nested, array-root, malformed, duplicate, non-string, blank, mixed, oversized, or over-depth path shapes.
- Receipt descriptors record the owning source transaction, SHA-256, and byte count. Canonicalization validates the confined source file against its transaction, strips source-local event/path/time fields, removes local duplicate-row IDs, writes strict canonical UTF-8 JSON, and derives the descriptor from those exact bytes.
- Source rows map in deterministic source-ID order. Supplied orphan rows, cross-event member links, duplicate source IDs, per-event duplicate evidence IDs, invalid amounts/types, non-finite event totals, invalid normalized member names, and mismatched receipt ownership fail closed. Identical canonical evidence bytes may exist in separate events and are keyed by their globally unique source transaction plus content ID.
- Room preflights record counts and captures events, members, and transactions through one `@Transaction` DAO operation with primary-key ordering.
- The manifest codec writes fixed-order UTF-8 JSON directly through a 16 MiB bounded output, sorts source records, and rejects malformed UTF-8/JSON, duplicate, unknown, or missing fields, unsupported versions, invalid semantics, overlong strings, and excessive records.
- The package codec binds the manifest to sorted evidence records and rejects missing, duplicate, unknown, reordered, truncated, oversized, hash-mismatched, count-mismatched, and trailing data.
- The encrypted bridge derives a unique operation directory under `Context.cacheDir`, validates the complete plaintext frame before encryption or return, and removes its owned staging directory in `finally`.

The next integration decision must connect one transactional source snapshot to receipt canonicalization and package creation, define cancellation/process-death behavior, and design a separately reviewed restore transaction. The current foundation does not authorize export or restore UI.

## Plaintext Package Version-1 Format

All integers are big-endian. The package is plaintext only inside app-private staging before encryption.

| Offset | Size | Field |
|---:|---:|---|
| 0 | 4 | ASCII magic `CLPK` |
| 4 | 4 | Package format version `1` |
| 8 | 4 | Canonical manifest byte length |
| 12 | 4 | Receipt-evidence record count |
| 16 | variable | Canonical manifest bytes |
| next | variable | Sorted receipt-evidence records |

Each evidence record contains a 32-byte lowercase hexadecimal content ID, 4-byte source transaction ID, 4-byte evidence length, and the exact canonical evidence bytes. Records sort by source transaction ID and then content ID. Version 1 permits at most 512 KiB per evidence record, 16 MiB of aggregate evidence bytes, a 16 MiB manifest, and a 32 MiB complete plaintext package. A fixed synthetic package SHA-256 test prevents silent framing changes while the format version remains `1`.

## Encrypted Archive Version-1 Format

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

Base archive decryption uses two files inside a caller-provided private staging directory:

1. authenticated compressed bytes;
2. bounded, decompressed plaintext.

Nothing is copied to caller output until authentication and decompression both finish. Wrong passwords, modified headers or ciphertext, unsupported parameters, truncation, malformed compression, and configured size-limit violations fail closed: no invalid plaintext is written to caller output. Registered staging files receive best-effort deletion in `finally` blocks; if normal deletion fails, the codec attempts to truncate the file and delete it again. Files can remain after filesystem failure, abrupt process death, or power loss, and flash storage does not provide a portable secure-overwrite guarantee.

The package bridge does not accept an arbitrary filesystem path: it derives one unique canonical child directory under `Context.cacheDir` for each operation and deletes that owned directory afterward. Lower-level `EncryptedBackupCodec` callers still must provide trusted app-private staging. Production export and restore coordinators must write to private temporary destinations first because an output-stream failure can leave a partially copied destination. Integration must remove stale recovery staging directories during startup before enabling restore.

PBKDF2 key derivation necessarily runs before AES-GCM can authenticate an archive. A selected file may therefore consume the accepted 600,000-to-2,000,000-iteration work factor even when its ciphertext is invalid. Restore must require an explicit user action, serialize attempts, apply a bounded cooldown after failures, and never automatically process untrusted files in the background.

## Required Export Integration

Before export can be built:

1. Build one coordinator that uses the transactional logical source snapshot, resolves every referenced receipt, constructs the package, and aborts without output on any mismatch. Never copy an open Room database file.
2. Validate every amount, relationship, identifier, evidence reference, size, and payload checksum before encryption.
3. Define cancellation, low-storage, process-death, and stale-staging cleanup behavior.
4. Call `isSupported()` before presenting the action. Show an honest unsupported-device state; do not add a weaker fallback format.
5. Encrypt the complete package through the app-private bridge and publish the destination atomically.
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
