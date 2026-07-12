# Friend Beta Readiness

## Verdict

**Conditionally ready for a small trusted-device beta. Not ready as a synchronized shared-ledger product.**

## Safe Scope

- Install the debug APK on a small number of trusted Android devices.
- Use each installation as an independent local ledger.
- Test event creation, receipt OCR/review, duplicate blocking, member linking, totals, restart, and deletion.
- Do not use it as the only record for sensitive or irreplaceable money data.

## Blocking Claims

Do not claim:

- transactions synchronize between friends
- private events enforce access
- event-copy links authenticate people or create synchronized membership
- data can be recovered after uninstall/device loss
- production-grade multi-device test coverage exists

## Verified Release Evidence

The counts in this section describe the published `0.2.0-beta.1` artifact and are retained as historical release evidence.

- Explicit Room migrations; no destructive fallback
- Event/member/transaction foreign keys
- ML Kit-only OCR and private real-image checks
- Positive finite transaction write policy
- Duplicate receipt save blocking
- App-private receipt JSON and backup exclusions
- Deep-link collisions cannot replace local ledgers
- Full debug unit/Robolectric suite passed: 39 tests, zero failures/errors/skips
- API 36 Room collision, database reopen, shared text/image state, and app-context instrumentation passed
- Six private real-image OCR checks passed on the final emulator test APK
- Complete API 36 Android instrumentation suite passed: 6 classes / 8 tests / zero failures
- Two force-stop/cold-launch cycles completed and a real `ACTION_SEND text/plain` intent reached `MainActivity`
- First-use local-only/data-loss disclosure rendered, required acknowledgement, and remained dismissed after force-stop/relaunch
- Local identity gate rejected malformed input, normalized/persisted valid email, and blocked Create Event until saved
- Receipt review separates ledger contributor/vendor attribution from OCR counterparty evidence and labels Money in versus Money out
- Transaction deletion requires permanent-impact acknowledgement before the hard delete is enabled
- Final APK smoke check confirmed identity-gated actions can be cancelled safely back to Dashboard
- SIGKILL during real-image OCR produced an interruption notice after cold relaunch; no transaction was saved and acknowledgement remained cleared after restart
- Original launcher mark, in-app Trust Center, manual update check, and truthful event-copy wording compiled and rendered on API 36
- Static launch package passed local link/script/JSON checks and measured desktop/mobile rendering with three real privacy-safe app screenshots
- Debug APK build passed after the integrity audit
- Rebuilt candidate passed the complete API 36 Android suite: 6 classes / 8 tests / zero failures
- Signed `0.2.0-beta.1` APK installed on a clean API 36 emulator, launched successfully, and cold-relaunched after force-stop with no crash/ANR record
- Installed public APK manual update check reached the live manifest only after an explicit tap and reported `0.2.0-beta.1` current

## Beta.2 Release Evidence

- Receipt review is compact and human-readable; raw JSON, overall confidence percentages, terminal-like logs, and fake processing delays are removed.
- Amount evidence is gated separately from optional detail completeness; large labelled amounts and balance-noise regressions pass, while unlabelled amounts require explicit confirmation.
- Receipt evidence-file persistence and the Room mutation run in one awaited operation; database failure removes the new file, and the saved private JSON records its own path.
- Full debug unit/Robolectric suite passed: 9 suites / 54 tests / zero failures, errors, or skips.
- Complete isolated API 36 instrumentation suite passed: 6 classes / 14 tests / zero failures.
- Room migration 4 to 5 preserved an existing event and transaction amount while assigning an opaque event key.
- Reused numeric IDs from unrelated devices create distinct local shells; repeated opaque-key links reopen one shell.
- Regular and event-copy inserts fail closed; duplicate opaque keys cannot replace a ledger or delete its transaction.
- Static join fallback accepts both new `eventKey` and legacy `eventId` links; five-page site validation reports zero errors.
- This evidence does not implement or verify live member, receipt, transaction, total, utilization, or event-detail synchronization.

## Current Signed Public Beta

```text
Release asset: community-ledger-0.2.0-beta.2.apk
Package: com.aistudio.communityledger.yrtqwx
Version: 0.2.0-beta.2 (4)
Minimum SDK: 24
Target SDK: 36
Size: 56,257,295 bytes
SHA-256: 1E007E7045A4A909CC0202F90AFCFB3C27D53CDDAF1428F1FEFAC0241EFA3685
Signing certificate SHA-256: BC1415F8C2236009109CBDA483F351AB9F2C379B7E9A7661599D369E2FACA3CF
Signature: APK Signature Scheme v2, permanent Community Ledger release certificate
Alignment: verified with Android build-tools zipalign
```

This release-signed artifact is published as a limited GitHub prerelease for voluntary testing. It is not production-ready, and incomplete physical-device coverage remains an explicit beta risk.

## Remaining Gates Before Broader Reliance

1. Repeat the complete physical-phone matrix with the signed `1E007E...A3685` candidate; partial evidence from superseded candidates is documented separately.
2. Share a real image from an external payment/gallery app and verify review/save/cancel/navigation behavior.
3. Repeat the final APK smoke flow on a second physical device and record Android versions.
4. Obtain qualified legal review for intended launch jurisdictions; do not claim regulatory compliance before that review.

## Rollback

The beta has no remote rollout control. Rollback means uninstalling or installing a previous APK. Uninstalling deletes local data, so there is currently no safe rollback for user-created ledgers.

## Highest-Priority Milestone

**Public Launch Trust Package**

Done when:

- local-only limitations are visible in the app
- transaction, invite collision, Room reopen, share-state, and real-image OCR invariants are tested on-device
- the full unit suite is repeatable in hosted CI
- restart and share-intent flows pass on at least two real devices
- permanent release signing, deployed HTTPS routes, release metadata, and update checks are verified
- users explicitly accept unrecoverable local data before use; export/restore remains a decision gate before wider reliance