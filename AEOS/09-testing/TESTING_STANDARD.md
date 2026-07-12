# Testing Standard

## Risk-Driven Layers

1. Pure unit tests for deterministic policy and transformation logic.
2. Contract tests for schemas, boundaries, compatibility, and error behavior.
3. Integration tests for persistence, process, network, and external-tool boundaries.
4. UI or end-to-end tests for critical user journeys and state transitions.
5. Operational and physical-environment checks for behavior automation cannot faithfully reproduce.

Use the lowest layer that can disprove the behavior. Higher-level coverage complements deterministic tests; it does not replace them.

## Required Cases

For each material invariant, cover the normal path and applicable malformed, missing, duplicate, boundary, cancellation, retry, concurrency, permission, outage, restart, migration, and rollback paths.

Every test should prove one named invariant. Avoid tests that only mirror implementation details or assert that a screen exists without exercising its outcome.

## Test Data

- Use deterministic synthetic data for routine automated tests.
- Use minimized, consented, access-controlled real fixtures only when synthetic data cannot reproduce the behavior.
- Do not commit secrets or unnecessary personal, health, financial, or production data.
- Record fixture provenance, expected fields, redaction, retention, and deletion when real data is required.

## Reliability

- Bound asynchronous waits using observable conditions rather than arbitrary sleeps.
- Give CI jobs an explicit timeout and preserve failure artifacts without sensitive data.
- Classify flaky tests as defects with an owner and bounded quarantine; repeated execution must not manufacture a pass.
- Verify the exact build, environment, platform, and configuration when they affect the result.

## Evidence

Record command, source revision, environment, test count, failures, skips, and relevant artifact identifiers. A partial or filtered run must not be described as the full suite.

Apply repository-specific test matrices and commands after this universal standard.
