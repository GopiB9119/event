# User Research

Ground design and prioritization in observed user context rather than imagined personas.

- **Domain ID:** `user-research`
- **Boundary:** observed behavior versus interpretation
- **Invariant:** claims about users remain proportional to collected evidence
- **Default evidence:** traceable observations, interviews, usability results, or support patterns
- **Risk classes:** product, privacy, human

## Behavior (10)

### `behavior-user-research-choose-falsifier`

User Research: Choose Cheapest Falsifier. Choose the lowest-cost check of research evidence and insight record that could disprove the current hypothesis.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-communicate-uncertainty`

User Research: Communicate Uncertainty. State confidence, missing evidence, failure impact 'design decisions exclude or burden real users while satisfying invented needs', and the next discriminating check.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-establish-state`

User Research: Establish Current State. Inspect research evidence and insight record and record the current behavior before proposing change.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-identify-owner`

User Research: Identify Owner And Boundary. Name the owner of research evidence and insight record, the boundary 'observed behavior versus interpretation', and who may decide or mutate it.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-minimize-change`

User Research: Make The Smallest Useful Change. Change only the owning slice of research evidence and insight record needed to protect 'claims about users remain proportional to collected evidence'.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-protect-invariant`

User Research: Protect The Domain Invariant. Reject an option that can violate 'claims about users remain proportional to collected evidence' without an approved mitigation.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-stop-and-escalate`

User Research: Stop And Escalate. Stop mutation, preserve evidence, and route 'separate observation from inference and collect targeted evidence' to the accountable owner.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-surface-assumptions`

User Research: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'claims about users remain proportional to collected evidence'.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-update-memory`

User Research: Update Durable Knowledge. Update the decision or memory record for research evidence and insight record with provenance and invalidation triggers.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

### `behavior-user-research-validate-immediately`

User Research: Validate Immediately. Run traceable observations, interviews, usability results, or support patterns or the cheapest stronger check before opening another edit slice.

**Domain delta:** For User Research, this behavior operates on research evidence and insight record, uses traceable observations, interviews, usability results, or support patterns, and protects 'claims about users remain proportional to collected evidence'.

## Failure (6)

### `failure-user-research-boundary-violation`

User Research: Boundary Violation. A local optimization bypasses the domain ownership model for user behavior, constraints, and workarounds.

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

### `failure-user-research-evidence-overclaim`

User Research: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

### `failure-user-research-premature-action`

User Research: Premature Action. small or biased observations are generalized without limits

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

### `failure-user-research-silent-failure`

User Research: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

### `failure-user-research-stale-context`

User Research: Stale Context. The state of research evidence and insight record changed while routing continued from a stale checkpoint.

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

### `failure-user-research-unbounded-loop`

User Research: Unbounded Repair Loop. Failures do not trigger a reset of small or biased observations are generalized without limits.

**Domain delta:** In User Research, this failure threatens 'claims about users remain proportional to collected evidence' through small or biased observations are generalized without limits.

## Signal (4)

### `signal-user-research-constraint-risk`

User Research: Constraint Or Risk Signal. A current constraint or risk threatens 'claims about users remain proportional to collected evidence' for user behavior, constraints, and workarounds.

**Domain delta:** For User Research, this signal observes user behavior, constraints, and workarounds through research evidence and insight record while rejecting stale or untrusted substitutes.

### `signal-user-research-explicit-mission`

User Research: Explicit Mission Signal. The current user request explicitly concerns user behavior, constraints, and workarounds and states an observable outcome.

**Domain delta:** For User Research, this signal observes user behavior, constraints, and workarounds through research evidence and insight record while rejecting stale or untrusted substitutes.

### `signal-user-research-repository-evidence`

User Research: Repository Evidence Signal. Current source or accepted documentation identifies research evidence and insight record as the owning surface for user behavior, constraints, and workarounds.

**Domain delta:** For User Research, this signal observes user behavior, constraints, and workarounds through research evidence and insight record while rejecting stale or untrusted substitutes.

### `signal-user-research-runtime-failure`

User Research: Runtime Failure Signal. A reproducible observation shows small or biased observations are generalized without limits in research evidence and insight record.

**Domain delta:** For User Research, this signal observes user behavior, constraints, and workarounds through research evidence and insight record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-user-research-escalate-and-contain`

User Research: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route separate observation from inference and collect targeted evidence to the accountable owner

**Domain delta:** For User Research, recovery targets small or biased observations are generalized without limits in research evidence and insight record and exits only with traceable observations, interviews, usability results, or support patterns.

### `recovery-user-research-isolate-and-repair`

User Research: Isolate And Repair. Reduce to the smallest failing path in research evidence and insight record Apply one bounded repair Run traceable observations, interviews, usability results, or support patterns Check adjacent invariants

**Domain delta:** For User Research, recovery targets small or biased observations are generalized without limits in research evidence and insight record and exits only with traceable observations, interviews, usability results, or support patterns.

### `recovery-user-research-reset-and-reconstruct`

User Research: Reset And Reconstruct. Stop mutation Re-read research evidence and insight record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For User Research, recovery targets small or biased observations are generalized without limits in research evidence and insight record and exits only with traceable observations, interviews, usability results, or support patterns.

## Decision (2)

### `decision-user-research-build-versus-test`

User Research: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For User Research, this model decides research more, prototype, segment, or proceed using traceable observations, interviews, usability results, or support patterns and the constraint 'claims about users remain proportional to collected evidence'.

### `decision-user-research-local-versus-systemic`

User Research: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening claims about users remain proportional to collected evidence.

**Domain delta:** For User Research, this model decides research more, prototype, segment, or proceed using traceable observations, interviews, usability results, or support patterns and the constraint 'claims about users remain proportional to collected evidence'.

## Mental Model (2)

### `mental-model-user-research-feedback-loop`

User Research: Feedback Loop. Actions on user behavior, constraints, and workarounds change research evidence and insight record, which changes the next evidence and decision environment.

**Domain delta:** For User Research, this model maps sampling and observation shape the validity of user conclusions onto user behavior, constraints, and workarounds and research evidence and insight record.

### `mental-model-user-research-weakest-link`

User Research: Weakest Link And Bottleneck. End-to-end quality for user behavior, constraints, and workarounds is limited by the least trustworthy boundary in the path through research evidence and insight record.

**Domain delta:** For User Research, this model maps sampling and observation shape the validity of user conclusions onto user behavior, constraints, and workarounds and research evidence and insight record.

## Governance (1)

### `governance-user-research-evidence-authority-policy`

User Research: Evidence And Authority Policy. Work on user behavior, constraints, and workarounds must preserve 'claims about users remain proportional to collected evidence', cite traceable observations, interviews, usability results, or support patterns, and remain within 'observed behavior versus interpretation'.

**Domain delta:** For User Research, this policy enforces private user data or unsupported generalization enters product decisions at observed behavior versus interpretation.

