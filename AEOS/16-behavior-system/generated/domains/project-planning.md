# Project Planning

Sequence bounded work around dependencies, risks, owners, and acceptance evidence.

- **Domain ID:** `project-planning`
- **Boundary:** planning recommendation versus accountable schedule commitment
- **Invariant:** the plan is executable, reviewable, and updated when evidence changes
- **Default evidence:** dependency ordering, owner assignments, estimates with assumptions, and done-criteria
- **Risk classes:** delivery, coordination

## Behavior (10)

### `behavior-project-planning-choose-falsifier`

Project Planning: Choose Cheapest Falsifier. Choose the lowest-cost check of prioritized delivery plan that could disprove the current hypothesis.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-communicate-uncertainty`

Project Planning: Communicate Uncertainty. State confidence, missing evidence, failure impact 'hidden dependencies and optimistic sequencing create avoidable delay', and the next discriminating check.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-establish-state`

Project Planning: Establish Current State. Inspect prioritized delivery plan and record the current behavior before proposing change.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-identify-owner`

Project Planning: Identify Owner And Boundary. Name the owner of prioritized delivery plan, the boundary 'planning recommendation versus accountable schedule commitment', and who may decide or mutate it.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-minimize-change`

Project Planning: Make The Smallest Useful Change. Change only the owning slice of prioritized delivery plan needed to protect 'the plan is executable, reviewable, and updated when evidence changes'.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-protect-invariant`

Project Planning: Protect The Domain Invariant. Reject an option that can violate 'the plan is executable, reviewable, and updated when evidence changes' without an approved mitigation.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-stop-and-escalate`

Project Planning: Stop And Escalate. Stop mutation, preserve evidence, and route 'rebuild the critical path from current evidence and constraints' to the accountable owner.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-surface-assumptions`

Project Planning: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'the plan is executable, reviewable, and updated when evidence changes'.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-update-memory`

Project Planning: Update Durable Knowledge. Update the decision or memory record for prioritized delivery plan with provenance and invalidation triggers.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

### `behavior-project-planning-validate-immediately`

Project Planning: Validate Immediately. Run dependency ordering, owner assignments, estimates with assumptions, and done-criteria or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Project Planning, this behavior operates on prioritized delivery plan, uses dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and protects 'the plan is executable, reviewable, and updated when evidence changes'.

## Failure (6)

### `failure-project-planning-boundary-violation`

Project Planning: Boundary Violation. A local optimization bypasses the domain ownership model for project scope, dependencies, and milestones.

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

### `failure-project-planning-evidence-overclaim`

Project Planning: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

### `failure-project-planning-premature-action`

Project Planning: Premature Action. task lists omit dependencies, risk, capacity, or verification work

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

### `failure-project-planning-silent-failure`

Project Planning: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

### `failure-project-planning-stale-context`

Project Planning: Stale Context. The state of prioritized delivery plan changed while routing continued from a stale checkpoint.

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

### `failure-project-planning-unbounded-loop`

Project Planning: Unbounded Repair Loop. Failures do not trigger a reset of task lists omit dependencies, risk, capacity, or verification work.

**Domain delta:** In Project Planning, this failure threatens 'the plan is executable, reviewable, and updated when evidence changes' through task lists omit dependencies, risk, capacity, or verification work.

## Signal (4)

### `signal-project-planning-constraint-risk`

Project Planning: Constraint Or Risk Signal. A current constraint or risk threatens 'the plan is executable, reviewable, and updated when evidence changes' for project scope, dependencies, and milestones.

**Domain delta:** For Project Planning, this signal observes project scope, dependencies, and milestones through prioritized delivery plan while rejecting stale or untrusted substitutes.

### `signal-project-planning-explicit-mission`

Project Planning: Explicit Mission Signal. The current user request explicitly concerns project scope, dependencies, and milestones and states an observable outcome.

**Domain delta:** For Project Planning, this signal observes project scope, dependencies, and milestones through prioritized delivery plan while rejecting stale or untrusted substitutes.

### `signal-project-planning-repository-evidence`

Project Planning: Repository Evidence Signal. Current source or accepted documentation identifies prioritized delivery plan as the owning surface for project scope, dependencies, and milestones.

**Domain delta:** For Project Planning, this signal observes project scope, dependencies, and milestones through prioritized delivery plan while rejecting stale or untrusted substitutes.

### `signal-project-planning-runtime-failure`

Project Planning: Runtime Failure Signal. A reproducible observation shows task lists omit dependencies, risk, capacity, or verification work in prioritized delivery plan.

**Domain delta:** For Project Planning, this signal observes project scope, dependencies, and milestones through prioritized delivery plan while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-project-planning-escalate-and-contain`

Project Planning: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route rebuild the critical path from current evidence and constraints to the accountable owner

**Domain delta:** For Project Planning, recovery targets task lists omit dependencies, risk, capacity, or verification work in prioritized delivery plan and exits only with dependency ordering, owner assignments, estimates with assumptions, and done-criteria.

### `recovery-project-planning-isolate-and-repair`

Project Planning: Isolate And Repair. Reduce to the smallest failing path in prioritized delivery plan Apply one bounded repair Run dependency ordering, owner assignments, estimates with assumptions, and done-criteria Check adjacent invariants

**Domain delta:** For Project Planning, recovery targets task lists omit dependencies, risk, capacity, or verification work in prioritized delivery plan and exits only with dependency ordering, owner assignments, estimates with assumptions, and done-criteria.

### `recovery-project-planning-reset-and-reconstruct`

Project Planning: Reset And Reconstruct. Stop mutation Re-read prioritized delivery plan and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Project Planning, recovery targets task lists omit dependencies, risk, capacity, or verification work in prioritized delivery plan and exits only with dependency ordering, owner assignments, estimates with assumptions, and done-criteria.

## Decision (2)

### `decision-project-planning-build-versus-test`

Project Planning: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Project Planning, this model decides sequence, split, parallelize, defer, or stop using dependency ordering, owner assignments, estimates with assumptions, and done-criteria and the constraint 'the plan is executable, reviewable, and updated when evidence changes'.

### `decision-project-planning-local-versus-systemic`

Project Planning: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening the plan is executable, reviewable, and updated when evidence changes.

**Domain delta:** For Project Planning, this model decides sequence, split, parallelize, defer, or stop using dependency ordering, owner assignments, estimates with assumptions, and done-criteria and the constraint 'the plan is executable, reviewable, and updated when evidence changes'.

## Mental Model (2)

### `mental-model-project-planning-feedback-loop`

Project Planning: Feedback Loop. Actions on project scope, dependencies, and milestones change prioritized delivery plan, which changes the next evidence and decision environment.

**Domain delta:** For Project Planning, this model maps critical paths and queues determine delivery more than task count onto project scope, dependencies, and milestones and prioritized delivery plan.

### `mental-model-project-planning-weakest-link`

Project Planning: Weakest Link And Bottleneck. End-to-end quality for project scope, dependencies, and milestones is limited by the least trustworthy boundary in the path through prioritized delivery plan.

**Domain delta:** For Project Planning, this model maps critical paths and queues determine delivery more than task count onto project scope, dependencies, and milestones and prioritized delivery plan.

## Governance (1)

### `governance-project-planning-evidence-authority-policy`

Project Planning: Evidence And Authority Policy. Work on project scope, dependencies, and milestones must preserve 'the plan is executable, reviewable, and updated when evidence changes', cite dependency ordering, owner assignments, estimates with assumptions, and done-criteria, and remain within 'planning recommendation versus accountable schedule commitment'.

**Domain delta:** For Project Planning, this policy enforces dates or capacity are invented without owner input at planning recommendation versus accountable schedule commitment.

