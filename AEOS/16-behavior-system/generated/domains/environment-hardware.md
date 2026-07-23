# Environment And Hardware Awareness

Adapt engineering strategy to actual OS, hardware, storage, network, tools, and developer capacity.

- **Domain ID:** `environment-hardware`
- **Boundary:** observed local environment versus assumed or recommended infrastructure
- **Invariant:** the plan fits the real environment without avoidable failure or resource exhaustion
- **Default evidence:** measured RAM, disk, process, SDK, device, network, and tool availability
- **Risk classes:** environment, cost, safety

## Behavior (10)

### `behavior-environment-hardware-choose-falsifier`

Environment And Hardware Awareness: Choose Cheapest Falsifier. Choose the lowest-cost check of environment snapshot and resource-aware execution plan that could disprove the current hypothesis.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-communicate-uncertainty`

Environment And Hardware Awareness: Communicate Uncertainty. State confidence, missing evidence, failure impact 'builds hang, storage is exhausted, downloads are blocked, or user devices are modified unexpectedly', and the next discriminating check.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-establish-state`

Environment And Hardware Awareness: Establish Current State. Inspect environment snapshot and resource-aware execution plan and record the current behavior before proposing change.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-identify-owner`

Environment And Hardware Awareness: Identify Owner And Boundary. Name the owner of environment snapshot and resource-aware execution plan, the boundary 'observed local environment versus assumed or recommended infrastructure', and who may decide or mutate it.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-minimize-change`

Environment And Hardware Awareness: Make The Smallest Useful Change. Change only the owning slice of environment snapshot and resource-aware execution plan needed to protect 'the plan fits the real environment without avoidable failure or resource exhaustion'.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-protect-invariant`

Environment And Hardware Awareness: Protect The Domain Invariant. Reject an option that can violate 'the plan fits the real environment without avoidable failure or resource exhaustion' without an approved mitigation.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-stop-and-escalate`

Environment And Hardware Awareness: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop overlapping workloads, measure constraints, and choose a lower-resource path' to the accountable owner.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-surface-assumptions`

Environment And Hardware Awareness: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the plan fits the real environment without avoidable failure or resource exhaustion'.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-update-memory`

Environment And Hardware Awareness: Update Durable Knowledge. Update the decision or memory record for environment snapshot and resource-aware execution plan with provenance and invalidation triggers.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `behavior-environment-hardware-validate-immediately`

Environment And Hardware Awareness: Validate Immediately. Run measured RAM, disk, process, SDK, device, network, and tool availability or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Environment And Hardware Awareness, this behavior operates on environment snapshot and resource-aware execution plan, uses measured RAM, disk, process, SDK, device, network, and tool availability, and protects 'the plan fits the real environment without avoidable failure or resource exhaustion'.

## Failure (6)

### `failure-environment-hardware-boundary-violation`

Environment And Hardware Awareness: Boundary Violation. A local optimization bypasses the domain ownership model for execution environment and resource constraints.

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

### `failure-environment-hardware-evidence-overclaim`

Environment And Hardware Awareness: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

### `failure-environment-hardware-premature-action`

Environment And Hardware Awareness: Premature Action. generic instructions ignore measured hardware, network, policy, and connected-device state

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

### `failure-environment-hardware-silent-failure`

Environment And Hardware Awareness: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

### `failure-environment-hardware-stale-context`

Environment And Hardware Awareness: Stale Context. The state of environment snapshot and resource-aware execution plan changed while routing continued from a stale checkpoint.

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

### `failure-environment-hardware-unbounded-loop`

Environment And Hardware Awareness: Unbounded Repair Loop. Failures do not trigger a reset of generic instructions ignore measured hardware, network, policy, and connected-device state.

**Domain delta:** In Environment And Hardware Awareness, this failure threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' through generic instructions ignore measured hardware, network, policy, and connected-device state.

## Signal (4)

### `signal-environment-hardware-constraint-risk`

Environment And Hardware Awareness: Constraint Or Risk Signal. A current constraint or risk threatens 'the plan fits the real environment without avoidable failure or resource exhaustion' for execution environment and resource constraints.

**Domain delta:** For Environment And Hardware Awareness, this signal observes execution environment and resource constraints through environment snapshot and resource-aware execution plan while rejecting stale or untrusted substitutes.

### `signal-environment-hardware-explicit-mission`

Environment And Hardware Awareness: Explicit Mission Signal. The current user request explicitly concerns execution environment and resource constraints and states an observable outcome.

**Domain delta:** For Environment And Hardware Awareness, this signal observes execution environment and resource constraints through environment snapshot and resource-aware execution plan while rejecting stale or untrusted substitutes.

### `signal-environment-hardware-repository-evidence`

Environment And Hardware Awareness: Repository Evidence Signal. Current source or accepted documentation identifies environment snapshot and resource-aware execution plan as the owning surface for execution environment and resource constraints.

**Domain delta:** For Environment And Hardware Awareness, this signal observes execution environment and resource constraints through environment snapshot and resource-aware execution plan while rejecting stale or untrusted substitutes.

### `signal-environment-hardware-runtime-failure`

Environment And Hardware Awareness: Runtime Failure Signal. A reproducible observation shows generic instructions ignore measured hardware, network, policy, and connected-device state in environment snapshot and resource-aware execution plan.

**Domain delta:** For Environment And Hardware Awareness, this signal observes execution environment and resource constraints through environment snapshot and resource-aware execution plan while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-environment-hardware-escalate-and-contain`

Environment And Hardware Awareness: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop overlapping workloads, measure constraints, and choose a lower-resource path to the accountable owner

**Domain delta:** For Environment And Hardware Awareness, recovery targets generic instructions ignore measured hardware, network, policy, and connected-device state in environment snapshot and resource-aware execution plan and exits only with measured RAM, disk, process, SDK, device, network, and tool availability.

### `recovery-environment-hardware-isolate-and-repair`

Environment And Hardware Awareness: Isolate And Repair. Reduce to the smallest failing path in environment snapshot and resource-aware execution plan Apply one bounded repair Run measured RAM, disk, process, SDK, device, network, and tool availability Check adjacent invariants

**Domain delta:** For Environment And Hardware Awareness, recovery targets generic instructions ignore measured hardware, network, policy, and connected-device state in environment snapshot and resource-aware execution plan and exits only with measured RAM, disk, process, SDK, device, network, and tool availability.

### `recovery-environment-hardware-reset-and-reconstruct`

Environment And Hardware Awareness: Reset And Reconstruct. Stop mutation Re-read environment snapshot and resource-aware execution plan and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Environment And Hardware Awareness, recovery targets generic instructions ignore measured hardware, network, policy, and connected-device state in environment snapshot and resource-aware execution plan and exits only with measured RAM, disk, process, SDK, device, network, and tool availability.

## Decision (2)

### `decision-environment-hardware-build-versus-test`

Environment And Hardware Awareness: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Environment And Hardware Awareness, this model decides local tool, emulator, physical device, remote runner, defer, or reduce scope using measured RAM, disk, process, SDK, device, network, and tool availability and the constraint 'the plan fits the real environment without avoidable failure or resource exhaustion'.

### `decision-environment-hardware-local-versus-systemic`

Environment And Hardware Awareness: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the plan fits the real environment without avoidable failure or resource exhaustion.

**Domain delta:** For Environment And Hardware Awareness, this model decides local tool, emulator, physical device, remote runner, defer, or reduce scope using measured RAM, disk, process, SDK, device, network, and tool availability and the constraint 'the plan fits the real environment without avoidable failure or resource exhaustion'.

## Mental Model (2)

### `mental-model-environment-hardware-feedback-loop`

Environment And Hardware Awareness: Feedback Loop. Actions on execution environment and resource constraints change environment snapshot and resource-aware execution plan, which changes the next evidence and decision environment.

**Domain delta:** For Environment And Hardware Awareness, this model maps available resources constrain feasible workflows and change the optimal verification strategy onto execution environment and resource constraints and environment snapshot and resource-aware execution plan.

### `mental-model-environment-hardware-weakest-link`

Environment And Hardware Awareness: Weakest Link And Bottleneck. End-to-end quality for execution environment and resource constraints is limited by the least trustworthy boundary in the path through environment snapshot and resource-aware execution plan.

**Domain delta:** For Environment And Hardware Awareness, this model maps available resources constrain feasible workflows and change the optimal verification strategy onto execution environment and resource constraints and environment snapshot and resource-aware execution plan.

## Governance (1)

### `governance-environment-hardware-evidence-authority-policy`

Environment And Hardware Awareness: Evidence And Authority Policy. Work on execution environment and resource constraints must preserve 'the plan fits the real environment without avoidable failure or resource exhaustion', cite measured RAM, disk, process, SDK, device, network, and tool availability, and remain within 'observed local environment versus assumed or recommended infrastructure'.

**Domain delta:** For Environment And Hardware Awareness, this policy enforces tools install, download, elevate, or touch devices without need and approval at observed local environment versus assumed or recommended infrastructure.

