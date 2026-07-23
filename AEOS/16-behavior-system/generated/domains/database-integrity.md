# Database Integrity

Preserve ownership, constraints, transactions, migrations, and recoverability of persisted state.

- **Domain ID:** `database-integrity`
- **Boundary:** database authority versus cache, UI state, and external projections
- **Invariant:** persisted state remains valid, attributable, and recoverable across versions and failures
- **Default evidence:** migration tests, constraint failures, transaction rollback, restart, and reconciliation results
- **Risk classes:** data, high-stakes

## Behavior (10)

### `behavior-database-integrity-choose-falsifier`

Database Integrity: Choose Cheapest Falsifier. Choose the lowest-cost check of schema, migration, DAO, transaction boundary, and integrity tests that could disprove the current hypothesis.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-communicate-uncertainty`

Database Integrity: Communicate Uncertainty. State confidence, missing evidence, failure impact 'data loss, wrong ownership, duplicate effects, partial commits, or unrecoverable upgrades', and the next discriminating check.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-establish-state`

Database Integrity: Establish Current State. Inspect schema, migration, DAO, transaction boundary, and integrity tests and record the current behavior before proposing change.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-identify-owner`

Database Integrity: Identify Owner And Boundary. Name the owner of schema, migration, DAO, transaction boundary, and integrity tests, the boundary 'database authority versus cache, UI state, and external projections', and who may decide or mutate it.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-minimize-change`

Database Integrity: Make The Smallest Useful Change. Change only the owning slice of schema, migration, DAO, transaction boundary, and integrity tests needed to protect 'persisted state remains valid, attributable, and recoverable across versions and failures'.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-protect-invariant`

Database Integrity: Protect The Domain Invariant. Reject an option that can violate 'persisted state remains valid, attributable, and recoverable across versions and failures' without an approved mitigation.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-stop-and-escalate`

Database Integrity: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop writes, reproduce on a copy, repair the smallest schema or transaction boundary, and validate upgrade paths' to the accountable owner.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-surface-assumptions`

Database Integrity: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'persisted state remains valid, attributable, and recoverable across versions and failures'.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-update-memory`

Database Integrity: Update Durable Knowledge. Update the decision or memory record for schema, migration, DAO, transaction boundary, and integrity tests with provenance and invalidation triggers.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `behavior-database-integrity-validate-immediately`

Database Integrity: Validate Immediately. Run migration tests, constraint failures, transaction rollback, restart, and reconciliation results or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Database Integrity, this behavior operates on schema, migration, DAO, transaction boundary, and integrity tests, uses migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and protects 'persisted state remains valid, attributable, and recoverable across versions and failures'.

## Failure (6)

### `failure-database-integrity-boundary-violation`

Database Integrity: Boundary Violation. A local optimization bypasses the domain ownership model for transactional schema and persisted invariants.

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

### `failure-database-integrity-evidence-overclaim`

Database Integrity: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

### `failure-database-integrity-premature-action`

Database Integrity: Premature Action. schema and mutation paths diverge from entity constraints or migration history

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

### `failure-database-integrity-silent-failure`

Database Integrity: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

### `failure-database-integrity-stale-context`

Database Integrity: Stale Context. The state of schema, migration, DAO, transaction boundary, and integrity tests changed while routing continued from a stale checkpoint.

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

### `failure-database-integrity-unbounded-loop`

Database Integrity: Unbounded Repair Loop. Failures do not trigger a reset of schema and mutation paths diverge from entity constraints or migration history.

**Domain delta:** In Database Integrity, this failure threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' through schema and mutation paths diverge from entity constraints or migration history.

## Signal (4)

### `signal-database-integrity-constraint-risk`

Database Integrity: Constraint Or Risk Signal. A current constraint or risk threatens 'persisted state remains valid, attributable, and recoverable across versions and failures' for transactional schema and persisted invariants.

**Domain delta:** For Database Integrity, this signal observes transactional schema and persisted invariants through schema, migration, DAO, transaction boundary, and integrity tests while rejecting stale or untrusted substitutes.

### `signal-database-integrity-explicit-mission`

Database Integrity: Explicit Mission Signal. The current user request explicitly concerns transactional schema and persisted invariants and states an observable outcome.

**Domain delta:** For Database Integrity, this signal observes transactional schema and persisted invariants through schema, migration, DAO, transaction boundary, and integrity tests while rejecting stale or untrusted substitutes.

### `signal-database-integrity-repository-evidence`

Database Integrity: Repository Evidence Signal. Current source or accepted documentation identifies schema, migration, DAO, transaction boundary, and integrity tests as the owning surface for transactional schema and persisted invariants.

**Domain delta:** For Database Integrity, this signal observes transactional schema and persisted invariants through schema, migration, DAO, transaction boundary, and integrity tests while rejecting stale or untrusted substitutes.

### `signal-database-integrity-runtime-failure`

Database Integrity: Runtime Failure Signal. A reproducible observation shows schema and mutation paths diverge from entity constraints or migration history in schema, migration, DAO, transaction boundary, and integrity tests.

**Domain delta:** For Database Integrity, this signal observes transactional schema and persisted invariants through schema, migration, DAO, transaction boundary, and integrity tests while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-database-integrity-escalate-and-contain`

Database Integrity: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop writes, reproduce on a copy, repair the smallest schema or transaction boundary, and validate upgrade paths to the accountable owner

**Domain delta:** For Database Integrity, recovery targets schema and mutation paths diverge from entity constraints or migration history in schema, migration, DAO, transaction boundary, and integrity tests and exits only with migration tests, constraint failures, transaction rollback, restart, and reconciliation results.

### `recovery-database-integrity-isolate-and-repair`

Database Integrity: Isolate And Repair. Reduce to the smallest failing path in schema, migration, DAO, transaction boundary, and integrity tests Apply one bounded repair Run migration tests, constraint failures, transaction rollback, restart, and reconciliation results Check adjacent invariants

**Domain delta:** For Database Integrity, recovery targets schema and mutation paths diverge from entity constraints or migration history in schema, migration, DAO, transaction boundary, and integrity tests and exits only with migration tests, constraint failures, transaction rollback, restart, and reconciliation results.

### `recovery-database-integrity-reset-and-reconstruct`

Database Integrity: Reset And Reconstruct. Stop mutation Re-read schema, migration, DAO, transaction boundary, and integrity tests and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Database Integrity, recovery targets schema and mutation paths diverge from entity constraints or migration history in schema, migration, DAO, transaction boundary, and integrity tests and exits only with migration tests, constraint failures, transaction rollback, restart, and reconciliation results.

## Decision (2)

### `decision-database-integrity-build-versus-test`

Database Integrity: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Database Integrity, this model decides constraint, transaction, migration, repair, rebuild, or block using migration tests, constraint failures, transaction rollback, restart, and reconciliation results and the constraint 'persisted state remains valid, attributable, and recoverable across versions and failures'.

### `decision-database-integrity-local-versus-systemic`

Database Integrity: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening persisted state remains valid, attributable, and recoverable across versions and failures.

**Domain delta:** For Database Integrity, this model decides constraint, transaction, migration, repair, rebuild, or block using migration tests, constraint failures, transaction rollback, restart, and reconciliation results and the constraint 'persisted state remains valid, attributable, and recoverable across versions and failures'.

## Mental Model (2)

### `mental-model-database-integrity-feedback-loop`

Database Integrity: Feedback Loop. Actions on transactional schema and persisted invariants change schema, migration, DAO, transaction boundary, and integrity tests, which changes the next evidence and decision environment.

**Domain delta:** For Database Integrity, this model maps persistent invariants must survive every write path, failure point, and version transition onto transactional schema and persisted invariants and schema, migration, DAO, transaction boundary, and integrity tests.

### `mental-model-database-integrity-weakest-link`

Database Integrity: Weakest Link And Bottleneck. End-to-end quality for transactional schema and persisted invariants is limited by the least trustworthy boundary in the path through schema, migration, DAO, transaction boundary, and integrity tests.

**Domain delta:** For Database Integrity, this model maps persistent invariants must survive every write path, failure point, and version transition onto transactional schema and persisted invariants and schema, migration, DAO, transaction boundary, and integrity tests.

## Governance (1)

### `governance-database-integrity-evidence-authority-policy`

Database Integrity: Evidence And Authority Policy. Work on transactional schema and persisted invariants must preserve 'persisted state remains valid, attributable, and recoverable across versions and failures', cite migration tests, constraint failures, transaction rollback, restart, and reconciliation results, and remain within 'database authority versus cache, UI state, and external projections'.

**Domain delta:** For Database Integrity, this policy enforces destructive migration or data rewrite lacks backup, rollback, and owner approval at database authority versus cache, UI state, and external projections.

