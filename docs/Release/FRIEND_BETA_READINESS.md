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

- Explicit Room migrations; no destructive fallback
- Event/member/transaction foreign keys
- ML Kit-only OCR and private real-image checks
- Positive finite transaction write policy
- Duplicate receipt save blocking
- App-private receipt JSON and backup exclusions
- Deep-link collisions cannot replace local ledgers
- Full debug unit/Robolectric suite passed: 35 tests, zero failures/errors/skips
- API 36 Room collision, database reopen, shared text/image state, and app-context instrumentation passed
- Six private real-image OCR checks passed on the final emulator test APK
- Complete API 36 Android instrumentation suite passed: 5 classes / 6 tests / zero failures
- Two force-stop/cold-launch cycles completed and a real `ACTION_SEND text/plain` intent reached `MainActivity`
- First-use local-only/data-loss disclosure rendered, required acknowledgement, and remained dismissed after force-stop/relaunch
- Local identity gate rejected malformed input, normalized/persisted valid email, and blocked Create Event until saved
- Final APK smoke check confirmed identity-gated actions can be cancelled safely back to Dashboard
- SIGKILL during real-image OCR produced an interruption notice after cold relaunch; no transaction was saved and acknowledgement remained cleared after restart
- Original launcher mark, in-app Trust Center, manual update check, and truthful event-copy wording compiled and rendered on API 36
- Static launch package passed local link/script/JSON checks and measured desktop/mobile rendering with three real privacy-safe app screenshots
- Debug APK build passed after the integrity audit

## Current Debug Artifact

```text
Path: app/build/outputs/apk/debug/app-debug.apk
Package: com.aistudio.communityledger.yrtqwx
Version: 0.2.0-beta (2)
Minimum SDK: 24
Target SDK: 36
Size: 62,360,125 bytes
SHA-256: 83F237EB6877FD839F73879F3500853B3F2A48A263130B2724620505C0B468FE
Signature: APK Signature Scheme v2, Android Debug certificate
Alignment: verified with Android build-tools zipalign
```

This is a debug-signed beta artifact. Do not treat it as a production release or publish it to an app store.

## Remaining Gates Before Broader Sharing

1. On a physical phone, create an event, save a receipt, force-stop, reopen, and verify the visible ledger.
2. Share a real image from an external payment/gallery app and verify review/save/cancel/navigation behavior.
3. Repeat the final APK smoke flow on a second physical device and record Android versions.
4. Push `.github/workflows/android-ci.yml` and record its first green hosted run; the bounded command already passes locally.

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