# Release Engineering

Produce traceable, reproducible, correctly identified artifacts and gate publication separately.

- **Domain ID:** `release-engineering`
- **Boundary:** artifact preparation versus publication authority
- **Invariant:** the published artifact matches reviewed source, identity, permissions, and release decision
- **Default evidence:** build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks
- **Risk classes:** release, security

## Behavior (10)

### `behavior-release-engineering-choose-falsifier`

Release Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of release manifest, signed artifacts, checksums, and release runbook that could disprove the current hypothesis.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-communicate-uncertainty`

Release Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'wrong, unsigned, over-permissioned, or unverified artifacts reach users', and the next discriminating check.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-establish-state`

Release Engineering: Establish Current State. Inspect release manifest, signed artifacts, checksums, and release runbook and record the current behavior before proposing change.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-identify-owner`

Release Engineering: Identify Owner And Boundary. Name the owner of release manifest, signed artifacts, checksums, and release runbook, the boundary 'artifact preparation versus publication authority', and who may decide or mutate it.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-minimize-change`

Release Engineering: Make The Smallest Useful Change. Change only the owning slice of release manifest, signed artifacts, checksums, and release runbook needed to protect 'the published artifact matches reviewed source, identity, permissions, and release decision'.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-protect-invariant`

Release Engineering: Protect The Domain Invariant. Reject an option that can violate 'the published artifact matches reviewed source, identity, permissions, and release decision' without an approved mitigation.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-stop-and-escalate`

Release Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'quarantine the candidate, reproduce artifacts, and rerun release gates' to the accountable owner.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-surface-assumptions`

Release Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the published artifact matches reviewed source, identity, permissions, and release decision'.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-update-memory`

Release Engineering: Update Durable Knowledge. Update the decision or memory record for release manifest, signed artifacts, checksums, and release runbook with provenance and invalidation triggers.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `behavior-release-engineering-validate-immediately`

Release Engineering: Validate Immediately. Run build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Release Engineering, this behavior operates on release manifest, signed artifacts, checksums, and release runbook, uses build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and protects 'the published artifact matches reviewed source, identity, permissions, and release decision'.

## Failure (6)

### `failure-release-engineering-boundary-violation`

Release Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for release candidate, artifacts, and distribution contract.

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

### `failure-release-engineering-evidence-overclaim`

Release Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

### `failure-release-engineering-premature-action`

Release Engineering: Premature Action. packaging success is mistaken for release readiness or publication approval

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

### `failure-release-engineering-silent-failure`

Release Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

### `failure-release-engineering-stale-context`

Release Engineering: Stale Context. The state of release manifest, signed artifacts, checksums, and release runbook changed while routing continued from a stale checkpoint.

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

### `failure-release-engineering-unbounded-loop`

Release Engineering: Unbounded Repair Loop. Failures do not trigger a reset of packaging success is mistaken for release readiness or publication approval.

**Domain delta:** In Release Engineering, this failure threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' through packaging success is mistaken for release readiness or publication approval.

## Signal (4)

### `signal-release-engineering-constraint-risk`

Release Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'the published artifact matches reviewed source, identity, permissions, and release decision' for release candidate, artifacts, and distribution contract.

**Domain delta:** For Release Engineering, this signal observes release candidate, artifacts, and distribution contract through release manifest, signed artifacts, checksums, and release runbook while rejecting stale or untrusted substitutes.

### `signal-release-engineering-explicit-mission`

Release Engineering: Explicit Mission Signal. The current user request explicitly concerns release candidate, artifacts, and distribution contract and states an observable outcome.

**Domain delta:** For Release Engineering, this signal observes release candidate, artifacts, and distribution contract through release manifest, signed artifacts, checksums, and release runbook while rejecting stale or untrusted substitutes.

### `signal-release-engineering-repository-evidence`

Release Engineering: Repository Evidence Signal. Current source or accepted documentation identifies release manifest, signed artifacts, checksums, and release runbook as the owning surface for release candidate, artifacts, and distribution contract.

**Domain delta:** For Release Engineering, this signal observes release candidate, artifacts, and distribution contract through release manifest, signed artifacts, checksums, and release runbook while rejecting stale or untrusted substitutes.

### `signal-release-engineering-runtime-failure`

Release Engineering: Runtime Failure Signal. A reproducible observation shows packaging success is mistaken for release readiness or publication approval in release manifest, signed artifacts, checksums, and release runbook.

**Domain delta:** For Release Engineering, this signal observes release candidate, artifacts, and distribution contract through release manifest, signed artifacts, checksums, and release runbook while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-release-engineering-escalate-and-contain`

Release Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route quarantine the candidate, reproduce artifacts, and rerun release gates to the accountable owner

**Domain delta:** For Release Engineering, recovery targets packaging success is mistaken for release readiness or publication approval in release manifest, signed artifacts, checksums, and release runbook and exits only with build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks.

### `recovery-release-engineering-isolate-and-repair`

Release Engineering: Isolate And Repair. Reduce to the smallest failing path in release manifest, signed artifacts, checksums, and release runbook Apply one bounded repair Run build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks Check adjacent invariants

**Domain delta:** For Release Engineering, recovery targets packaging success is mistaken for release readiness or publication approval in release manifest, signed artifacts, checksums, and release runbook and exits only with build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks.

### `recovery-release-engineering-reset-and-reconstruct`

Release Engineering: Reset And Reconstruct. Stop mutation Re-read release manifest, signed artifacts, checksums, and release runbook and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Release Engineering, recovery targets packaging success is mistaken for release readiness or publication approval in release manifest, signed artifacts, checksums, and release runbook and exits only with build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks.

## Decision (2)

### `decision-release-engineering-build-versus-test`

Release Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Release Engineering, this model decides prepare, hold, rollback, stage, or publish with approval using build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks and the constraint 'the published artifact matches reviewed source, identity, permissions, and release decision'.

### `decision-release-engineering-local-versus-systemic`

Release Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the published artifact matches reviewed source, identity, permissions, and release decision.

**Domain delta:** For Release Engineering, this model decides prepare, hold, rollback, stage, or publish with approval using build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks and the constraint 'the published artifact matches reviewed source, identity, permissions, and release decision'.

## Mental Model (2)

### `mental-model-release-engineering-feedback-loop`

Release Engineering: Feedback Loop. Actions on release candidate, artifacts, and distribution contract change release manifest, signed artifacts, checksums, and release runbook, which changes the next evidence and decision environment.

**Domain delta:** For Release Engineering, this model maps release quality is an end-to-end chain whose weakest artifact or approval invalidates the candidate onto release candidate, artifacts, and distribution contract and release manifest, signed artifacts, checksums, and release runbook.

### `mental-model-release-engineering-weakest-link`

Release Engineering: Weakest Link And Bottleneck. End-to-end quality for release candidate, artifacts, and distribution contract is limited by the least trustworthy boundary in the path through release manifest, signed artifacts, checksums, and release runbook.

**Domain delta:** For Release Engineering, this model maps release quality is an end-to-end chain whose weakest artifact or approval invalidates the candidate onto release candidate, artifacts, and distribution contract and release manifest, signed artifacts, checksums, and release runbook.

## Governance (1)

### `governance-release-engineering-evidence-authority-policy`

Release Engineering: Evidence And Authority Policy. Work on release candidate, artifacts, and distribution contract must preserve 'the published artifact matches reviewed source, identity, permissions, and release decision', cite build, lint, package identity, signature, alignment, upgrade, rollback, and channel checks, and remain within 'artifact preparation versus publication authority'.

**Domain delta:** For Release Engineering, this policy enforces tagging, publishing, signing, or deployment occurs without current approval at artifact preparation versus publication authority.

