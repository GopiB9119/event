# Memory And Knowledge Management

Preserve durable verified decisions and lessons while expiring stale or sensitive derived knowledge.

- **Domain ID:** `memory-knowledge`
- **Boundary:** source of truth versus durable memory and rebuildable indexes
- **Invariant:** memory accelerates routing but never overrides stronger current evidence
- **Default evidence:** provenance, freshness, owner, invalidation, source revision, and retrieval outcomes
- **Risk classes:** context, privacy, knowledge

## Behavior (10)

### `behavior-memory-knowledge-choose-falsifier`

Memory And Knowledge Management: Choose Cheapest Falsifier. Choose the lowest-cost check of versioned memory record and knowledge index that could disprove the current hypothesis.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-communicate-uncertainty`

Memory And Knowledge Management: Communicate Uncertainty. State confidence, missing evidence, failure impact 'stale facts, repeated mistakes, privacy leakage, and contradictory decisions persist across sessions', and the next discriminating check.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-establish-state`

Memory And Knowledge Management: Establish Current State. Inspect versioned memory record and knowledge index and record the current behavior before proposing change.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-identify-owner`

Memory And Knowledge Management: Identify Owner And Boundary. Name the owner of versioned memory record and knowledge index, the boundary 'source of truth versus durable memory and rebuildable indexes', and who may decide or mutate it.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-minimize-change`

Memory And Knowledge Management: Make The Smallest Useful Change. Change only the owning slice of versioned memory record and knowledge index needed to protect 'memory accelerates routing but never overrides stronger current evidence'.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-protect-invariant`

Memory And Knowledge Management: Protect The Domain Invariant. Reject an option that can violate 'memory accelerates routing but never overrides stronger current evidence' without an approved mitigation.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-stop-and-escalate`

Memory And Knowledge Management: Stop And Escalate. Stop mutation, preserve evidence, and route 'mark the fact stale, inspect authoritative sources, and update or remove memory' to the accountable owner.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-surface-assumptions`

Memory And Knowledge Management: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'memory accelerates routing but never overrides stronger current evidence'.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-update-memory`

Memory And Knowledge Management: Update Durable Knowledge. Update the decision or memory record for versioned memory record and knowledge index with provenance and invalidation triggers.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

### `behavior-memory-knowledge-validate-immediately`

Memory And Knowledge Management: Validate Immediately. Run provenance, freshness, owner, invalidation, source revision, and retrieval outcomes or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Memory And Knowledge Management, this behavior operates on versioned memory record and knowledge index, uses provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and protects 'memory accelerates routing but never overrides stronger current evidence'.

## Failure (6)

### `failure-memory-knowledge-boundary-violation`

Memory And Knowledge Management: Boundary Violation. A local optimization bypasses the domain ownership model for project, procedural, episodic, and user-preference memory.

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

### `failure-memory-knowledge-evidence-overclaim`

Memory And Knowledge Management: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

### `failure-memory-knowledge-premature-action`

Memory And Knowledge Management: Premature Action. chat history or generated summaries are treated as authoritative project state

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

### `failure-memory-knowledge-silent-failure`

Memory And Knowledge Management: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

### `failure-memory-knowledge-stale-context`

Memory And Knowledge Management: Stale Context. The state of versioned memory record and knowledge index changed while routing continued from a stale checkpoint.

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

### `failure-memory-knowledge-unbounded-loop`

Memory And Knowledge Management: Unbounded Repair Loop. Failures do not trigger a reset of chat history or generated summaries are treated as authoritative project state.

**Domain delta:** In Memory And Knowledge Management, this failure threatens 'memory accelerates routing but never overrides stronger current evidence' through chat history or generated summaries are treated as authoritative project state.

## Signal (4)

### `signal-memory-knowledge-constraint-risk`

Memory And Knowledge Management: Constraint Or Risk Signal. A current constraint or risk threatens 'memory accelerates routing but never overrides stronger current evidence' for project, procedural, episodic, and user-preference memory.

**Domain delta:** For Memory And Knowledge Management, this signal observes project, procedural, episodic, and user-preference memory through versioned memory record and knowledge index while rejecting stale or untrusted substitutes.

### `signal-memory-knowledge-explicit-mission`

Memory And Knowledge Management: Explicit Mission Signal. The current user request explicitly concerns project, procedural, episodic, and user-preference memory and states an observable outcome.

**Domain delta:** For Memory And Knowledge Management, this signal observes project, procedural, episodic, and user-preference memory through versioned memory record and knowledge index while rejecting stale or untrusted substitutes.

### `signal-memory-knowledge-repository-evidence`

Memory And Knowledge Management: Repository Evidence Signal. Current source or accepted documentation identifies versioned memory record and knowledge index as the owning surface for project, procedural, episodic, and user-preference memory.

**Domain delta:** For Memory And Knowledge Management, this signal observes project, procedural, episodic, and user-preference memory through versioned memory record and knowledge index while rejecting stale or untrusted substitutes.

### `signal-memory-knowledge-runtime-failure`

Memory And Knowledge Management: Runtime Failure Signal. A reproducible observation shows chat history or generated summaries are treated as authoritative project state in versioned memory record and knowledge index.

**Domain delta:** For Memory And Knowledge Management, this signal observes project, procedural, episodic, and user-preference memory through versioned memory record and knowledge index while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-memory-knowledge-escalate-and-contain`

Memory And Knowledge Management: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route mark the fact stale, inspect authoritative sources, and update or remove memory to the accountable owner

**Domain delta:** For Memory And Knowledge Management, recovery targets chat history or generated summaries are treated as authoritative project state in versioned memory record and knowledge index and exits only with provenance, freshness, owner, invalidation, source revision, and retrieval outcomes.

### `recovery-memory-knowledge-isolate-and-repair`

Memory And Knowledge Management: Isolate And Repair. Reduce to the smallest failing path in versioned memory record and knowledge index Apply one bounded repair Run provenance, freshness, owner, invalidation, source revision, and retrieval outcomes Check adjacent invariants

**Domain delta:** For Memory And Knowledge Management, recovery targets chat history or generated summaries are treated as authoritative project state in versioned memory record and knowledge index and exits only with provenance, freshness, owner, invalidation, source revision, and retrieval outcomes.

### `recovery-memory-knowledge-reset-and-reconstruct`

Memory And Knowledge Management: Reset And Reconstruct. Stop mutation Re-read versioned memory record and knowledge index and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Memory And Knowledge Management, recovery targets chat history or generated summaries are treated as authoritative project state in versioned memory record and knowledge index and exits only with provenance, freshness, owner, invalidation, source revision, and retrieval outcomes.

## Decision (2)

### `decision-memory-knowledge-build-versus-test`

Memory And Knowledge Management: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Memory And Knowledge Management, this model decides store, update, expire, delete, index, or keep session-only using provenance, freshness, owner, invalidation, source revision, and retrieval outcomes and the constraint 'memory accelerates routing but never overrides stronger current evidence'.

### `decision-memory-knowledge-local-versus-systemic`

Memory And Knowledge Management: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening memory accelerates routing but never overrides stronger current evidence.

**Domain delta:** For Memory And Knowledge Management, this model decides store, update, expire, delete, index, or keep session-only using provenance, freshness, owner, invalidation, source revision, and retrieval outcomes and the constraint 'memory accelerates routing but never overrides stronger current evidence'.

## Mental Model (2)

### `mental-model-memory-knowledge-feedback-loop`

Memory And Knowledge Management: Feedback Loop. Actions on project, procedural, episodic, and user-preference memory change versioned memory record and knowledge index, which changes the next evidence and decision environment.

**Domain delta:** For Memory And Knowledge Management, this model maps memory is useful when selective, consolidated, retrievable, and continuously invalidated onto project, procedural, episodic, and user-preference memory and versioned memory record and knowledge index.

### `mental-model-memory-knowledge-weakest-link`

Memory And Knowledge Management: Weakest Link And Bottleneck. End-to-end quality for project, procedural, episodic, and user-preference memory is limited by the least trustworthy boundary in the path through versioned memory record and knowledge index.

**Domain delta:** For Memory And Knowledge Management, this model maps memory is useful when selective, consolidated, retrievable, and continuously invalidated onto project, procedural, episodic, and user-preference memory and versioned memory record and knowledge index.

## Governance (1)

### `governance-memory-knowledge-evidence-authority-policy`

Memory And Knowledge Management: Evidence And Authority Policy. Work on project, procedural, episodic, and user-preference memory must preserve 'memory accelerates routing but never overrides stronger current evidence', cite provenance, freshness, owner, invalidation, source revision, and retrieval outcomes, and remain within 'source of truth versus durable memory and rebuildable indexes'.

**Domain delta:** For Memory And Knowledge Management, this policy enforces secrets, private content, or unsupported inference enters persistent memory at source of truth versus durable memory and rebuildable indexes.

