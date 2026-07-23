# Compliance And Legal Readiness

Map applicable obligations to owned controls and qualified approval without inventing legal conclusions.

- **Domain ID:** `compliance`
- **Boundary:** engineering evidence versus qualified legal interpretation and acceptance
- **Invariant:** the product does not claim compliance beyond reviewed and implemented controls
- **Default evidence:** authoritative requirement, jurisdiction scope, control mapping, and qualified review
- **Risk classes:** legal, governance

## Behavior (10)

### `behavior-compliance-choose-falsifier`

Compliance And Legal Readiness: Choose Cheapest Falsifier. Choose the lowest-cost check of obligation-control matrix and legal review record that could disprove the current hypothesis.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-communicate-uncertainty`

Compliance And Legal Readiness: Communicate Uncertainty. State confidence, missing evidence, failure impact 'unlawful processing, contractual breach, licensing violation, or misleading public claims', and the next discriminating check.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-establish-state`

Compliance And Legal Readiness: Establish Current State. Inspect obligation-control matrix and legal review record and record the current behavior before proposing change.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-identify-owner`

Compliance And Legal Readiness: Identify Owner And Boundary. Name the owner of obligation-control matrix and legal review record, the boundary 'engineering evidence versus qualified legal interpretation and acceptance', and who may decide or mutate it.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-minimize-change`

Compliance And Legal Readiness: Make The Smallest Useful Change. Change only the owning slice of obligation-control matrix and legal review record needed to protect 'the product does not claim compliance beyond reviewed and implemented controls'.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-protect-invariant`

Compliance And Legal Readiness: Protect The Domain Invariant. Reject an option that can violate 'the product does not claim compliance beyond reviewed and implemented controls' without an approved mitigation.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-stop-and-escalate`

Compliance And Legal Readiness: Stop And Escalate. Stop mutation, preserve evidence, and route 'pause the affected release or processing, identify qualified ownership, and close the control gap' to the accountable owner.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-surface-assumptions`

Compliance And Legal Readiness: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the product does not claim compliance beyond reviewed and implemented controls'.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-update-memory`

Compliance And Legal Readiness: Update Durable Knowledge. Update the decision or memory record for obligation-control matrix and legal review record with provenance and invalidation triggers.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

### `behavior-compliance-validate-immediately`

Compliance And Legal Readiness: Validate Immediately. Run authoritative requirement, jurisdiction scope, control mapping, and qualified review or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Compliance And Legal Readiness, this behavior operates on obligation-control matrix and legal review record, uses authoritative requirement, jurisdiction scope, control mapping, and qualified review, and protects 'the product does not claim compliance beyond reviewed and implemented controls'.

## Failure (6)

### `failure-compliance-boundary-violation`

Compliance And Legal Readiness: Boundary Violation. A local optimization bypasses the domain ownership model for regulatory, contractual, policy, and licensing obligations.

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

### `failure-compliance-evidence-overclaim`

Compliance And Legal Readiness: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

### `failure-compliance-premature-action`

Compliance And Legal Readiness: Premature Action. generic guidance is applied as jurisdiction-specific approval

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

### `failure-compliance-silent-failure`

Compliance And Legal Readiness: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

### `failure-compliance-stale-context`

Compliance And Legal Readiness: Stale Context. The state of obligation-control matrix and legal review record changed while routing continued from a stale checkpoint.

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

### `failure-compliance-unbounded-loop`

Compliance And Legal Readiness: Unbounded Repair Loop. Failures do not trigger a reset of generic guidance is applied as jurisdiction-specific approval.

**Domain delta:** In Compliance And Legal Readiness, this failure threatens 'the product does not claim compliance beyond reviewed and implemented controls' through generic guidance is applied as jurisdiction-specific approval.

## Signal (4)

### `signal-compliance-constraint-risk`

Compliance And Legal Readiness: Constraint Or Risk Signal. A current constraint or risk threatens 'the product does not claim compliance beyond reviewed and implemented controls' for regulatory, contractual, policy, and licensing obligations.

**Domain delta:** For Compliance And Legal Readiness, this signal observes regulatory, contractual, policy, and licensing obligations through obligation-control matrix and legal review record while rejecting stale or untrusted substitutes.

### `signal-compliance-explicit-mission`

Compliance And Legal Readiness: Explicit Mission Signal. The current user request explicitly concerns regulatory, contractual, policy, and licensing obligations and states an observable outcome.

**Domain delta:** For Compliance And Legal Readiness, this signal observes regulatory, contractual, policy, and licensing obligations through obligation-control matrix and legal review record while rejecting stale or untrusted substitutes.

### `signal-compliance-repository-evidence`

Compliance And Legal Readiness: Repository Evidence Signal. Current source or accepted documentation identifies obligation-control matrix and legal review record as the owning surface for regulatory, contractual, policy, and licensing obligations.

**Domain delta:** For Compliance And Legal Readiness, this signal observes regulatory, contractual, policy, and licensing obligations through obligation-control matrix and legal review record while rejecting stale or untrusted substitutes.

### `signal-compliance-runtime-failure`

Compliance And Legal Readiness: Runtime Failure Signal. A reproducible observation shows generic guidance is applied as jurisdiction-specific approval in obligation-control matrix and legal review record.

**Domain delta:** For Compliance And Legal Readiness, this signal observes regulatory, contractual, policy, and licensing obligations through obligation-control matrix and legal review record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-compliance-escalate-and-contain`

Compliance And Legal Readiness: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route pause the affected release or processing, identify qualified ownership, and close the control gap to the accountable owner

**Domain delta:** For Compliance And Legal Readiness, recovery targets generic guidance is applied as jurisdiction-specific approval in obligation-control matrix and legal review record and exits only with authoritative requirement, jurisdiction scope, control mapping, and qualified review.

### `recovery-compliance-isolate-and-repair`

Compliance And Legal Readiness: Isolate And Repair. Reduce to the smallest failing path in obligation-control matrix and legal review record Apply one bounded repair Run authoritative requirement, jurisdiction scope, control mapping, and qualified review Check adjacent invariants

**Domain delta:** For Compliance And Legal Readiness, recovery targets generic guidance is applied as jurisdiction-specific approval in obligation-control matrix and legal review record and exits only with authoritative requirement, jurisdiction scope, control mapping, and qualified review.

### `recovery-compliance-reset-and-reconstruct`

Compliance And Legal Readiness: Reset And Reconstruct. Stop mutation Re-read obligation-control matrix and legal review record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Compliance And Legal Readiness, recovery targets generic guidance is applied as jurisdiction-specific approval in obligation-control matrix and legal review record and exits only with authoritative requirement, jurisdiction scope, control mapping, and qualified review.

## Decision (2)

### `decision-compliance-build-versus-test`

Compliance And Legal Readiness: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Compliance And Legal Readiness, this model decides in scope, out of scope, seek counsel, implement control, or block release using authoritative requirement, jurisdiction scope, control mapping, and qualified review and the constraint 'the product does not claim compliance beyond reviewed and implemented controls'.

### `decision-compliance-local-versus-systemic`

Compliance And Legal Readiness: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the product does not claim compliance beyond reviewed and implemented controls.

**Domain delta:** For Compliance And Legal Readiness, this model decides in scope, out of scope, seek counsel, implement control, or block release using authoritative requirement, jurisdiction scope, control mapping, and qualified review and the constraint 'the product does not claim compliance beyond reviewed and implemented controls'.

## Mental Model (2)

### `mental-model-compliance-feedback-loop`

Compliance And Legal Readiness: Feedback Loop. Actions on regulatory, contractual, policy, and licensing obligations change obligation-control matrix and legal review record, which changes the next evidence and decision environment.

**Domain delta:** For Compliance And Legal Readiness, this model maps compliance depends on context, jurisdiction, evidence, and continuing operations rather than one checklist onto regulatory, contractual, policy, and licensing obligations and obligation-control matrix and legal review record.

### `mental-model-compliance-weakest-link`

Compliance And Legal Readiness: Weakest Link And Bottleneck. End-to-end quality for regulatory, contractual, policy, and licensing obligations is limited by the least trustworthy boundary in the path through obligation-control matrix and legal review record.

**Domain delta:** For Compliance And Legal Readiness, this model maps compliance depends on context, jurisdiction, evidence, and continuing operations rather than one checklist onto regulatory, contractual, policy, and licensing obligations and obligation-control matrix and legal review record.

## Governance (1)

### `governance-compliance-evidence-authority-policy`

Compliance And Legal Readiness: Evidence And Authority Policy. Work on regulatory, contractual, policy, and licensing obligations must preserve 'the product does not claim compliance beyond reviewed and implemented controls', cite authoritative requirement, jurisdiction scope, control mapping, and qualified review, and remain within 'engineering evidence versus qualified legal interpretation and acceptance'.

**Domain delta:** For Compliance And Legal Readiness, this policy enforces legal or compliance acceptance is performed by an unqualified agent at engineering evidence versus qualified legal interpretation and acceptance.

