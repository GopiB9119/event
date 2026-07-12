# Test Strategy

## Risk Order

1. Ledger mutation and calculation integrity
2. Receipt OCR/parser evidence quality
3. Room relationships, migrations, and conflict behavior
4. Share-intent and deep-link lifecycle
5. Process death, restart, permissions, and large-image handling
6. UI usability and accessibility

## Current Evidence

| Check | Status |
|---|---|
| `compileDebugKotlin` | Reliable required baseline |
| `ReceiptParserTest` | Has passed focused runs; covers real OCR regressions |
| `LedgerTransactionPolicyTest` | Passed; covers positive finite amount/type/event invariants |
| `ReceiptImageOcrInstrumentedTest` | Passed on six private real screenshots on API 36 emulator; previously passed on a physical phone |
| `EventDaoIntegrityInstrumentedTest` | Passed on API 36 emulator; collision cannot replace event or delete transaction |
| `DatabaseRestartInstrumentedTest` | Passed; event and transaction survive file-backed Room close/reopen |
| `SharedReceiptIntentInstrumentedTest` | Passed; shared text/image remain pending until explicit clear |
| `LedgerSafetyInstrumentedTest` | Passed; Money out stores the OCR counterparty without inheriting uploader identity, and deletion requires acknowledgement while cancel preserves the entry |
| `ExampleInstrumentedTest` | Passed with the real application ID |
| Complete Android instrumentation suite | Passed together: 6 classes / 8 tests / zero failures |
| Signed release APK emulator smoke | Exact `0.2.0-beta.1` asset installed on a clean API 36 emulator; first launch and force-stop/cold relaunch passed with no crash/ANR exit record |
| Full Robolectric/unit suite | Passed: 8 suites, 39 tests, zero failures/errors/skips |
| Screenshot test | Included in the passing full suite |
| First-use disclosure runtime check | Rendered, gated, accepted, and persisted after force-stop/relaunch on API 36 emulator |
| Local identity runtime check | Invalid email rejected; valid email normalized/persisted; guarded Create Event opened afterward; Cancel returned safely to Dashboard |
| Receipt process-death runtime check | SIGKILL during real-image OCR produced an interruption notice after cold relaunch; ledger remained at zero; acknowledgement persisted |
| Update manifest validation | Passed 11 focused cases covering unpublished/current/new releases, schema/hash validation, and deceptive URL rejection |
| Receipt attribution policy | Passed; uploader-derived default, confirmed uploader email, another-person isolation, and blank-person rejection |
| Website rendering | Five pages, three real privacy-safe screenshots, zero missing local targets; measured without horizontal overflow at 390x844 and 1440x1000 |
| Bounded GitHub Actions workflow | Added with a 30-minute timeout; YAML and exact release command validated locally; first hosted run pending |

## Required Test Matrix

| Area | Cases | Level |
|---|---|---|
| Ledger policy | zero, negative, NaN, infinity, invalid event/type | JVM unit |
| Parser | clear/noisy, missing amount/ref, unrelated numbers, each supported app | JVM unit from sanitized OCR text |
| OCR | real private images, low light, crop, rotation, large image, permission failure | Device instrumentation/manual |
| Room | event cascade, member SET_NULL, migration 2->3->4, insert-if-absent collision | Instrumentation |
| Invite | malformed, expired, modified, legacy, private marker, local ID collision | Integration/device |
| Shared receipt | image/text, cancel, navigate away, new intent during OCR, restart | UI/device |
| Persistence | app restart, process death, upgrade, uninstall warning | Device/manual |
| Accessibility | TalkBack, text scaling, touch targets, long content | Manual/device |
| Local identity | malformed email, normalization, persistence, guarded create/invite/receipt actions | JVM/UI/device |
| Receipt attribution | OCR counterparty differs from contributor, another person does not inherit uploader email, Money in/out totals | JVM/UI/device |
| Transaction deletion | cancel, disabled confirm, acknowledged permanent delete, totals after delete, restart | UI/device |

## Release Gates

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDebugUnitTest
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDebug
```

Run high-risk Room/OCR tests directly through Android instrumentation on a connected device.

## Test Data Policy

- Never commit real receipt images or PII.
- Private OCR images remain ignored.
- Runtime production paths contain no sample or dummy financial values.
- Deterministic synthetic values are allowed only inside isolated tests where they prove an invariant.

## Highest Testing Blocker

Verify actual image sharing from an external payment/gallery app, cancellation/navigation during OCR, and final APK behavior on at least two physical devices. Repeat the verified process-death recovery on a physical device and obtain the first green hosted run of `.github/workflows/android-ci.yml`.

## Emulator Test Environment

The API 36 Google Play AVD required 4 GB RAM, host GPU, Play Store disabled, and ahead-of-time package compilation. With the default 2 GB/SwiftShader/first-boot update load, Android killed the app with a bind-application ANR before tests started.