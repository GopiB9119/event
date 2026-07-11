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

```text
Package: com.aistudio.communityledger.yrtqwx
Version: 0.2.0-beta (2)
APK: app/build/outputs/apk/debug/app-debug.apk
Size: 62,360,125 bytes
SHA-256: 83F237EB6877FD839F73879F3500853B3F2A48A263130B2724620505C0B468FE
Signature: Android debug certificate, APK Signature Scheme v2
Scope: trusted beta only; not the permanent public signing lineage
```

## Preflight

Connect one unlocked phone with USB debugging enabled and accept the RSA prompt.

Inspect only:

```powershell
.\scripts\physical-device-preflight.ps1
```

Install/update while preserving existing app data:

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
| Cancel during image selection/processing/review creates no transaction | | | |
| Real private receipt OCR shows source text, amount, reference/receiver evidence, confidence, and warnings | | | |
| Low-confidence/missing-amount/duplicate receipt cannot mutate totals | | | |
| Save confirmed receipt creates one transaction and changes the correct total exactly once | | | |
| Saved transaction/evidence survives force-stop and relaunch | | | |
| Receipt process termination shows interruption notice and saves nothing | | | |
| Airplane mode: local event/ledger and on-device OCR remain usable | | | |
| Airplane mode: manual update check fails honestly without affecting local data | | | |
| Permission denial/cancel returns safely with no mutation | | | |
| Large/rotated/cropped image fails safely or reaches review without crash | | | |
| Event-copy link adds an independent shell on Device B | | | |
| Event-copy collision cannot replace an unrelated local event | | | |
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