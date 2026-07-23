# Model Routing

Select the least costly capable model or deterministic tool for each bounded reasoning need.

- **Domain ID:** `model-routing`
- **Boundary:** routing recommendation versus configured model availability and budget ownership
- **Invariant:** expensive or external models are used only when they add measurable decision value
- **Default evidence:** quality, latency, cost, privacy, context, and failure measurements by task class
- **Risk classes:** ai, cost, privacy

## Behavior (10)

### `behavior-model-routing-choose-falsifier`

Model Routing: Choose Cheapest Falsifier. Choose the lowest-cost check of routing policy, capability registry, budget, and outcome telemetry that could disprove the current hypothesis.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-communicate-uncertainty`

Model Routing: Communicate Uncertainty. State confidence, missing evidence, failure impact 'token cost, latency, privacy exposure, or weak-model failure grows without outcome benefit', and the next discriminating check.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-establish-state`

Model Routing: Establish Current State. Inspect routing policy, capability registry, budget, and outcome telemetry and record the current behavior before proposing change.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-identify-owner`

Model Routing: Identify Owner And Boundary. Name the owner of routing policy, capability registry, budget, and outcome telemetry, the boundary 'routing recommendation versus configured model availability and budget ownership', and who may decide or mutate it.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-minimize-change`

Model Routing: Make The Smallest Useful Change. Change only the owning slice of routing policy, capability registry, budget, and outcome telemetry needed to protect 'expensive or external models are used only when they add measurable decision value'.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-protect-invariant`

Model Routing: Protect The Domain Invariant. Reject an option that can violate 'expensive or external models are used only when they add measurable decision value' without an approved mitigation.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-stop-and-escalate`

Model Routing: Stop And Escalate. Stop mutation, preserve evidence, and route 'classify the task, compare measured capability, and downgrade or escalate with a budget' to the accountable owner.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-surface-assumptions`

Model Routing: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'expensive or external models are used only when they add measurable decision value'.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-update-memory`

Model Routing: Update Durable Knowledge. Update the decision or memory record for routing policy, capability registry, budget, and outcome telemetry with provenance and invalidation triggers.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

### `behavior-model-routing-validate-immediately`

Model Routing: Validate Immediately. Run quality, latency, cost, privacy, context, and failure measurements by task class or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Model Routing, this behavior operates on routing policy, capability registry, budget, and outcome telemetry, uses quality, latency, cost, privacy, context, and failure measurements by task class, and protects 'expensive or external models are used only when they add measurable decision value'.

## Failure (6)

### `failure-model-routing-boundary-violation`

Model Routing: Boundary Violation. A local optimization bypasses the domain ownership model for model and non-model execution choice.

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

### `failure-model-routing-evidence-overclaim`

Model Routing: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

### `failure-model-routing-premature-action`

Model Routing: Premature Action. one model is used for every task or routing follows brand assumptions instead of evidence

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

### `failure-model-routing-silent-failure`

Model Routing: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

### `failure-model-routing-stale-context`

Model Routing: Stale Context. The state of routing policy, capability registry, budget, and outcome telemetry changed while routing continued from a stale checkpoint.

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

### `failure-model-routing-unbounded-loop`

Model Routing: Unbounded Repair Loop. Failures do not trigger a reset of one model is used for every task or routing follows brand assumptions instead of evidence.

**Domain delta:** In Model Routing, this failure threatens 'expensive or external models are used only when they add measurable decision value' through one model is used for every task or routing follows brand assumptions instead of evidence.

## Signal (4)

### `signal-model-routing-constraint-risk`

Model Routing: Constraint Or Risk Signal. A current constraint or risk threatens 'expensive or external models are used only when they add measurable decision value' for model and non-model execution choice.

**Domain delta:** For Model Routing, this signal observes model and non-model execution choice through routing policy, capability registry, budget, and outcome telemetry while rejecting stale or untrusted substitutes.

### `signal-model-routing-explicit-mission`

Model Routing: Explicit Mission Signal. The current user request explicitly concerns model and non-model execution choice and states an observable outcome.

**Domain delta:** For Model Routing, this signal observes model and non-model execution choice through routing policy, capability registry, budget, and outcome telemetry while rejecting stale or untrusted substitutes.

### `signal-model-routing-repository-evidence`

Model Routing: Repository Evidence Signal. Current source or accepted documentation identifies routing policy, capability registry, budget, and outcome telemetry as the owning surface for model and non-model execution choice.

**Domain delta:** For Model Routing, this signal observes model and non-model execution choice through routing policy, capability registry, budget, and outcome telemetry while rejecting stale or untrusted substitutes.

### `signal-model-routing-runtime-failure`

Model Routing: Runtime Failure Signal. A reproducible observation shows one model is used for every task or routing follows brand assumptions instead of evidence in routing policy, capability registry, budget, and outcome telemetry.

**Domain delta:** For Model Routing, this signal observes model and non-model execution choice through routing policy, capability registry, budget, and outcome telemetry while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-model-routing-escalate-and-contain`

Model Routing: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route classify the task, compare measured capability, and downgrade or escalate with a budget to the accountable owner

**Domain delta:** For Model Routing, recovery targets one model is used for every task or routing follows brand assumptions instead of evidence in routing policy, capability registry, budget, and outcome telemetry and exits only with quality, latency, cost, privacy, context, and failure measurements by task class.

### `recovery-model-routing-isolate-and-repair`

Model Routing: Isolate And Repair. Reduce to the smallest failing path in routing policy, capability registry, budget, and outcome telemetry Apply one bounded repair Run quality, latency, cost, privacy, context, and failure measurements by task class Check adjacent invariants

**Domain delta:** For Model Routing, recovery targets one model is used for every task or routing follows brand assumptions instead of evidence in routing policy, capability registry, budget, and outcome telemetry and exits only with quality, latency, cost, privacy, context, and failure measurements by task class.

### `recovery-model-routing-reset-and-reconstruct`

Model Routing: Reset And Reconstruct. Stop mutation Re-read routing policy, capability registry, budget, and outcome telemetry and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Model Routing, recovery targets one model is used for every task or routing follows brand assumptions instead of evidence in routing policy, capability registry, budget, and outcome telemetry and exits only with quality, latency, cost, privacy, context, and failure measurements by task class.

## Decision (2)

### `decision-model-routing-build-versus-test`

Model Routing: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Model Routing, this model decides deterministic tool, cache, local model, small cloud model, strong model, or human using quality, latency, cost, privacy, context, and failure measurements by task class and the constraint 'expensive or external models are used only when they add measurable decision value'.

### `decision-model-routing-local-versus-systemic`

Model Routing: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening expensive or external models are used only when they add measurable decision value.

**Domain delta:** For Model Routing, this model decides deterministic tool, cache, local model, small cloud model, strong model, or human using quality, latency, cost, privacy, context, and failure measurements by task class and the constraint 'expensive or external models are used only when they add measurable decision value'.

## Mental Model (2)

### `mental-model-model-routing-feedback-loop`

Model Routing: Feedback Loop. Actions on model and non-model execution choice change routing policy, capability registry, budget, and outcome telemetry, which changes the next evidence and decision environment.

**Domain delta:** For Model Routing, this model maps routing is an optimization under quality, cost, latency, privacy, and risk constraints onto model and non-model execution choice and routing policy, capability registry, budget, and outcome telemetry.

### `mental-model-model-routing-weakest-link`

Model Routing: Weakest Link And Bottleneck. End-to-end quality for model and non-model execution choice is limited by the least trustworthy boundary in the path through routing policy, capability registry, budget, and outcome telemetry.

**Domain delta:** For Model Routing, this model maps routing is an optimization under quality, cost, latency, privacy, and risk constraints onto model and non-model execution choice and routing policy, capability registry, budget, and outcome telemetry.

## Governance (1)

### `governance-model-routing-evidence-authority-policy`

Model Routing: Evidence And Authority Policy. Work on model and non-model execution choice must preserve 'expensive or external models are used only when they add measurable decision value', cite quality, latency, cost, privacy, context, and failure measurements by task class, and remain within 'routing recommendation versus configured model availability and budget ownership'.

**Domain delta:** For Model Routing, this policy enforces unapproved model, data transfer, or budget expansion occurs through routing at routing recommendation versus configured model availability and budget ownership.

