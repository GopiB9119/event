# Accessibility

Ensure people with diverse abilities can perceive, understand, navigate, and operate the product.

- **Domain ID:** `accessibility`
- **Boundary:** accessibility engineering versus qualified user research and legal review
- **Invariant:** critical functionality does not depend on one sensory, motor, or cognitive ability
- **Default evidence:** screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks
- **Risk classes:** accessibility, human, legal

## Behavior (10)

### `behavior-accessibility-choose-falsifier`

Accessibility: Choose Cheapest Falsifier. Choose the lowest-cost check of accessibility requirements, semantic tree, contrast/layout evidence, and device tests that could disprove the current hypothesis.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-communicate-uncertainty`

Accessibility: Communicate Uncertainty. State confidence, missing evidence, failure impact 'users are excluded from essential workflows or receive inaccessible error and safety information', and the next discriminating check.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-establish-state`

Accessibility: Establish Current State. Inspect accessibility requirements, semantic tree, contrast/layout evidence, and device tests and record the current behavior before proposing change.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-identify-owner`

Accessibility: Identify Owner And Boundary. Name the owner of accessibility requirements, semantic tree, contrast/layout evidence, and device tests, the boundary 'accessibility engineering versus qualified user research and legal review', and who may decide or mutate it.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-minimize-change`

Accessibility: Make The Smallest Useful Change. Change only the owning slice of accessibility requirements, semantic tree, contrast/layout evidence, and device tests needed to protect 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-protect-invariant`

Accessibility: Protect The Domain Invariant. Reject an option that can violate 'critical functionality does not depend on one sensory, motor, or cognitive ability' without an approved mitigation.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-stop-and-escalate`

Accessibility: Stop And Escalate. Stop mutation, preserve evidence, and route 'identify the blocked journey, repair semantics or interaction, and validate with assistive technology' to the accountable owner.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-surface-assumptions`

Accessibility: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-update-memory`

Accessibility: Update Durable Knowledge. Update the decision or memory record for accessibility requirements, semantic tree, contrast/layout evidence, and device tests with provenance and invalidation triggers.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `behavior-accessibility-validate-immediately`

Accessibility: Validate Immediately. Run screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Accessibility, this behavior operates on accessibility requirements, semantic tree, contrast/layout evidence, and device tests, uses screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and protects 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

## Failure (6)

### `failure-accessibility-boundary-violation`

Accessibility: Boundary Violation. A local optimization bypasses the domain ownership model for accessible semantics, interaction, content, and assistive technology behavior.

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

### `failure-accessibility-evidence-overclaim`

Accessibility: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

### `failure-accessibility-premature-action`

Accessibility: Premature Action. visual appearance is tested while semantics, focus, scaling, and alternative input are ignored

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

### `failure-accessibility-silent-failure`

Accessibility: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

### `failure-accessibility-stale-context`

Accessibility: Stale Context. The state of accessibility requirements, semantic tree, contrast/layout evidence, and device tests changed while routing continued from a stale checkpoint.

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

### `failure-accessibility-unbounded-loop`

Accessibility: Unbounded Repair Loop. Failures do not trigger a reset of visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

**Domain delta:** In Accessibility, this failure threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' through visual appearance is tested while semantics, focus, scaling, and alternative input are ignored.

## Signal (4)

### `signal-accessibility-constraint-risk`

Accessibility: Constraint Or Risk Signal. A current constraint or risk threatens 'critical functionality does not depend on one sensory, motor, or cognitive ability' for accessible semantics, interaction, content, and assistive technology behavior.

**Domain delta:** For Accessibility, this signal observes accessible semantics, interaction, content, and assistive technology behavior through accessibility requirements, semantic tree, contrast/layout evidence, and device tests while rejecting stale or untrusted substitutes.

### `signal-accessibility-explicit-mission`

Accessibility: Explicit Mission Signal. The current user request explicitly concerns accessible semantics, interaction, content, and assistive technology behavior and states an observable outcome.

**Domain delta:** For Accessibility, this signal observes accessible semantics, interaction, content, and assistive technology behavior through accessibility requirements, semantic tree, contrast/layout evidence, and device tests while rejecting stale or untrusted substitutes.

### `signal-accessibility-repository-evidence`

Accessibility: Repository Evidence Signal. Current source or accepted documentation identifies accessibility requirements, semantic tree, contrast/layout evidence, and device tests as the owning surface for accessible semantics, interaction, content, and assistive technology behavior.

**Domain delta:** For Accessibility, this signal observes accessible semantics, interaction, content, and assistive technology behavior through accessibility requirements, semantic tree, contrast/layout evidence, and device tests while rejecting stale or untrusted substitutes.

### `signal-accessibility-runtime-failure`

Accessibility: Runtime Failure Signal. A reproducible observation shows visual appearance is tested while semantics, focus, scaling, and alternative input are ignored in accessibility requirements, semantic tree, contrast/layout evidence, and device tests.

**Domain delta:** For Accessibility, this signal observes accessible semantics, interaction, content, and assistive technology behavior through accessibility requirements, semantic tree, contrast/layout evidence, and device tests while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-accessibility-escalate-and-contain`

Accessibility: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route identify the blocked journey, repair semantics or interaction, and validate with assistive technology to the accountable owner

**Domain delta:** For Accessibility, recovery targets visual appearance is tested while semantics, focus, scaling, and alternative input are ignored in accessibility requirements, semantic tree, contrast/layout evidence, and device tests and exits only with screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks.

### `recovery-accessibility-isolate-and-repair`

Accessibility: Isolate And Repair. Reduce to the smallest failing path in accessibility requirements, semantic tree, contrast/layout evidence, and device tests Apply one bounded repair Run screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks Check adjacent invariants

**Domain delta:** For Accessibility, recovery targets visual appearance is tested while semantics, focus, scaling, and alternative input are ignored in accessibility requirements, semantic tree, contrast/layout evidence, and device tests and exits only with screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks.

### `recovery-accessibility-reset-and-reconstruct`

Accessibility: Reset And Reconstruct. Stop mutation Re-read accessibility requirements, semantic tree, contrast/layout evidence, and device tests and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Accessibility, recovery targets visual appearance is tested while semantics, focus, scaling, and alternative input are ignored in accessibility requirements, semantic tree, contrast/layout evidence, and device tests and exits only with screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks.

## Decision (2)

### `decision-accessibility-build-versus-test`

Accessibility: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Accessibility, this model decides adapt, simplify, annotate, reorder, provide alternative, or block release using screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks and the constraint 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

### `decision-accessibility-local-versus-systemic`

Accessibility: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening critical functionality does not depend on one sensory, motor, or cognitive ability.

**Domain delta:** For Accessibility, this model decides adapt, simplify, annotate, reorder, provide alternative, or block release using screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks and the constraint 'critical functionality does not depend on one sensory, motor, or cognitive ability'.

## Mental Model (2)

### `mental-model-accessibility-feedback-loop`

Accessibility: Feedback Loop. Actions on accessible semantics, interaction, content, and assistive technology behavior change accessibility requirements, semantic tree, contrast/layout evidence, and device tests, which changes the next evidence and decision environment.

**Domain delta:** For Accessibility, this model maps disability emerges from interaction between user capability, environment, and design barriers onto accessible semantics, interaction, content, and assistive technology behavior and accessibility requirements, semantic tree, contrast/layout evidence, and device tests.

### `mental-model-accessibility-weakest-link`

Accessibility: Weakest Link And Bottleneck. End-to-end quality for accessible semantics, interaction, content, and assistive technology behavior is limited by the least trustworthy boundary in the path through accessibility requirements, semantic tree, contrast/layout evidence, and device tests.

**Domain delta:** For Accessibility, this model maps disability emerges from interaction between user capability, environment, and design barriers onto accessible semantics, interaction, content, and assistive technology behavior and accessibility requirements, semantic tree, contrast/layout evidence, and device tests.

## Governance (1)

### `governance-accessibility-evidence-authority-policy`

Accessibility: Evidence And Authority Policy. Work on accessible semantics, interaction, content, and assistive technology behavior must preserve 'critical functionality does not depend on one sensory, motor, or cognitive ability', cite screen reader, keyboard, switch, scaling, contrast, target-size, and error-recovery checks, and remain within 'accessibility engineering versus qualified user research and legal review'.

**Domain delta:** For Accessibility, this policy enforces critical accessibility failures are waived without accountable approval and evidence at accessibility engineering versus qualified user research and legal review.

