# Verification And Completion

Require reproducible evidence before upgrading a claim from implemented to complete.

- **Domain ID:** `verification`
- **Boundary:** implementation self-review versus independent acceptance
- **Invariant:** claim strength never exceeds the strongest relevant evidence
- **Default evidence:** compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review
- **Risk classes:** quality, governance

## Behavior (10)

### `behavior-verification-choose-falsifier`

Verification And Completion: Choose Cheapest Falsifier. Choose the lowest-cost check of requirement-to-evidence matrix and completion audit that could disprove the current hypothesis.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-communicate-uncertainty`

Verification And Completion: Communicate Uncertainty. State confidence, missing evidence, failure impact 'unfinished or unsafe work is released because code exists or one check passed', and the next discriminating check.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-establish-state`

Verification And Completion: Establish Current State. Inspect requirement-to-evidence matrix and completion audit and record the current behavior before proposing change.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-identify-owner`

Verification And Completion: Identify Owner And Boundary. Name the owner of requirement-to-evidence matrix and completion audit, the boundary 'implementation self-review versus independent acceptance', and who may decide or mutate it.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-minimize-change`

Verification And Completion: Make The Smallest Useful Change. Change only the owning slice of requirement-to-evidence matrix and completion audit needed to protect 'claim strength never exceeds the strongest relevant evidence'.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-protect-invariant`

Verification And Completion: Protect The Domain Invariant. Reject an option that can violate 'claim strength never exceeds the strongest relevant evidence' without an approved mitigation.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-stop-and-escalate`

Verification And Completion: Stop And Escalate. Stop mutation, preserve evidence, and route 'downgrade the claim, enumerate missing evidence, and run the highest-value remaining check' to the accountable owner.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-surface-assumptions`

Verification And Completion: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'claim strength never exceeds the strongest relevant evidence'.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-update-memory`

Verification And Completion: Update Durable Knowledge. Update the decision or memory record for requirement-to-evidence matrix and completion audit with provenance and invalidation triggers.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

### `behavior-verification-validate-immediately`

Verification And Completion: Validate Immediately. Run compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Verification And Completion, this behavior operates on requirement-to-evidence matrix and completion audit, uses compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and protects 'claim strength never exceeds the strongest relevant evidence'.

## Failure (6)

### `failure-verification-boundary-violation`

Verification And Completion: Boundary Violation. A local optimization bypasses the domain ownership model for requirement satisfaction and completion claims.

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

### `failure-verification-evidence-overclaim`

Verification And Completion: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

### `failure-verification-premature-action`

Verification And Completion: Premature Action. evidence gaps are hidden behind broad done language

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

### `failure-verification-silent-failure`

Verification And Completion: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

### `failure-verification-stale-context`

Verification And Completion: Stale Context. The state of requirement-to-evidence matrix and completion audit changed while routing continued from a stale checkpoint.

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

### `failure-verification-unbounded-loop`

Verification And Completion: Unbounded Repair Loop. Failures do not trigger a reset of evidence gaps are hidden behind broad done language.

**Domain delta:** In Verification And Completion, this failure threatens 'claim strength never exceeds the strongest relevant evidence' through evidence gaps are hidden behind broad done language.

## Signal (4)

### `signal-verification-constraint-risk`

Verification And Completion: Constraint Or Risk Signal. A current constraint or risk threatens 'claim strength never exceeds the strongest relevant evidence' for requirement satisfaction and completion claims.

**Domain delta:** For Verification And Completion, this signal observes requirement satisfaction and completion claims through requirement-to-evidence matrix and completion audit while rejecting stale or untrusted substitutes.

### `signal-verification-explicit-mission`

Verification And Completion: Explicit Mission Signal. The current user request explicitly concerns requirement satisfaction and completion claims and states an observable outcome.

**Domain delta:** For Verification And Completion, this signal observes requirement satisfaction and completion claims through requirement-to-evidence matrix and completion audit while rejecting stale or untrusted substitutes.

### `signal-verification-repository-evidence`

Verification And Completion: Repository Evidence Signal. Current source or accepted documentation identifies requirement-to-evidence matrix and completion audit as the owning surface for requirement satisfaction and completion claims.

**Domain delta:** For Verification And Completion, this signal observes requirement satisfaction and completion claims through requirement-to-evidence matrix and completion audit while rejecting stale or untrusted substitutes.

### `signal-verification-runtime-failure`

Verification And Completion: Runtime Failure Signal. A reproducible observation shows evidence gaps are hidden behind broad done language in requirement-to-evidence matrix and completion audit.

**Domain delta:** For Verification And Completion, this signal observes requirement satisfaction and completion claims through requirement-to-evidence matrix and completion audit while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-verification-escalate-and-contain`

Verification And Completion: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route downgrade the claim, enumerate missing evidence, and run the highest-value remaining check to the accountable owner

**Domain delta:** For Verification And Completion, recovery targets evidence gaps are hidden behind broad done language in requirement-to-evidence matrix and completion audit and exits only with compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review.

### `recovery-verification-isolate-and-repair`

Verification And Completion: Isolate And Repair. Reduce to the smallest failing path in requirement-to-evidence matrix and completion audit Apply one bounded repair Run compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review Check adjacent invariants

**Domain delta:** For Verification And Completion, recovery targets evidence gaps are hidden behind broad done language in requirement-to-evidence matrix and completion audit and exits only with compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review.

### `recovery-verification-reset-and-reconstruct`

Verification And Completion: Reset And Reconstruct. Stop mutation Re-read requirement-to-evidence matrix and completion audit and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Verification And Completion, recovery targets evidence gaps are hidden behind broad done language in requirement-to-evidence matrix and completion audit and exits only with compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review.

## Decision (2)

### `decision-verification-build-versus-test`

Verification And Completion: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Verification And Completion, this model decides not ready, conditionally ready, ready, or blocked using compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review and the constraint 'claim strength never exceeds the strongest relevant evidence'.

### `decision-verification-local-versus-systemic`

Verification And Completion: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening claim strength never exceeds the strongest relevant evidence.

**Domain delta:** For Verification And Completion, this model decides not ready, conditionally ready, ready, or blocked using compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review and the constraint 'claim strength never exceeds the strongest relevant evidence'.

## Mental Model (2)

### `mental-model-verification-feedback-loop`

Verification And Completion: Feedback Loop. Actions on requirement satisfaction and completion claims change requirement-to-evidence matrix and completion audit, which changes the next evidence and decision environment.

**Domain delta:** For Verification And Completion, this model maps confidence should update according to independent evidence quality, not effort spent onto requirement satisfaction and completion claims and requirement-to-evidence matrix and completion audit.

### `mental-model-verification-weakest-link`

Verification And Completion: Weakest Link And Bottleneck. End-to-end quality for requirement satisfaction and completion claims is limited by the least trustworthy boundary in the path through requirement-to-evidence matrix and completion audit.

**Domain delta:** For Verification And Completion, this model maps confidence should update according to independent evidence quality, not effort spent onto requirement satisfaction and completion claims and requirement-to-evidence matrix and completion audit.

## Governance (1)

### `governance-verification-evidence-authority-policy`

Verification And Completion: Evidence And Authority Policy. Work on requirement satisfaction and completion claims must preserve 'claim strength never exceeds the strongest relevant evidence', cite compile, tests, static analysis, runtime journeys, artifacts, and residual-risk review, and remain within 'implementation self-review versus independent acceptance'.

**Domain delta:** For Verification And Completion, this policy enforces the implementer waives a failed gate or certifies material risk alone at implementation self-review versus independent acceptance.

