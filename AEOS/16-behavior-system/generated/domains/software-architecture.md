# Software Architecture

Define durable module boundaries, responsibilities, interfaces, and tradeoffs before scale.

- **Domain ID:** `software-architecture`
- **Boundary:** architecture decision versus local implementation detail
- **Invariant:** responsibilities are explicit and dependencies do not violate ownership
- **Default evidence:** dependency graph, critical flows, interface contracts, and failure-mode analysis
- **Risk classes:** architecture, maintainability

## Behavior (10)

### `behavior-software-architecture-choose-falsifier`

Software Architecture: Choose Cheapest Falsifier. Choose the lowest-cost check of architecture model and decision record that could disprove the current hypothesis.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-communicate-uncertainty`

Software Architecture: Communicate Uncertainty. State confidence, missing evidence, failure impact 'coupling, duplication, and hidden authority make change unsafe and expensive', and the next discriminating check.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-establish-state`

Software Architecture: Establish Current State. Inspect architecture model and decision record and record the current behavior before proposing change.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-identify-owner`

Software Architecture: Identify Owner And Boundary. Name the owner of architecture model and decision record, the boundary 'architecture decision versus local implementation detail', and who may decide or mutate it.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-minimize-change`

Software Architecture: Make The Smallest Useful Change. Change only the owning slice of architecture model and decision record needed to protect 'responsibilities are explicit and dependencies do not violate ownership'.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-protect-invariant`

Software Architecture: Protect The Domain Invariant. Reject an option that can violate 'responsibilities are explicit and dependencies do not violate ownership' without an approved mitigation.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-stop-and-escalate`

Software Architecture: Stop And Escalate. Stop mutation, preserve evidence, and route 'map data, flow, boundaries, and failure modes before choosing the smallest structural correction' to the accountable owner.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-surface-assumptions`

Software Architecture: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'responsibilities are explicit and dependencies do not violate ownership'.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-update-memory`

Software Architecture: Update Durable Knowledge. Update the decision or memory record for architecture model and decision record with provenance and invalidation triggers.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

### `behavior-software-architecture-validate-immediately`

Software Architecture: Validate Immediately. Run dependency graph, critical flows, interface contracts, and failure-mode analysis or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Software Architecture, this behavior operates on architecture model and decision record, uses dependency graph, critical flows, interface contracts, and failure-mode analysis, and protects 'responsibilities are explicit and dependencies do not violate ownership'.

## Failure (6)

### `failure-software-architecture-boundary-violation`

Software Architecture: Boundary Violation. A local optimization bypasses the domain ownership model for software structure and ownership boundaries.

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

### `failure-software-architecture-evidence-overclaim`

Software Architecture: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

### `failure-software-architecture-premature-action`

Software Architecture: Premature Action. code structure emerges from local edits without a system model

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

### `failure-software-architecture-silent-failure`

Software Architecture: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

### `failure-software-architecture-stale-context`

Software Architecture: Stale Context. The state of architecture model and decision record changed while routing continued from a stale checkpoint.

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

### `failure-software-architecture-unbounded-loop`

Software Architecture: Unbounded Repair Loop. Failures do not trigger a reset of code structure emerges from local edits without a system model.

**Domain delta:** In Software Architecture, this failure threatens 'responsibilities are explicit and dependencies do not violate ownership' through code structure emerges from local edits without a system model.

## Signal (4)

### `signal-software-architecture-constraint-risk`

Software Architecture: Constraint Or Risk Signal. A current constraint or risk threatens 'responsibilities are explicit and dependencies do not violate ownership' for software structure and ownership boundaries.

**Domain delta:** For Software Architecture, this signal observes software structure and ownership boundaries through architecture model and decision record while rejecting stale or untrusted substitutes.

### `signal-software-architecture-explicit-mission`

Software Architecture: Explicit Mission Signal. The current user request explicitly concerns software structure and ownership boundaries and states an observable outcome.

**Domain delta:** For Software Architecture, this signal observes software structure and ownership boundaries through architecture model and decision record while rejecting stale or untrusted substitutes.

### `signal-software-architecture-repository-evidence`

Software Architecture: Repository Evidence Signal. Current source or accepted documentation identifies architecture model and decision record as the owning surface for software structure and ownership boundaries.

**Domain delta:** For Software Architecture, this signal observes software structure and ownership boundaries through architecture model and decision record while rejecting stale or untrusted substitutes.

### `signal-software-architecture-runtime-failure`

Software Architecture: Runtime Failure Signal. A reproducible observation shows code structure emerges from local edits without a system model in architecture model and decision record.

**Domain delta:** For Software Architecture, this signal observes software structure and ownership boundaries through architecture model and decision record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-software-architecture-escalate-and-contain`

Software Architecture: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route map data, flow, boundaries, and failure modes before choosing the smallest structural correction to the accountable owner

**Domain delta:** For Software Architecture, recovery targets code structure emerges from local edits without a system model in architecture model and decision record and exits only with dependency graph, critical flows, interface contracts, and failure-mode analysis.

### `recovery-software-architecture-isolate-and-repair`

Software Architecture: Isolate And Repair. Reduce to the smallest failing path in architecture model and decision record Apply one bounded repair Run dependency graph, critical flows, interface contracts, and failure-mode analysis Check adjacent invariants

**Domain delta:** For Software Architecture, recovery targets code structure emerges from local edits without a system model in architecture model and decision record and exits only with dependency graph, critical flows, interface contracts, and failure-mode analysis.

### `recovery-software-architecture-reset-and-reconstruct`

Software Architecture: Reset And Reconstruct. Stop mutation Re-read architecture model and decision record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Software Architecture, recovery targets code structure emerges from local edits without a system model in architecture model and decision record and exits only with dependency graph, critical flows, interface contracts, and failure-mode analysis.

## Decision (2)

### `decision-software-architecture-build-versus-test`

Software Architecture: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Software Architecture, this model decides local design, abstraction, module boundary, or architectural change using dependency graph, critical flows, interface contracts, and failure-mode analysis and the constraint 'responsibilities are explicit and dependencies do not violate ownership'.

### `decision-software-architecture-local-versus-systemic`

Software Architecture: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening responsibilities are explicit and dependencies do not violate ownership.

**Domain delta:** For Software Architecture, this model decides local design, abstraction, module boundary, or architectural change using dependency graph, critical flows, interface contracts, and failure-mode analysis and the constraint 'responsibilities are explicit and dependencies do not violate ownership'.

## Mental Model (2)

### `mental-model-software-architecture-feedback-loop`

Software Architecture: Feedback Loop. Actions on software structure and ownership boundaries change architecture model and decision record, which changes the next evidence and decision environment.

**Domain delta:** For Software Architecture, this model maps architecture constrains the cost and safety of future change onto software structure and ownership boundaries and architecture model and decision record.

### `mental-model-software-architecture-weakest-link`

Software Architecture: Weakest Link And Bottleneck. End-to-end quality for software structure and ownership boundaries is limited by the least trustworthy boundary in the path through architecture model and decision record.

**Domain delta:** For Software Architecture, this model maps architecture constrains the cost and safety of future change onto software structure and ownership boundaries and architecture model and decision record.

## Governance (1)

### `governance-software-architecture-evidence-authority-policy`

Software Architecture: Evidence And Authority Policy. Work on software structure and ownership boundaries must preserve 'responsibilities are explicit and dependencies do not violate ownership', cite dependency graph, critical flows, interface contracts, and failure-mode analysis, and remain within 'architecture decision versus local implementation detail'.

**Domain delta:** For Software Architecture, this policy enforces a durable boundary changes without a reviewed decision at architecture decision versus local implementation detail.

