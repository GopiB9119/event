# Data Engineering

Design traceable ingestion, transformation, quality, lineage, and lifecycle for analytical or operational data.

- **Domain ID:** `data-engineering`
- **Boundary:** data production versus downstream interpretation and business use
- **Invariant:** derived data is traceable to authoritative inputs and deterministic transformations
- **Default evidence:** source-to-output reconciliation, schema checks, quality metrics, and replay tests
- **Risk classes:** data, privacy

## Behavior (10)

### `behavior-data-engineering-choose-falsifier`

Data Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of data contract, lineage map, validation suite, and pipeline run evidence that could disprove the current hypothesis.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-communicate-uncertainty`

Data Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'silent corruption, duplication, loss, stale conclusions, or ungoverned personal data', and the next discriminating check.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-establish-state`

Data Engineering: Establish Current State. Inspect data contract, lineage map, validation suite, and pipeline run evidence and record the current behavior before proposing change.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-identify-owner`

Data Engineering: Identify Owner And Boundary. Name the owner of data contract, lineage map, validation suite, and pipeline run evidence, the boundary 'data production versus downstream interpretation and business use', and who may decide or mutate it.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-minimize-change`

Data Engineering: Make The Smallest Useful Change. Change only the owning slice of data contract, lineage map, validation suite, and pipeline run evidence needed to protect 'derived data is traceable to authoritative inputs and deterministic transformations'.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-protect-invariant`

Data Engineering: Protect The Domain Invariant. Reject an option that can violate 'derived data is traceable to authoritative inputs and deterministic transformations' without an approved mitigation.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-stop-and-escalate`

Data Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop propagation, identify the first bad transformation, replay from trusted input, and reconcile' to the accountable owner.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-surface-assumptions`

Data Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'derived data is traceable to authoritative inputs and deterministic transformations'.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-update-memory`

Data Engineering: Update Durable Knowledge. Update the decision or memory record for data contract, lineage map, validation suite, and pipeline run evidence with provenance and invalidation triggers.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `behavior-data-engineering-validate-immediately`

Data Engineering: Validate Immediately. Run source-to-output reconciliation, schema checks, quality metrics, and replay tests or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Data Engineering, this behavior operates on data contract, lineage map, validation suite, and pipeline run evidence, uses source-to-output reconciliation, schema checks, quality metrics, and replay tests, and protects 'derived data is traceable to authoritative inputs and deterministic transformations'.

## Failure (6)

### `failure-data-engineering-boundary-violation`

Data Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for data pipelines, schemas, lineage, and quality.

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

### `failure-data-engineering-evidence-overclaim`

Data Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

### `failure-data-engineering-premature-action`

Data Engineering: Premature Action. pipeline transformations lack contracts, idempotency, lineage, or reconciliation

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

### `failure-data-engineering-silent-failure`

Data Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

### `failure-data-engineering-stale-context`

Data Engineering: Stale Context. The state of data contract, lineage map, validation suite, and pipeline run evidence changed while routing continued from a stale checkpoint.

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

### `failure-data-engineering-unbounded-loop`

Data Engineering: Unbounded Repair Loop. Failures do not trigger a reset of pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

**Domain delta:** In Data Engineering, this failure threatens 'derived data is traceable to authoritative inputs and deterministic transformations' through pipeline transformations lack contracts, idempotency, lineage, or reconciliation.

## Signal (4)

### `signal-data-engineering-constraint-risk`

Data Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'derived data is traceable to authoritative inputs and deterministic transformations' for data pipelines, schemas, lineage, and quality.

**Domain delta:** For Data Engineering, this signal observes data pipelines, schemas, lineage, and quality through data contract, lineage map, validation suite, and pipeline run evidence while rejecting stale or untrusted substitutes.

### `signal-data-engineering-explicit-mission`

Data Engineering: Explicit Mission Signal. The current user request explicitly concerns data pipelines, schemas, lineage, and quality and states an observable outcome.

**Domain delta:** For Data Engineering, this signal observes data pipelines, schemas, lineage, and quality through data contract, lineage map, validation suite, and pipeline run evidence while rejecting stale or untrusted substitutes.

### `signal-data-engineering-repository-evidence`

Data Engineering: Repository Evidence Signal. Current source or accepted documentation identifies data contract, lineage map, validation suite, and pipeline run evidence as the owning surface for data pipelines, schemas, lineage, and quality.

**Domain delta:** For Data Engineering, this signal observes data pipelines, schemas, lineage, and quality through data contract, lineage map, validation suite, and pipeline run evidence while rejecting stale or untrusted substitutes.

### `signal-data-engineering-runtime-failure`

Data Engineering: Runtime Failure Signal. A reproducible observation shows pipeline transformations lack contracts, idempotency, lineage, or reconciliation in data contract, lineage map, validation suite, and pipeline run evidence.

**Domain delta:** For Data Engineering, this signal observes data pipelines, schemas, lineage, and quality through data contract, lineage map, validation suite, and pipeline run evidence while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-data-engineering-escalate-and-contain`

Data Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop propagation, identify the first bad transformation, replay from trusted input, and reconcile to the accountable owner

**Domain delta:** For Data Engineering, recovery targets pipeline transformations lack contracts, idempotency, lineage, or reconciliation in data contract, lineage map, validation suite, and pipeline run evidence and exits only with source-to-output reconciliation, schema checks, quality metrics, and replay tests.

### `recovery-data-engineering-isolate-and-repair`

Data Engineering: Isolate And Repair. Reduce to the smallest failing path in data contract, lineage map, validation suite, and pipeline run evidence Apply one bounded repair Run source-to-output reconciliation, schema checks, quality metrics, and replay tests Check adjacent invariants

**Domain delta:** For Data Engineering, recovery targets pipeline transformations lack contracts, idempotency, lineage, or reconciliation in data contract, lineage map, validation suite, and pipeline run evidence and exits only with source-to-output reconciliation, schema checks, quality metrics, and replay tests.

### `recovery-data-engineering-reset-and-reconstruct`

Data Engineering: Reset And Reconstruct. Stop mutation Re-read data contract, lineage map, validation suite, and pipeline run evidence and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Data Engineering, recovery targets pipeline transformations lack contracts, idempotency, lineage, or reconciliation in data contract, lineage map, validation suite, and pipeline run evidence and exits only with source-to-output reconciliation, schema checks, quality metrics, and replay tests.

## Decision (2)

### `decision-data-engineering-build-versus-test`

Data Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Data Engineering, this model decides reject, quarantine, transform, backfill, replay, or deprecate using source-to-output reconciliation, schema checks, quality metrics, and replay tests and the constraint 'derived data is traceable to authoritative inputs and deterministic transformations'.

### `decision-data-engineering-local-versus-systemic`

Data Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening derived data is traceable to authoritative inputs and deterministic transformations.

**Domain delta:** For Data Engineering, this model decides reject, quarantine, transform, backfill, replay, or deprecate using source-to-output reconciliation, schema checks, quality metrics, and replay tests and the constraint 'derived data is traceable to authoritative inputs and deterministic transformations'.

## Mental Model (2)

### `mental-model-data-engineering-feedback-loop`

Data Engineering: Feedback Loop. Actions on data pipelines, schemas, lineage, and quality change data contract, lineage map, validation suite, and pipeline run evidence, which changes the next evidence and decision environment.

**Domain delta:** For Data Engineering, this model maps data quality degrades multiplicatively as errors propagate through dependent transformations onto data pipelines, schemas, lineage, and quality and data contract, lineage map, validation suite, and pipeline run evidence.

### `mental-model-data-engineering-weakest-link`

Data Engineering: Weakest Link And Bottleneck. End-to-end quality for data pipelines, schemas, lineage, and quality is limited by the least trustworthy boundary in the path through data contract, lineage map, validation suite, and pipeline run evidence.

**Domain delta:** For Data Engineering, this model maps data quality degrades multiplicatively as errors propagate through dependent transformations onto data pipelines, schemas, lineage, and quality and data contract, lineage map, validation suite, and pipeline run evidence.

## Governance (1)

### `governance-data-engineering-evidence-authority-policy`

Data Engineering: Evidence And Authority Policy. Work on data pipelines, schemas, lineage, and quality must preserve 'derived data is traceable to authoritative inputs and deterministic transformations', cite source-to-output reconciliation, schema checks, quality metrics, and replay tests, and remain within 'data production versus downstream interpretation and business use'.

**Domain delta:** For Data Engineering, this policy enforces untraceable or sensitive data is published without ownership and lifecycle controls at data production versus downstream interpretation and business use.

