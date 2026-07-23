# Requirement Engineering

Translate desired outcomes into testable requirements, non-requirements, and acceptance evidence.

- **Domain ID:** `requirement-engineering`
- **Boundary:** requirement definition versus implementation choice
- **Invariant:** implementation and verification cover the agreed contract
- **Default evidence:** each requirement maps to an observable acceptance check and owner
- **Risk classes:** product, quality

## Behavior (10)

### `behavior-requirement-engineering-choose-falsifier`

Requirement Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of requirement-to-evidence ledger that could disprove the current hypothesis.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-communicate-uncertainty`

Requirement Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'features appear complete while required behavior or edge cases are missing', and the next discriminating check.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-establish-state`

Requirement Engineering: Establish Current State. Inspect requirement-to-evidence ledger and record the current behavior before proposing change.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-identify-owner`

Requirement Engineering: Identify Owner And Boundary. Name the owner of requirement-to-evidence ledger, the boundary 'requirement definition versus implementation choice', and who may decide or mutate it.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-minimize-change`

Requirement Engineering: Make The Smallest Useful Change. Change only the owning slice of requirement-to-evidence ledger needed to protect 'implementation and verification cover the agreed contract'.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-protect-invariant`

Requirement Engineering: Protect The Domain Invariant. Reject an option that can violate 'implementation and verification cover the agreed contract' without an approved mitigation.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-stop-and-escalate`

Requirement Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'reconstruct requirements and acceptance criteria before further implementation' to the accountable owner.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-surface-assumptions`

Requirement Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'implementation and verification cover the agreed contract'.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-update-memory`

Requirement Engineering: Update Durable Knowledge. Update the decision or memory record for requirement-to-evidence ledger with provenance and invalidation triggers.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

### `behavior-requirement-engineering-validate-immediately`

Requirement Engineering: Validate Immediately. Run each requirement maps to an observable acceptance check and owner or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Requirement Engineering, this behavior operates on requirement-to-evidence ledger, uses each requirement maps to an observable acceptance check and owner, and protects 'implementation and verification cover the agreed contract'.

## Failure (6)

### `failure-requirement-engineering-boundary-violation`

Requirement Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for functional and non-functional requirements.

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

### `failure-requirement-engineering-evidence-overclaim`

Requirement Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

### `failure-requirement-engineering-premature-action`

Requirement Engineering: Premature Action. requirements remain implicit, contradictory, or unverifiable

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

### `failure-requirement-engineering-silent-failure`

Requirement Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

### `failure-requirement-engineering-stale-context`

Requirement Engineering: Stale Context. The state of requirement-to-evidence ledger changed while routing continued from a stale checkpoint.

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

### `failure-requirement-engineering-unbounded-loop`

Requirement Engineering: Unbounded Repair Loop. Failures do not trigger a reset of requirements remain implicit, contradictory, or unverifiable.

**Domain delta:** In Requirement Engineering, this failure threatens 'implementation and verification cover the agreed contract' through requirements remain implicit, contradictory, or unverifiable.

## Signal (4)

### `signal-requirement-engineering-constraint-risk`

Requirement Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'implementation and verification cover the agreed contract' for functional and non-functional requirements.

**Domain delta:** For Requirement Engineering, this signal observes functional and non-functional requirements through requirement-to-evidence ledger while rejecting stale or untrusted substitutes.

### `signal-requirement-engineering-explicit-mission`

Requirement Engineering: Explicit Mission Signal. The current user request explicitly concerns functional and non-functional requirements and states an observable outcome.

**Domain delta:** For Requirement Engineering, this signal observes functional and non-functional requirements through requirement-to-evidence ledger while rejecting stale or untrusted substitutes.

### `signal-requirement-engineering-repository-evidence`

Requirement Engineering: Repository Evidence Signal. Current source or accepted documentation identifies requirement-to-evidence ledger as the owning surface for functional and non-functional requirements.

**Domain delta:** For Requirement Engineering, this signal observes functional and non-functional requirements through requirement-to-evidence ledger while rejecting stale or untrusted substitutes.

### `signal-requirement-engineering-runtime-failure`

Requirement Engineering: Runtime Failure Signal. A reproducible observation shows requirements remain implicit, contradictory, or unverifiable in requirement-to-evidence ledger.

**Domain delta:** For Requirement Engineering, this signal observes functional and non-functional requirements through requirement-to-evidence ledger while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-requirement-engineering-escalate-and-contain`

Requirement Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reconstruct requirements and acceptance criteria before further implementation to the accountable owner

**Domain delta:** For Requirement Engineering, recovery targets requirements remain implicit, contradictory, or unverifiable in requirement-to-evidence ledger and exits only with each requirement maps to an observable acceptance check and owner.

### `recovery-requirement-engineering-isolate-and-repair`

Requirement Engineering: Isolate And Repair. Reduce to the smallest failing path in requirement-to-evidence ledger Apply one bounded repair Run each requirement maps to an observable acceptance check and owner Check adjacent invariants

**Domain delta:** For Requirement Engineering, recovery targets requirements remain implicit, contradictory, or unverifiable in requirement-to-evidence ledger and exits only with each requirement maps to an observable acceptance check and owner.

### `recovery-requirement-engineering-reset-and-reconstruct`

Requirement Engineering: Reset And Reconstruct. Stop mutation Re-read requirement-to-evidence ledger and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Requirement Engineering, recovery targets requirements remain implicit, contradictory, or unverifiable in requirement-to-evidence ledger and exits only with each requirement maps to an observable acceptance check and owner.

## Decision (2)

### `decision-requirement-engineering-build-versus-test`

Requirement Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Requirement Engineering, this model decides require, defer, remove, or clarify using each requirement maps to an observable acceptance check and owner and the constraint 'implementation and verification cover the agreed contract'.

### `decision-requirement-engineering-local-versus-systemic`

Requirement Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening implementation and verification cover the agreed contract.

**Domain delta:** For Requirement Engineering, this model decides require, defer, remove, or clarify using each requirement maps to an observable acceptance check and owner and the constraint 'implementation and verification cover the agreed contract'.

## Mental Model (2)

### `mental-model-requirement-engineering-feedback-loop`

Requirement Engineering: Feedback Loop. Actions on functional and non-functional requirements change requirement-to-evidence ledger, which changes the next evidence and decision environment.

**Domain delta:** For Requirement Engineering, this model maps explicit contracts reduce coordination entropy and hidden work onto functional and non-functional requirements and requirement-to-evidence ledger.

### `mental-model-requirement-engineering-weakest-link`

Requirement Engineering: Weakest Link And Bottleneck. End-to-end quality for functional and non-functional requirements is limited by the least trustworthy boundary in the path through requirement-to-evidence ledger.

**Domain delta:** For Requirement Engineering, this model maps explicit contracts reduce coordination entropy and hidden work onto functional and non-functional requirements and requirement-to-evidence ledger.

## Governance (1)

### `governance-requirement-engineering-evidence-authority-policy`

Requirement Engineering: Evidence And Authority Policy. Work on functional and non-functional requirements must preserve 'implementation and verification cover the agreed contract', cite each requirement maps to an observable acceptance check and owner, and remain within 'requirement definition versus implementation choice'.

**Domain delta:** For Requirement Engineering, this policy enforces a material requirement lacks an owner or verifier at requirement definition versus implementation choice.

