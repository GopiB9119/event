# Privacy Engineering

Minimize personal data and make collection, use, retention, access, and deletion proportionate and transparent.

- **Domain ID:** `privacy`
- **Boundary:** product data use versus legal and privacy risk acceptance
- **Invariant:** personal data is collected and exposed only for an explicit necessary purpose
- **Default evidence:** field-level inventory, access tests, lifecycle observations, and policy-to-code mapping
- **Risk classes:** privacy, legal, high-stakes

## Behavior (10)

### `behavior-privacy-choose-falsifier`

Privacy Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of data inventory, flow map, retention/deletion design, and disclosure that could disprove the current hypothesis.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-communicate-uncertainty`

Privacy Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'unnecessary collection, unauthorized exposure, misleading disclosure, or irreversible retention', and the next discriminating check.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-establish-state`

Privacy Engineering: Establish Current State. Inspect data inventory, flow map, retention/deletion design, and disclosure and record the current behavior before proposing change.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-identify-owner`

Privacy Engineering: Identify Owner And Boundary. Name the owner of data inventory, flow map, retention/deletion design, and disclosure, the boundary 'product data use versus legal and privacy risk acceptance', and who may decide or mutate it.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-minimize-change`

Privacy Engineering: Make The Smallest Useful Change. Change only the owning slice of data inventory, flow map, retention/deletion design, and disclosure needed to protect 'personal data is collected and exposed only for an explicit necessary purpose'.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-protect-invariant`

Privacy Engineering: Protect The Domain Invariant. Reject an option that can violate 'personal data is collected and exposed only for an explicit necessary purpose' without an approved mitigation.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-stop-and-escalate`

Privacy Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop collection or sharing, contain data, correct lifecycle controls, and update disclosure' to the accountable owner.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-surface-assumptions`

Privacy Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'personal data is collected and exposed only for an explicit necessary purpose'.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-update-memory`

Privacy Engineering: Update Durable Knowledge. Update the decision or memory record for data inventory, flow map, retention/deletion design, and disclosure with provenance and invalidation triggers.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

### `behavior-privacy-validate-immediately`

Privacy Engineering: Validate Immediately. Run field-level inventory, access tests, lifecycle observations, and policy-to-code mapping or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Privacy Engineering, this behavior operates on data inventory, flow map, retention/deletion design, and disclosure, uses field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and protects 'personal data is collected and exposed only for an explicit necessary purpose'.

## Failure (6)

### `failure-privacy-boundary-violation`

Privacy Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for personal data lifecycle and user expectations.

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

### `failure-privacy-evidence-overclaim`

Privacy Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

### `failure-privacy-premature-action`

Privacy Engineering: Premature Action. data enters a system without ownership, minimization, lifecycle, or user expectation review

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

### `failure-privacy-silent-failure`

Privacy Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

### `failure-privacy-stale-context`

Privacy Engineering: Stale Context. The state of data inventory, flow map, retention/deletion design, and disclosure changed while routing continued from a stale checkpoint.

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

### `failure-privacy-unbounded-loop`

Privacy Engineering: Unbounded Repair Loop. Failures do not trigger a reset of data enters a system without ownership, minimization, lifecycle, or user expectation review.

**Domain delta:** In Privacy Engineering, this failure threatens 'personal data is collected and exposed only for an explicit necessary purpose' through data enters a system without ownership, minimization, lifecycle, or user expectation review.

## Signal (4)

### `signal-privacy-constraint-risk`

Privacy Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'personal data is collected and exposed only for an explicit necessary purpose' for personal data lifecycle and user expectations.

**Domain delta:** For Privacy Engineering, this signal observes personal data lifecycle and user expectations through data inventory, flow map, retention/deletion design, and disclosure while rejecting stale or untrusted substitutes.

### `signal-privacy-explicit-mission`

Privacy Engineering: Explicit Mission Signal. The current user request explicitly concerns personal data lifecycle and user expectations and states an observable outcome.

**Domain delta:** For Privacy Engineering, this signal observes personal data lifecycle and user expectations through data inventory, flow map, retention/deletion design, and disclosure while rejecting stale or untrusted substitutes.

### `signal-privacy-repository-evidence`

Privacy Engineering: Repository Evidence Signal. Current source or accepted documentation identifies data inventory, flow map, retention/deletion design, and disclosure as the owning surface for personal data lifecycle and user expectations.

**Domain delta:** For Privacy Engineering, this signal observes personal data lifecycle and user expectations through data inventory, flow map, retention/deletion design, and disclosure while rejecting stale or untrusted substitutes.

### `signal-privacy-runtime-failure`

Privacy Engineering: Runtime Failure Signal. A reproducible observation shows data enters a system without ownership, minimization, lifecycle, or user expectation review in data inventory, flow map, retention/deletion design, and disclosure.

**Domain delta:** For Privacy Engineering, this signal observes personal data lifecycle and user expectations through data inventory, flow map, retention/deletion design, and disclosure while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-privacy-escalate-and-contain`

Privacy Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop collection or sharing, contain data, correct lifecycle controls, and update disclosure to the accountable owner

**Domain delta:** For Privacy Engineering, recovery targets data enters a system without ownership, minimization, lifecycle, or user expectation review in data inventory, flow map, retention/deletion design, and disclosure and exits only with field-level inventory, access tests, lifecycle observations, and policy-to-code mapping.

### `recovery-privacy-isolate-and-repair`

Privacy Engineering: Isolate And Repair. Reduce to the smallest failing path in data inventory, flow map, retention/deletion design, and disclosure Apply one bounded repair Run field-level inventory, access tests, lifecycle observations, and policy-to-code mapping Check adjacent invariants

**Domain delta:** For Privacy Engineering, recovery targets data enters a system without ownership, minimization, lifecycle, or user expectation review in data inventory, flow map, retention/deletion design, and disclosure and exits only with field-level inventory, access tests, lifecycle observations, and policy-to-code mapping.

### `recovery-privacy-reset-and-reconstruct`

Privacy Engineering: Reset And Reconstruct. Stop mutation Re-read data inventory, flow map, retention/deletion design, and disclosure and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Privacy Engineering, recovery targets data enters a system without ownership, minimization, lifecycle, or user expectation review in data inventory, flow map, retention/deletion design, and disclosure and exits only with field-level inventory, access tests, lifecycle observations, and policy-to-code mapping.

## Decision (2)

### `decision-privacy-build-versus-test`

Privacy Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Privacy Engineering, this model decides avoid, minimize, localize, aggregate, retain, delete, or obtain approval using field-level inventory, access tests, lifecycle observations, and policy-to-code mapping and the constraint 'personal data is collected and exposed only for an explicit necessary purpose'.

### `decision-privacy-local-versus-systemic`

Privacy Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening personal data is collected and exposed only for an explicit necessary purpose.

**Domain delta:** For Privacy Engineering, this model decides avoid, minimize, localize, aggregate, retain, delete, or obtain approval using field-level inventory, access tests, lifecycle observations, and policy-to-code mapping and the constraint 'personal data is collected and exposed only for an explicit necessary purpose'.

## Mental Model (2)

### `mental-model-privacy-feedback-loop`

Privacy Engineering: Feedback Loop. Actions on personal data lifecycle and user expectations change data inventory, flow map, retention/deletion design, and disclosure, which changes the next evidence and decision environment.

**Domain delta:** For Privacy Engineering, this model maps privacy risk accumulates across collection, linkage, access, retention, and inference onto personal data lifecycle and user expectations and data inventory, flow map, retention/deletion design, and disclosure.

### `mental-model-privacy-weakest-link`

Privacy Engineering: Weakest Link And Bottleneck. End-to-end quality for personal data lifecycle and user expectations is limited by the least trustworthy boundary in the path through data inventory, flow map, retention/deletion design, and disclosure.

**Domain delta:** For Privacy Engineering, this model maps privacy risk accumulates across collection, linkage, access, retention, and inference onto personal data lifecycle and user expectations and data inventory, flow map, retention/deletion design, and disclosure.

## Governance (1)

### `governance-privacy-evidence-authority-policy`

Privacy Engineering: Evidence And Authority Policy. Work on personal data lifecycle and user expectations must preserve 'personal data is collected and exposed only for an explicit necessary purpose', cite field-level inventory, access tests, lifecycle observations, and policy-to-code mapping, and remain within 'product data use versus legal and privacy risk acceptance'.

**Domain delta:** For Privacy Engineering, this policy enforces personal data processing lacks purpose, owner, retention, deletion, or disclosure at product data use versus legal and privacy risk acceptance.

