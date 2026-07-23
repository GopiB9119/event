# Debugging And Root Cause Analysis

Reproduce, classify, isolate, explain, and repair the first causal failure rather than its final symptom.

- **Domain ID:** `debugging`
- **Boundary:** investigation evidence versus repair authority
- **Invariant:** each edit tests one changed hypothesis about the verified cause
- **Default evidence:** reproduction, logs, stack trace, history, environment, and focused regression
- **Risk classes:** engineering, reliability

## Behavior (10)

### `behavior-debugging-choose-falsifier`

Debugging And Root Cause Analysis: Choose Cheapest Falsifier. Choose the lowest-cost check of failure timeline, hypothesis ledger, and minimal reproduction that could disprove the current hypothesis.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-communicate-uncertainty`

Debugging And Root Cause Analysis: Communicate Uncertainty. State confidence, missing evidence, failure impact 'random fixes mask the root cause and create cascading defects', and the next discriminating check.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-establish-state`

Debugging And Root Cause Analysis: Establish Current State. Inspect failure timeline, hypothesis ledger, and minimal reproduction and record the current behavior before proposing change.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-identify-owner`

Debugging And Root Cause Analysis: Identify Owner And Boundary. Name the owner of failure timeline, hypothesis ledger, and minimal reproduction, the boundary 'investigation evidence versus repair authority', and who may decide or mutate it.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-minimize-change`

Debugging And Root Cause Analysis: Make The Smallest Useful Change. Change only the owning slice of failure timeline, hypothesis ledger, and minimal reproduction needed to protect 'each edit tests one changed hypothesis about the verified cause'.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-protect-invariant`

Debugging And Root Cause Analysis: Protect The Domain Invariant. Reject an option that can violate 'each edit tests one changed hypothesis about the verified cause' without an approved mitigation.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-stop-and-escalate`

Debugging And Root Cause Analysis: Stop And Escalate. Stop mutation, preserve evidence, and route 'freeze edits, reconstruct the timeline, and test one new hypothesis' to the accountable owner.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-surface-assumptions`

Debugging And Root Cause Analysis: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'each edit tests one changed hypothesis about the verified cause'.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-update-memory`

Debugging And Root Cause Analysis: Update Durable Knowledge. Update the decision or memory record for failure timeline, hypothesis ledger, and minimal reproduction with provenance and invalidation triggers.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

### `behavior-debugging-validate-immediately`

Debugging And Root Cause Analysis: Validate Immediately. Run reproduction, logs, stack trace, history, environment, and focused regression or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Debugging And Root Cause Analysis, this behavior operates on failure timeline, hypothesis ledger, and minimal reproduction, uses reproduction, logs, stack trace, history, environment, and focused regression, and protects 'each edit tests one changed hypothesis about the verified cause'.

## Failure (6)

### `failure-debugging-boundary-violation`

Debugging And Root Cause Analysis: Boundary Violation. A local optimization bypasses the domain ownership model for a reproducible defect or failed verifier.

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

### `failure-debugging-evidence-overclaim`

Debugging And Root Cause Analysis: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

### `failure-debugging-premature-action`

Debugging And Root Cause Analysis: Premature Action. the visible error is patched before the causal chain is understood

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

### `failure-debugging-silent-failure`

Debugging And Root Cause Analysis: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

### `failure-debugging-stale-context`

Debugging And Root Cause Analysis: Stale Context. The state of failure timeline, hypothesis ledger, and minimal reproduction changed while routing continued from a stale checkpoint.

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

### `failure-debugging-unbounded-loop`

Debugging And Root Cause Analysis: Unbounded Repair Loop. Failures do not trigger a reset of the visible error is patched before the causal chain is understood.

**Domain delta:** In Debugging And Root Cause Analysis, this failure threatens 'each edit tests one changed hypothesis about the verified cause' through the visible error is patched before the causal chain is understood.

## Signal (4)

### `signal-debugging-constraint-risk`

Debugging And Root Cause Analysis: Constraint Or Risk Signal. A current constraint or risk threatens 'each edit tests one changed hypothesis about the verified cause' for a reproducible defect or failed verifier.

**Domain delta:** For Debugging And Root Cause Analysis, this signal observes a reproducible defect or failed verifier through failure timeline, hypothesis ledger, and minimal reproduction while rejecting stale or untrusted substitutes.

### `signal-debugging-explicit-mission`

Debugging And Root Cause Analysis: Explicit Mission Signal. The current user request explicitly concerns a reproducible defect or failed verifier and states an observable outcome.

**Domain delta:** For Debugging And Root Cause Analysis, this signal observes a reproducible defect or failed verifier through failure timeline, hypothesis ledger, and minimal reproduction while rejecting stale or untrusted substitutes.

### `signal-debugging-repository-evidence`

Debugging And Root Cause Analysis: Repository Evidence Signal. Current source or accepted documentation identifies failure timeline, hypothesis ledger, and minimal reproduction as the owning surface for a reproducible defect or failed verifier.

**Domain delta:** For Debugging And Root Cause Analysis, this signal observes a reproducible defect or failed verifier through failure timeline, hypothesis ledger, and minimal reproduction while rejecting stale or untrusted substitutes.

### `signal-debugging-runtime-failure`

Debugging And Root Cause Analysis: Runtime Failure Signal. A reproducible observation shows the visible error is patched before the causal chain is understood in failure timeline, hypothesis ledger, and minimal reproduction.

**Domain delta:** For Debugging And Root Cause Analysis, this signal observes a reproducible defect or failed verifier through failure timeline, hypothesis ledger, and minimal reproduction while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-debugging-escalate-and-contain`

Debugging And Root Cause Analysis: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route freeze edits, reconstruct the timeline, and test one new hypothesis to the accountable owner

**Domain delta:** For Debugging And Root Cause Analysis, recovery targets the visible error is patched before the causal chain is understood in failure timeline, hypothesis ledger, and minimal reproduction and exits only with reproduction, logs, stack trace, history, environment, and focused regression.

### `recovery-debugging-isolate-and-repair`

Debugging And Root Cause Analysis: Isolate And Repair. Reduce to the smallest failing path in failure timeline, hypothesis ledger, and minimal reproduction Apply one bounded repair Run reproduction, logs, stack trace, history, environment, and focused regression Check adjacent invariants

**Domain delta:** For Debugging And Root Cause Analysis, recovery targets the visible error is patched before the causal chain is understood in failure timeline, hypothesis ledger, and minimal reproduction and exits only with reproduction, logs, stack trace, history, environment, and focused regression.

### `recovery-debugging-reset-and-reconstruct`

Debugging And Root Cause Analysis: Reset And Reconstruct. Stop mutation Re-read failure timeline, hypothesis ledger, and minimal reproduction and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Debugging And Root Cause Analysis, recovery targets the visible error is patched before the causal chain is understood in failure timeline, hypothesis ledger, and minimal reproduction and exits only with reproduction, logs, stack trace, history, environment, and focused regression.

## Decision (2)

### `decision-debugging-build-versus-test`

Debugging And Root Cause Analysis: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Debugging And Root Cause Analysis, this model decides investigate, isolate, repair, rollback, or escalate using reproduction, logs, stack trace, history, environment, and focused regression and the constraint 'each edit tests one changed hypothesis about the verified cause'.

### `decision-debugging-local-versus-systemic`

Debugging And Root Cause Analysis: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening each edit tests one changed hypothesis about the verified cause.

**Domain delta:** For Debugging And Root Cause Analysis, this model decides investigate, isolate, repair, rollback, or escalate using reproduction, logs, stack trace, history, environment, and focused regression and the constraint 'each edit tests one changed hypothesis about the verified cause'.

## Mental Model (2)

### `mental-model-debugging-feedback-loop`

Debugging And Root Cause Analysis: Feedback Loop. Actions on a reproducible defect or failed verifier change failure timeline, hypothesis ledger, and minimal reproduction, which changes the next evidence and decision environment.

**Domain delta:** For Debugging And Root Cause Analysis, this model maps causal diagnosis improves when hypotheses are falsifiable and evidence is time-ordered onto a reproducible defect or failed verifier and failure timeline, hypothesis ledger, and minimal reproduction.

### `mental-model-debugging-weakest-link`

Debugging And Root Cause Analysis: Weakest Link And Bottleneck. End-to-end quality for a reproducible defect or failed verifier is limited by the least trustworthy boundary in the path through failure timeline, hypothesis ledger, and minimal reproduction.

**Domain delta:** For Debugging And Root Cause Analysis, this model maps causal diagnosis improves when hypotheses are falsifiable and evidence is time-ordered onto a reproducible defect or failed verifier and failure timeline, hypothesis ledger, and minimal reproduction.

## Governance (1)

### `governance-debugging-evidence-authority-policy`

Debugging And Root Cause Analysis: Evidence And Authority Policy. Work on a reproducible defect or failed verifier must preserve 'each edit tests one changed hypothesis about the verified cause', cite reproduction, logs, stack trace, history, environment, and focused regression, and remain within 'investigation evidence versus repair authority'.

**Domain delta:** For Debugging And Root Cause Analysis, this policy enforces three failed hypotheses continue without a mandatory reset at investigation evidence versus repair authority.

