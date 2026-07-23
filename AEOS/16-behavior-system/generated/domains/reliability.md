# Reliability Engineering

Keep critical user outcomes correct and available across faults, restarts, retries, and degradation.

- **Domain ID:** `reliability`
- **Boundary:** reliability engineering versus product risk acceptance
- **Invariant:** expected faults do not silently corrupt state or violate critical outcomes
- **Default evidence:** failure injection, restart, retry, degradation, capacity, and recovery observations
- **Risk classes:** reliability, operations

## Behavior (10)

### `behavior-reliability-choose-falsifier`

Reliability Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of reliability model, SLO evidence, fault tests, and recovery runbook that could disprove the current hypothesis.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-communicate-uncertainty`

Reliability Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'intermittent failures become data loss, prolonged outage, or false success', and the next discriminating check.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-establish-state`

Reliability Engineering: Establish Current State. Inspect reliability model, SLO evidence, fault tests, and recovery runbook and record the current behavior before proposing change.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-identify-owner`

Reliability Engineering: Identify Owner And Boundary. Name the owner of reliability model, SLO evidence, fault tests, and recovery runbook, the boundary 'reliability engineering versus product risk acceptance', and who may decide or mutate it.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-minimize-change`

Reliability Engineering: Make The Smallest Useful Change. Change only the owning slice of reliability model, SLO evidence, fault tests, and recovery runbook needed to protect 'expected faults do not silently corrupt state or violate critical outcomes'.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-protect-invariant`

Reliability Engineering: Protect The Domain Invariant. Reject an option that can violate 'expected faults do not silently corrupt state or violate critical outcomes' without an approved mitigation.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-stop-and-escalate`

Reliability Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'contain the fault, restore the critical path, and add a regression or monitor' to the accountable owner.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-surface-assumptions`

Reliability Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'expected faults do not silently corrupt state or violate critical outcomes'.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-update-memory`

Reliability Engineering: Update Durable Knowledge. Update the decision or memory record for reliability model, SLO evidence, fault tests, and recovery runbook with provenance and invalidation triggers.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

### `behavior-reliability-validate-immediately`

Reliability Engineering: Validate Immediately. Run failure injection, restart, retry, degradation, capacity, and recovery observations or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Reliability Engineering, this behavior operates on reliability model, SLO evidence, fault tests, and recovery runbook, uses failure injection, restart, retry, degradation, capacity, and recovery observations, and protects 'expected faults do not silently corrupt state or violate critical outcomes'.

## Failure (6)

### `failure-reliability-boundary-violation`

Reliability Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for service reliability and failure recovery.

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

### `failure-reliability-evidence-overclaim`

Reliability Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

### `failure-reliability-premature-action`

Reliability Engineering: Premature Action. happy-path design omits fault containment, idempotency, or recovery evidence

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

### `failure-reliability-silent-failure`

Reliability Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

### `failure-reliability-stale-context`

Reliability Engineering: Stale Context. The state of reliability model, SLO evidence, fault tests, and recovery runbook changed while routing continued from a stale checkpoint.

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

### `failure-reliability-unbounded-loop`

Reliability Engineering: Unbounded Repair Loop. Failures do not trigger a reset of happy-path design omits fault containment, idempotency, or recovery evidence.

**Domain delta:** In Reliability Engineering, this failure threatens 'expected faults do not silently corrupt state or violate critical outcomes' through happy-path design omits fault containment, idempotency, or recovery evidence.

## Signal (4)

### `signal-reliability-constraint-risk`

Reliability Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'expected faults do not silently corrupt state or violate critical outcomes' for service reliability and failure recovery.

**Domain delta:** For Reliability Engineering, this signal observes service reliability and failure recovery through reliability model, SLO evidence, fault tests, and recovery runbook while rejecting stale or untrusted substitutes.

### `signal-reliability-explicit-mission`

Reliability Engineering: Explicit Mission Signal. The current user request explicitly concerns service reliability and failure recovery and states an observable outcome.

**Domain delta:** For Reliability Engineering, this signal observes service reliability and failure recovery through reliability model, SLO evidence, fault tests, and recovery runbook while rejecting stale or untrusted substitutes.

### `signal-reliability-repository-evidence`

Reliability Engineering: Repository Evidence Signal. Current source or accepted documentation identifies reliability model, SLO evidence, fault tests, and recovery runbook as the owning surface for service reliability and failure recovery.

**Domain delta:** For Reliability Engineering, this signal observes service reliability and failure recovery through reliability model, SLO evidence, fault tests, and recovery runbook while rejecting stale or untrusted substitutes.

### `signal-reliability-runtime-failure`

Reliability Engineering: Runtime Failure Signal. A reproducible observation shows happy-path design omits fault containment, idempotency, or recovery evidence in reliability model, SLO evidence, fault tests, and recovery runbook.

**Domain delta:** For Reliability Engineering, this signal observes service reliability and failure recovery through reliability model, SLO evidence, fault tests, and recovery runbook while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-reliability-escalate-and-contain`

Reliability Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route contain the fault, restore the critical path, and add a regression or monitor to the accountable owner

**Domain delta:** For Reliability Engineering, recovery targets happy-path design omits fault containment, idempotency, or recovery evidence in reliability model, SLO evidence, fault tests, and recovery runbook and exits only with failure injection, restart, retry, degradation, capacity, and recovery observations.

### `recovery-reliability-isolate-and-repair`

Reliability Engineering: Isolate And Repair. Reduce to the smallest failing path in reliability model, SLO evidence, fault tests, and recovery runbook Apply one bounded repair Run failure injection, restart, retry, degradation, capacity, and recovery observations Check adjacent invariants

**Domain delta:** For Reliability Engineering, recovery targets happy-path design omits fault containment, idempotency, or recovery evidence in reliability model, SLO evidence, fault tests, and recovery runbook and exits only with failure injection, restart, retry, degradation, capacity, and recovery observations.

### `recovery-reliability-reset-and-reconstruct`

Reliability Engineering: Reset And Reconstruct. Stop mutation Re-read reliability model, SLO evidence, fault tests, and recovery runbook and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Reliability Engineering, recovery targets happy-path design omits fault containment, idempotency, or recovery evidence in reliability model, SLO evidence, fault tests, and recovery runbook and exits only with failure injection, restart, retry, degradation, capacity, and recovery observations.

## Decision (2)

### `decision-reliability-build-versus-test`

Reliability Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Reliability Engineering, this model decides prevent, tolerate, degrade, recover, or accept with owner approval using failure injection, restart, retry, degradation, capacity, and recovery observations and the constraint 'expected faults do not silently corrupt state or violate critical outcomes'.

### `decision-reliability-local-versus-systemic`

Reliability Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening expected faults do not silently corrupt state or violate critical outcomes.

**Domain delta:** For Reliability Engineering, this model decides prevent, tolerate, degrade, recover, or accept with owner approval using failure injection, restart, retry, degradation, capacity, and recovery observations and the constraint 'expected faults do not silently corrupt state or violate critical outcomes'.

## Mental Model (2)

### `mental-model-reliability-feedback-loop`

Reliability Engineering: Feedback Loop. Actions on service reliability and failure recovery change reliability model, SLO evidence, fault tests, and recovery runbook, which changes the next evidence and decision environment.

**Domain delta:** For Reliability Engineering, this model maps reliability emerges from fault isolation, redundancy, feedback, and bounded recovery onto service reliability and failure recovery and reliability model, SLO evidence, fault tests, and recovery runbook.

### `mental-model-reliability-weakest-link`

Reliability Engineering: Weakest Link And Bottleneck. End-to-end quality for service reliability and failure recovery is limited by the least trustworthy boundary in the path through reliability model, SLO evidence, fault tests, and recovery runbook.

**Domain delta:** For Reliability Engineering, this model maps reliability emerges from fault isolation, redundancy, feedback, and bounded recovery onto service reliability and failure recovery and reliability model, SLO evidence, fault tests, and recovery runbook.

## Governance (1)

### `governance-reliability-evidence-authority-policy`

Reliability Engineering: Evidence And Authority Policy. Work on service reliability and failure recovery must preserve 'expected faults do not silently corrupt state or violate critical outcomes', cite failure injection, restart, retry, degradation, capacity, and recovery observations, and remain within 'reliability engineering versus product risk acceptance'.

**Domain delta:** For Reliability Engineering, this policy enforces critical reliability risk is accepted without an owner, SLO, or rollback at reliability engineering versus product risk acceptance.

