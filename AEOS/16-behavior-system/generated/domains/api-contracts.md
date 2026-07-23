# API And Interface Contracts

Make service and module interfaces explicit, bounded, versioned, and failure-aware.

- **Domain ID:** `api-contracts`
- **Boundary:** public interface versus internal implementation
- **Invariant:** callers and providers share one enforceable contract across success and failure paths
- **Default evidence:** consumer/provider tests, malformed input tests, compatibility checks, and observed responses
- **Risk classes:** architecture, integration

## Behavior (10)

### `behavior-api-contracts-choose-falsifier`

API And Interface Contracts: Choose Cheapest Falsifier. Choose the lowest-cost check of interface specification, schema, examples, and contract tests that could disprove the current hypothesis.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-communicate-uncertainty`

API And Interface Contracts: Communicate Uncertainty. State confidence, missing evidence, failure impact 'silent incompatibility, unsafe input, ambiguous errors, or cascading client failures', and the next discriminating check.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-establish-state`

API And Interface Contracts: Establish Current State. Inspect interface specification, schema, examples, and contract tests and record the current behavior before proposing change.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-identify-owner`

API And Interface Contracts: Identify Owner And Boundary. Name the owner of interface specification, schema, examples, and contract tests, the boundary 'public interface versus internal implementation', and who may decide or mutate it.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-minimize-change`

API And Interface Contracts: Make The Smallest Useful Change. Change only the owning slice of interface specification, schema, examples, and contract tests needed to protect 'callers and providers share one enforceable contract across success and failure paths'.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-protect-invariant`

API And Interface Contracts: Protect The Domain Invariant. Reject an option that can violate 'callers and providers share one enforceable contract across success and failure paths' without an approved mitigation.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-stop-and-escalate`

API And Interface Contracts: Stop And Escalate. Stop mutation, preserve evidence, and route 'freeze incompatible rollout, restore the accepted contract, and add boundary validation' to the accountable owner.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-surface-assumptions`

API And Interface Contracts: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'callers and providers share one enforceable contract across success and failure paths'.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-update-memory`

API And Interface Contracts: Update Durable Knowledge. Update the decision or memory record for interface specification, schema, examples, and contract tests with provenance and invalidation triggers.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

### `behavior-api-contracts-validate-immediately`

API And Interface Contracts: Validate Immediately. Run consumer/provider tests, malformed input tests, compatibility checks, and observed responses or the cheapest stronger check before opening another edit slice.

**Domain delta:** For API And Interface Contracts, this behavior operates on interface specification, schema, examples, and contract tests, uses consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and protects 'callers and providers share one enforceable contract across success and failure paths'.

## Failure (6)

### `failure-api-contracts-boundary-violation`

API And Interface Contracts: Boundary Violation. A local optimization bypasses the domain ownership model for API inputs, outputs, errors, compatibility, and ownership.

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

### `failure-api-contracts-evidence-overclaim`

API And Interface Contracts: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

### `failure-api-contracts-premature-action`

API And Interface Contracts: Premature Action. implementation details become an undocumented contract or versioning is ignored

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

### `failure-api-contracts-silent-failure`

API And Interface Contracts: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

### `failure-api-contracts-stale-context`

API And Interface Contracts: Stale Context. The state of interface specification, schema, examples, and contract tests changed while routing continued from a stale checkpoint.

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

### `failure-api-contracts-unbounded-loop`

API And Interface Contracts: Unbounded Repair Loop. Failures do not trigger a reset of implementation details become an undocumented contract or versioning is ignored.

**Domain delta:** In API And Interface Contracts, this failure threatens 'callers and providers share one enforceable contract across success and failure paths' through implementation details become an undocumented contract or versioning is ignored.

## Signal (4)

### `signal-api-contracts-constraint-risk`

API And Interface Contracts: Constraint Or Risk Signal. A current constraint or risk threatens 'callers and providers share one enforceable contract across success and failure paths' for API inputs, outputs, errors, compatibility, and ownership.

**Domain delta:** For API And Interface Contracts, this signal observes API inputs, outputs, errors, compatibility, and ownership through interface specification, schema, examples, and contract tests while rejecting stale or untrusted substitutes.

### `signal-api-contracts-explicit-mission`

API And Interface Contracts: Explicit Mission Signal. The current user request explicitly concerns API inputs, outputs, errors, compatibility, and ownership and states an observable outcome.

**Domain delta:** For API And Interface Contracts, this signal observes API inputs, outputs, errors, compatibility, and ownership through interface specification, schema, examples, and contract tests while rejecting stale or untrusted substitutes.

### `signal-api-contracts-repository-evidence`

API And Interface Contracts: Repository Evidence Signal. Current source or accepted documentation identifies interface specification, schema, examples, and contract tests as the owning surface for API inputs, outputs, errors, compatibility, and ownership.

**Domain delta:** For API And Interface Contracts, this signal observes API inputs, outputs, errors, compatibility, and ownership through interface specification, schema, examples, and contract tests while rejecting stale or untrusted substitutes.

### `signal-api-contracts-runtime-failure`

API And Interface Contracts: Runtime Failure Signal. A reproducible observation shows implementation details become an undocumented contract or versioning is ignored in interface specification, schema, examples, and contract tests.

**Domain delta:** For API And Interface Contracts, this signal observes API inputs, outputs, errors, compatibility, and ownership through interface specification, schema, examples, and contract tests while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-api-contracts-escalate-and-contain`

API And Interface Contracts: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route freeze incompatible rollout, restore the accepted contract, and add boundary validation to the accountable owner

**Domain delta:** For API And Interface Contracts, recovery targets implementation details become an undocumented contract or versioning is ignored in interface specification, schema, examples, and contract tests and exits only with consumer/provider tests, malformed input tests, compatibility checks, and observed responses.

### `recovery-api-contracts-isolate-and-repair`

API And Interface Contracts: Isolate And Repair. Reduce to the smallest failing path in interface specification, schema, examples, and contract tests Apply one bounded repair Run consumer/provider tests, malformed input tests, compatibility checks, and observed responses Check adjacent invariants

**Domain delta:** For API And Interface Contracts, recovery targets implementation details become an undocumented contract or versioning is ignored in interface specification, schema, examples, and contract tests and exits only with consumer/provider tests, malformed input tests, compatibility checks, and observed responses.

### `recovery-api-contracts-reset-and-reconstruct`

API And Interface Contracts: Reset And Reconstruct. Stop mutation Re-read interface specification, schema, examples, and contract tests and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For API And Interface Contracts, recovery targets implementation details become an undocumented contract or versioning is ignored in interface specification, schema, examples, and contract tests and exits only with consumer/provider tests, malformed input tests, compatibility checks, and observed responses.

## Decision (2)

### `decision-api-contracts-build-versus-test`

API And Interface Contracts: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For API And Interface Contracts, this model decides add, version, deprecate, adapt, reject, or remove using consumer/provider tests, malformed input tests, compatibility checks, and observed responses and the constraint 'callers and providers share one enforceable contract across success and failure paths'.

### `decision-api-contracts-local-versus-systemic`

API And Interface Contracts: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening callers and providers share one enforceable contract across success and failure paths.

**Domain delta:** For API And Interface Contracts, this model decides add, version, deprecate, adapt, reject, or remove using consumer/provider tests, malformed input tests, compatibility checks, and observed responses and the constraint 'callers and providers share one enforceable contract across success and failure paths'.

## Mental Model (2)

### `mental-model-api-contracts-feedback-loop`

API And Interface Contracts: Feedback Loop. Actions on API inputs, outputs, errors, compatibility, and ownership change interface specification, schema, examples, and contract tests, which changes the next evidence and decision environment.

**Domain delta:** For API And Interface Contracts, this model maps interfaces concentrate coupling and propagate ambiguity across ownership boundaries onto API inputs, outputs, errors, compatibility, and ownership and interface specification, schema, examples, and contract tests.

### `mental-model-api-contracts-weakest-link`

API And Interface Contracts: Weakest Link And Bottleneck. End-to-end quality for API inputs, outputs, errors, compatibility, and ownership is limited by the least trustworthy boundary in the path through interface specification, schema, examples, and contract tests.

**Domain delta:** For API And Interface Contracts, this model maps interfaces concentrate coupling and propagate ambiguity across ownership boundaries onto API inputs, outputs, errors, compatibility, and ownership and interface specification, schema, examples, and contract tests.

## Governance (1)

### `governance-api-contracts-evidence-authority-policy`

API And Interface Contracts: Evidence And Authority Policy. Work on API inputs, outputs, errors, compatibility, and ownership must preserve 'callers and providers share one enforceable contract across success and failure paths', cite consumer/provider tests, malformed input tests, compatibility checks, and observed responses, and remain within 'public interface versus internal implementation'.

**Domain delta:** For API And Interface Contracts, this policy enforces breaking contract changes lack versioning, migration, consumer evidence, or approval at public interface versus internal implementation.

