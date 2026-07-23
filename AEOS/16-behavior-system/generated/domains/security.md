# Security Engineering

Protect assets by designing threat-aware boundaries, least privilege, and fail-closed controls.

- **Domain ID:** `security`
- **Boundary:** security assurance versus accountable risk acceptance
- **Invariant:** unauthorized actors cannot gain protected capability or data through expected threat paths
- **Default evidence:** abuse cases, authorization tests, secret scans, dependency checks, and control observations
- **Risk classes:** security, high-stakes

## Behavior (10)

### `behavior-security-choose-falsifier`

Security Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of threat model, control design, test evidence, and residual-risk record that could disprove the current hypothesis.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-communicate-uncertainty`

Security Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'confidentiality, integrity, availability, identity, or supply-chain compromise', and the next discriminating check.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-establish-state`

Security Engineering: Establish Current State. Inspect threat model, control design, test evidence, and residual-risk record and record the current behavior before proposing change.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-identify-owner`

Security Engineering: Identify Owner And Boundary. Name the owner of threat model, control design, test evidence, and residual-risk record, the boundary 'security assurance versus accountable risk acceptance', and who may decide or mutate it.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-minimize-change`

Security Engineering: Make The Smallest Useful Change. Change only the owning slice of threat model, control design, test evidence, and residual-risk record needed to protect 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-protect-invariant`

Security Engineering: Protect The Domain Invariant. Reject an option that can violate 'unauthorized actors cannot gain protected capability or data through expected threat paths' without an approved mitigation.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-stop-and-escalate`

Security Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'contain exposure, revoke affected capability, preserve evidence, and repair the owning control' to the accountable owner.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-surface-assumptions`

Security Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-update-memory`

Security Engineering: Update Durable Knowledge. Update the decision or memory record for threat model, control design, test evidence, and residual-risk record with provenance and invalidation triggers.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `behavior-security-validate-immediately`

Security Engineering: Validate Immediately. Run abuse cases, authorization tests, secret scans, dependency checks, and control observations or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Security Engineering, this behavior operates on threat model, control design, test evidence, and residual-risk record, uses abuse cases, authorization tests, secret scans, dependency checks, and control observations, and protects 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

## Failure (6)

### `failure-security-boundary-violation`

Security Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for security assets, threats, identities, and controls.

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

### `failure-security-evidence-overclaim`

Security Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

### `failure-security-premature-action`

Security Engineering: Premature Action. trust is assumed at a boundary or a control exists only in UI or prompt text

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

### `failure-security-silent-failure`

Security Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

### `failure-security-stale-context`

Security Engineering: Stale Context. The state of threat model, control design, test evidence, and residual-risk record changed while routing continued from a stale checkpoint.

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

### `failure-security-unbounded-loop`

Security Engineering: Unbounded Repair Loop. Failures do not trigger a reset of trust is assumed at a boundary or a control exists only in UI or prompt text.

**Domain delta:** In Security Engineering, this failure threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' through trust is assumed at a boundary or a control exists only in UI or prompt text.

## Signal (4)

### `signal-security-constraint-risk`

Security Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'unauthorized actors cannot gain protected capability or data through expected threat paths' for security assets, threats, identities, and controls.

**Domain delta:** For Security Engineering, this signal observes security assets, threats, identities, and controls through threat model, control design, test evidence, and residual-risk record while rejecting stale or untrusted substitutes.

### `signal-security-explicit-mission`

Security Engineering: Explicit Mission Signal. The current user request explicitly concerns security assets, threats, identities, and controls and states an observable outcome.

**Domain delta:** For Security Engineering, this signal observes security assets, threats, identities, and controls through threat model, control design, test evidence, and residual-risk record while rejecting stale or untrusted substitutes.

### `signal-security-repository-evidence`

Security Engineering: Repository Evidence Signal. Current source or accepted documentation identifies threat model, control design, test evidence, and residual-risk record as the owning surface for security assets, threats, identities, and controls.

**Domain delta:** For Security Engineering, this signal observes security assets, threats, identities, and controls through threat model, control design, test evidence, and residual-risk record while rejecting stale or untrusted substitutes.

### `signal-security-runtime-failure`

Security Engineering: Runtime Failure Signal. A reproducible observation shows trust is assumed at a boundary or a control exists only in UI or prompt text in threat model, control design, test evidence, and residual-risk record.

**Domain delta:** For Security Engineering, this signal observes security assets, threats, identities, and controls through threat model, control design, test evidence, and residual-risk record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-security-escalate-and-contain`

Security Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route contain exposure, revoke affected capability, preserve evidence, and repair the owning control to the accountable owner

**Domain delta:** For Security Engineering, recovery targets trust is assumed at a boundary or a control exists only in UI or prompt text in threat model, control design, test evidence, and residual-risk record and exits only with abuse cases, authorization tests, secret scans, dependency checks, and control observations.

### `recovery-security-isolate-and-repair`

Security Engineering: Isolate And Repair. Reduce to the smallest failing path in threat model, control design, test evidence, and residual-risk record Apply one bounded repair Run abuse cases, authorization tests, secret scans, dependency checks, and control observations Check adjacent invariants

**Domain delta:** For Security Engineering, recovery targets trust is assumed at a boundary or a control exists only in UI or prompt text in threat model, control design, test evidence, and residual-risk record and exits only with abuse cases, authorization tests, secret scans, dependency checks, and control observations.

### `recovery-security-reset-and-reconstruct`

Security Engineering: Reset And Reconstruct. Stop mutation Re-read threat model, control design, test evidence, and residual-risk record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Security Engineering, recovery targets trust is assumed at a boundary or a control exists only in UI or prompt text in threat model, control design, test evidence, and residual-risk record and exits only with abuse cases, authorization tests, secret scans, dependency checks, and control observations.

## Decision (2)

### `decision-security-build-versus-test`

Security Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Security Engineering, this model decides eliminate, prevent, detect, contain, transfer, or accept with authority using abuse cases, authorization tests, secret scans, dependency checks, and control observations and the constraint 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

### `decision-security-local-versus-systemic`

Security Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening unauthorized actors cannot gain protected capability or data through expected threat paths.

**Domain delta:** For Security Engineering, this model decides eliminate, prevent, detect, contain, transfer, or accept with authority using abuse cases, authorization tests, secret scans, dependency checks, and control observations and the constraint 'unauthorized actors cannot gain protected capability or data through expected threat paths'.

## Mental Model (2)

### `mental-model-security-feedback-loop`

Security Engineering: Feedback Loop. Actions on security assets, threats, identities, and controls change threat model, control design, test evidence, and residual-risk record, which changes the next evidence and decision environment.

**Domain delta:** For Security Engineering, this model maps attackers exploit the cheapest reachable boundary and compose small weaknesses onto security assets, threats, identities, and controls and threat model, control design, test evidence, and residual-risk record.

### `mental-model-security-weakest-link`

Security Engineering: Weakest Link And Bottleneck. End-to-end quality for security assets, threats, identities, and controls is limited by the least trustworthy boundary in the path through threat model, control design, test evidence, and residual-risk record.

**Domain delta:** For Security Engineering, this model maps attackers exploit the cheapest reachable boundary and compose small weaknesses onto security assets, threats, identities, and controls and threat model, control design, test evidence, and residual-risk record.

## Governance (1)

### `governance-security-evidence-authority-policy`

Security Engineering: Evidence And Authority Policy. Work on security assets, threats, identities, and controls must preserve 'unauthorized actors cannot gain protected capability or data through expected threat paths', cite abuse cases, authorization tests, secret scans, dependency checks, and control observations, and remain within 'security assurance versus accountable risk acceptance'.

**Domain delta:** For Security Engineering, this policy enforces security risk or exception is accepted without qualified ownership and audit at security assurance versus accountable risk acceptance.

