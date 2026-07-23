# Frontend And User Experience

Make critical user workflows understandable, efficient, responsive, and honest across states.

- **Domain ID:** `frontend-ux`
- **Boundary:** presentation and interaction versus domain authority and persisted truth
- **Invariant:** users can understand the primary action, current state, consequences, and recovery path
- **Default evidence:** runtime walkthroughs, screenshots, usability checks, and state coverage
- **Risk classes:** ux, product

## Behavior (10)

### `behavior-frontend-ux-choose-falsifier`

Frontend And User Experience: Choose Cheapest Falsifier. Choose the lowest-cost check of screen flow, UI state model, copy, and interaction evidence that could disprove the current hypothesis.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-communicate-uncertainty`

Frontend And User Experience: Communicate Uncertainty. State confidence, missing evidence, failure impact 'users make wrong decisions, lose work, or trust misleading state', and the next discriminating check.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-establish-state`

Frontend And User Experience: Establish Current State. Inspect screen flow, UI state model, copy, and interaction evidence and record the current behavior before proposing change.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-identify-owner`

Frontend And User Experience: Identify Owner And Boundary. Name the owner of screen flow, UI state model, copy, and interaction evidence, the boundary 'presentation and interaction versus domain authority and persisted truth', and who may decide or mutate it.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-minimize-change`

Frontend And User Experience: Make The Smallest Useful Change. Change only the owning slice of screen flow, UI state model, copy, and interaction evidence needed to protect 'users can understand the primary action, current state, consequences, and recovery path'.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-protect-invariant`

Frontend And User Experience: Protect The Domain Invariant. Reject an option that can violate 'users can understand the primary action, current state, consequences, and recovery path' without an approved mitigation.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-stop-and-escalate`

Frontend And User Experience: Stop And Escalate. Stop mutation, preserve evidence, and route 'restore truthful state, simplify the critical path, and test realistic user journeys' to the accountable owner.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-surface-assumptions`

Frontend And User Experience: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'users can understand the primary action, current state, consequences, and recovery path'.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-update-memory`

Frontend And User Experience: Update Durable Knowledge. Update the decision or memory record for screen flow, UI state model, copy, and interaction evidence with provenance and invalidation triggers.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

### `behavior-frontend-ux-validate-immediately`

Frontend And User Experience: Validate Immediately. Run runtime walkthroughs, screenshots, usability checks, and state coverage or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Frontend And User Experience, this behavior operates on screen flow, UI state model, copy, and interaction evidence, uses runtime walkthroughs, screenshots, usability checks, and state coverage, and protects 'users can understand the primary action, current state, consequences, and recovery path'.

## Failure (6)

### `failure-frontend-ux-boundary-violation`

Frontend And User Experience: Boundary Violation. A local optimization bypasses the domain ownership model for user interface, interaction flow, and visible system state.

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

### `failure-frontend-ux-evidence-overclaim`

Frontend And User Experience: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

### `failure-frontend-ux-premature-action`

Frontend And User Experience: Premature Action. happy-path visuals hide loading, error, empty, stale, permission, or irreversible states

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

### `failure-frontend-ux-silent-failure`

Frontend And User Experience: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

### `failure-frontend-ux-stale-context`

Frontend And User Experience: Stale Context. The state of screen flow, UI state model, copy, and interaction evidence changed while routing continued from a stale checkpoint.

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

### `failure-frontend-ux-unbounded-loop`

Frontend And User Experience: Unbounded Repair Loop. Failures do not trigger a reset of happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

**Domain delta:** In Frontend And User Experience, this failure threatens 'users can understand the primary action, current state, consequences, and recovery path' through happy-path visuals hide loading, error, empty, stale, permission, or irreversible states.

## Signal (4)

### `signal-frontend-ux-constraint-risk`

Frontend And User Experience: Constraint Or Risk Signal. A current constraint or risk threatens 'users can understand the primary action, current state, consequences, and recovery path' for user interface, interaction flow, and visible system state.

**Domain delta:** For Frontend And User Experience, this signal observes user interface, interaction flow, and visible system state through screen flow, UI state model, copy, and interaction evidence while rejecting stale or untrusted substitutes.

### `signal-frontend-ux-explicit-mission`

Frontend And User Experience: Explicit Mission Signal. The current user request explicitly concerns user interface, interaction flow, and visible system state and states an observable outcome.

**Domain delta:** For Frontend And User Experience, this signal observes user interface, interaction flow, and visible system state through screen flow, UI state model, copy, and interaction evidence while rejecting stale or untrusted substitutes.

### `signal-frontend-ux-repository-evidence`

Frontend And User Experience: Repository Evidence Signal. Current source or accepted documentation identifies screen flow, UI state model, copy, and interaction evidence as the owning surface for user interface, interaction flow, and visible system state.

**Domain delta:** For Frontend And User Experience, this signal observes user interface, interaction flow, and visible system state through screen flow, UI state model, copy, and interaction evidence while rejecting stale or untrusted substitutes.

### `signal-frontend-ux-runtime-failure`

Frontend And User Experience: Runtime Failure Signal. A reproducible observation shows happy-path visuals hide loading, error, empty, stale, permission, or irreversible states in screen flow, UI state model, copy, and interaction evidence.

**Domain delta:** For Frontend And User Experience, this signal observes user interface, interaction flow, and visible system state through screen flow, UI state model, copy, and interaction evidence while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-frontend-ux-escalate-and-contain`

Frontend And User Experience: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route restore truthful state, simplify the critical path, and test realistic user journeys to the accountable owner

**Domain delta:** For Frontend And User Experience, recovery targets happy-path visuals hide loading, error, empty, stale, permission, or irreversible states in screen flow, UI state model, copy, and interaction evidence and exits only with runtime walkthroughs, screenshots, usability checks, and state coverage.

### `recovery-frontend-ux-isolate-and-repair`

Frontend And User Experience: Isolate And Repair. Reduce to the smallest failing path in screen flow, UI state model, copy, and interaction evidence Apply one bounded repair Run runtime walkthroughs, screenshots, usability checks, and state coverage Check adjacent invariants

**Domain delta:** For Frontend And User Experience, recovery targets happy-path visuals hide loading, error, empty, stale, permission, or irreversible states in screen flow, UI state model, copy, and interaction evidence and exits only with runtime walkthroughs, screenshots, usability checks, and state coverage.

### `recovery-frontend-ux-reset-and-reconstruct`

Frontend And User Experience: Reset And Reconstruct. Stop mutation Re-read screen flow, UI state model, copy, and interaction evidence and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Frontend And User Experience, recovery targets happy-path visuals hide loading, error, empty, stale, permission, or irreversible states in screen flow, UI state model, copy, and interaction evidence and exits only with runtime walkthroughs, screenshots, usability checks, and state coverage.

## Decision (2)

### `decision-frontend-ux-build-versus-test`

Frontend And User Experience: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Frontend And User Experience, this model decides show, hide, disable, explain, confirm, defer, or remove using runtime walkthroughs, screenshots, usability checks, and state coverage and the constraint 'users can understand the primary action, current state, consequences, and recovery path'.

### `decision-frontend-ux-local-versus-systemic`

Frontend And User Experience: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening users can understand the primary action, current state, consequences, and recovery path.

**Domain delta:** For Frontend And User Experience, this model decides show, hide, disable, explain, confirm, defer, or remove using runtime walkthroughs, screenshots, usability checks, and state coverage and the constraint 'users can understand the primary action, current state, consequences, and recovery path'.

## Mental Model (2)

### `mental-model-frontend-ux-feedback-loop`

Frontend And User Experience: Feedback Loop. Actions on user interface, interaction flow, and visible system state change screen flow, UI state model, copy, and interaction evidence, which changes the next evidence and decision environment.

**Domain delta:** For Frontend And User Experience, this model maps attention and feedback shape user decisions more than feature count onto user interface, interaction flow, and visible system state and screen flow, UI state model, copy, and interaction evidence.

### `mental-model-frontend-ux-weakest-link`

Frontend And User Experience: Weakest Link And Bottleneck. End-to-end quality for user interface, interaction flow, and visible system state is limited by the least trustworthy boundary in the path through screen flow, UI state model, copy, and interaction evidence.

**Domain delta:** For Frontend And User Experience, this model maps attention and feedback shape user decisions more than feature count onto user interface, interaction flow, and visible system state and screen flow, UI state model, copy, and interaction evidence.

## Governance (1)

### `governance-frontend-ux-evidence-authority-policy`

Frontend And User Experience: Evidence And Authority Policy. Work on user interface, interaction flow, and visible system state must preserve 'users can understand the primary action, current state, consequences, and recovery path', cite runtime walkthroughs, screenshots, usability checks, and state coverage, and remain within 'presentation and interaction versus domain authority and persisted truth'.

**Domain delta:** For Frontend And User Experience, this policy enforces UI copy promises security, certainty, or completion the implementation cannot enforce at presentation and interaction versus domain authority and persisted truth.

