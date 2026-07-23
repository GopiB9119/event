# Test Strategy

## Risk Order

1. Ledger mutation and calculation integrity
2. Receipt OCR/parser evidence quality
3. Room relationships, migrations, and conflict behavior
4. Share-intent and deep-link lifecycle
5. Shared authorization, idempotency, evidence review, convergence, and revocation
6. Process death, restart, permissions, and large-image handling
7. UI usability and accessibility

## Current Evidence

| Check | Status |
|---|---|
| Both flavored Kotlin compiles | `compileDirectDebugKotlin` and `compilePlayDebugKotlin` pass |
| Debug artifacts | Direct/Play app APKs and instrumentation APKs build; all four are zip-aligned and signature-verified under JBR 21; Play debug AAB builds and its JAR signature verifies |
| Release isolation | Direct/Play release Kotlin compiles and lint pass; both use `com.communityledger.app`, disable the shared backend, blank the emulator host, and reject cleartext; Play release removes `INTERNET`; Firebase Auth/Firestore/RTDB SDKs are absent from both release runtime classpaths |
| Release lint advisories | Both reports contain zero errors/fatal findings, 46 warnings, and 3 hints; current findings are dependency/version, KTX-style, unused-resource, allocation/style, and inlined-API advisories |
| Firebase backend contract | 51/51 emulator tests pass across Auth, Firestore, Functions, and Realtime Database rules/mutations |
| Android shared convergence | Passed exactly one API 36 test: two anonymous clients create/invite/join, see membership and approximate presence, submit pending entries, review private evidence, confirm, and converge on equal history/totals |
| `ReceiptParserTest` | Has passed focused runs; covers real OCR regressions, large amounts, split balance noise, and counterparty cleanup |
| `LedgerTransactionPolicyTest` | Passed; covers positive finite amount/type/event invariants |
| `ReceiptImageOcrInstrumentedTest` | Passed on six private real screenshots on API 36 emulator; previously passed on a physical phone |
| `EventDaoIntegrityInstrumentedTest` | Passed on API 36 emulator; regular and import inserts fail closed so numeric-ID or opaque-key conflicts cannot replace an event or delete its transaction |
| `DatabaseRestartInstrumentedTest` | Passed; event/transaction survive file-backed Room close/reopen, migration 4 to 5 preserves them while assigning an opaque key, and migration 5 to 6 creates Room-compatible shared link/nullable pre-link operation tables |
| `SharedReceiptIntentInstrumentedTest` | Passed; shared text/image remain pending until explicit clear |
| `LedgerSafetyInstrumentedTest` | All 5 tests pass; compact review hides JSON/confidence text, reliable labelled amounts remain read-only, weak/unlabelled amounts cannot be manually overridden, private evidence paths exist, Money out attribution is correct, and deletion remains acknowledged |
| `ExampleInstrumentedTest` | Passed with the real application ID |
| Complete local Android instrumentation suite | Current 7-class / 18-test non-Firebase source passes together on an isolated API 36 AVD in 127.456 seconds; the separate Firebase convergence class passes its 1 test against local emulators |
| Signed release APK emulator smoke | Exact `0.2.0-beta.2` asset installed fresh and over beta.1 on API 36; launch, migration-preserving cold relaunch, and post-update-check health checks passed with no app crash/ANR exit record |
| Installed-app live update check | Public beta.2 APK on API 36 reached the deployed HTTPS manifest after an explicit Trust Center tap and reported `0.2.0-beta.2` current; no automatic check or silent install was observed |
| Full Robolectric/unit suite | Both Direct and Play variants pass: 26 suites, 177 tests each, zero failures/errors/skips |
| Backup package compatibility | Original version-1/schema-5 compatibility frame retains its pinned SHA-256 and decodes under current schema 6; unsupported source schemas still fail closed |
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
| Room | event cascade, member SET_NULL, migrations 2->3->4->5->6, shared-table nullability, numeric-ID and opaque-key insert-if-absent collision | Instrumentation |
| Invite | malformed, expired, modified, legacy, private marker, reused local ID, opaque-key deduplication | Integration/device |
| Shared receipt | image/text, cancel, navigate away, new intent during OCR, restart | UI/device |
| Persistence | app restart, process death, upgrade, uninstall warning | Device/manual |
| Accessibility | TalkBack, text scaling, touch targets, long content | Manual/device |
| Local identity | malformed email, normalization, persistence, guarded create/invite/receipt actions | JVM/UI/device |
| Receipt attribution | OCR counterparty differs from contributor, another person does not inherit uploader email, Money in/out totals | JVM/UI/device |
| Transaction deletion | cancel, disabled confirm, acknowledged permanent delete, totals after delete, restart | UI/device |
| Shared authorization | public discovery, private invite, role projections, revoked/non-member denial, evidence scope | Emulator integration |
| Shared convergence | restart replay, idempotent retries, two clients, pending review, equal totals/history, presence | API 36 instrumentation / physical devices |

## Release Gates

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDirectDebugUnitTest :app:testPlayDebugUnitTest
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDirectDebug :app:assemblePlayDebug
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDirectDebugAndroidTest :app:assemblePlayDebugAndroidTest
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectReleaseKotlin :app:compilePlayReleaseKotlin
.\gradlew.bat --no-daemon --no-configuration-cache :app:lintDirectRelease :app:lintPlayRelease
.\gradlew.bat --no-daemon --no-configuration-cache :app:bundlePlayDebug
.\firebase\functions\node_modules\node\bin\node.exe .\firebase\functions\scripts\runEmulators.js
```

Signed release APK/AAB packaging additionally requires the publisher-owned keystore path, alias, and passwords. Those credentials are intentionally absent from the repository and were not requested during local isolation validation.

Run high-risk Room/OCR tests directly through Android instrumentation on a connected device.

## Test Data Policy

- Never commit real receipt images or PII.
- Private OCR images remain ignored.
- Runtime production paths contain no sample or dummy financial values.
- Deterministic synthetic values are allowed only inside isolated tests where they prove an invariant.

## Highest Testing Blocker

Verify actual image sharing from an external payment/gallery app, cancellation/navigation during OCR, and final APK behavior on at least two physical devices. Shared mode additionally needs physical two-phone convergence against an owner-approved cloud environment; local API 36 convergence is complete. Repeat the verified process-death recovery on a physical device and obtain the first green hosted run of `.github/workflows/android-ci.yml` for the current source.

## Emulator Test Environment

The API 36 Google Play AVD required 4 GB RAM, host GPU, Play Store disabled, and ahead-of-time package compilation. With the default 2 GB/SwiftShader/first-boot update load, Android killed the app with a bind-application ANR before tests started. The shared convergence runner accepts only one ready `emulator-*` serial, never an accidentally connected physical phone; Firebase child npm checks run offline and the debug callable transport has a bounded 120-second read allowance for cold Functions startup.