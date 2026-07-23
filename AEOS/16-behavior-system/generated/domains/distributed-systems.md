# Distributed Systems

Reason explicitly about partial failure, consistency, ordering, retries, identity, and network partitions.

- **Domain ID:** `distributed-systems`
- **Boundary:** server authority, client cache, network, and external service ownership
- **Invariant:** authorized participants converge without double effects or silent overwrite under expected failures
- **Default evidence:** partition, retry, duplicate, reordering, stale-read, and convergence observations
- **Risk classes:** distributed, reliability, data

## Behavior (10)

### `behavior-distributed-systems-choose-falsifier`

Distributed Systems: Choose Cheapest Falsifier. Choose the lowest-cost check of consistency model, protocol, idempotency design, and convergence tests that could disprove the current hypothesis.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-communicate-uncertainty`

Distributed Systems: Communicate Uncertainty. State confidence, missing evidence, failure impact 'split-brain state, duplicate money effects, stale authorization, or irreconcilable history', and the next discriminating check.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-establish-state`

Distributed Systems: Establish Current State. Inspect consistency model, protocol, idempotency design, and convergence tests and record the current behavior before proposing change.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-identify-owner`

Distributed Systems: Identify Owner And Boundary. Name the owner of consistency model, protocol, idempotency design, and convergence tests, the boundary 'server authority, client cache, network, and external service ownership', and who may decide or mutate it.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-minimize-change`

Distributed Systems: Make The Smallest Useful Change. Change only the owning slice of consistency model, protocol, idempotency design, and convergence tests needed to protect 'authorized participants converge without double effects or silent overwrite under expected failures'.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-protect-invariant`

Distributed Systems: Protect The Domain Invariant. Reject an option that can violate 'authorized participants converge without double effects or silent overwrite under expected failures' without an approved mitigation.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-stop-and-escalate`

Distributed Systems: Stop And Escalate. Stop mutation, preserve evidence, and route 'identify authoritative state, contain writes, reconcile using idempotent history, and expose staleness' to the accountable owner.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-surface-assumptions`

Distributed Systems: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'authorized participants converge without double effects or silent overwrite under expected failures'.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-update-memory`

Distributed Systems: Update Durable Knowledge. Update the decision or memory record for consistency model, protocol, idempotency design, and convergence tests with provenance and invalidation triggers.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `behavior-distributed-systems-validate-immediately`

Distributed Systems: Validate Immediately. Run partition, retry, duplicate, reordering, stale-read, and convergence observations or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Distributed Systems, this behavior operates on consistency model, protocol, idempotency design, and convergence tests, uses partition, retry, duplicate, reordering, stale-read, and convergence observations, and protects 'authorized participants converge without double effects or silent overwrite under expected failures'.

## Failure (6)

### `failure-distributed-systems-boundary-violation`

Distributed Systems: Boundary Violation. A local optimization bypasses the domain ownership model for multi-node state and networked coordination.

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

### `failure-distributed-systems-evidence-overclaim`

Distributed Systems: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

### `failure-distributed-systems-premature-action`

Distributed Systems: Premature Action. single-process assumptions are applied across unreliable networks and clocks

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

### `failure-distributed-systems-silent-failure`

Distributed Systems: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

### `failure-distributed-systems-stale-context`

Distributed Systems: Stale Context. The state of consistency model, protocol, idempotency design, and convergence tests changed while routing continued from a stale checkpoint.

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

### `failure-distributed-systems-unbounded-loop`

Distributed Systems: Unbounded Repair Loop. Failures do not trigger a reset of single-process assumptions are applied across unreliable networks and clocks.

**Domain delta:** In Distributed Systems, this failure threatens 'authorized participants converge without double effects or silent overwrite under expected failures' through single-process assumptions are applied across unreliable networks and clocks.

## Signal (4)

### `signal-distributed-systems-constraint-risk`

Distributed Systems: Constraint Or Risk Signal. A current constraint or risk threatens 'authorized participants converge without double effects or silent overwrite under expected failures' for multi-node state and networked coordination.

**Domain delta:** For Distributed Systems, this signal observes multi-node state and networked coordination through consistency model, protocol, idempotency design, and convergence tests while rejecting stale or untrusted substitutes.

### `signal-distributed-systems-explicit-mission`

Distributed Systems: Explicit Mission Signal. The current user request explicitly concerns multi-node state and networked coordination and states an observable outcome.

**Domain delta:** For Distributed Systems, this signal observes multi-node state and networked coordination through consistency model, protocol, idempotency design, and convergence tests while rejecting stale or untrusted substitutes.

### `signal-distributed-systems-repository-evidence`

Distributed Systems: Repository Evidence Signal. Current source or accepted documentation identifies consistency model, protocol, idempotency design, and convergence tests as the owning surface for multi-node state and networked coordination.

**Domain delta:** For Distributed Systems, this signal observes multi-node state and networked coordination through consistency model, protocol, idempotency design, and convergence tests while rejecting stale or untrusted substitutes.

### `signal-distributed-systems-runtime-failure`

Distributed Systems: Runtime Failure Signal. A reproducible observation shows single-process assumptions are applied across unreliable networks and clocks in consistency model, protocol, idempotency design, and convergence tests.

**Domain delta:** For Distributed Systems, this signal observes multi-node state and networked coordination through consistency model, protocol, idempotency design, and convergence tests while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-distributed-systems-escalate-and-contain`

Distributed Systems: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route identify authoritative state, contain writes, reconcile using idempotent history, and expose staleness to the accountable owner

**Domain delta:** For Distributed Systems, recovery targets single-process assumptions are applied across unreliable networks and clocks in consistency model, protocol, idempotency design, and convergence tests and exits only with partition, retry, duplicate, reordering, stale-read, and convergence observations.

### `recovery-distributed-systems-isolate-and-repair`

Distributed Systems: Isolate And Repair. Reduce to the smallest failing path in consistency model, protocol, idempotency design, and convergence tests Apply one bounded repair Run partition, retry, duplicate, reordering, stale-read, and convergence observations Check adjacent invariants

**Domain delta:** For Distributed Systems, recovery targets single-process assumptions are applied across unreliable networks and clocks in consistency model, protocol, idempotency design, and convergence tests and exits only with partition, retry, duplicate, reordering, stale-read, and convergence observations.

### `recovery-distributed-systems-reset-and-reconstruct`

Distributed Systems: Reset And Reconstruct. Stop mutation Re-read consistency model, protocol, idempotency design, and convergence tests and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Distributed Systems, recovery targets single-process assumptions are applied across unreliable networks and clocks in consistency model, protocol, idempotency design, and convergence tests and exits only with partition, retry, duplicate, reordering, stale-read, and convergence observations.

## Decision (2)

### `decision-distributed-systems-build-versus-test`

Distributed Systems: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Distributed Systems, this model decides strong consistency, eventual consistency, reject, retry, reconcile, or degrade using partition, retry, duplicate, reordering, stale-read, and convergence observations and the constraint 'authorized participants converge without double effects or silent overwrite under expected failures'.

### `decision-distributed-systems-local-versus-systemic`

Distributed Systems: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening authorized participants converge without double effects or silent overwrite under expected failures.

**Domain delta:** For Distributed Systems, this model decides strong consistency, eventual consistency, reject, retry, reconcile, or degrade using partition, retry, duplicate, reordering, stale-read, and convergence observations and the constraint 'authorized participants converge without double effects or silent overwrite under expected failures'.

## Mental Model (2)

### `mental-model-distributed-systems-feedback-loop`

Distributed Systems: Feedback Loop. Actions on multi-node state and networked coordination change consistency model, protocol, idempotency design, and convergence tests, which changes the next evidence and decision environment.

**Domain delta:** For Distributed Systems, this model maps networks permit delay, duplication, reordering, and partition rather than reliable immediate delivery onto multi-node state and networked coordination and consistency model, protocol, idempotency design, and convergence tests.

### `mental-model-distributed-systems-weakest-link`

Distributed Systems: Weakest Link And Bottleneck. End-to-end quality for multi-node state and networked coordination is limited by the least trustworthy boundary in the path through consistency model, protocol, idempotency design, and convergence tests.

**Domain delta:** For Distributed Systems, this model maps networks permit delay, duplication, reordering, and partition rather than reliable immediate delivery onto multi-node state and networked coordination and consistency model, protocol, idempotency design, and convergence tests.

## Governance (1)

### `governance-distributed-systems-evidence-authority-policy`

Distributed Systems: Evidence And Authority Policy. Work on multi-node state and networked coordination must preserve 'authorized participants converge without double effects or silent overwrite under expected failures', cite partition, retry, duplicate, reordering, stale-read, and convergence observations, and remain within 'server authority, client cache, network, and external service ownership'.

**Domain delta:** For Distributed Systems, this policy enforces distributed financial or authorization writes lack server authority and idempotency at server authority, client cache, network, and external service ownership.

