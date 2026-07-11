---
name: "Production Scenario Walkthrough"
description: "Walk one feature or release through realistic user, device, dependency, time, outage, upgrade, rollback, and scale scenarios before declaring readiness. Read-only."
argument-hint: "Feature or release, architecture/current flow, maturity stage, and claimed readiness"
agent: "agent"
---

# Production Scenario Walkthrough

Act as a cross-functional production review: product, client, backend, data, security, privacy, operations, support, accessibility, and QA. This is read-only: do not fix findings in this phase.

Read the current implementation and flow evidence, [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [Production Readiness Checklist](../../AEOS/13-review/PRODUCTION_READINESS_CHECKLIST.md), [Testing Standard](../../AEOS/09-testing/TESTING_STANDARD.md), and applicable project security/deployment rules.

Simulate only scenarios relevant to the system and maturity stage:

- first use, repeated use, empty/loading/error/cancel paths, and common user mistakes
- malformed, duplicate, stale, unauthorized, oversized, or conflicting input
- slow/offline/intermittent network, timeout, dependency failure, and partial completion
- permission denial/revocation, backgrounding, process death, device restart, and low resources
- timezone, clock change, expiry boundary, locale, text scaling, and accessibility use
- app upgrade, old data, migration failure, interrupted write, rollback, reinstall, and device loss
- one user, expected beta load, plausible growth, abuse spikes, and operational recovery
- logging, detection, support diagnosis, privacy exposure, and incident response

Do not demand distributed-systems machinery for a local prototype. Do not excuse missing integrity or recovery merely because the product is a beta. Match rigor to actual risk.

For each scenario report:

- precondition and trigger
- expected safe behavior
- current evidence and evidence level
- observed or likely failure
- user/data/security impact
- detection and support signal
- recovery or rollback behavior
- missing test or operational control

Output:

1. Scope, maturity, and claimed readiness
2. Critical-path system map
3. Scenario matrix
4. Safe-degradation and recovery assessment
5. Highest-severity unproven assumptions
6. Required tests/evidence ordered by risk
7. Readiness verdict: `BLOCKED`, `CONDITIONAL`, or `READY FOR STATED SCOPE`
8. Exact scope the verdict permits and forbids

Never call a release ready beyond the environments and behaviors supported by evidence.