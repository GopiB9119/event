# Systems Engineering

Optimize end-to-end lifecycle outcomes across components, humans, operations, and retirement.

- **Domain ID:** `systems-engineering`
- **Boundary:** system integration versus component ownership
- **Invariant:** local improvements do not degrade the whole-system outcome
- **Default evidence:** end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks
- **Risk classes:** systems, safety

## Behavior (10)

### `behavior-systems-engineering-choose-falsifier`

Systems Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of system context, interfaces, lifecycle, and tradeoff model that could disprove the current hypothesis.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-communicate-uncertainty`

Systems Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'component success hides system failure, unsafe handoffs, or lifecycle debt', and the next discriminating check.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-establish-state`

Systems Engineering: Establish Current State. Inspect system context, interfaces, lifecycle, and tradeoff model and record the current behavior before proposing change.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-identify-owner`

Systems Engineering: Identify Owner And Boundary. Name the owner of system context, interfaces, lifecycle, and tradeoff model, the boundary 'system integration versus component ownership', and who may decide or mutate it.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-minimize-change`

Systems Engineering: Make The Smallest Useful Change. Change only the owning slice of system context, interfaces, lifecycle, and tradeoff model needed to protect 'local improvements do not degrade the whole-system outcome'.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-protect-invariant`

Systems Engineering: Protect The Domain Invariant. Reject an option that can violate 'local improvements do not degrade the whole-system outcome' without an approved mitigation.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-stop-and-escalate`

Systems Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'reconstruct the end-to-end system and evaluate cross-boundary effects' to the accountable owner.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-surface-assumptions`

Systems Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'local improvements do not degrade the whole-system outcome'.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-update-memory`

Systems Engineering: Update Durable Knowledge. Update the decision or memory record for system context, interfaces, lifecycle, and tradeoff model with provenance and invalidation triggers.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

### `behavior-systems-engineering-validate-immediately`

Systems Engineering: Validate Immediately. Run end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Systems Engineering, this behavior operates on system context, interfaces, lifecycle, and tradeoff model, uses end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and protects 'local improvements do not degrade the whole-system outcome'.

## Failure (6)

### `failure-systems-engineering-boundary-violation`

Systems Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for the complete sociotechnical system and lifecycle.

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

### `failure-systems-engineering-evidence-overclaim`

Systems Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

### `failure-systems-engineering-premature-action`

Systems Engineering: Premature Action. optimization ignores interactions, operations, users, or retirement costs

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

### `failure-systems-engineering-silent-failure`

Systems Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

### `failure-systems-engineering-stale-context`

Systems Engineering: Stale Context. The state of system context, interfaces, lifecycle, and tradeoff model changed while routing continued from a stale checkpoint.

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

### `failure-systems-engineering-unbounded-loop`

Systems Engineering: Unbounded Repair Loop. Failures do not trigger a reset of optimization ignores interactions, operations, users, or retirement costs.

**Domain delta:** In Systems Engineering, this failure threatens 'local improvements do not degrade the whole-system outcome' through optimization ignores interactions, operations, users, or retirement costs.

## Signal (4)

### `signal-systems-engineering-constraint-risk`

Systems Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'local improvements do not degrade the whole-system outcome' for the complete sociotechnical system and lifecycle.

**Domain delta:** For Systems Engineering, this signal observes the complete sociotechnical system and lifecycle through system context, interfaces, lifecycle, and tradeoff model while rejecting stale or untrusted substitutes.

### `signal-systems-engineering-explicit-mission`

Systems Engineering: Explicit Mission Signal. The current user request explicitly concerns the complete sociotechnical system and lifecycle and states an observable outcome.

**Domain delta:** For Systems Engineering, this signal observes the complete sociotechnical system and lifecycle through system context, interfaces, lifecycle, and tradeoff model while rejecting stale or untrusted substitutes.

### `signal-systems-engineering-repository-evidence`

Systems Engineering: Repository Evidence Signal. Current source or accepted documentation identifies system context, interfaces, lifecycle, and tradeoff model as the owning surface for the complete sociotechnical system and lifecycle.

**Domain delta:** For Systems Engineering, this signal observes the complete sociotechnical system and lifecycle through system context, interfaces, lifecycle, and tradeoff model while rejecting stale or untrusted substitutes.

### `signal-systems-engineering-runtime-failure`

Systems Engineering: Runtime Failure Signal. A reproducible observation shows optimization ignores interactions, operations, users, or retirement costs in system context, interfaces, lifecycle, and tradeoff model.

**Domain delta:** For Systems Engineering, this signal observes the complete sociotechnical system and lifecycle through system context, interfaces, lifecycle, and tradeoff model while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-systems-engineering-escalate-and-contain`

Systems Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reconstruct the end-to-end system and evaluate cross-boundary effects to the accountable owner

**Domain delta:** For Systems Engineering, recovery targets optimization ignores interactions, operations, users, or retirement costs in system context, interfaces, lifecycle, and tradeoff model and exits only with end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks.

### `recovery-systems-engineering-isolate-and-repair`

Systems Engineering: Isolate And Repair. Reduce to the smallest failing path in system context, interfaces, lifecycle, and tradeoff model Apply one bounded repair Run end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks Check adjacent invariants

**Domain delta:** For Systems Engineering, recovery targets optimization ignores interactions, operations, users, or retirement costs in system context, interfaces, lifecycle, and tradeoff model and exits only with end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks.

### `recovery-systems-engineering-reset-and-reconstruct`

Systems Engineering: Reset And Reconstruct. Stop mutation Re-read system context, interfaces, lifecycle, and tradeoff model and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Systems Engineering, recovery targets optimization ignores interactions, operations, users, or retirement costs in system context, interfaces, lifecycle, and tradeoff model and exits only with end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks.

## Decision (2)

### `decision-systems-engineering-build-versus-test`

Systems Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Systems Engineering, this model decides component repair, integration change, operational control, or system redesign using end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks and the constraint 'local improvements do not degrade the whole-system outcome'.

### `decision-systems-engineering-local-versus-systemic`

Systems Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening local improvements do not degrade the whole-system outcome.

**Domain delta:** For Systems Engineering, this model decides component repair, integration change, operational control, or system redesign using end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks and the constraint 'local improvements do not degrade the whole-system outcome'.

## Mental Model (2)

### `mental-model-systems-engineering-feedback-loop`

Systems Engineering: Feedback Loop. Actions on the complete sociotechnical system and lifecycle change system context, interfaces, lifecycle, and tradeoff model, which changes the next evidence and decision environment.

**Domain delta:** For Systems Engineering, this model maps emergent behavior arises from interactions and feedback across the lifecycle onto the complete sociotechnical system and lifecycle and system context, interfaces, lifecycle, and tradeoff model.

### `mental-model-systems-engineering-weakest-link`

Systems Engineering: Weakest Link And Bottleneck. End-to-end quality for the complete sociotechnical system and lifecycle is limited by the least trustworthy boundary in the path through system context, interfaces, lifecycle, and tradeoff model.

**Domain delta:** For Systems Engineering, this model maps emergent behavior arises from interactions and feedback across the lifecycle onto the complete sociotechnical system and lifecycle and system context, interfaces, lifecycle, and tradeoff model.

## Governance (1)

### `governance-systems-engineering-evidence-authority-policy`

Systems Engineering: Evidence And Authority Policy. Work on the complete sociotechnical system and lifecycle must preserve 'local improvements do not degrade the whole-system outcome', cite end-to-end scenarios, interface evidence, operational constraints, and lifecycle risks, and remain within 'system integration versus component ownership'.

**Domain delta:** For Systems Engineering, this policy enforces system-level risk lacks an accountable integrator at system integration versus component ownership.

