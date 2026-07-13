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
| `ReceiptParserTest` | Has passed focused runs; covers real OCR regressions, large amounts, split balance noise, and counterparty cleanup |
| `LedgerTransactionPolicyTest` | Passed; covers positive finite amount/type/event invariants |
| `ReceiptImageOcrInstrumentedTest` | Passed on six private real screenshots on API 36 emulator; previously passed on a physical phone |
| `EventDaoIntegrityInstrumentedTest` | Passed on API 36 emulator; regular and import inserts fail closed so numeric-ID or opaque-key conflicts cannot replace an event or delete its transaction |
| `DatabaseRestartInstrumentedTest` | Passed; event/transaction survive file-backed Room close/reopen and migration 4 to 5 preserves them while assigning an opaque key |
| `SharedReceiptIntentInstrumentedTest` | Passed; shared text/image remain pending until explicit clear |
| `LedgerSafetyInstrumentedTest` | Focused API 36 run passed; compact review hides JSON/confidence text, labelled amounts survive missing optional fields, unlabelled amounts require confirmation, private evidence paths exist, Money out attribution is correct, and deletion remains acknowledged |
| `ExampleInstrumentedTest` | Passed with the real application ID |
| Complete Android instrumentation suite | Passed together on isolated API 36 AVD: 6 classes / 14 tests / zero failures |
| Signed release APK emulator smoke | Exact `0.2.0-beta.2` asset installed fresh and over beta.1 on API 36; launch, migration-preserving cold relaunch, and post-update-check health checks passed with no app crash/ANR exit record |
| Installed-app live update check | Public beta.2 APK on API 36 reached the deployed HTTPS manifest after an explicit Trust Center tap and reported `0.2.0-beta.2` current; no automatic check or silent install was observed |
| Full Robolectric/unit suite | Passed after the first post-beta.2 UI/performance slice: 11 suites, 61 tests, zero failures/errors/skips |
| Ledger presentation summary | Passed linked/fallback matching, exact event/member totals, duplicate-assignment prevention, and a synthetic 100-member / 10,000-transaction correctness fixture; no frame-time claim is inferred |
| Event-card UI contract | Passed independent open/delete callbacks, 48dp delete target, event-specific accessibility text, truthful public/private marker wording, and deterministic Pixel 8/API 36 screenshot capture |
| Screenshot tests | Greeting and privacy-safe EventCard images are included in the passing full suite; `recordRoborazziDebug` generated the EventCard baseline |
| First-use disclosure runtime check | Rendered, gated, accepted, and persisted after force-stop/relaunch on API 36 emulator |
| Local identity runtime check | Invalid email rejected; valid email normalized/persisted; guarded Create Event opened afterward; Cancel returned safely to Dashboard |
| Receipt process-death runtime check | SIGKILL during real-image OCR produced an interruption notice after cold relaunch; ledger remained at zero; acknowledgement persisted |
| Update manifest validation | Passed 11 focused cases covering unpublished/current/new releases, schema/hash validation, and deceptive URL rejection |
| Receipt attribution policy | Passed; uploader-derived default, confirmed uploader email, another-person isolation, and blank-person rejection |
| Website rendering | Five pages, three real privacy-safe screenshots, zero missing local targets; measured without horizontal overflow at 390x844 and 1440x1000 |
| Bounded GitHub Actions workflow | Required Android gate has a 30-minute timeout; both push and PR runs passed for the beta.2 channel commit, alongside both Site CI runs. The current uncommitted post-beta.2 slice has local full-suite/APK evidence only. |

## Required Test Matrix

| Area | Cases | Level |
|---|---|---|
| Ledger policy | zero, negative, NaN, infinity, invalid event/type | JVM unit |
| Parser | clear/noisy, missing amount/ref, unrelated numbers, each supported app | JVM unit from sanitized OCR text |
| OCR | real private images, low light, crop, rotation, large image, permission failure | Device instrumentation/manual |
| Room | event cascade, member SET_NULL, migration 2->3->4->5, numeric-ID and opaque-key insert-if-absent collision | Instrumentation |
| Invite | malformed, expired, modified, legacy, private marker, reused local ID, opaque-key deduplication | Integration/device |
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