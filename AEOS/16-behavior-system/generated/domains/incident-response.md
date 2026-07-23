# Incident Response

Detect, contain, communicate, recover, and learn from active production or data-integrity harm.

- **Domain ID:** `incident-response`
- **Boundary:** incident coordination versus source repair, legal, security, and public communication ownership
- **Invariant:** containment protects people and data while preserving evidence and authority
- **Default evidence:** alerts, logs, impact scope, state snapshots, recovery checks, and communications
- **Risk classes:** incident, high-stakes, operations

## Behavior (10)

### `behavior-incident-response-choose-falsifier`

Incident Response: Choose Cheapest Falsifier. Choose the lowest-cost check of incident timeline, containment record, runbook, and postmortem that could disprove the current hypothesis.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-communicate-uncertainty`

Incident Response: Communicate Uncertainty. State confidence, missing evidence, failure impact 'continued harm, evidence loss, unsafe recovery, misinformation, or recurrence', and the next discriminating check.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-establish-state`

Incident Response: Establish Current State. Inspect incident timeline, containment record, runbook, and postmortem and record the current behavior before proposing change.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-identify-owner`

Incident Response: Identify Owner And Boundary. Name the owner of incident timeline, containment record, runbook, and postmortem, the boundary 'incident coordination versus source repair, legal, security, and public communication ownership', and who may decide or mutate it.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-minimize-change`

Incident Response: Make The Smallest Useful Change. Change only the owning slice of incident timeline, containment record, runbook, and postmortem needed to protect 'containment protects people and data while preserving evidence and authority'.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-protect-invariant`

Incident Response: Protect The Domain Invariant. Reject an option that can violate 'containment protects people and data while preserving evidence and authority' without an approved mitigation.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-stop-and-escalate`

Incident Response: Stop And Escalate. Stop mutation, preserve evidence, and route 'activate incident command, contain impact, preserve evidence, restore safely, and run a blameless review' to the accountable owner.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-surface-assumptions`

Incident Response: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'containment protects people and data while preserving evidence and authority'.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-update-memory`

Incident Response: Update Durable Knowledge. Update the decision or memory record for incident timeline, containment record, runbook, and postmortem with provenance and invalidation triggers.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

### `behavior-incident-response-validate-immediately`

Incident Response: Validate Immediately. Run alerts, logs, impact scope, state snapshots, recovery checks, and communications or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Incident Response, this behavior operates on incident timeline, containment record, runbook, and postmortem, uses alerts, logs, impact scope, state snapshots, recovery checks, and communications, and protects 'containment protects people and data while preserving evidence and authority'.

## Failure (6)

### `failure-incident-response-boundary-violation`

Incident Response: Boundary Violation. A local optimization bypasses the domain ownership model for active incident and affected users or systems.

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

### `failure-incident-response-evidence-overclaim`

Incident Response: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

### `failure-incident-response-premature-action`

Incident Response: Premature Action. repair begins before impact, authority, containment, and evidence are understood

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

### `failure-incident-response-silent-failure`

Incident Response: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

### `failure-incident-response-stale-context`

Incident Response: Stale Context. The state of incident timeline, containment record, runbook, and postmortem changed while routing continued from a stale checkpoint.

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

### `failure-incident-response-unbounded-loop`

Incident Response: Unbounded Repair Loop. Failures do not trigger a reset of repair begins before impact, authority, containment, and evidence are understood.

**Domain delta:** In Incident Response, this failure threatens 'containment protects people and data while preserving evidence and authority' through repair begins before impact, authority, containment, and evidence are understood.

## Signal (4)

### `signal-incident-response-constraint-risk`

Incident Response: Constraint Or Risk Signal. A current constraint or risk threatens 'containment protects people and data while preserving evidence and authority' for active incident and affected users or systems.

**Domain delta:** For Incident Response, this signal observes active incident and affected users or systems through incident timeline, containment record, runbook, and postmortem while rejecting stale or untrusted substitutes.

### `signal-incident-response-explicit-mission`

Incident Response: Explicit Mission Signal. The current user request explicitly concerns active incident and affected users or systems and states an observable outcome.

**Domain delta:** For Incident Response, this signal observes active incident and affected users or systems through incident timeline, containment record, runbook, and postmortem while rejecting stale or untrusted substitutes.

### `signal-incident-response-repository-evidence`

Incident Response: Repository Evidence Signal. Current source or accepted documentation identifies incident timeline, containment record, runbook, and postmortem as the owning surface for active incident and affected users or systems.

**Domain delta:** For Incident Response, this signal observes active incident and affected users or systems through incident timeline, containment record, runbook, and postmortem while rejecting stale or untrusted substitutes.

### `signal-incident-response-runtime-failure`

Incident Response: Runtime Failure Signal. A reproducible observation shows repair begins before impact, authority, containment, and evidence are understood in incident timeline, containment record, runbook, and postmortem.

**Domain delta:** For Incident Response, this signal observes active incident and affected users or systems through incident timeline, containment record, runbook, and postmortem while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-incident-response-escalate-and-contain`

Incident Response: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route activate incident command, contain impact, preserve evidence, restore safely, and run a blameless review to the accountable owner

**Domain delta:** For Incident Response, recovery targets repair begins before impact, authority, containment, and evidence are understood in incident timeline, containment record, runbook, and postmortem and exits only with alerts, logs, impact scope, state snapshots, recovery checks, and communications.

### `recovery-incident-response-isolate-and-repair`

Incident Response: Isolate And Repair. Reduce to the smallest failing path in incident timeline, containment record, runbook, and postmortem Apply one bounded repair Run alerts, logs, impact scope, state snapshots, recovery checks, and communications Check adjacent invariants

**Domain delta:** For Incident Response, recovery targets repair begins before impact, authority, containment, and evidence are understood in incident timeline, containment record, runbook, and postmortem and exits only with alerts, logs, impact scope, state snapshots, recovery checks, and communications.

### `recovery-incident-response-reset-and-reconstruct`

Incident Response: Reset And Reconstruct. Stop mutation Re-read incident timeline, containment record, runbook, and postmortem and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Incident Response, recovery targets repair begins before impact, authority, containment, and evidence are understood in incident timeline, containment record, runbook, and postmortem and exits only with alerts, logs, impact scope, state snapshots, recovery checks, and communications.

## Decision (2)

### `decision-incident-response-build-versus-test`

Incident Response: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Incident Response, this model decides observe, contain, rollback, disable, recover, communicate, or escalate using alerts, logs, impact scope, state snapshots, recovery checks, and communications and the constraint 'containment protects people and data while preserving evidence and authority'.

### `decision-incident-response-local-versus-systemic`

Incident Response: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening containment protects people and data while preserving evidence and authority.

**Domain delta:** For Incident Response, this model decides observe, contain, rollback, disable, recover, communicate, or escalate using alerts, logs, impact scope, state snapshots, recovery checks, and communications and the constraint 'containment protects people and data while preserving evidence and authority'.

## Mental Model (2)

### `mental-model-incident-response-feedback-loop`

Incident Response: Feedback Loop. Actions on active incident and affected users or systems change incident timeline, containment record, runbook, and postmortem, which changes the next evidence and decision environment.

**Domain delta:** For Incident Response, this model maps incident outcomes depend on response latency, coordination, containment, and learning loops onto active incident and affected users or systems and incident timeline, containment record, runbook, and postmortem.

### `mental-model-incident-response-weakest-link`

Incident Response: Weakest Link And Bottleneck. End-to-end quality for active incident and affected users or systems is limited by the least trustworthy boundary in the path through incident timeline, containment record, runbook, and postmortem.

**Domain delta:** For Incident Response, this model maps incident outcomes depend on response latency, coordination, containment, and learning loops onto active incident and affected users or systems and incident timeline, containment record, runbook, and postmortem.

## Governance (1)

### `governance-incident-response-evidence-authority-policy`

Incident Response: Evidence And Authority Policy. Work on active incident and affected users or systems must preserve 'containment protects people and data while preserving evidence and authority', cite alerts, logs, impact scope, state snapshots, recovery checks, and communications, and remain within 'incident coordination versus source repair, legal, security, and public communication ownership'.

**Domain delta:** For Incident Response, this policy enforces production action or public communication occurs outside incident authority at incident coordination versus source repair, legal, security, and public communication ownership.

