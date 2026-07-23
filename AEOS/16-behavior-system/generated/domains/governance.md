# AI And Engineering Governance

Define authority, approval, audit, exceptions, lifecycle, and accountability outside model prompts.

- **Domain ID:** `governance`
- **Boundary:** policy definition, enforcement, exception approval, and implementation roles
- **Invariant:** no agent or contributor can grant itself authority or waive a failed gate
- **Default evidence:** enforcement tests, authority mapping, approvals, logs, and periodic review
- **Risk classes:** governance, security

## Behavior (10)

### `behavior-governance-choose-falsifier`

AI And Engineering Governance: Choose Cheapest Falsifier. Choose the lowest-cost check of governance policy, approval record, audit trail, and exception register that could disprove the current hypothesis.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-communicate-uncertainty`

AI And Engineering Governance: Communicate Uncertainty. State confidence, missing evidence, failure impact 'unlimited autonomy, unaudited changes, unsafe exceptions, and accountability gaps', and the next discriminating check.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-establish-state`

AI And Engineering Governance: Establish Current State. Inspect governance policy, approval record, audit trail, and exception register and record the current behavior before proposing change.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-identify-owner`

AI And Engineering Governance: Identify Owner And Boundary. Name the owner of governance policy, approval record, audit trail, and exception register, the boundary 'policy definition, enforcement, exception approval, and implementation roles', and who may decide or mutate it.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-minimize-change`

AI And Engineering Governance: Make The Smallest Useful Change. Change only the owning slice of governance policy, approval record, audit trail, and exception register needed to protect 'no agent or contributor can grant itself authority or waive a failed gate'.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-protect-invariant`

AI And Engineering Governance: Protect The Domain Invariant. Reject an option that can violate 'no agent or contributor can grant itself authority or waive a failed gate' without an approved mitigation.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-stop-and-escalate`

AI And Engineering Governance: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop unauthorized work, restore the policy boundary, audit effects, and route exceptions' to the accountable owner.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-surface-assumptions`

AI And Engineering Governance: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'no agent or contributor can grant itself authority or waive a failed gate'.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-update-memory`

AI And Engineering Governance: Update Durable Knowledge. Update the decision or memory record for governance policy, approval record, audit trail, and exception register with provenance and invalidation triggers.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

### `behavior-governance-validate-immediately`

AI And Engineering Governance: Validate Immediately. Run enforcement tests, authority mapping, approvals, logs, and periodic review or the cheapest stronger check before opening another edit slice.

**Domain delta:** For AI And Engineering Governance, this behavior operates on governance policy, approval record, audit trail, and exception register, uses enforcement tests, authority mapping, approvals, logs, and periodic review, and protects 'no agent or contributor can grant itself authority or waive a failed gate'.

## Failure (6)

### `failure-governance-boundary-violation`

AI And Engineering Governance: Boundary Violation. A local optimization bypasses the domain ownership model for engineering authority and policy enforcement.

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

### `failure-governance-evidence-overclaim`

AI And Engineering Governance: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

### `failure-governance-premature-action`

AI And Engineering Governance: Premature Action. governance exists as prose without deterministic controls or named owners

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

### `failure-governance-silent-failure`

AI And Engineering Governance: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

### `failure-governance-stale-context`

AI And Engineering Governance: Stale Context. The state of governance policy, approval record, audit trail, and exception register changed while routing continued from a stale checkpoint.

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

### `failure-governance-unbounded-loop`

AI And Engineering Governance: Unbounded Repair Loop. Failures do not trigger a reset of governance exists as prose without deterministic controls or named owners.

**Domain delta:** In AI And Engineering Governance, this failure threatens 'no agent or contributor can grant itself authority or waive a failed gate' through governance exists as prose without deterministic controls or named owners.

## Signal (4)

### `signal-governance-constraint-risk`

AI And Engineering Governance: Constraint Or Risk Signal. A current constraint or risk threatens 'no agent or contributor can grant itself authority or waive a failed gate' for engineering authority and policy enforcement.

**Domain delta:** For AI And Engineering Governance, this signal observes engineering authority and policy enforcement through governance policy, approval record, audit trail, and exception register while rejecting stale or untrusted substitutes.

### `signal-governance-explicit-mission`

AI And Engineering Governance: Explicit Mission Signal. The current user request explicitly concerns engineering authority and policy enforcement and states an observable outcome.

**Domain delta:** For AI And Engineering Governance, this signal observes engineering authority and policy enforcement through governance policy, approval record, audit trail, and exception register while rejecting stale or untrusted substitutes.

### `signal-governance-repository-evidence`

AI And Engineering Governance: Repository Evidence Signal. Current source or accepted documentation identifies governance policy, approval record, audit trail, and exception register as the owning surface for engineering authority and policy enforcement.

**Domain delta:** For AI And Engineering Governance, this signal observes engineering authority and policy enforcement through governance policy, approval record, audit trail, and exception register while rejecting stale or untrusted substitutes.

### `signal-governance-runtime-failure`

AI And Engineering Governance: Runtime Failure Signal. A reproducible observation shows governance exists as prose without deterministic controls or named owners in governance policy, approval record, audit trail, and exception register.

**Domain delta:** For AI And Engineering Governance, this signal observes engineering authority and policy enforcement through governance policy, approval record, audit trail, and exception register while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-governance-escalate-and-contain`

AI And Engineering Governance: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop unauthorized work, restore the policy boundary, audit effects, and route exceptions to the accountable owner

**Domain delta:** For AI And Engineering Governance, recovery targets governance exists as prose without deterministic controls or named owners in governance policy, approval record, audit trail, and exception register and exits only with enforcement tests, authority mapping, approvals, logs, and periodic review.

### `recovery-governance-isolate-and-repair`

AI And Engineering Governance: Isolate And Repair. Reduce to the smallest failing path in governance policy, approval record, audit trail, and exception register Apply one bounded repair Run enforcement tests, authority mapping, approvals, logs, and periodic review Check adjacent invariants

**Domain delta:** For AI And Engineering Governance, recovery targets governance exists as prose without deterministic controls or named owners in governance policy, approval record, audit trail, and exception register and exits only with enforcement tests, authority mapping, approvals, logs, and periodic review.

### `recovery-governance-reset-and-reconstruct`

AI And Engineering Governance: Reset And Reconstruct. Stop mutation Re-read governance policy, approval record, audit trail, and exception register and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For AI And Engineering Governance, recovery targets governance exists as prose without deterministic controls or named owners in governance policy, approval record, audit trail, and exception register and exits only with enforcement tests, authority mapping, approvals, logs, and periodic review.

## Decision (2)

### `decision-governance-build-versus-test`

AI And Engineering Governance: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For AI And Engineering Governance, this model decides allow, constrain, require approval, audit, suspend, deprecate, or prohibit using enforcement tests, authority mapping, approvals, logs, and periodic review and the constraint 'no agent or contributor can grant itself authority or waive a failed gate'.

### `decision-governance-local-versus-systemic`

AI And Engineering Governance: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening no agent or contributor can grant itself authority or waive a failed gate.

**Domain delta:** For AI And Engineering Governance, this model decides allow, constrain, require approval, audit, suspend, deprecate, or prohibit using enforcement tests, authority mapping, approvals, logs, and periodic review and the constraint 'no agent or contributor can grant itself authority or waive a failed gate'.

## Mental Model (2)

### `mental-model-governance-feedback-loop`

AI And Engineering Governance: Feedback Loop. Actions on engineering authority and policy enforcement change governance policy, approval record, audit trail, and exception register, which changes the next evidence and decision environment.

**Domain delta:** For AI And Engineering Governance, this model maps governance shapes incentives and constrains power through enforceable institutions onto engineering authority and policy enforcement and governance policy, approval record, audit trail, and exception register.

### `mental-model-governance-weakest-link`

AI And Engineering Governance: Weakest Link And Bottleneck. End-to-end quality for engineering authority and policy enforcement is limited by the least trustworthy boundary in the path through governance policy, approval record, audit trail, and exception register.

**Domain delta:** For AI And Engineering Governance, this model maps governance shapes incentives and constrains power through enforceable institutions onto engineering authority and policy enforcement and governance policy, approval record, audit trail, and exception register.

## Governance (1)

### `governance-governance-evidence-authority-policy`

AI And Engineering Governance: Evidence And Authority Policy. Work on engineering authority and policy enforcement must preserve 'no agent or contributor can grant itself authority or waive a failed gate', cite enforcement tests, authority mapping, approvals, logs, and periodic review, and remain within 'policy definition, enforcement, exception approval, and implementation roles'.

**Domain delta:** For AI And Engineering Governance, this policy enforces the same actor proposes, implements, verifies, and accepts material risk without separation at policy definition, enforcement, exception approval, and implementation roles.

