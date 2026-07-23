# Startup Execution

Protect a young product from feature addiction, drift, and premature scale.

- **Domain ID:** `startup-execution`
- **Boundary:** founder priority versus engineering feasibility and safety
- **Invariant:** current work advances the agreed milestone before new scope is admitted
- **Default evidence:** milestone done-criteria, user signal, runway constraint, and shipped learning
- **Risk classes:** business, delivery

## Behavior (10)

### `behavior-startup-execution-choose-falsifier`

Startup Execution: Choose Cheapest Falsifier. Choose the lowest-cost check of evidence-based backlog and milestone record that could disprove the current hypothesis.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-communicate-uncertainty`

Startup Execution: Communicate Uncertainty. State confidence, missing evidence, failure impact 'runway and attention are consumed by unfinished features and rewrites', and the next discriminating check.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-establish-state`

Startup Execution: Establish Current State. Inspect evidence-based backlog and milestone record and record the current behavior before proposing change.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-identify-owner`

Startup Execution: Identify Owner And Boundary. Name the owner of evidence-based backlog and milestone record, the boundary 'founder priority versus engineering feasibility and safety', and who may decide or mutate it.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-minimize-change`

Startup Execution: Make The Smallest Useful Change. Change only the owning slice of evidence-based backlog and milestone record needed to protect 'current work advances the agreed milestone before new scope is admitted'.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-protect-invariant`

Startup Execution: Protect The Domain Invariant. Reject an option that can violate 'current work advances the agreed milestone before new scope is admitted' without an approved mitigation.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-stop-and-escalate`

Startup Execution: Stop And Escalate. Stop mutation, preserve evidence, and route 'cut scope, restore the milestone, and choose one observable next action' to the accountable owner.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-surface-assumptions`

Startup Execution: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'current work advances the agreed milestone before new scope is admitted'.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-update-memory`

Startup Execution: Update Durable Knowledge. Update the decision or memory record for evidence-based backlog and milestone record with provenance and invalidation triggers.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

### `behavior-startup-execution-validate-immediately`

Startup Execution: Validate Immediately. Run milestone done-criteria, user signal, runway constraint, and shipped learning or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Startup Execution, this behavior operates on evidence-based backlog and milestone record, uses milestone done-criteria, user signal, runway constraint, and shipped learning, and protects 'current work advances the agreed milestone before new scope is admitted'.

## Failure (6)

### `failure-startup-execution-boundary-violation`

Startup Execution: Boundary Violation. A local optimization bypasses the domain ownership model for startup milestone and smallest shippable learning slice.

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

### `failure-startup-execution-evidence-overclaim`

Startup Execution: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

### `failure-startup-execution-premature-action`

Startup Execution: Premature Action. new ideas bypass milestone and validation gates

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

### `failure-startup-execution-silent-failure`

Startup Execution: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

### `failure-startup-execution-stale-context`

Startup Execution: Stale Context. The state of evidence-based backlog and milestone record changed while routing continued from a stale checkpoint.

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

### `failure-startup-execution-unbounded-loop`

Startup Execution: Unbounded Repair Loop. Failures do not trigger a reset of new ideas bypass milestone and validation gates.

**Domain delta:** In Startup Execution, this failure threatens 'current work advances the agreed milestone before new scope is admitted' through new ideas bypass milestone and validation gates.

## Signal (4)

### `signal-startup-execution-constraint-risk`

Startup Execution: Constraint Or Risk Signal. A current constraint or risk threatens 'current work advances the agreed milestone before new scope is admitted' for startup milestone and smallest shippable learning slice.

**Domain delta:** For Startup Execution, this signal observes startup milestone and smallest shippable learning slice through evidence-based backlog and milestone record while rejecting stale or untrusted substitutes.

### `signal-startup-execution-explicit-mission`

Startup Execution: Explicit Mission Signal. The current user request explicitly concerns startup milestone and smallest shippable learning slice and states an observable outcome.

**Domain delta:** For Startup Execution, this signal observes startup milestone and smallest shippable learning slice through evidence-based backlog and milestone record while rejecting stale or untrusted substitutes.

### `signal-startup-execution-repository-evidence`

Startup Execution: Repository Evidence Signal. Current source or accepted documentation identifies evidence-based backlog and milestone record as the owning surface for startup milestone and smallest shippable learning slice.

**Domain delta:** For Startup Execution, this signal observes startup milestone and smallest shippable learning slice through evidence-based backlog and milestone record while rejecting stale or untrusted substitutes.

### `signal-startup-execution-runtime-failure`

Startup Execution: Runtime Failure Signal. A reproducible observation shows new ideas bypass milestone and validation gates in evidence-based backlog and milestone record.

**Domain delta:** For Startup Execution, this signal observes startup milestone and smallest shippable learning slice through evidence-based backlog and milestone record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-startup-execution-escalate-and-contain`

Startup Execution: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route cut scope, restore the milestone, and choose one observable next action to the accountable owner

**Domain delta:** For Startup Execution, recovery targets new ideas bypass milestone and validation gates in evidence-based backlog and milestone record and exits only with milestone done-criteria, user signal, runway constraint, and shipped learning.

### `recovery-startup-execution-isolate-and-repair`

Startup Execution: Isolate And Repair. Reduce to the smallest failing path in evidence-based backlog and milestone record Apply one bounded repair Run milestone done-criteria, user signal, runway constraint, and shipped learning Check adjacent invariants

**Domain delta:** For Startup Execution, recovery targets new ideas bypass milestone and validation gates in evidence-based backlog and milestone record and exits only with milestone done-criteria, user signal, runway constraint, and shipped learning.

### `recovery-startup-execution-reset-and-reconstruct`

Startup Execution: Reset And Reconstruct. Stop mutation Re-read evidence-based backlog and milestone record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Startup Execution, recovery targets new ideas bypass milestone and validation gates in evidence-based backlog and milestone record and exits only with milestone done-criteria, user signal, runway constraint, and shipped learning.

## Decision (2)

### `decision-startup-execution-build-versus-test`

Startup Execution: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Startup Execution, this model decides ship, cut, test, defer, or stop using milestone done-criteria, user signal, runway constraint, and shipped learning and the constraint 'current work advances the agreed milestone before new scope is admitted'.

### `decision-startup-execution-local-versus-systemic`

Startup Execution: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening current work advances the agreed milestone before new scope is admitted.

**Domain delta:** For Startup Execution, this model decides ship, cut, test, defer, or stop using milestone done-criteria, user signal, runway constraint, and shipped learning and the constraint 'current work advances the agreed milestone before new scope is admitted'.

## Mental Model (2)

### `mental-model-startup-execution-feedback-loop`

Startup Execution: Feedback Loop. Actions on startup milestone and smallest shippable learning slice change evidence-based backlog and milestone record, which changes the next evidence and decision environment.

**Domain delta:** For Startup Execution, this model maps startup progress is constrained by focus, feedback speed, and opportunity cost onto startup milestone and smallest shippable learning slice and evidence-based backlog and milestone record.

### `mental-model-startup-execution-weakest-link`

Startup Execution: Weakest Link And Bottleneck. End-to-end quality for startup milestone and smallest shippable learning slice is limited by the least trustworthy boundary in the path through evidence-based backlog and milestone record.

**Domain delta:** For Startup Execution, this model maps startup progress is constrained by focus, feedback speed, and opportunity cost onto startup milestone and smallest shippable learning slice and evidence-based backlog and milestone record.

## Governance (1)

### `governance-startup-execution-evidence-authority-policy`

Startup Execution: Evidence And Authority Policy. Work on startup milestone and smallest shippable learning slice must preserve 'current work advances the agreed milestone before new scope is admitted', cite milestone done-criteria, user signal, runway constraint, and shipped learning, and remain within 'founder priority versus engineering feasibility and safety'.

**Domain delta:** For Startup Execution, this policy enforces scope expands without proving why it should delay the milestone at founder priority versus engineering feasibility and safety.

