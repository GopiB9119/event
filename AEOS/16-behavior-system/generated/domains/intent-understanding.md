# Intent Understanding

Recover the real requested outcome and protect it from conversational drift.

- **Domain ID:** `intent-understanding`
- **Boundary:** user outcome and requirement meaning
- **Invariant:** implementation solves the requested problem rather than a nearby interpretation
- **Default evidence:** a contradiction-free restatement accepted by the user or grounded in current task evidence
- **Risk classes:** product, collaboration

## Behavior (10)

### `behavior-intent-understanding-choose-falsifier`

Intent Understanding: Choose Cheapest Falsifier. Choose the lowest-cost check of mission intent record that could disprove the current hypothesis.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-communicate-uncertainty`

Intent Understanding: Communicate Uncertainty. State confidence, missing evidence, failure impact 'building the wrong solution with high rework and trust loss', and the next discriminating check.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-establish-state`

Intent Understanding: Establish Current State. Inspect mission intent record and record the current behavior before proposing change.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-identify-owner`

Intent Understanding: Identify Owner And Boundary. Name the owner of mission intent record, the boundary 'user outcome and requirement meaning', and who may decide or mutate it.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-minimize-change`

Intent Understanding: Make The Smallest Useful Change. Change only the owning slice of mission intent record needed to protect 'implementation solves the requested problem rather than a nearby interpretation'.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-protect-invariant`

Intent Understanding: Protect The Domain Invariant. Reject an option that can violate 'implementation solves the requested problem rather than a nearby interpretation' without an approved mitigation.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-stop-and-escalate`

Intent Understanding: Stop And Escalate. Stop mutation, preserve evidence, and route 'restate the mission and resolve the decision-changing ambiguity' to the accountable owner.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-surface-assumptions`

Intent Understanding: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'implementation solves the requested problem rather than a nearby interpretation'.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-update-memory`

Intent Understanding: Update Durable Knowledge. Update the decision or memory record for mission intent record with provenance and invalidation triggers.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

### `behavior-intent-understanding-validate-immediately`

Intent Understanding: Validate Immediately. Run a contradiction-free restatement accepted by the user or grounded in current task evidence or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Intent Understanding, this behavior operates on mission intent record, uses a contradiction-free restatement accepted by the user or grounded in current task evidence, and protects 'implementation solves the requested problem rather than a nearby interpretation'.

## Failure (6)

### `failure-intent-understanding-boundary-violation`

Intent Understanding: Boundary Violation. A local optimization bypasses the domain ownership model for the user's explicit and latent intent.

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

### `failure-intent-understanding-evidence-overclaim`

Intent Understanding: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

### `failure-intent-understanding-premature-action`

Intent Understanding: Premature Action. ambiguous language becomes an architecture-changing assumption

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

### `failure-intent-understanding-silent-failure`

Intent Understanding: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

### `failure-intent-understanding-stale-context`

Intent Understanding: Stale Context. The state of mission intent record changed while routing continued from a stale checkpoint.

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

### `failure-intent-understanding-unbounded-loop`

Intent Understanding: Unbounded Repair Loop. Failures do not trigger a reset of ambiguous language becomes an architecture-changing assumption.

**Domain delta:** In Intent Understanding, this failure threatens 'implementation solves the requested problem rather than a nearby interpretation' through ambiguous language becomes an architecture-changing assumption.

## Signal (4)

### `signal-intent-understanding-constraint-risk`

Intent Understanding: Constraint Or Risk Signal. A current constraint or risk threatens 'implementation solves the requested problem rather than a nearby interpretation' for the user's explicit and latent intent.

**Domain delta:** For Intent Understanding, this signal observes the user's explicit and latent intent through mission intent record while rejecting stale or untrusted substitutes.

### `signal-intent-understanding-explicit-mission`

Intent Understanding: Explicit Mission Signal. The current user request explicitly concerns the user's explicit and latent intent and states an observable outcome.

**Domain delta:** For Intent Understanding, this signal observes the user's explicit and latent intent through mission intent record while rejecting stale or untrusted substitutes.

### `signal-intent-understanding-repository-evidence`

Intent Understanding: Repository Evidence Signal. Current source or accepted documentation identifies mission intent record as the owning surface for the user's explicit and latent intent.

**Domain delta:** For Intent Understanding, this signal observes the user's explicit and latent intent through mission intent record while rejecting stale or untrusted substitutes.

### `signal-intent-understanding-runtime-failure`

Intent Understanding: Runtime Failure Signal. A reproducible observation shows ambiguous language becomes an architecture-changing assumption in mission intent record.

**Domain delta:** For Intent Understanding, this signal observes the user's explicit and latent intent through mission intent record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-intent-understanding-escalate-and-contain`

Intent Understanding: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route restate the mission and resolve the decision-changing ambiguity to the accountable owner

**Domain delta:** For Intent Understanding, recovery targets ambiguous language becomes an architecture-changing assumption in mission intent record and exits only with a contradiction-free restatement accepted by the user or grounded in current task evidence.

### `recovery-intent-understanding-isolate-and-repair`

Intent Understanding: Isolate And Repair. Reduce to the smallest failing path in mission intent record Apply one bounded repair Run a contradiction-free restatement accepted by the user or grounded in current task evidence Check adjacent invariants

**Domain delta:** For Intent Understanding, recovery targets ambiguous language becomes an architecture-changing assumption in mission intent record and exits only with a contradiction-free restatement accepted by the user or grounded in current task evidence.

### `recovery-intent-understanding-reset-and-reconstruct`

Intent Understanding: Reset And Reconstruct. Stop mutation Re-read mission intent record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Intent Understanding, recovery targets ambiguous language becomes an architecture-changing assumption in mission intent record and exits only with a contradiction-free restatement accepted by the user or grounded in current task evidence.

## Decision (2)

### `decision-intent-understanding-build-versus-test`

Intent Understanding: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Intent Understanding, this model decides clarify, test intent, or proceed using a contradiction-free restatement accepted by the user or grounded in current task evidence and the constraint 'implementation solves the requested problem rather than a nearby interpretation'.

### `decision-intent-understanding-local-versus-systemic`

Intent Understanding: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening implementation solves the requested problem rather than a nearby interpretation.

**Domain delta:** For Intent Understanding, this model decides clarify, test intent, or proceed using a contradiction-free restatement accepted by the user or grounded in current task evidence and the constraint 'implementation solves the requested problem rather than a nearby interpretation'.

## Mental Model (2)

### `mental-model-intent-understanding-feedback-loop`

Intent Understanding: Feedback Loop. Actions on the user's explicit and latent intent change mission intent record, which changes the next evidence and decision environment.

**Domain delta:** For Intent Understanding, this model maps shared mental models reduce semantic drift between human and agent onto the user's explicit and latent intent and mission intent record.

### `mental-model-intent-understanding-weakest-link`

Intent Understanding: Weakest Link And Bottleneck. End-to-end quality for the user's explicit and latent intent is limited by the least trustworthy boundary in the path through mission intent record.

**Domain delta:** For Intent Understanding, this model maps shared mental models reduce semantic drift between human and agent onto the user's explicit and latent intent and mission intent record.

## Governance (1)

### `governance-intent-understanding-evidence-authority-policy`

Intent Understanding: Evidence And Authority Policy. Work on the user's explicit and latent intent must preserve 'implementation solves the requested problem rather than a nearby interpretation', cite a contradiction-free restatement accepted by the user or grounded in current task evidence, and remain within 'user outcome and requirement meaning'.

**Domain delta:** For Intent Understanding, this policy enforces decision-changing ambiguity remains unresolved at user outcome and requirement meaning.

