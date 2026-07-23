# Delivery Management

Move accepted work through implementation, verification, release, and learning without hidden queues.

- **Domain ID:** `delivery-management`
- **Boundary:** coordination authority versus source-owner implementation decisions
- **Invariant:** work is not called complete before its acceptance and release gates pass
- **Default evidence:** current work state, blocked reasons, cycle evidence, and terminal outcomes
- **Risk classes:** delivery, quality

## Behavior (10)

### `behavior-delivery-management-choose-falsifier`

Delivery Management: Choose Cheapest Falsifier. Choose the lowest-cost check of delivery board and requirement-to-evidence status that could disprove the current hypothesis.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-communicate-uncertainty`

Delivery Management: Communicate Uncertainty. State confidence, missing evidence, failure impact 'partially finished work accumulates and completion claims become unreliable', and the next discriminating check.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-establish-state`

Delivery Management: Establish Current State. Inspect delivery board and requirement-to-evidence status and record the current behavior before proposing change.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-identify-owner`

Delivery Management: Identify Owner And Boundary. Name the owner of delivery board and requirement-to-evidence status, the boundary 'coordination authority versus source-owner implementation decisions', and who may decide or mutate it.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-minimize-change`

Delivery Management: Make The Smallest Useful Change. Change only the owning slice of delivery board and requirement-to-evidence status needed to protect 'work is not called complete before its acceptance and release gates pass'.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-protect-invariant`

Delivery Management: Protect The Domain Invariant. Reject an option that can violate 'work is not called complete before its acceptance and release gates pass' without an approved mitigation.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-stop-and-escalate`

Delivery Management: Stop And Escalate. Stop mutation, preserve evidence, and route 'limit work in progress, expose blockers, and close one slice end to end' to the accountable owner.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-surface-assumptions`

Delivery Management: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'work is not called complete before its acceptance and release gates pass'.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-update-memory`

Delivery Management: Update Durable Knowledge. Update the decision or memory record for delivery board and requirement-to-evidence status with provenance and invalidation triggers.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

### `behavior-delivery-management-validate-immediately`

Delivery Management: Validate Immediately. Run current work state, blocked reasons, cycle evidence, and terminal outcomes or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Delivery Management, this behavior operates on delivery board and requirement-to-evidence status, uses current work state, blocked reasons, cycle evidence, and terminal outcomes, and protects 'work is not called complete before its acceptance and release gates pass'.

## Failure (6)

### `failure-delivery-management-boundary-violation`

Delivery Management: Boundary Violation. A local optimization bypasses the domain ownership model for delivery flow and milestone state.

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

### `failure-delivery-management-evidence-overclaim`

Delivery Management: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

### `failure-delivery-management-premature-action`

Delivery Management: Premature Action. progress is measured by activity or code volume instead of verified outcomes

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

### `failure-delivery-management-silent-failure`

Delivery Management: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

### `failure-delivery-management-stale-context`

Delivery Management: Stale Context. The state of delivery board and requirement-to-evidence status changed while routing continued from a stale checkpoint.

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

### `failure-delivery-management-unbounded-loop`

Delivery Management: Unbounded Repair Loop. Failures do not trigger a reset of progress is measured by activity or code volume instead of verified outcomes.

**Domain delta:** In Delivery Management, this failure threatens 'work is not called complete before its acceptance and release gates pass' through progress is measured by activity or code volume instead of verified outcomes.

## Signal (4)

### `signal-delivery-management-constraint-risk`

Delivery Management: Constraint Or Risk Signal. A current constraint or risk threatens 'work is not called complete before its acceptance and release gates pass' for delivery flow and milestone state.

**Domain delta:** For Delivery Management, this signal observes delivery flow and milestone state through delivery board and requirement-to-evidence status while rejecting stale or untrusted substitutes.

### `signal-delivery-management-explicit-mission`

Delivery Management: Explicit Mission Signal. The current user request explicitly concerns delivery flow and milestone state and states an observable outcome.

**Domain delta:** For Delivery Management, this signal observes delivery flow and milestone state through delivery board and requirement-to-evidence status while rejecting stale or untrusted substitutes.

### `signal-delivery-management-repository-evidence`

Delivery Management: Repository Evidence Signal. Current source or accepted documentation identifies delivery board and requirement-to-evidence status as the owning surface for delivery flow and milestone state.

**Domain delta:** For Delivery Management, this signal observes delivery flow and milestone state through delivery board and requirement-to-evidence status while rejecting stale or untrusted substitutes.

### `signal-delivery-management-runtime-failure`

Delivery Management: Runtime Failure Signal. A reproducible observation shows progress is measured by activity or code volume instead of verified outcomes in delivery board and requirement-to-evidence status.

**Domain delta:** For Delivery Management, this signal observes delivery flow and milestone state through delivery board and requirement-to-evidence status while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-delivery-management-escalate-and-contain`

Delivery Management: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route limit work in progress, expose blockers, and close one slice end to end to the accountable owner

**Domain delta:** For Delivery Management, recovery targets progress is measured by activity or code volume instead of verified outcomes in delivery board and requirement-to-evidence status and exits only with current work state, blocked reasons, cycle evidence, and terminal outcomes.

### `recovery-delivery-management-isolate-and-repair`

Delivery Management: Isolate And Repair. Reduce to the smallest failing path in delivery board and requirement-to-evidence status Apply one bounded repair Run current work state, blocked reasons, cycle evidence, and terminal outcomes Check adjacent invariants

**Domain delta:** For Delivery Management, recovery targets progress is measured by activity or code volume instead of verified outcomes in delivery board and requirement-to-evidence status and exits only with current work state, blocked reasons, cycle evidence, and terminal outcomes.

### `recovery-delivery-management-reset-and-reconstruct`

Delivery Management: Reset And Reconstruct. Stop mutation Re-read delivery board and requirement-to-evidence status and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Delivery Management, recovery targets progress is measured by activity or code volume instead of verified outcomes in delivery board and requirement-to-evidence status and exits only with current work state, blocked reasons, cycle evidence, and terminal outcomes.

## Decision (2)

### `decision-delivery-management-build-versus-test`

Delivery Management: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Delivery Management, this model decides continue, unblock, split, cancel, or release using current work state, blocked reasons, cycle evidence, and terminal outcomes and the constraint 'work is not called complete before its acceptance and release gates pass'.

### `decision-delivery-management-local-versus-systemic`

Delivery Management: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening work is not called complete before its acceptance and release gates pass.

**Domain delta:** For Delivery Management, this model decides continue, unblock, split, cancel, or release using current work state, blocked reasons, cycle evidence, and terminal outcomes and the constraint 'work is not called complete before its acceptance and release gates pass'.

## Mental Model (2)

### `mental-model-delivery-management-feedback-loop`

Delivery Management: Feedback Loop. Actions on delivery flow and milestone state change delivery board and requirement-to-evidence status, which changes the next evidence and decision environment.

**Domain delta:** For Delivery Management, this model maps flow efficiency falls as work in progress and handoff queues grow onto delivery flow and milestone state and delivery board and requirement-to-evidence status.

### `mental-model-delivery-management-weakest-link`

Delivery Management: Weakest Link And Bottleneck. End-to-end quality for delivery flow and milestone state is limited by the least trustworthy boundary in the path through delivery board and requirement-to-evidence status.

**Domain delta:** For Delivery Management, this model maps flow efficiency falls as work in progress and handoff queues grow onto delivery flow and milestone state and delivery board and requirement-to-evidence status.

## Governance (1)

### `governance-delivery-management-evidence-authority-policy`

Delivery Management: Evidence And Authority Policy. Work on delivery flow and milestone state must preserve 'work is not called complete before its acceptance and release gates pass', cite current work state, blocked reasons, cycle evidence, and terminal outcomes, and remain within 'coordination authority versus source-owner implementation decisions'.

**Domain delta:** For Delivery Management, this policy enforces blocked or failed work is reported as complete at coordination authority versus source-owner implementation decisions.

