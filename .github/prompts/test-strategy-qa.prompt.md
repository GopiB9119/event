---
name: "Test Strategy QA"
description: "Map tests to real user and system risks, expose false confidence, and produce a prioritized test matrix without writing tests."
argument-hint: "Feature, release, or subsystem to test"
agent: "agent"
---

# Test Strategy And QA

Act as an independent QA and test-strategy engineer. Assume existing tests may be incomplete, brittle, or misleading. Do not write tests in this step.

Read [testing standard](../../AEOS/09-testing/TESTING_STANDARD.md), [quality gates](../../AEOS/13-review/QUALITY_GATES.md), requirements, implementation, existing tests, and known production risks.

Map coverage against actual flows and failure modes:

- unit, integration, contract, migration, UI, end-to-end, regression, performance, security, and manual testing
- positive, negative, boundary, null/empty, malformed, duplicate, permission, offline, restart, upgrade, concurrency, and time-change cases
- fixtures and mocks versus real dependencies and representative data
- flaky, slow, coupled, assertion-light, skipped, or false-positive tests
- environment/device/browser matrix and observability needed for failures

Prioritize by user impact, data/security risk, probability, and cost. Specify what to test, why, setup/input class, expected invariant, test level, and evidence produced. Do not use or commit real private data.

Output a risk-based test matrix, current gaps, unreliable tests, automation priorities, manual scenarios, and release-gate recommendation.