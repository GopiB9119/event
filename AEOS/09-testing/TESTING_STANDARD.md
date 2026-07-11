# Testing Standard

## Test Pyramid

1. Pure unit tests for deterministic logic.
2. Integration tests for persistence boundaries.
3. UI tests for critical flows.
4. Manual device tests for OCR and platform share intents.

## Deterministic Logic

Receipt parsing and ledger write policy live in plain Kotlin and require focused JVM tests.

Test cases should include:

- clear receipt
- noisy receipt
- missing amount
- missing transaction ID
- duplicate receipt
- unrelated balance number
- local-script label
- Samsung Pay approval code
- zero, negative, NaN, and infinite amounts
- invalid event ID and unsupported ledger type

## Persistence Testing

Room instrumentation must cover:

- event cascade delete
- member delete with transaction `memberId` set null
- migrations 2 -> 3 -> 4
- conflict-safe event insertion
- transaction preservation on invite ID collision

## Device Testing

Use instrumentation/manual checks for:

- private real-image OCR fixtures
- Android image/text share intents
- deep-link join and collision handling
- cancel/navigation during OCR
- force-stop/restart persistence
- permissions, large images, and process death

## Unit Test Infrastructure

The apparent `0 tests completed` stall was caused by deep-link Robolectric tests polling asynchronous Room work for only one second and asserting stale copy. Use a bounded result waiter for ViewModel coroutine tests.

The complete debug unit suite is a required gate:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDebugUnitTest
```

Establish repeatability in CI with a job timeout before calling the suite production-stable.

## Test Output

Every test should prove one invariant.
