---
name: "Completion Audit"
description: "Assume a feature is incomplete and verify requirements, user flows, states, edge cases, security, docs, and runtime evidence before release."
argument-hint: "Feature, mission, or release candidate to audit"
agent: "agent"
---

# Completion Audit

Act as an independent completion auditor. Do not implement fixes during the audit.

Read [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [production readiness checklist](../../AEOS/13-review/PRODUCTION_READINESS_CHECKLIST.md), [quality gates](../../AEOS/13-review/QUALITY_GATES.md), [testing standard](../../AEOS/09-testing/TESTING_STANDARD.md), [Behavior Runtime](../../AEOS/16-behavior-system/RUNTIME_AND_ACTIVATION.md), requirements, and implementation evidence.

Assume the feature is incomplete until proven otherwise. Verify:

- each requirement and non-requirement
- every affected button, navigation path, API/interface, state transition, and persistence mutation
- loading, empty, success, failure, retry, permission, offline, duplicate, restart, upgrade, and time-change behavior as applicable
- security, privacy, authorization, input validation, secrets, and sensitive logging
- accessibility, responsiveness, and user-facing copy
- migrations, rollback, observability, supportability, docs, and project memory
- compile, tests, static analysis, runtime behavior, and produced artifacts
- when behavior packs were used: selected signal/domain/item IDs, context budget, authority ceiling, routing conflicts, and deactivation evidence

Label material claims `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`. Output the originating decision state, a requirement-to-evidence matrix, uncovered scenarios, findings by severity, release-blocking items, residual risk, and a final verdict: Not Ready, Conditionally Ready, or Ready with Evidence. Never upgrade a `TEST`, `DEFER`, or `STOP` product decision into release readiness merely because code exists.