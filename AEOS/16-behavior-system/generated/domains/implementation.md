# Implementation

Translate an accepted design into the smallest readable, testable, maintainable change.

- **Domain ID:** `implementation`
- **Boundary:** accepted implementation scope and source ownership
- **Invariant:** the change solves the verified cause without unrelated mutation
- **Default evidence:** focused compile, tests, static analysis, and behavior observation
- **Risk classes:** engineering, quality

## Behavior (10)

### `behavior-implementation-choose-falsifier`

Implementation: Choose Cheapest Falsifier. Choose the lowest-cost check of owned code and configuration slice that could disprove the current hypothesis.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-communicate-uncertainty`

Implementation: Communicate Uncertainty. State confidence, missing evidence, failure impact 'large speculative edits create regressions and obscure causality', and the next discriminating check.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-establish-state`

Implementation: Establish Current State. Inspect owned code and configuration slice and record the current behavior before proposing change.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-identify-owner`

Implementation: Identify Owner And Boundary. Name the owner of owned code and configuration slice, the boundary 'accepted implementation scope and source ownership', and who may decide or mutate it.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-minimize-change`

Implementation: Make The Smallest Useful Change. Change only the owning slice of owned code and configuration slice needed to protect 'the change solves the verified cause without unrelated mutation'.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-protect-invariant`

Implementation: Protect The Domain Invariant. Reject an option that can violate 'the change solves the verified cause without unrelated mutation' without an approved mitigation.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-stop-and-escalate`

Implementation: Stop And Escalate. Stop mutation, preserve evidence, and route 'reduce to one owned slice and one falsifiable change' to the accountable owner.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-surface-assumptions`

Implementation: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the change solves the verified cause without unrelated mutation'.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-update-memory`

Implementation: Update Durable Knowledge. Update the decision or memory record for owned code and configuration slice with provenance and invalidation triggers.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

### `behavior-implementation-validate-immediately`

Implementation: Validate Immediately. Run focused compile, tests, static analysis, and behavior observation or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Implementation, this behavior operates on owned code and configuration slice, uses focused compile, tests, static analysis, and behavior observation, and protects 'the change solves the verified cause without unrelated mutation'.

## Failure (6)

### `failure-implementation-boundary-violation`

Implementation: Boundary Violation. A local optimization bypasses the domain ownership model for bounded source implementation.

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

### `failure-implementation-evidence-overclaim`

Implementation: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

### `failure-implementation-premature-action`

Implementation: Premature Action. implementation outruns understanding or combines multiple hypotheses

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

### `failure-implementation-silent-failure`

Implementation: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

### `failure-implementation-stale-context`

Implementation: Stale Context. The state of owned code and configuration slice changed while routing continued from a stale checkpoint.

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

### `failure-implementation-unbounded-loop`

Implementation: Unbounded Repair Loop. Failures do not trigger a reset of implementation outruns understanding or combines multiple hypotheses.

**Domain delta:** In Implementation, this failure threatens 'the change solves the verified cause without unrelated mutation' through implementation outruns understanding or combines multiple hypotheses.

## Signal (4)

### `signal-implementation-constraint-risk`

Implementation: Constraint Or Risk Signal. A current constraint or risk threatens 'the change solves the verified cause without unrelated mutation' for bounded source implementation.

**Domain delta:** For Implementation, this signal observes bounded source implementation through owned code and configuration slice while rejecting stale or untrusted substitutes.

### `signal-implementation-explicit-mission`

Implementation: Explicit Mission Signal. The current user request explicitly concerns bounded source implementation and states an observable outcome.

**Domain delta:** For Implementation, this signal observes bounded source implementation through owned code and configuration slice while rejecting stale or untrusted substitutes.

### `signal-implementation-repository-evidence`

Implementation: Repository Evidence Signal. Current source or accepted documentation identifies owned code and configuration slice as the owning surface for bounded source implementation.

**Domain delta:** For Implementation, this signal observes bounded source implementation through owned code and configuration slice while rejecting stale or untrusted substitutes.

### `signal-implementation-runtime-failure`

Implementation: Runtime Failure Signal. A reproducible observation shows implementation outruns understanding or combines multiple hypotheses in owned code and configuration slice.

**Domain delta:** For Implementation, this signal observes bounded source implementation through owned code and configuration slice while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-implementation-escalate-and-contain`

Implementation: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reduce to one owned slice and one falsifiable change to the accountable owner

**Domain delta:** For Implementation, recovery targets implementation outruns understanding or combines multiple hypotheses in owned code and configuration slice and exits only with focused compile, tests, static analysis, and behavior observation.

### `recovery-implementation-isolate-and-repair`

Implementation: Isolate And Repair. Reduce to the smallest failing path in owned code and configuration slice Apply one bounded repair Run focused compile, tests, static analysis, and behavior observation Check adjacent invariants

**Domain delta:** For Implementation, recovery targets implementation outruns understanding or combines multiple hypotheses in owned code and configuration slice and exits only with focused compile, tests, static analysis, and behavior observation.

### `recovery-implementation-reset-and-reconstruct`

Implementation: Reset And Reconstruct. Stop mutation Re-read owned code and configuration slice and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Implementation, recovery targets implementation outruns understanding or combines multiple hypotheses in owned code and configuration slice and exits only with focused compile, tests, static analysis, and behavior observation.

## Decision (2)

### `decision-implementation-build-versus-test`

Implementation: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Implementation, this model decides edit, probe, refactor locally, or stop using focused compile, tests, static analysis, and behavior observation and the constraint 'the change solves the verified cause without unrelated mutation'.

### `decision-implementation-local-versus-systemic`

Implementation: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the change solves the verified cause without unrelated mutation.

**Domain delta:** For Implementation, this model decides edit, probe, refactor locally, or stop using focused compile, tests, static analysis, and behavior observation and the constraint 'the change solves the verified cause without unrelated mutation'.

## Mental Model (2)

### `mental-model-implementation-feedback-loop`

Implementation: Feedback Loop. Actions on bounded source implementation change owned code and configuration slice, which changes the next evidence and decision environment.

**Domain delta:** For Implementation, this model maps small reversible changes preserve causal feedback and reviewability onto bounded source implementation and owned code and configuration slice.

### `mental-model-implementation-weakest-link`

Implementation: Weakest Link And Bottleneck. End-to-end quality for bounded source implementation is limited by the least trustworthy boundary in the path through owned code and configuration slice.

**Domain delta:** For Implementation, this model maps small reversible changes preserve causal feedback and reviewability onto bounded source implementation and owned code and configuration slice.

## Governance (1)

### `governance-implementation-evidence-authority-policy`

Implementation: Evidence And Authority Policy. Work on bounded source implementation must preserve 'the change solves the verified cause without unrelated mutation', cite focused compile, tests, static analysis, and behavior observation, and remain within 'accepted implementation scope and source ownership'.

**Domain delta:** For Implementation, this policy enforces implementation begins before Definition of Ready or outside authority at accepted implementation scope and source ownership.

