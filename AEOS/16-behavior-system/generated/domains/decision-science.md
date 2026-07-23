# Decision Science

Make explicit choices under uncertainty using evidence, alternatives, value, risk, and reversibility.

- **Domain ID:** `decision-science`
- **Boundary:** decision analysis versus accountable value and risk acceptance
- **Invariant:** the chosen option follows an explicit rule and can be revised when evidence changes
- **Default evidence:** option comparison, probability basis, costs, harms, owner values, and post-decision outcomes
- **Risk classes:** decision, governance

## Behavior (10)

### `behavior-decision-science-choose-falsifier`

Decision Science: Choose Cheapest Falsifier. Choose the lowest-cost check of decision record with evidence ledger and falsifier that could disprove the current hypothesis.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-communicate-uncertainty`

Decision Science: Communicate Uncertainty. State confidence, missing evidence, failure impact 'bias, anchoring, sunk cost, fake precision, and hidden tradeoffs drive irreversible choices', and the next discriminating check.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-establish-state`

Decision Science: Establish Current State. Inspect decision record with evidence ledger and falsifier and record the current behavior before proposing change.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-identify-owner`

Decision Science: Identify Owner And Boundary. Name the owner of decision record with evidence ledger and falsifier, the boundary 'decision analysis versus accountable value and risk acceptance', and who may decide or mutate it.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-minimize-change`

Decision Science: Make The Smallest Useful Change. Change only the owning slice of decision record with evidence ledger and falsifier needed to protect 'the chosen option follows an explicit rule and can be revised when evidence changes'.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-protect-invariant`

Decision Science: Protect The Domain Invariant. Reject an option that can violate 'the chosen option follows an explicit rule and can be revised when evidence changes' without an approved mitigation.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-stop-and-escalate`

Decision Science: Stop And Escalate. Stop mutation, preserve evidence, and route 'reframe one decision question, label evidence, compare no-build, and run the cheapest falsifier' to the accountable owner.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-surface-assumptions`

Decision Science: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the chosen option follows an explicit rule and can be revised when evidence changes'.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-update-memory`

Decision Science: Update Durable Knowledge. Update the decision or memory record for decision record with evidence ledger and falsifier with provenance and invalidation triggers.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `behavior-decision-science-validate-immediately`

Decision Science: Validate Immediately. Run option comparison, probability basis, costs, harms, owner values, and post-decision outcomes or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Decision Science, this behavior operates on decision record with evidence ledger and falsifier, uses option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and protects 'the chosen option follows an explicit rule and can be revised when evidence changes'.

## Failure (6)

### `failure-decision-science-boundary-violation`

Decision Science: Boundary Violation. A local optimization bypasses the domain ownership model for material engineering or product decision.

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

### `failure-decision-science-evidence-overclaim`

Decision Science: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

### `failure-decision-science-premature-action`

Decision Science: Premature Action. the preferred answer is selected before alternatives and falsifiers are examined

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

### `failure-decision-science-silent-failure`

Decision Science: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

### `failure-decision-science-stale-context`

Decision Science: Stale Context. The state of decision record with evidence ledger and falsifier changed while routing continued from a stale checkpoint.

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

### `failure-decision-science-unbounded-loop`

Decision Science: Unbounded Repair Loop. Failures do not trigger a reset of the preferred answer is selected before alternatives and falsifiers are examined.

**Domain delta:** In Decision Science, this failure threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' through the preferred answer is selected before alternatives and falsifiers are examined.

## Signal (4)

### `signal-decision-science-constraint-risk`

Decision Science: Constraint Or Risk Signal. A current constraint or risk threatens 'the chosen option follows an explicit rule and can be revised when evidence changes' for material engineering or product decision.

**Domain delta:** For Decision Science, this signal observes material engineering or product decision through decision record with evidence ledger and falsifier while rejecting stale or untrusted substitutes.

### `signal-decision-science-explicit-mission`

Decision Science: Explicit Mission Signal. The current user request explicitly concerns material engineering or product decision and states an observable outcome.

**Domain delta:** For Decision Science, this signal observes material engineering or product decision through decision record with evidence ledger and falsifier while rejecting stale or untrusted substitutes.

### `signal-decision-science-repository-evidence`

Decision Science: Repository Evidence Signal. Current source or accepted documentation identifies decision record with evidence ledger and falsifier as the owning surface for material engineering or product decision.

**Domain delta:** For Decision Science, this signal observes material engineering or product decision through decision record with evidence ledger and falsifier while rejecting stale or untrusted substitutes.

### `signal-decision-science-runtime-failure`

Decision Science: Runtime Failure Signal. A reproducible observation shows the preferred answer is selected before alternatives and falsifiers are examined in decision record with evidence ledger and falsifier.

**Domain delta:** For Decision Science, this signal observes material engineering or product decision through decision record with evidence ledger and falsifier while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-decision-science-escalate-and-contain`

Decision Science: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reframe one decision question, label evidence, compare no-build, and run the cheapest falsifier to the accountable owner

**Domain delta:** For Decision Science, recovery targets the preferred answer is selected before alternatives and falsifiers are examined in decision record with evidence ledger and falsifier and exits only with option comparison, probability basis, costs, harms, owner values, and post-decision outcomes.

### `recovery-decision-science-isolate-and-repair`

Decision Science: Isolate And Repair. Reduce to the smallest failing path in decision record with evidence ledger and falsifier Apply one bounded repair Run option comparison, probability basis, costs, harms, owner values, and post-decision outcomes Check adjacent invariants

**Domain delta:** For Decision Science, recovery targets the preferred answer is selected before alternatives and falsifiers are examined in decision record with evidence ledger and falsifier and exits only with option comparison, probability basis, costs, harms, owner values, and post-decision outcomes.

### `recovery-decision-science-reset-and-reconstruct`

Decision Science: Reset And Reconstruct. Stop mutation Re-read decision record with evidence ledger and falsifier and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Decision Science, recovery targets the preferred answer is selected before alternatives and falsifiers are examined in decision record with evidence ledger and falsifier and exits only with option comparison, probability basis, costs, harms, owner values, and post-decision outcomes.

## Decision (2)

### `decision-decision-science-build-versus-test`

Decision Science: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Decision Science, this model decides stop, defer, test, build, choose, or revisit using option comparison, probability basis, costs, harms, owner values, and post-decision outcomes and the constraint 'the chosen option follows an explicit rule and can be revised when evidence changes'.

### `decision-decision-science-local-versus-systemic`

Decision Science: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the chosen option follows an explicit rule and can be revised when evidence changes.

**Domain delta:** For Decision Science, this model decides stop, defer, test, build, choose, or revisit using option comparison, probability basis, costs, harms, owner values, and post-decision outcomes and the constraint 'the chosen option follows an explicit rule and can be revised when evidence changes'.

## Mental Model (2)

### `mental-model-decision-science-feedback-loop`

Decision Science: Feedback Loop. Actions on material engineering or product decision change decision record with evidence ledger and falsifier, which changes the next evidence and decision environment.

**Domain delta:** For Decision Science, this model maps good decisions maximize expected value under uncertainty while respecting constraints and option value onto material engineering or product decision and decision record with evidence ledger and falsifier.

### `mental-model-decision-science-weakest-link`

Decision Science: Weakest Link And Bottleneck. End-to-end quality for material engineering or product decision is limited by the least trustworthy boundary in the path through decision record with evidence ledger and falsifier.

**Domain delta:** For Decision Science, this model maps good decisions maximize expected value under uncertainty while respecting constraints and option value onto material engineering or product decision and decision record with evidence ledger and falsifier.

## Governance (1)

### `governance-decision-science-evidence-authority-policy`

Decision Science: Evidence And Authority Policy. Work on material engineering or product decision must preserve 'the chosen option follows an explicit rule and can be revised when evidence changes', cite option comparison, probability basis, costs, harms, owner values, and post-decision outcomes, and remain within 'decision analysis versus accountable value and risk acceptance'.

**Domain delta:** For Decision Science, this policy enforces the analyst invents probabilities, deadlines, economics, or owner preferences at decision analysis versus accountable value and risk acceptance.

