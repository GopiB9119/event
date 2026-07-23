# Physical Device Launch Matrix

Use this matrix with the exact candidate recorded in [Friend Beta Readiness](../Release/FRIEND_BETA_READINESS.md). The goal is evidence, not a demonstration. Stop on silent ledger mutation, unexplained totals, data loss, blocked upgrade, or misleading synchronization/access-control behavior.

## Safety Rules

- Use a dedicated test device/profile or test ledger. Do not risk the only copy of important financial records.
- Never use real member contact data in screenshots, Issues, logs, or committed evidence.
- Real receipt fixtures remain private and ignored. Record only redacted expected/actual fields.
- Do not uninstall or clear app data unless the device contains disposable test data and the test explicitly requires it.
- Airplane mode, permission changes, clock changes, and process termination are manual, visible actions. Restore the device afterward.
- The script below never clears data, uninstalls, changes permissions/time/network, or force-stops the app.

## Candidate

The candidate below is historical evidence for the published old-package beta. Current source uses `com.communityledger.app`, which is a separate app and requires a new candidate record, hash, signing record, and full matrix. Do not treat this old-package matrix as evidence for the new Play identity.

```text
Package: com.aistudio.communityledger.yrtqwx
Version: 0.2.0-beta.2 (4)
Release asset: community-ledger-0.2.0-beta.2.apk
Size: 56,257,295 bytes
SHA-256: 1E007E7045A4A909CC0202F90AFCFB3C27D53CDDAF1428F1FEFAC0241EFA3685
Signing certificate SHA-256: BC1415F8C2236009109CBDA483F351AB9F2C379B7E9A7661599D369E2FACA3CF
Signature: permanent Community Ledger release certificate, APK Signature Scheme v2
Scope: current limited public prerelease; broader-reliance claims remain blocked until this matrix and other open gates pass
```

Clean-emulator evidence for this exact candidate is complete: all 14 Android tests passed on API 36, the signed APK verified and installed over beta.1 with `-r`, a synthetic beta.1 event survived migration 4 to 5 and cold relaunch, and Android recorded no Community Ledger crash/ANR. Emulator evidence does not satisfy the physical-device rows below.

## Preflight

Current execution status (11 July 2026): `BLOCKED` after a partial Galaxy A54 run. The preflight script was parser-validated and its no-device, missing-APK, and hash-mismatch paths returned the documented codes without installing, clearing data, or writing an evidence record.

### Partial Device A Evidence

- Device: Samsung SM-A546E, Android 16 / SDK 36, ARM64.
- Candidate size/hash matched and fresh installation succeeded without a scripted clear or uninstall.
- The user completed onboarding and created a disposable private event with one member and one reviewed receipt transaction.
- Intentional force-stop/cold launch completed in 1.261 seconds; the event, private marker, member, and transaction remained visible. Android exit history contained only the intentional force-stop and no Community Ledger crash/ANR.
- Trust Center opened and exposed About, Privacy, and Terms tabs.
- The complete isolated Android instrumentation suite passed 6 tests in 12.615 seconds; raw private OCR output is retained only in ignored local `build/` evidence.
- At 19:48:16 Samsung Package Installer displayed its uninstall UI (`UninstallLaunch`). At 19:48:23 Android broadcast that `com.aistudio.communityledger.yrtqwx` was fully removed. The initiator is `UNKNOWN`; no shell uninstall command was issued, so this evidence must not be attributed to the app or test runner without further proof.
- Full removal deleted the disposable local ledger as documented. The app was not reinstalled automatically after the removal.

Device A verdict: `BLOCKED`. The partial evidence above applies to the superseded `83F237...B468FE` candidate. Repeat the complete matrix with the current signed `1E007E...A3685` candidate, explicit coordination, disposable data, and no concurrent device interaction; confirm whether the previous uninstall was intentional before reinstalling.

Connect one unlocked phone with USB debugging enabled and accept the RSA prompt.

Download or copy the official `community-ledger-0.2.0-beta.2.apk` asset into the repository root before using the default commands below. The APK is ignored by Git. Alternatively, pass `-ApkPath` with its absolute location; the script still requires the exact SHA-256 recorded above.

Inspect only:

```powershell
.\scripts\physical-device-preflight.ps1 `
	-PackageName com.aistudio.communityledger.yrtqwx `
	-ApkPath .\community-ledger-0.2.0-beta.2.apk `
	-ExpectedSha256 1E007E7045A4A909CC0202F90AFCFB3C27D53CDDAF1428F1FEFAC0241EFA3685
```

Install/update while preserving existing app data:

```powershell
.\scripts\physical-device-preflight.ps1 -Install
```

Resume command after connecting one physical phone:

```powershell
.\scripts\physical-device-preflight.ps1 -Install
```

When multiple phones are attached, pass `-Serial`. Do not commit the generated `build/physical-device-preflight.json`.

## Device Record

| Field | Device A | Device B |
|---|---|---|
| Manufacturer/model | | |
| Android version / SDK | | |
| ABI | | |
| Fresh install or update | | |
| Candidate SHA-256 matched | | |
| Test date | | |
| Tester | | |

## Acceptance Matrix

Use `PASS`, `FAIL`, `BLOCKED`, or `NOT APPLICABLE`. Attach redacted evidence location, not private data.

| Flow | Device A | Device B | Evidence / observed result |
|---|---|---|---|
| APK installs or updates without data clear | | | |
| Cold launch reaches first-use disclosure or existing Dashboard | | | |
| First-use disclosure blocks entry until limits are accepted | | | |
| Trust Center shows publisher, Privacy, Terms, support, and version | | | |
| Local identity rejects malformed email and normalizes valid email | | | |
| Create Event works with synthetic title/duration | | | |
| Dashboard shows the event and truthful local-only copy | | | |
| Event survives force-stop and cold relaunch | | | |
| Scan Receipt picker opens without broad storage permission | | | |
| External Gallery/payment-app share reaches the selected event review | | | |
| Receipt review keeps OCR counterparty separate from contributor/vendor attribution | | | |
| Money in changes Total Collected; Money out changes Total Spent | | | |
| Cancel during image selection/processing/review creates no transaction | | | |
| Real private receipt OCR shows source text, amount, reference/receiver evidence, confidence, and warnings | | | |
| Low-confidence/missing-amount/duplicate receipt cannot mutate totals | | | |
| Save confirmed receipt creates one transaction and changes the correct total exactly once | | | |
| Saved transaction/evidence survives force-stop and relaunch | | | |
| Delete icon opens warning; cancel/disabled confirm preserves entry; acknowledged delete changes totals once | | | |
| Receipt process termination shows interruption notice and saves nothing | | | |
| Airplane mode: local event/ledger and on-device OCR remain usable | | | |
| Airplane mode: manual update check fails honestly without affecting local data | | | |
| Permission denial/cancel returns safely with no mutation | | | |
| Large/rotated/cropped image fails safely or reaches review without crash | | | |
| Event-copy link adds an independent shell on Device B | | | |
| Device B can open a link whose source Room ID matches an unrelated local event; both ledgers remain intact | | | |
| Opening the same opaque-key link again reopens one local shell instead of creating a duplicate | | | |
| Share icon/copy explains that opening the link does not add a live member | | | |
| Device A transaction does not appear on Device B; both devices state no sync | | | |
| Public/private remains a local marker, not access control | | | |
| Delete/correction authority uses the local identity rules visibly | | | |
| Uninstall data-loss warning is visible before relying on the ledger | | | |
| No app crash/ANR attributable to Community Ledger during the matrix | | | |

## Boundary Checks

### Time And Expiry

- Generate an event-copy link with a normal expiry and open it before expiry.
- Verify an expired link is rejected with event-copy wording.
- Do not change the device clock unless using disposable test conditions; record timezone and any manual clock change.

### Restart And Process Death

- Force-stop from Android Settings or `adb shell am force-stop <package>` only after recording the current state.
- For debug-only process-death testing, background the app and use a controlled `run-as` process kill. Do not clear data.
- Confirm unsaved OCR state does not become a transaction and the interruption notice clears after acknowledgment.

### Offline State

- Enter airplane mode manually.
- Confirm local CRUD/ledger viewing works.
- Test OCR with a previously working private fixture.
- Confirm update check reports failure/unavailability without blocking the app.
- Restore connectivity and confirm local data is unchanged.

## Failure Record

For every failure, record:

```text
Flow:
Device/Android:
Precondition:
Exact steps:
Expected:
Actual:
Ledger/data impact:
Reproduces after restart: yes/no/unknown
Redacted logs/evidence:
Severity hypothesis:
Stop-ship: yes/no, with reason
```

Open a public issue only when the report contains no private/security-sensitive data. Use [SECURITY.md](../../SECURITY.md) for suspected vulnerabilities.

## Verdict

```text
Device A: PASS / FAIL / BLOCKED
Device B: PASS / FAIL / BLOCKED
Exact candidate hash verified: yes/no
All stop conditions clear: yes/no
Remaining unknowns:
Tester:
Review date:
Verdict: BLOCKED / CONDITIONAL / READY FOR STATED TRUSTED-BETA SCOPE
```

This matrix cannot authorize broad public distribution by itself. Permanent release signing, hosted CI/Pages/Release evidence, legal review scope, and independent Decision Challenge remain separate gates.