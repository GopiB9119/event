# Performance Engineering

Measure and improve user-relevant latency, throughput, memory, energy, and capacity without guessing.

- **Domain ID:** `performance`
- **Boundary:** performance recommendation versus product experience and infrastructure budget
- **Invariant:** optimization preserves correctness and targets a measured bottleneck
- **Default evidence:** repeatable measurements on representative workloads and environments
- **Risk classes:** performance, cost

## Behavior (10)

### `behavior-performance-choose-falsifier`

Performance Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of benchmark, profile, budget, and capacity model that could disprove the current hypothesis.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-communicate-uncertainty`

Performance Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'premature or misdirected optimization adds complexity while real bottlenecks persist', and the next discriminating check.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-establish-state`

Performance Engineering: Establish Current State. Inspect benchmark, profile, budget, and capacity model and record the current behavior before proposing change.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-identify-owner`

Performance Engineering: Identify Owner And Boundary. Name the owner of benchmark, profile, budget, and capacity model, the boundary 'performance recommendation versus product experience and infrastructure budget', and who may decide or mutate it.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-minimize-change`

Performance Engineering: Make The Smallest Useful Change. Change only the owning slice of benchmark, profile, budget, and capacity model needed to protect 'optimization preserves correctness and targets a measured bottleneck'.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-protect-invariant`

Performance Engineering: Protect The Domain Invariant. Reject an option that can violate 'optimization preserves correctness and targets a measured bottleneck' without an approved mitigation.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-stop-and-escalate`

Performance Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'restore a baseline, profile the critical path, and optimize one measured constraint' to the accountable owner.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-surface-assumptions`

Performance Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'optimization preserves correctness and targets a measured bottleneck'.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-update-memory`

Performance Engineering: Update Durable Knowledge. Update the decision or memory record for benchmark, profile, budget, and capacity model with provenance and invalidation triggers.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

### `behavior-performance-validate-immediately`

Performance Engineering: Validate Immediately. Run repeatable measurements on representative workloads and environments or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Performance Engineering, this behavior operates on benchmark, profile, budget, and capacity model, uses repeatable measurements on representative workloads and environments, and protects 'optimization preserves correctness and targets a measured bottleneck'.

## Failure (6)

### `failure-performance-boundary-violation`

Performance Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for performance behavior and resource consumption.

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

### `failure-performance-evidence-overclaim`

Performance Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

### `failure-performance-premature-action`

Performance Engineering: Premature Action. anecdotes or synthetic microbenchmarks substitute for representative measurement

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

### `failure-performance-silent-failure`

Performance Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

### `failure-performance-stale-context`

Performance Engineering: Stale Context. The state of benchmark, profile, budget, and capacity model changed while routing continued from a stale checkpoint.

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

### `failure-performance-unbounded-loop`

Performance Engineering: Unbounded Repair Loop. Failures do not trigger a reset of anecdotes or synthetic microbenchmarks substitute for representative measurement.

**Domain delta:** In Performance Engineering, this failure threatens 'optimization preserves correctness and targets a measured bottleneck' through anecdotes or synthetic microbenchmarks substitute for representative measurement.

## Signal (4)

### `signal-performance-constraint-risk`

Performance Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'optimization preserves correctness and targets a measured bottleneck' for performance behavior and resource consumption.

**Domain delta:** For Performance Engineering, this signal observes performance behavior and resource consumption through benchmark, profile, budget, and capacity model while rejecting stale or untrusted substitutes.

### `signal-performance-explicit-mission`

Performance Engineering: Explicit Mission Signal. The current user request explicitly concerns performance behavior and resource consumption and states an observable outcome.

**Domain delta:** For Performance Engineering, this signal observes performance behavior and resource consumption through benchmark, profile, budget, and capacity model while rejecting stale or untrusted substitutes.

### `signal-performance-repository-evidence`

Performance Engineering: Repository Evidence Signal. Current source or accepted documentation identifies benchmark, profile, budget, and capacity model as the owning surface for performance behavior and resource consumption.

**Domain delta:** For Performance Engineering, this signal observes performance behavior and resource consumption through benchmark, profile, budget, and capacity model while rejecting stale or untrusted substitutes.

### `signal-performance-runtime-failure`

Performance Engineering: Runtime Failure Signal. A reproducible observation shows anecdotes or synthetic microbenchmarks substitute for representative measurement in benchmark, profile, budget, and capacity model.

**Domain delta:** For Performance Engineering, this signal observes performance behavior and resource consumption through benchmark, profile, budget, and capacity model while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-performance-escalate-and-contain`

Performance Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route restore a baseline, profile the critical path, and optimize one measured constraint to the accountable owner

**Domain delta:** For Performance Engineering, recovery targets anecdotes or synthetic microbenchmarks substitute for representative measurement in benchmark, profile, budget, and capacity model and exits only with repeatable measurements on representative workloads and environments.

### `recovery-performance-isolate-and-repair`

Performance Engineering: Isolate And Repair. Reduce to the smallest failing path in benchmark, profile, budget, and capacity model Apply one bounded repair Run repeatable measurements on representative workloads and environments Check adjacent invariants

**Domain delta:** For Performance Engineering, recovery targets anecdotes or synthetic microbenchmarks substitute for representative measurement in benchmark, profile, budget, and capacity model and exits only with repeatable measurements on representative workloads and environments.

### `recovery-performance-reset-and-reconstruct`

Performance Engineering: Reset And Reconstruct. Stop mutation Re-read benchmark, profile, budget, and capacity model and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Performance Engineering, recovery targets anecdotes or synthetic microbenchmarks substitute for representative measurement in benchmark, profile, budget, and capacity model and exits only with repeatable measurements on representative workloads and environments.

## Decision (2)

### `decision-performance-build-versus-test`

Performance Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Performance Engineering, this model decides measure, optimize, cache, scale, simplify, or defer using repeatable measurements on representative workloads and environments and the constraint 'optimization preserves correctness and targets a measured bottleneck'.

### `decision-performance-local-versus-systemic`

Performance Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening optimization preserves correctness and targets a measured bottleneck.

**Domain delta:** For Performance Engineering, this model decides measure, optimize, cache, scale, simplify, or defer using repeatable measurements on representative workloads and environments and the constraint 'optimization preserves correctness and targets a measured bottleneck'.

## Mental Model (2)

### `mental-model-performance-feedback-loop`

Performance Engineering: Feedback Loop. Actions on performance behavior and resource consumption change benchmark, profile, budget, and capacity model, which changes the next evidence and decision environment.

**Domain delta:** For Performance Engineering, this model maps queueing, locality, contention, and bottlenecks shape end-to-end performance onto performance behavior and resource consumption and benchmark, profile, budget, and capacity model.

### `mental-model-performance-weakest-link`

Performance Engineering: Weakest Link And Bottleneck. End-to-end quality for performance behavior and resource consumption is limited by the least trustworthy boundary in the path through benchmark, profile, budget, and capacity model.

**Domain delta:** For Performance Engineering, this model maps queueing, locality, contention, and bottlenecks shape end-to-end performance onto performance behavior and resource consumption and benchmark, profile, budget, and capacity model.

## Governance (1)

### `governance-performance-evidence-authority-policy`

Performance Engineering: Evidence And Authority Policy. Work on performance behavior and resource consumption must preserve 'optimization preserves correctness and targets a measured bottleneck', cite repeatable measurements on representative workloads and environments, and remain within 'performance recommendation versus product experience and infrastructure budget'.

**Domain delta:** For Performance Engineering, this policy enforces performance claims lack reproducible methodology or environment context at performance recommendation versus product experience and infrastructure budget.

