# Documentation Engineering

Preserve accurate, navigable, audience-specific knowledge that remains useful after the conversation.

- **Domain ID:** `documentation`
- **Boundary:** normative source versus explanation, generated index, and historical record
- **Invariant:** documentation describes verified current reality and routes readers to authoritative sources
- **Default evidence:** source comparison, runnable examples, resolved links, audience review, and freshness checks
- **Risk classes:** knowledge, quality

## Behavior (10)

### `behavior-documentation-choose-falsifier`

Documentation Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of versioned documentation set and link graph that could disprove the current hypothesis.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-communicate-uncertainty`

Documentation Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'operators, users, and future engineers act on stale or misleading guidance', and the next discriminating check.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-establish-state`

Documentation Engineering: Establish Current State. Inspect versioned documentation set and link graph and record the current behavior before proposing change.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-identify-owner`

Documentation Engineering: Identify Owner And Boundary. Name the owner of versioned documentation set and link graph, the boundary 'normative source versus explanation, generated index, and historical record', and who may decide or mutate it.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-minimize-change`

Documentation Engineering: Make The Smallest Useful Change. Change only the owning slice of versioned documentation set and link graph needed to protect 'documentation describes verified current reality and routes readers to authoritative sources'.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-protect-invariant`

Documentation Engineering: Protect The Domain Invariant. Reject an option that can violate 'documentation describes verified current reality and routes readers to authoritative sources' without an approved mitigation.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-stop-and-escalate`

Documentation Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'compare against current source and evidence, correct the owning document, and invalidate stale copies' to the accountable owner.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-surface-assumptions`

Documentation Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'documentation describes verified current reality and routes readers to authoritative sources'.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-update-memory`

Documentation Engineering: Update Durable Knowledge. Update the decision or memory record for versioned documentation set and link graph with provenance and invalidation triggers.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

### `behavior-documentation-validate-immediately`

Documentation Engineering: Validate Immediately. Run source comparison, runnable examples, resolved links, audience review, and freshness checks or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Documentation Engineering, this behavior operates on versioned documentation set and link graph, uses source comparison, runnable examples, resolved links, audience review, and freshness checks, and protects 'documentation describes verified current reality and routes readers to authoritative sources'.

## Failure (6)

### `failure-documentation-boundary-violation`

Documentation Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for technical, product, operational, and decision documentation.

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

### `failure-documentation-evidence-overclaim`

Documentation Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

### `failure-documentation-premature-action`

Documentation Engineering: Premature Action. documentation is updated from memory, duplicates source, or mixes current and historical claims

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

### `failure-documentation-silent-failure`

Documentation Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

### `failure-documentation-stale-context`

Documentation Engineering: Stale Context. The state of versioned documentation set and link graph changed while routing continued from a stale checkpoint.

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

### `failure-documentation-unbounded-loop`

Documentation Engineering: Unbounded Repair Loop. Failures do not trigger a reset of documentation is updated from memory, duplicates source, or mixes current and historical claims.

**Domain delta:** In Documentation Engineering, this failure threatens 'documentation describes verified current reality and routes readers to authoritative sources' through documentation is updated from memory, duplicates source, or mixes current and historical claims.

## Signal (4)

### `signal-documentation-constraint-risk`

Documentation Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'documentation describes verified current reality and routes readers to authoritative sources' for technical, product, operational, and decision documentation.

**Domain delta:** For Documentation Engineering, this signal observes technical, product, operational, and decision documentation through versioned documentation set and link graph while rejecting stale or untrusted substitutes.

### `signal-documentation-explicit-mission`

Documentation Engineering: Explicit Mission Signal. The current user request explicitly concerns technical, product, operational, and decision documentation and states an observable outcome.

**Domain delta:** For Documentation Engineering, this signal observes technical, product, operational, and decision documentation through versioned documentation set and link graph while rejecting stale or untrusted substitutes.

### `signal-documentation-repository-evidence`

Documentation Engineering: Repository Evidence Signal. Current source or accepted documentation identifies versioned documentation set and link graph as the owning surface for technical, product, operational, and decision documentation.

**Domain delta:** For Documentation Engineering, this signal observes technical, product, operational, and decision documentation through versioned documentation set and link graph while rejecting stale or untrusted substitutes.

### `signal-documentation-runtime-failure`

Documentation Engineering: Runtime Failure Signal. A reproducible observation shows documentation is updated from memory, duplicates source, or mixes current and historical claims in versioned documentation set and link graph.

**Domain delta:** For Documentation Engineering, this signal observes technical, product, operational, and decision documentation through versioned documentation set and link graph while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-documentation-escalate-and-contain`

Documentation Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route compare against current source and evidence, correct the owning document, and invalidate stale copies to the accountable owner

**Domain delta:** For Documentation Engineering, recovery targets documentation is updated from memory, duplicates source, or mixes current and historical claims in versioned documentation set and link graph and exits only with source comparison, runnable examples, resolved links, audience review, and freshness checks.

### `recovery-documentation-isolate-and-repair`

Documentation Engineering: Isolate And Repair. Reduce to the smallest failing path in versioned documentation set and link graph Apply one bounded repair Run source comparison, runnable examples, resolved links, audience review, and freshness checks Check adjacent invariants

**Domain delta:** For Documentation Engineering, recovery targets documentation is updated from memory, duplicates source, or mixes current and historical claims in versioned documentation set and link graph and exits only with source comparison, runnable examples, resolved links, audience review, and freshness checks.

### `recovery-documentation-reset-and-reconstruct`

Documentation Engineering: Reset And Reconstruct. Stop mutation Re-read versioned documentation set and link graph and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Documentation Engineering, recovery targets documentation is updated from memory, duplicates source, or mixes current and historical claims in versioned documentation set and link graph and exits only with source comparison, runnable examples, resolved links, audience review, and freshness checks.

## Decision (2)

### `decision-documentation-build-versus-test`

Documentation Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Documentation Engineering, this model decides document, link, generate, archive, deprecate, or omit using source comparison, runnable examples, resolved links, audience review, and freshness checks and the constraint 'documentation describes verified current reality and routes readers to authoritative sources'.

### `decision-documentation-local-versus-systemic`

Documentation Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening documentation describes verified current reality and routes readers to authoritative sources.

**Domain delta:** For Documentation Engineering, this model decides document, link, generate, archive, deprecate, or omit using source comparison, runnable examples, resolved links, audience review, and freshness checks and the constraint 'documentation describes verified current reality and routes readers to authoritative sources'.

## Mental Model (2)

### `mental-model-documentation-feedback-loop`

Documentation Engineering: Feedback Loop. Actions on technical, product, operational, and decision documentation change versioned documentation set and link graph, which changes the next evidence and decision environment.

**Domain delta:** For Documentation Engineering, this model maps documentation is an interface whose usefulness depends on accuracy, structure, audience, and maintenance onto technical, product, operational, and decision documentation and versioned documentation set and link graph.

### `mental-model-documentation-weakest-link`

Documentation Engineering: Weakest Link And Bottleneck. End-to-end quality for technical, product, operational, and decision documentation is limited by the least trustworthy boundary in the path through versioned documentation set and link graph.

**Domain delta:** For Documentation Engineering, this model maps documentation is an interface whose usefulness depends on accuracy, structure, audience, and maintenance onto technical, product, operational, and decision documentation and versioned documentation set and link graph.

## Governance (1)

### `governance-documentation-evidence-authority-policy`

Documentation Engineering: Evidence And Authority Policy. Work on technical, product, operational, and decision documentation must preserve 'documentation describes verified current reality and routes readers to authoritative sources', cite source comparison, runnable examples, resolved links, audience review, and freshness checks, and remain within 'normative source versus explanation, generated index, and historical record'.

**Domain delta:** For Documentation Engineering, this policy enforces generated or historical text is presented as current normative policy at normative source versus explanation, generated index, and historical record.

