# Risk And Safety Engineering

Identify hazards, controls, failure costs, and safe states before high-impact work proceeds.

- **Domain ID:** `risk-safety`
- **Boundary:** engineering risk analysis versus qualified and accountable risk acceptance
- **Invariant:** unacceptable harm is prevented or the activity remains blocked
- **Default evidence:** scenarios, probability/impact basis, control tests, independent review, and owner acceptance
- **Risk classes:** safety, high-stakes, governance

## Behavior (10)

### `behavior-risk-safety-choose-falsifier`

Risk And Safety Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of risk register, hazard analysis, safety case, and control evidence that could disprove the current hypothesis.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-communicate-uncertainty`

Risk And Safety Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'physical, financial, operational, legal, or social harm exceeds tolerable limits', and the next discriminating check.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-establish-state`

Risk And Safety Engineering: Establish Current State. Inspect risk register, hazard analysis, safety case, and control evidence and record the current behavior before proposing change.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-identify-owner`

Risk And Safety Engineering: Identify Owner And Boundary. Name the owner of risk register, hazard analysis, safety case, and control evidence, the boundary 'engineering risk analysis versus qualified and accountable risk acceptance', and who may decide or mutate it.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-minimize-change`

Risk And Safety Engineering: Make The Smallest Useful Change. Change only the owning slice of risk register, hazard analysis, safety case, and control evidence needed to protect 'unacceptable harm is prevented or the activity remains blocked'.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-protect-invariant`

Risk And Safety Engineering: Protect The Domain Invariant. Reject an option that can violate 'unacceptable harm is prevented or the activity remains blocked' without an approved mitigation.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-stop-and-escalate`

Risk And Safety Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'move to a safe state, contain exposure, strengthen controls, and seek qualified review' to the accountable owner.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-surface-assumptions`

Risk And Safety Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'unacceptable harm is prevented or the activity remains blocked'.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-update-memory`

Risk And Safety Engineering: Update Durable Knowledge. Update the decision or memory record for risk register, hazard analysis, safety case, and control evidence with provenance and invalidation triggers.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

### `behavior-risk-safety-validate-immediately`

Risk And Safety Engineering: Validate Immediately. Run scenarios, probability/impact basis, control tests, independent review, and owner acceptance or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Risk And Safety Engineering, this behavior operates on risk register, hazard analysis, safety case, and control evidence, uses scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and protects 'unacceptable harm is prevented or the activity remains blocked'.

## Failure (6)

### `failure-risk-safety-boundary-violation`

Risk And Safety Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for hazards, harms, controls, and residual risk.

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

### `failure-risk-safety-evidence-overclaim`

Risk And Safety Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

### `failure-risk-safety-premature-action`

Risk And Safety Engineering: Premature Action. low probability, hidden coupling, or human factors are ignored until after failure

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

### `failure-risk-safety-silent-failure`

Risk And Safety Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

### `failure-risk-safety-stale-context`

Risk And Safety Engineering: Stale Context. The state of risk register, hazard analysis, safety case, and control evidence changed while routing continued from a stale checkpoint.

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

### `failure-risk-safety-unbounded-loop`

Risk And Safety Engineering: Unbounded Repair Loop. Failures do not trigger a reset of low probability, hidden coupling, or human factors are ignored until after failure.

**Domain delta:** In Risk And Safety Engineering, this failure threatens 'unacceptable harm is prevented or the activity remains blocked' through low probability, hidden coupling, or human factors are ignored until after failure.

## Signal (4)

### `signal-risk-safety-constraint-risk`

Risk And Safety Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'unacceptable harm is prevented or the activity remains blocked' for hazards, harms, controls, and residual risk.

**Domain delta:** For Risk And Safety Engineering, this signal observes hazards, harms, controls, and residual risk through risk register, hazard analysis, safety case, and control evidence while rejecting stale or untrusted substitutes.

### `signal-risk-safety-explicit-mission`

Risk And Safety Engineering: Explicit Mission Signal. The current user request explicitly concerns hazards, harms, controls, and residual risk and states an observable outcome.

**Domain delta:** For Risk And Safety Engineering, this signal observes hazards, harms, controls, and residual risk through risk register, hazard analysis, safety case, and control evidence while rejecting stale or untrusted substitutes.

### `signal-risk-safety-repository-evidence`

Risk And Safety Engineering: Repository Evidence Signal. Current source or accepted documentation identifies risk register, hazard analysis, safety case, and control evidence as the owning surface for hazards, harms, controls, and residual risk.

**Domain delta:** For Risk And Safety Engineering, this signal observes hazards, harms, controls, and residual risk through risk register, hazard analysis, safety case, and control evidence while rejecting stale or untrusted substitutes.

### `signal-risk-safety-runtime-failure`

Risk And Safety Engineering: Runtime Failure Signal. A reproducible observation shows low probability, hidden coupling, or human factors are ignored until after failure in risk register, hazard analysis, safety case, and control evidence.

**Domain delta:** For Risk And Safety Engineering, this signal observes hazards, harms, controls, and residual risk through risk register, hazard analysis, safety case, and control evidence while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-risk-safety-escalate-and-contain`

Risk And Safety Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route move to a safe state, contain exposure, strengthen controls, and seek qualified review to the accountable owner

**Domain delta:** For Risk And Safety Engineering, recovery targets low probability, hidden coupling, or human factors are ignored until after failure in risk register, hazard analysis, safety case, and control evidence and exits only with scenarios, probability/impact basis, control tests, independent review, and owner acceptance.

### `recovery-risk-safety-isolate-and-repair`

Risk And Safety Engineering: Isolate And Repair. Reduce to the smallest failing path in risk register, hazard analysis, safety case, and control evidence Apply one bounded repair Run scenarios, probability/impact basis, control tests, independent review, and owner acceptance Check adjacent invariants

**Domain delta:** For Risk And Safety Engineering, recovery targets low probability, hidden coupling, or human factors are ignored until after failure in risk register, hazard analysis, safety case, and control evidence and exits only with scenarios, probability/impact basis, control tests, independent review, and owner acceptance.

### `recovery-risk-safety-reset-and-reconstruct`

Risk And Safety Engineering: Reset And Reconstruct. Stop mutation Re-read risk register, hazard analysis, safety case, and control evidence and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Risk And Safety Engineering, recovery targets low probability, hidden coupling, or human factors are ignored until after failure in risk register, hazard analysis, safety case, and control evidence and exits only with scenarios, probability/impact basis, control tests, independent review, and owner acceptance.

## Decision (2)

### `decision-risk-safety-build-versus-test`

Risk And Safety Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Risk And Safety Engineering, this model decides eliminate, reduce, detect, contain, transfer, accept, or stop using scenarios, probability/impact basis, control tests, independent review, and owner acceptance and the constraint 'unacceptable harm is prevented or the activity remains blocked'.

### `decision-risk-safety-local-versus-systemic`

Risk And Safety Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening unacceptable harm is prevented or the activity remains blocked.

**Domain delta:** For Risk And Safety Engineering, this model decides eliminate, reduce, detect, contain, transfer, accept, or stop using scenarios, probability/impact basis, control tests, independent review, and owner acceptance and the constraint 'unacceptable harm is prevented or the activity remains blocked'.

## Mental Model (2)

### `mental-model-risk-safety-feedback-loop`

Risk And Safety Engineering: Feedback Loop. Actions on hazards, harms, controls, and residual risk change risk register, hazard analysis, safety case, and control evidence, which changes the next evidence and decision environment.

**Domain delta:** For Risk And Safety Engineering, this model maps risk combines likelihood, exposure, impact, detectability, and control dependence onto hazards, harms, controls, and residual risk and risk register, hazard analysis, safety case, and control evidence.

### `mental-model-risk-safety-weakest-link`

Risk And Safety Engineering: Weakest Link And Bottleneck. End-to-end quality for hazards, harms, controls, and residual risk is limited by the least trustworthy boundary in the path through risk register, hazard analysis, safety case, and control evidence.

**Domain delta:** For Risk And Safety Engineering, this model maps risk combines likelihood, exposure, impact, detectability, and control dependence onto hazards, harms, controls, and residual risk and risk register, hazard analysis, safety case, and control evidence.

## Governance (1)

### `governance-risk-safety-evidence-authority-policy`

Risk And Safety Engineering: Evidence And Authority Policy. Work on hazards, harms, controls, and residual risk must preserve 'unacceptable harm is prevented or the activity remains blocked', cite scenarios, probability/impact basis, control tests, independent review, and owner acceptance, and remain within 'engineering risk analysis versus qualified and accountable risk acceptance'.

**Domain delta:** For Risk And Safety Engineering, this policy enforces material safety risk is self-approved by the implementer or hidden in aggregate scoring at engineering risk analysis versus qualified and accountable risk acceptance.

