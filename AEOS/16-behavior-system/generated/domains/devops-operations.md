# DevOps And Operations

Operate build, deployment, configuration, and service workflows safely and repeatably.

- **Domain ID:** `devops-operations`
- **Boundary:** engineering preparation versus operations and deployment authority
- **Invariant:** operational mutations are explicit, least-privilege, observable, and reversible or approved
- **Default evidence:** dry runs, policy checks, deployment plans, health signals, and rollback verification
- **Risk classes:** operations, security, cost

## Behavior (10)

### `behavior-devops-operations-choose-falsifier`

DevOps And Operations: Choose Cheapest Falsifier. Choose the lowest-cost check of runbook, pipeline, infrastructure configuration, and operational evidence that could disprove the current hypothesis.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-communicate-uncertainty`

DevOps And Operations: Communicate Uncertainty. State confidence, missing evidence, failure impact 'automation causes outages, data loss, secret exposure, or uncontrolled spend', and the next discriminating check.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-establish-state`

DevOps And Operations: Establish Current State. Inspect runbook, pipeline, infrastructure configuration, and operational evidence and record the current behavior before proposing change.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-identify-owner`

DevOps And Operations: Identify Owner And Boundary. Name the owner of runbook, pipeline, infrastructure configuration, and operational evidence, the boundary 'engineering preparation versus operations and deployment authority', and who may decide or mutate it.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-minimize-change`

DevOps And Operations: Make The Smallest Useful Change. Change only the owning slice of runbook, pipeline, infrastructure configuration, and operational evidence needed to protect 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-protect-invariant`

DevOps And Operations: Protect The Domain Invariant. Reject an option that can violate 'operational mutations are explicit, least-privilege, observable, and reversible or approved' without an approved mitigation.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-stop-and-escalate`

DevOps And Operations: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop automation, contain impact, restore from a verified runbook, and escalate' to the accountable owner.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-surface-assumptions`

DevOps And Operations: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-update-memory`

DevOps And Operations: Update Durable Knowledge. Update the decision or memory record for runbook, pipeline, infrastructure configuration, and operational evidence with provenance and invalidation triggers.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `behavior-devops-operations-validate-immediately`

DevOps And Operations: Validate Immediately. Run dry runs, policy checks, deployment plans, health signals, and rollback verification or the cheapest stronger check before opening another edit slice.

**Domain delta:** For DevOps And Operations, this behavior operates on runbook, pipeline, infrastructure configuration, and operational evidence, uses dry runs, policy checks, deployment plans, health signals, and rollback verification, and protects 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

## Failure (6)

### `failure-devops-operations-boundary-violation`

DevOps And Operations: Boundary Violation. A local optimization bypasses the domain ownership model for operational environment and delivery automation.

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

### `failure-devops-operations-evidence-overclaim`

DevOps And Operations: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

### `failure-devops-operations-premature-action`

DevOps And Operations: Premature Action. environment assumptions and side effects are hidden inside commands or pipelines

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

### `failure-devops-operations-silent-failure`

DevOps And Operations: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

### `failure-devops-operations-stale-context`

DevOps And Operations: Stale Context. The state of runbook, pipeline, infrastructure configuration, and operational evidence changed while routing continued from a stale checkpoint.

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

### `failure-devops-operations-unbounded-loop`

DevOps And Operations: Unbounded Repair Loop. Failures do not trigger a reset of environment assumptions and side effects are hidden inside commands or pipelines.

**Domain delta:** In DevOps And Operations, this failure threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' through environment assumptions and side effects are hidden inside commands or pipelines.

## Signal (4)

### `signal-devops-operations-constraint-risk`

DevOps And Operations: Constraint Or Risk Signal. A current constraint or risk threatens 'operational mutations are explicit, least-privilege, observable, and reversible or approved' for operational environment and delivery automation.

**Domain delta:** For DevOps And Operations, this signal observes operational environment and delivery automation through runbook, pipeline, infrastructure configuration, and operational evidence while rejecting stale or untrusted substitutes.

### `signal-devops-operations-explicit-mission`

DevOps And Operations: Explicit Mission Signal. The current user request explicitly concerns operational environment and delivery automation and states an observable outcome.

**Domain delta:** For DevOps And Operations, this signal observes operational environment and delivery automation through runbook, pipeline, infrastructure configuration, and operational evidence while rejecting stale or untrusted substitutes.

### `signal-devops-operations-repository-evidence`

DevOps And Operations: Repository Evidence Signal. Current source or accepted documentation identifies runbook, pipeline, infrastructure configuration, and operational evidence as the owning surface for operational environment and delivery automation.

**Domain delta:** For DevOps And Operations, this signal observes operational environment and delivery automation through runbook, pipeline, infrastructure configuration, and operational evidence while rejecting stale or untrusted substitutes.

### `signal-devops-operations-runtime-failure`

DevOps And Operations: Runtime Failure Signal. A reproducible observation shows environment assumptions and side effects are hidden inside commands or pipelines in runbook, pipeline, infrastructure configuration, and operational evidence.

**Domain delta:** For DevOps And Operations, this signal observes operational environment and delivery automation through runbook, pipeline, infrastructure configuration, and operational evidence while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-devops-operations-escalate-and-contain`

DevOps And Operations: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop automation, contain impact, restore from a verified runbook, and escalate to the accountable owner

**Domain delta:** For DevOps And Operations, recovery targets environment assumptions and side effects are hidden inside commands or pipelines in runbook, pipeline, infrastructure configuration, and operational evidence and exits only with dry runs, policy checks, deployment plans, health signals, and rollback verification.

### `recovery-devops-operations-isolate-and-repair`

DevOps And Operations: Isolate And Repair. Reduce to the smallest failing path in runbook, pipeline, infrastructure configuration, and operational evidence Apply one bounded repair Run dry runs, policy checks, deployment plans, health signals, and rollback verification Check adjacent invariants

**Domain delta:** For DevOps And Operations, recovery targets environment assumptions and side effects are hidden inside commands or pipelines in runbook, pipeline, infrastructure configuration, and operational evidence and exits only with dry runs, policy checks, deployment plans, health signals, and rollback verification.

### `recovery-devops-operations-reset-and-reconstruct`

DevOps And Operations: Reset And Reconstruct. Stop mutation Re-read runbook, pipeline, infrastructure configuration, and operational evidence and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For DevOps And Operations, recovery targets environment assumptions and side effects are hidden inside commands or pipelines in runbook, pipeline, infrastructure configuration, and operational evidence and exits only with dry runs, policy checks, deployment plans, health signals, and rollback verification.

## Decision (2)

### `decision-devops-operations-build-versus-test`

DevOps And Operations: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For DevOps And Operations, this model decides dry run, stage, deploy, rollback, or block using dry runs, policy checks, deployment plans, health signals, and rollback verification and the constraint 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

### `decision-devops-operations-local-versus-systemic`

DevOps And Operations: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening operational mutations are explicit, least-privilege, observable, and reversible or approved.

**Domain delta:** For DevOps And Operations, this model decides dry run, stage, deploy, rollback, or block using dry runs, policy checks, deployment plans, health signals, and rollback verification and the constraint 'operational mutations are explicit, least-privilege, observable, and reversible or approved'.

## Mental Model (2)

### `mental-model-devops-operations-feedback-loop`

DevOps And Operations: Feedback Loop. Actions on operational environment and delivery automation change runbook, pipeline, infrastructure configuration, and operational evidence, which changes the next evidence and decision environment.

**Domain delta:** For DevOps And Operations, this model maps automation amplifies both correctness and mistakes across environments onto operational environment and delivery automation and runbook, pipeline, infrastructure configuration, and operational evidence.

### `mental-model-devops-operations-weakest-link`

DevOps And Operations: Weakest Link And Bottleneck. End-to-end quality for operational environment and delivery automation is limited by the least trustworthy boundary in the path through runbook, pipeline, infrastructure configuration, and operational evidence.

**Domain delta:** For DevOps And Operations, this model maps automation amplifies both correctness and mistakes across environments onto operational environment and delivery automation and runbook, pipeline, infrastructure configuration, and operational evidence.

## Governance (1)

### `governance-devops-operations-evidence-authority-policy`

DevOps And Operations: Evidence And Authority Policy. Work on operational environment and delivery automation must preserve 'operational mutations are explicit, least-privilege, observable, and reversible or approved', cite dry runs, policy checks, deployment plans, health signals, and rollback verification, and remain within 'engineering preparation versus operations and deployment authority'.

**Domain delta:** For DevOps And Operations, this policy enforces production mutation, secret use, or spending lacks approval and audit evidence at engineering preparation versus operations and deployment authority.

