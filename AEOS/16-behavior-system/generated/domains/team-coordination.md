# Team Coordination

Coordinate owners, dependencies, independent review, conflicts, and handoffs across a team.

- **Domain ID:** `team-coordination`
- **Boundary:** coordination authority versus specialist and source-owner decisions
- **Invariant:** parallel work has one integration owner and does not silently overlap authority or write scope
- **Default evidence:** ownership, isolated scopes, review results, conflict decisions, and merged outcomes
- **Risk classes:** coordination, governance

## Behavior (10)

### `behavior-team-coordination-choose-falsifier`

Team Coordination: Choose Cheapest Falsifier. Choose the lowest-cost check of assignment contracts, dependency map, handoffs, and integration record that could disprove the current hypothesis.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-communicate-uncertainty`

Team Coordination: Communicate Uncertainty. State confidence, missing evidence, failure impact 'duplicated work, conflicting edits, groupthink, dropped handoffs, and unclear accountability', and the next discriminating check.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-establish-state`

Team Coordination: Establish Current State. Inspect assignment contracts, dependency map, handoffs, and integration record and record the current behavior before proposing change.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-identify-owner`

Team Coordination: Identify Owner And Boundary. Name the owner of assignment contracts, dependency map, handoffs, and integration record, the boundary 'coordination authority versus specialist and source-owner decisions', and who may decide or mutate it.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-minimize-change`

Team Coordination: Make The Smallest Useful Change. Change only the owning slice of assignment contracts, dependency map, handoffs, and integration record needed to protect 'parallel work has one integration owner and does not silently overlap authority or write scope'.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-protect-invariant`

Team Coordination: Protect The Domain Invariant. Reject an option that can violate 'parallel work has one integration owner and does not silently overlap authority or write scope' without an approved mitigation.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-stop-and-escalate`

Team Coordination: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop overlapping work, restate assignments, isolate scopes, and route conflicts to owners' to the accountable owner.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-surface-assumptions`

Team Coordination: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'parallel work has one integration owner and does not silently overlap authority or write scope'.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-update-memory`

Team Coordination: Update Durable Knowledge. Update the decision or memory record for assignment contracts, dependency map, handoffs, and integration record with provenance and invalidation triggers.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `behavior-team-coordination-validate-immediately`

Team Coordination: Validate Immediately. Run ownership, isolated scopes, review results, conflict decisions, and merged outcomes or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Team Coordination, this behavior operates on assignment contracts, dependency map, handoffs, and integration record, uses ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and protects 'parallel work has one integration owner and does not silently overlap authority or write scope'.

## Failure (6)

### `failure-team-coordination-boundary-violation`

Team Coordination: Boundary Violation. A local optimization bypasses the domain ownership model for multi-person or multi-agent work allocation and integration.

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

### `failure-team-coordination-evidence-overclaim`

Team Coordination: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

### `failure-team-coordination-premature-action`

Team Coordination: Premature Action. roles are assigned by title without capability, authority, inputs, outputs, and stop conditions

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

### `failure-team-coordination-silent-failure`

Team Coordination: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

### `failure-team-coordination-stale-context`

Team Coordination: Stale Context. The state of assignment contracts, dependency map, handoffs, and integration record changed while routing continued from a stale checkpoint.

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

### `failure-team-coordination-unbounded-loop`

Team Coordination: Unbounded Repair Loop. Failures do not trigger a reset of roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

**Domain delta:** In Team Coordination, this failure threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' through roles are assigned by title without capability, authority, inputs, outputs, and stop conditions.

## Signal (4)

### `signal-team-coordination-constraint-risk`

Team Coordination: Constraint Or Risk Signal. A current constraint or risk threatens 'parallel work has one integration owner and does not silently overlap authority or write scope' for multi-person or multi-agent work allocation and integration.

**Domain delta:** For Team Coordination, this signal observes multi-person or multi-agent work allocation and integration through assignment contracts, dependency map, handoffs, and integration record while rejecting stale or untrusted substitutes.

### `signal-team-coordination-explicit-mission`

Team Coordination: Explicit Mission Signal. The current user request explicitly concerns multi-person or multi-agent work allocation and integration and states an observable outcome.

**Domain delta:** For Team Coordination, this signal observes multi-person or multi-agent work allocation and integration through assignment contracts, dependency map, handoffs, and integration record while rejecting stale or untrusted substitutes.

### `signal-team-coordination-repository-evidence`

Team Coordination: Repository Evidence Signal. Current source or accepted documentation identifies assignment contracts, dependency map, handoffs, and integration record as the owning surface for multi-person or multi-agent work allocation and integration.

**Domain delta:** For Team Coordination, this signal observes multi-person or multi-agent work allocation and integration through assignment contracts, dependency map, handoffs, and integration record while rejecting stale or untrusted substitutes.

### `signal-team-coordination-runtime-failure`

Team Coordination: Runtime Failure Signal. A reproducible observation shows roles are assigned by title without capability, authority, inputs, outputs, and stop conditions in assignment contracts, dependency map, handoffs, and integration record.

**Domain delta:** For Team Coordination, this signal observes multi-person or multi-agent work allocation and integration through assignment contracts, dependency map, handoffs, and integration record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-team-coordination-escalate-and-contain`

Team Coordination: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop overlapping work, restate assignments, isolate scopes, and route conflicts to owners to the accountable owner

**Domain delta:** For Team Coordination, recovery targets roles are assigned by title without capability, authority, inputs, outputs, and stop conditions in assignment contracts, dependency map, handoffs, and integration record and exits only with ownership, isolated scopes, review results, conflict decisions, and merged outcomes.

### `recovery-team-coordination-isolate-and-repair`

Team Coordination: Isolate And Repair. Reduce to the smallest failing path in assignment contracts, dependency map, handoffs, and integration record Apply one bounded repair Run ownership, isolated scopes, review results, conflict decisions, and merged outcomes Check adjacent invariants

**Domain delta:** For Team Coordination, recovery targets roles are assigned by title without capability, authority, inputs, outputs, and stop conditions in assignment contracts, dependency map, handoffs, and integration record and exits only with ownership, isolated scopes, review results, conflict decisions, and merged outcomes.

### `recovery-team-coordination-reset-and-reconstruct`

Team Coordination: Reset And Reconstruct. Stop mutation Re-read assignment contracts, dependency map, handoffs, and integration record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Team Coordination, recovery targets roles are assigned by title without capability, authority, inputs, outputs, and stop conditions in assignment contracts, dependency map, handoffs, and integration record and exits only with ownership, isolated scopes, review results, conflict decisions, and merged outcomes.

## Decision (2)

### `decision-team-coordination-build-versus-test`

Team Coordination: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Team Coordination, this model decides delegate, pair, parallelize, sequence, review, integrate, or escalate using ownership, isolated scopes, review results, conflict decisions, and merged outcomes and the constraint 'parallel work has one integration owner and does not silently overlap authority or write scope'.

### `decision-team-coordination-local-versus-systemic`

Team Coordination: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening parallel work has one integration owner and does not silently overlap authority or write scope.

**Domain delta:** For Team Coordination, this model decides delegate, pair, parallelize, sequence, review, integrate, or escalate using ownership, isolated scopes, review results, conflict decisions, and merged outcomes and the constraint 'parallel work has one integration owner and does not silently overlap authority or write scope'.

## Mental Model (2)

### `mental-model-team-coordination-feedback-loop`

Team Coordination: Feedback Loop. Actions on multi-person or multi-agent work allocation and integration change assignment contracts, dependency map, handoffs, and integration record, which changes the next evidence and decision environment.

**Domain delta:** For Team Coordination, this model maps team performance depends on coordination cost, shared models, specialization, and conflict quality onto multi-person or multi-agent work allocation and integration and assignment contracts, dependency map, handoffs, and integration record.

### `mental-model-team-coordination-weakest-link`

Team Coordination: Weakest Link And Bottleneck. End-to-end quality for multi-person or multi-agent work allocation and integration is limited by the least trustworthy boundary in the path through assignment contracts, dependency map, handoffs, and integration record.

**Domain delta:** For Team Coordination, this model maps team performance depends on coordination cost, shared models, specialization, and conflict quality onto multi-person or multi-agent work allocation and integration and assignment contracts, dependency map, handoffs, and integration record.

## Governance (1)

### `governance-team-coordination-evidence-authority-policy`

Team Coordination: Evidence And Authority Policy. Work on multi-person or multi-agent work allocation and integration must preserve 'parallel work has one integration owner and does not silently overlap authority or write scope', cite ownership, isolated scopes, review results, conflict decisions, and merged outcomes, and remain within 'coordination authority versus specialist and source-owner decisions'.

**Domain delta:** For Team Coordination, this policy enforces multiple actors mutate the same artifact without isolated ownership and integration control at coordination authority versus specialist and source-owner decisions.

