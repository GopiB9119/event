# Refactoring

Improve internal structure while preserving externally observable behavior.

- **Domain ID:** `refactoring`
- **Boundary:** structural improvement versus feature or migration work
- **Invariant:** public behavior and data contracts remain unchanged unless separately approved
- **Default evidence:** before-and-after behavior tests, dependency checks, and diff review
- **Risk classes:** maintainability, quality

## Behavior (10)

### `behavior-refactoring-choose-falsifier`

Refactoring: Choose Cheapest Falsifier. Choose the lowest-cost check of refactoring scope and regression suite that could disprove the current hypothesis.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-communicate-uncertainty`

Refactoring: Communicate Uncertainty. State confidence, missing evidence, failure impact 'a cleanup silently changes behavior, data, performance, or ownership', and the next discriminating check.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-establish-state`

Refactoring: Establish Current State. Inspect refactoring scope and regression suite and record the current behavior before proposing change.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-identify-owner`

Refactoring: Identify Owner And Boundary. Name the owner of refactoring scope and regression suite, the boundary 'structural improvement versus feature or migration work', and who may decide or mutate it.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-minimize-change`

Refactoring: Make The Smallest Useful Change. Change only the owning slice of refactoring scope and regression suite needed to protect 'public behavior and data contracts remain unchanged unless separately approved'.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-protect-invariant`

Refactoring: Protect The Domain Invariant. Reject an option that can violate 'public behavior and data contracts remain unchanged unless separately approved' without an approved mitigation.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-stop-and-escalate`

Refactoring: Stop And Escalate. Stop mutation, preserve evidence, and route 'separate behavior change, add characterization tests, and reduce the structural step' to the accountable owner.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-surface-assumptions`

Refactoring: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'public behavior and data contracts remain unchanged unless separately approved'.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-update-memory`

Refactoring: Update Durable Knowledge. Update the decision or memory record for refactoring scope and regression suite with provenance and invalidation triggers.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

### `behavior-refactoring-validate-immediately`

Refactoring: Validate Immediately. Run before-and-after behavior tests, dependency checks, and diff review or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Refactoring, this behavior operates on refactoring scope and regression suite, uses before-and-after behavior tests, dependency checks, and diff review, and protects 'public behavior and data contracts remain unchanged unless separately approved'.

## Failure (6)

### `failure-refactoring-boundary-violation`

Refactoring: Boundary Violation. A local optimization bypasses the domain ownership model for code structure under a behavior-preservation contract.

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

### `failure-refactoring-evidence-overclaim`

Refactoring: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

### `failure-refactoring-premature-action`

Refactoring: Premature Action. refactoring mixes functional changes or lacks characterization evidence

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

### `failure-refactoring-silent-failure`

Refactoring: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

### `failure-refactoring-stale-context`

Refactoring: Stale Context. The state of refactoring scope and regression suite changed while routing continued from a stale checkpoint.

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

### `failure-refactoring-unbounded-loop`

Refactoring: Unbounded Repair Loop. Failures do not trigger a reset of refactoring mixes functional changes or lacks characterization evidence.

**Domain delta:** In Refactoring, this failure threatens 'public behavior and data contracts remain unchanged unless separately approved' through refactoring mixes functional changes or lacks characterization evidence.

## Signal (4)

### `signal-refactoring-constraint-risk`

Refactoring: Constraint Or Risk Signal. A current constraint or risk threatens 'public behavior and data contracts remain unchanged unless separately approved' for code structure under a behavior-preservation contract.

**Domain delta:** For Refactoring, this signal observes code structure under a behavior-preservation contract through refactoring scope and regression suite while rejecting stale or untrusted substitutes.

### `signal-refactoring-explicit-mission`

Refactoring: Explicit Mission Signal. The current user request explicitly concerns code structure under a behavior-preservation contract and states an observable outcome.

**Domain delta:** For Refactoring, this signal observes code structure under a behavior-preservation contract through refactoring scope and regression suite while rejecting stale or untrusted substitutes.

### `signal-refactoring-repository-evidence`

Refactoring: Repository Evidence Signal. Current source or accepted documentation identifies refactoring scope and regression suite as the owning surface for code structure under a behavior-preservation contract.

**Domain delta:** For Refactoring, this signal observes code structure under a behavior-preservation contract through refactoring scope and regression suite while rejecting stale or untrusted substitutes.

### `signal-refactoring-runtime-failure`

Refactoring: Runtime Failure Signal. A reproducible observation shows refactoring mixes functional changes or lacks characterization evidence in refactoring scope and regression suite.

**Domain delta:** For Refactoring, this signal observes code structure under a behavior-preservation contract through refactoring scope and regression suite while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-refactoring-escalate-and-contain`

Refactoring: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route separate behavior change, add characterization tests, and reduce the structural step to the accountable owner

**Domain delta:** For Refactoring, recovery targets refactoring mixes functional changes or lacks characterization evidence in refactoring scope and regression suite and exits only with before-and-after behavior tests, dependency checks, and diff review.

### `recovery-refactoring-isolate-and-repair`

Refactoring: Isolate And Repair. Reduce to the smallest failing path in refactoring scope and regression suite Apply one bounded repair Run before-and-after behavior tests, dependency checks, and diff review Check adjacent invariants

**Domain delta:** For Refactoring, recovery targets refactoring mixes functional changes or lacks characterization evidence in refactoring scope and regression suite and exits only with before-and-after behavior tests, dependency checks, and diff review.

### `recovery-refactoring-reset-and-reconstruct`

Refactoring: Reset And Reconstruct. Stop mutation Re-read refactoring scope and regression suite and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Refactoring, recovery targets refactoring mixes functional changes or lacks characterization evidence in refactoring scope and regression suite and exits only with before-and-after behavior tests, dependency checks, and diff review.

## Decision (2)

### `decision-refactoring-build-versus-test`

Refactoring: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Refactoring, this model decides leave, local cleanup, extract, redesign, or defer using before-and-after behavior tests, dependency checks, and diff review and the constraint 'public behavior and data contracts remain unchanged unless separately approved'.

### `decision-refactoring-local-versus-systemic`

Refactoring: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening public behavior and data contracts remain unchanged unless separately approved.

**Domain delta:** For Refactoring, this model decides leave, local cleanup, extract, redesign, or defer using before-and-after behavior tests, dependency checks, and diff review and the constraint 'public behavior and data contracts remain unchanged unless separately approved'.

## Mental Model (2)

### `mental-model-refactoring-feedback-loop`

Refactoring: Feedback Loop. Actions on code structure under a behavior-preservation contract change refactoring scope and regression suite, which changes the next evidence and decision environment.

**Domain delta:** For Refactoring, this model maps structure affects future change cost but preservation evidence constrains safe movement onto code structure under a behavior-preservation contract and refactoring scope and regression suite.

### `mental-model-refactoring-weakest-link`

Refactoring: Weakest Link And Bottleneck. End-to-end quality for code structure under a behavior-preservation contract is limited by the least trustworthy boundary in the path through refactoring scope and regression suite.

**Domain delta:** For Refactoring, this model maps structure affects future change cost but preservation evidence constrains safe movement onto code structure under a behavior-preservation contract and refactoring scope and regression suite.

## Governance (1)

### `governance-refactoring-evidence-authority-policy`

Refactoring: Evidence And Authority Policy. Work on code structure under a behavior-preservation contract must preserve 'public behavior and data contracts remain unchanged unless separately approved', cite before-and-after behavior tests, dependency checks, and diff review, and remain within 'structural improvement versus feature or migration work'.

**Domain delta:** For Refactoring, this policy enforces unrequested tidying expands scope or changes public contracts at structural improvement versus feature or migration work.

