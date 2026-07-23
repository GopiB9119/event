# Business Strategy

Connect engineering choices to sustainable value, positioning, and opportunity cost.

- **Domain ID:** `business-strategy`
- **Boundary:** engineering recommendation versus business risk acceptance
- **Invariant:** technical effort supports an explicit business outcome without fabricated economics
- **Default evidence:** verified costs, constraints, market evidence, and owner priorities
- **Risk classes:** business, economics

## Behavior (10)

### `behavior-business-strategy-choose-falsifier`

Business Strategy: Choose Cheapest Falsifier. Choose the lowest-cost check of strategy decision record that could disprove the current hypothesis.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-communicate-uncertainty`

Business Strategy: Communicate Uncertainty. State confidence, missing evidence, failure impact 'the product becomes expensive, unfocused, or commercially indefensible', and the next discriminating check.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-establish-state`

Business Strategy: Establish Current State. Inspect strategy decision record and record the current behavior before proposing change.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-identify-owner`

Business Strategy: Identify Owner And Boundary. Name the owner of strategy decision record, the boundary 'engineering recommendation versus business risk acceptance', and who may decide or mutate it.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-minimize-change`

Business Strategy: Make The Smallest Useful Change. Change only the owning slice of strategy decision record needed to protect 'technical effort supports an explicit business outcome without fabricated economics'.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-protect-invariant`

Business Strategy: Protect The Domain Invariant. Reject an option that can violate 'technical effort supports an explicit business outcome without fabricated economics' without an approved mitigation.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-stop-and-escalate`

Business Strategy: Stop And Escalate. Stop mutation, preserve evidence, and route 'restate the business hypothesis, alternatives, economics, and owner decision' to the accountable owner.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-surface-assumptions`

Business Strategy: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'technical effort supports an explicit business outcome without fabricated economics'.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-update-memory`

Business Strategy: Update Durable Knowledge. Update the decision or memory record for strategy decision record with provenance and invalidation triggers.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

### `behavior-business-strategy-validate-immediately`

Business Strategy: Validate Immediately. Run verified costs, constraints, market evidence, and owner priorities or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Business Strategy, this behavior operates on strategy decision record, uses verified costs, constraints, market evidence, and owner priorities, and protects 'technical effort supports an explicit business outcome without fabricated economics'.

## Failure (6)

### `failure-business-strategy-boundary-violation`

Business Strategy: Boundary Violation. A local optimization bypasses the domain ownership model for business outcome and strategic tradeoff.

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

### `failure-business-strategy-evidence-overclaim`

Business Strategy: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

### `failure-business-strategy-premature-action`

Business Strategy: Premature Action. technical novelty is mistaken for durable customer or business value

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

### `failure-business-strategy-silent-failure`

Business Strategy: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

### `failure-business-strategy-stale-context`

Business Strategy: Stale Context. The state of strategy decision record changed while routing continued from a stale checkpoint.

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

### `failure-business-strategy-unbounded-loop`

Business Strategy: Unbounded Repair Loop. Failures do not trigger a reset of technical novelty is mistaken for durable customer or business value.

**Domain delta:** In Business Strategy, this failure threatens 'technical effort supports an explicit business outcome without fabricated economics' through technical novelty is mistaken for durable customer or business value.

## Signal (4)

### `signal-business-strategy-constraint-risk`

Business Strategy: Constraint Or Risk Signal. A current constraint or risk threatens 'technical effort supports an explicit business outcome without fabricated economics' for business outcome and strategic tradeoff.

**Domain delta:** For Business Strategy, this signal observes business outcome and strategic tradeoff through strategy decision record while rejecting stale or untrusted substitutes.

### `signal-business-strategy-explicit-mission`

Business Strategy: Explicit Mission Signal. The current user request explicitly concerns business outcome and strategic tradeoff and states an observable outcome.

**Domain delta:** For Business Strategy, this signal observes business outcome and strategic tradeoff through strategy decision record while rejecting stale or untrusted substitutes.

### `signal-business-strategy-repository-evidence`

Business Strategy: Repository Evidence Signal. Current source or accepted documentation identifies strategy decision record as the owning surface for business outcome and strategic tradeoff.

**Domain delta:** For Business Strategy, this signal observes business outcome and strategic tradeoff through strategy decision record while rejecting stale or untrusted substitutes.

### `signal-business-strategy-runtime-failure`

Business Strategy: Runtime Failure Signal. A reproducible observation shows technical novelty is mistaken for durable customer or business value in strategy decision record.

**Domain delta:** For Business Strategy, this signal observes business outcome and strategic tradeoff through strategy decision record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-business-strategy-escalate-and-contain`

Business Strategy: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route restate the business hypothesis, alternatives, economics, and owner decision to the accountable owner

**Domain delta:** For Business Strategy, recovery targets technical novelty is mistaken for durable customer or business value in strategy decision record and exits only with verified costs, constraints, market evidence, and owner priorities.

### `recovery-business-strategy-isolate-and-repair`

Business Strategy: Isolate And Repair. Reduce to the smallest failing path in strategy decision record Apply one bounded repair Run verified costs, constraints, market evidence, and owner priorities Check adjacent invariants

**Domain delta:** For Business Strategy, recovery targets technical novelty is mistaken for durable customer or business value in strategy decision record and exits only with verified costs, constraints, market evidence, and owner priorities.

### `recovery-business-strategy-reset-and-reconstruct`

Business Strategy: Reset And Reconstruct. Stop mutation Re-read strategy decision record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Business Strategy, recovery targets technical novelty is mistaken for durable customer or business value in strategy decision record and exits only with verified costs, constraints, market evidence, and owner priorities.

## Decision (2)

### `decision-business-strategy-build-versus-test`

Business Strategy: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Business Strategy, this model decides invest, test, partner, defer, or stop using verified costs, constraints, market evidence, and owner priorities and the constraint 'technical effort supports an explicit business outcome without fabricated economics'.

### `decision-business-strategy-local-versus-systemic`

Business Strategy: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening technical effort supports an explicit business outcome without fabricated economics.

**Domain delta:** For Business Strategy, this model decides invest, test, partner, defer, or stop using verified costs, constraints, market evidence, and owner priorities and the constraint 'technical effort supports an explicit business outcome without fabricated economics'.

## Mental Model (2)

### `mental-model-business-strategy-feedback-loop`

Business Strategy: Feedback Loop. Actions on business outcome and strategic tradeoff change strategy decision record, which changes the next evidence and decision environment.

**Domain delta:** For Business Strategy, this model maps strategy allocates scarce resources under uncertainty and competition onto business outcome and strategic tradeoff and strategy decision record.

### `mental-model-business-strategy-weakest-link`

Business Strategy: Weakest Link And Bottleneck. End-to-end quality for business outcome and strategic tradeoff is limited by the least trustworthy boundary in the path through strategy decision record.

**Domain delta:** For Business Strategy, this model maps strategy allocates scarce resources under uncertainty and competition onto business outcome and strategic tradeoff and strategy decision record.

## Governance (1)

### `governance-business-strategy-evidence-authority-policy`

Business Strategy: Evidence And Authority Policy. Work on business outcome and strategic tradeoff must preserve 'technical effort supports an explicit business outcome without fabricated economics', cite verified costs, constraints, market evidence, and owner priorities, and remain within 'engineering recommendation versus business risk acceptance'.

**Domain delta:** For Business Strategy, this policy enforces financial or market claims lack source and accountable ownership at engineering recommendation versus business risk acceptance.

