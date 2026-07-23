# Codebase Navigation

Find the owning implementation, callers, tests, history, and constraints before editing.

- **Domain ID:** `codebase-navigation`
- **Boundary:** derived search index versus authoritative current source
- **Invariant:** changes land in the code that actually controls behavior
- **Default evidence:** current definitions, usages, neighboring tests, and applicable instructions
- **Risk classes:** engineering, context

## Behavior (10)

### `behavior-codebase-navigation-choose-falsifier`

Codebase Navigation: Choose Cheapest Falsifier. Choose the lowest-cost check of candidate path and dependency map that could disprove the current hypothesis.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-communicate-uncertainty`

Codebase Navigation: Communicate Uncertainty. State confidence, missing evidence, failure impact 'the wrong file is edited, logic is duplicated, or hidden callers regress', and the next discriminating check.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-establish-state`

Codebase Navigation: Establish Current State. Inspect candidate path and dependency map and record the current behavior before proposing change.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-identify-owner`

Codebase Navigation: Identify Owner And Boundary. Name the owner of candidate path and dependency map, the boundary 'derived search index versus authoritative current source', and who may decide or mutate it.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-minimize-change`

Codebase Navigation: Make The Smallest Useful Change. Change only the owning slice of candidate path and dependency map needed to protect 'changes land in the code that actually controls behavior'.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-protect-invariant`

Codebase Navigation: Protect The Domain Invariant. Reject an option that can violate 'changes land in the code that actually controls behavior' without an approved mitigation.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-stop-and-escalate`

Codebase Navigation: Stop And Escalate. Stop mutation, preserve evidence, and route 'step to the nearest code that computes or mutates the behavior' to the accountable owner.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-surface-assumptions`

Codebase Navigation: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'changes land in the code that actually controls behavior'.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-update-memory`

Codebase Navigation: Update Durable Knowledge. Update the decision or memory record for candidate path and dependency map with provenance and invalidation triggers.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

### `behavior-codebase-navigation-validate-immediately`

Codebase Navigation: Validate Immediately. Run current definitions, usages, neighboring tests, and applicable instructions or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Codebase Navigation, this behavior operates on candidate path and dependency map, uses current definitions, usages, neighboring tests, and applicable instructions, and protects 'changes land in the code that actually controls behavior'.

## Failure (6)

### `failure-codebase-navigation-boundary-violation`

Codebase Navigation: Boundary Violation. A local optimization bypasses the domain ownership model for repository structure and semantic ownership.

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

### `failure-codebase-navigation-evidence-overclaim`

Codebase Navigation: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

### `failure-codebase-navigation-premature-action`

Codebase Navigation: Premature Action. surface matches are mistaken for ownership without tracing control flow

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

### `failure-codebase-navigation-silent-failure`

Codebase Navigation: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

### `failure-codebase-navigation-stale-context`

Codebase Navigation: Stale Context. The state of candidate path and dependency map changed while routing continued from a stale checkpoint.

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

### `failure-codebase-navigation-unbounded-loop`

Codebase Navigation: Unbounded Repair Loop. Failures do not trigger a reset of surface matches are mistaken for ownership without tracing control flow.

**Domain delta:** In Codebase Navigation, this failure threatens 'changes land in the code that actually controls behavior' through surface matches are mistaken for ownership without tracing control flow.

## Signal (4)

### `signal-codebase-navigation-constraint-risk`

Codebase Navigation: Constraint Or Risk Signal. A current constraint or risk threatens 'changes land in the code that actually controls behavior' for repository structure and semantic ownership.

**Domain delta:** For Codebase Navigation, this signal observes repository structure and semantic ownership through candidate path and dependency map while rejecting stale or untrusted substitutes.

### `signal-codebase-navigation-explicit-mission`

Codebase Navigation: Explicit Mission Signal. The current user request explicitly concerns repository structure and semantic ownership and states an observable outcome.

**Domain delta:** For Codebase Navigation, this signal observes repository structure and semantic ownership through candidate path and dependency map while rejecting stale or untrusted substitutes.

### `signal-codebase-navigation-repository-evidence`

Codebase Navigation: Repository Evidence Signal. Current source or accepted documentation identifies candidate path and dependency map as the owning surface for repository structure and semantic ownership.

**Domain delta:** For Codebase Navigation, this signal observes repository structure and semantic ownership through candidate path and dependency map while rejecting stale or untrusted substitutes.

### `signal-codebase-navigation-runtime-failure`

Codebase Navigation: Runtime Failure Signal. A reproducible observation shows surface matches are mistaken for ownership without tracing control flow in candidate path and dependency map.

**Domain delta:** For Codebase Navigation, this signal observes repository structure and semantic ownership through candidate path and dependency map while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-codebase-navigation-escalate-and-contain`

Codebase Navigation: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route step to the nearest code that computes or mutates the behavior to the accountable owner

**Domain delta:** For Codebase Navigation, recovery targets surface matches are mistaken for ownership without tracing control flow in candidate path and dependency map and exits only with current definitions, usages, neighboring tests, and applicable instructions.

### `recovery-codebase-navigation-isolate-and-repair`

Codebase Navigation: Isolate And Repair. Reduce to the smallest failing path in candidate path and dependency map Apply one bounded repair Run current definitions, usages, neighboring tests, and applicable instructions Check adjacent invariants

**Domain delta:** For Codebase Navigation, recovery targets surface matches are mistaken for ownership without tracing control flow in candidate path and dependency map and exits only with current definitions, usages, neighboring tests, and applicable instructions.

### `recovery-codebase-navigation-reset-and-reconstruct`

Codebase Navigation: Reset And Reconstruct. Stop mutation Re-read candidate path and dependency map and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Codebase Navigation, recovery targets surface matches are mistaken for ownership without tracing control flow in candidate path and dependency map and exits only with current definitions, usages, neighboring tests, and applicable instructions.

## Decision (2)

### `decision-codebase-navigation-build-versus-test`

Codebase Navigation: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Codebase Navigation, this model decides read locally, trace references, inspect history, or escalate discovery using current definitions, usages, neighboring tests, and applicable instructions and the constraint 'changes land in the code that actually controls behavior'.

### `decision-codebase-navigation-local-versus-systemic`

Codebase Navigation: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening changes land in the code that actually controls behavior.

**Domain delta:** For Codebase Navigation, this model decides read locally, trace references, inspect history, or escalate discovery using current definitions, usages, neighboring tests, and applicable instructions and the constraint 'changes land in the code that actually controls behavior'.

## Mental Model (2)

### `mental-model-codebase-navigation-feedback-loop`

Codebase Navigation: Feedback Loop. Actions on repository structure and semantic ownership change candidate path and dependency map, which changes the next evidence and decision environment.

**Domain delta:** For Codebase Navigation, this model maps semantic ownership is discovered through evidence paths, not file names alone onto repository structure and semantic ownership and candidate path and dependency map.

### `mental-model-codebase-navigation-weakest-link`

Codebase Navigation: Weakest Link And Bottleneck. End-to-end quality for repository structure and semantic ownership is limited by the least trustworthy boundary in the path through candidate path and dependency map.

**Domain delta:** For Codebase Navigation, this model maps semantic ownership is discovered through evidence paths, not file names alone onto repository structure and semantic ownership and candidate path and dependency map.

## Governance (1)

### `governance-codebase-navigation-evidence-authority-policy`

Codebase Navigation: Evidence And Authority Policy. Work on repository structure and semantic ownership must preserve 'changes land in the code that actually controls behavior', cite current definitions, usages, neighboring tests, and applicable instructions, and remain within 'derived search index versus authoritative current source'.

**Domain delta:** For Codebase Navigation, this policy enforces derived indexes override current source or repository instructions at derived search index versus authoritative current source.

