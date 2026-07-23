# Testing

Design layered checks that falsify defects and protect important behavior over time.

- **Domain ID:** `testing`
- **Boundary:** test evidence versus production observation
- **Invariant:** tests exercise meaningful contracts rather than merely implementation details
- **Default evidence:** failing-before, passing-after, edge, integration, and regression results
- **Risk classes:** quality, data

## Behavior (10)

### `behavior-testing-choose-falsifier`

Testing: Choose Cheapest Falsifier. Choose the lowest-cost check of test matrix and executable suites that could disprove the current hypothesis.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-communicate-uncertainty`

Testing: Communicate Uncertainty. State confidence, missing evidence, failure impact 'passing tests create false confidence while critical journeys remain untested', and the next discriminating check.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-establish-state`

Testing: Establish Current State. Inspect test matrix and executable suites and record the current behavior before proposing change.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-identify-owner`

Testing: Identify Owner And Boundary. Name the owner of test matrix and executable suites, the boundary 'test evidence versus production observation', and who may decide or mutate it.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-minimize-change`

Testing: Make The Smallest Useful Change. Change only the owning slice of test matrix and executable suites needed to protect 'tests exercise meaningful contracts rather than merely implementation details'.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-protect-invariant`

Testing: Protect The Domain Invariant. Reject an option that can violate 'tests exercise meaningful contracts rather than merely implementation details' without an approved mitigation.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-stop-and-escalate`

Testing: Stop And Escalate. Stop mutation, preserve evidence, and route 'map risks to the cheapest reliable test level and add the missing falsifier' to the accountable owner.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-surface-assumptions`

Testing: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'tests exercise meaningful contracts rather than merely implementation details'.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-update-memory`

Testing: Update Durable Knowledge. Update the decision or memory record for test matrix and executable suites with provenance and invalidation triggers.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

### `behavior-testing-validate-immediately`

Testing: Validate Immediately. Run failing-before, passing-after, edge, integration, and regression results or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Testing, this behavior operates on test matrix and executable suites, uses failing-before, passing-after, edge, integration, and regression results, and protects 'tests exercise meaningful contracts rather than merely implementation details'.

## Failure (6)

### `failure-testing-boundary-violation`

Testing: Boundary Violation. A local optimization bypasses the domain ownership model for test strategy, fixtures, and coverage boundaries.

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

### `failure-testing-evidence-overclaim`

Testing: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

### `failure-testing-premature-action`

Testing: Premature Action. tests mirror the implementation, omit failure states, or use unrealistic fixtures

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

### `failure-testing-silent-failure`

Testing: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

### `failure-testing-stale-context`

Testing: Stale Context. The state of test matrix and executable suites changed while routing continued from a stale checkpoint.

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

### `failure-testing-unbounded-loop`

Testing: Unbounded Repair Loop. Failures do not trigger a reset of tests mirror the implementation, omit failure states, or use unrealistic fixtures.

**Domain delta:** In Testing, this failure threatens 'tests exercise meaningful contracts rather than merely implementation details' through tests mirror the implementation, omit failure states, or use unrealistic fixtures.

## Signal (4)

### `signal-testing-constraint-risk`

Testing: Constraint Or Risk Signal. A current constraint or risk threatens 'tests exercise meaningful contracts rather than merely implementation details' for test strategy, fixtures, and coverage boundaries.

**Domain delta:** For Testing, this signal observes test strategy, fixtures, and coverage boundaries through test matrix and executable suites while rejecting stale or untrusted substitutes.

### `signal-testing-explicit-mission`

Testing: Explicit Mission Signal. The current user request explicitly concerns test strategy, fixtures, and coverage boundaries and states an observable outcome.

**Domain delta:** For Testing, this signal observes test strategy, fixtures, and coverage boundaries through test matrix and executable suites while rejecting stale or untrusted substitutes.

### `signal-testing-repository-evidence`

Testing: Repository Evidence Signal. Current source or accepted documentation identifies test matrix and executable suites as the owning surface for test strategy, fixtures, and coverage boundaries.

**Domain delta:** For Testing, this signal observes test strategy, fixtures, and coverage boundaries through test matrix and executable suites while rejecting stale or untrusted substitutes.

### `signal-testing-runtime-failure`

Testing: Runtime Failure Signal. A reproducible observation shows tests mirror the implementation, omit failure states, or use unrealistic fixtures in test matrix and executable suites.

**Domain delta:** For Testing, this signal observes test strategy, fixtures, and coverage boundaries through test matrix and executable suites while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-testing-escalate-and-contain`

Testing: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route map risks to the cheapest reliable test level and add the missing falsifier to the accountable owner

**Domain delta:** For Testing, recovery targets tests mirror the implementation, omit failure states, or use unrealistic fixtures in test matrix and executable suites and exits only with failing-before, passing-after, edge, integration, and regression results.

### `recovery-testing-isolate-and-repair`

Testing: Isolate And Repair. Reduce to the smallest failing path in test matrix and executable suites Apply one bounded repair Run failing-before, passing-after, edge, integration, and regression results Check adjacent invariants

**Domain delta:** For Testing, recovery targets tests mirror the implementation, omit failure states, or use unrealistic fixtures in test matrix and executable suites and exits only with failing-before, passing-after, edge, integration, and regression results.

### `recovery-testing-reset-and-reconstruct`

Testing: Reset And Reconstruct. Stop mutation Re-read test matrix and executable suites and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Testing, recovery targets tests mirror the implementation, omit failure states, or use unrealistic fixtures in test matrix and executable suites and exits only with failing-before, passing-after, edge, integration, and regression results.

## Decision (2)

### `decision-testing-build-versus-test`

Testing: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Testing, this model decides unit, integration, device, end-to-end, manual, or production observation using failing-before, passing-after, edge, integration, and regression results and the constraint 'tests exercise meaningful contracts rather than merely implementation details'.

### `decision-testing-local-versus-systemic`

Testing: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening tests exercise meaningful contracts rather than merely implementation details.

**Domain delta:** For Testing, this model decides unit, integration, device, end-to-end, manual, or production observation using failing-before, passing-after, edge, integration, and regression results and the constraint 'tests exercise meaningful contracts rather than merely implementation details'.

## Mental Model (2)

### `mental-model-testing-feedback-loop`

Testing: Feedback Loop. Actions on test strategy, fixtures, and coverage boundaries change test matrix and executable suites, which changes the next evidence and decision environment.

**Domain delta:** For Testing, this model maps test value depends on defect detection power, realism, speed, and maintenance cost onto test strategy, fixtures, and coverage boundaries and test matrix and executable suites.

### `mental-model-testing-weakest-link`

Testing: Weakest Link And Bottleneck. End-to-end quality for test strategy, fixtures, and coverage boundaries is limited by the least trustworthy boundary in the path through test matrix and executable suites.

**Domain delta:** For Testing, this model maps test value depends on defect detection power, realism, speed, and maintenance cost onto test strategy, fixtures, and coverage boundaries and test matrix and executable suites.

## Governance (1)

### `governance-testing-evidence-authority-policy`

Testing: Evidence And Authority Policy. Work on test strategy, fixtures, and coverage boundaries must preserve 'tests exercise meaningful contracts rather than merely implementation details', cite failing-before, passing-after, edge, integration, and regression results, and remain within 'test evidence versus production observation'.

**Domain delta:** For Testing, this policy enforces sensitive or fabricated production data enters test fixtures at test evidence versus production observation.

