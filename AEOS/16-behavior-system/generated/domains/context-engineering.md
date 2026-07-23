# Context Engineering

Provide sufficient, relevant, fresh, isolated, economical, and provenance-bearing context to reasoning systems.

- **Domain ID:** `context-engineering`
- **Boundary:** authoritative source versus memory, summary, index, and untrusted retrieved text
- **Invariant:** derived context routes back to authoritative current evidence and excludes unnecessary sensitive data
- **Default evidence:** retrieval relevance, source verification, token size, freshness, and task outcomes
- **Risk classes:** context, security, cost

## Behavior (10)

### `behavior-context-engineering-choose-falsifier`

Context Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of context pack, routing trace, provenance, and invalidation record that could disprove the current hypothesis.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-communicate-uncertainty`

Context Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'context bloat, prompt injection, stale decisions, privacy leakage, and architecture amnesia', and the next discriminating check.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-establish-state`

Context Engineering: Establish Current State. Inspect context pack, routing trace, provenance, and invalidation record and record the current behavior before proposing change.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-identify-owner`

Context Engineering: Identify Owner And Boundary. Name the owner of context pack, routing trace, provenance, and invalidation record, the boundary 'authoritative source versus memory, summary, index, and untrusted retrieved text', and who may decide or mutate it.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-minimize-change`

Context Engineering: Make The Smallest Useful Change. Change only the owning slice of context pack, routing trace, provenance, and invalidation record needed to protect 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-protect-invariant`

Context Engineering: Protect The Domain Invariant. Reject an option that can violate 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' without an approved mitigation.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-stop-and-escalate`

Context Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'invalidate stale context, rebuild from authoritative sources, and enforce a budget' to the accountable owner.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-surface-assumptions`

Context Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-update-memory`

Context Engineering: Update Durable Knowledge. Update the decision or memory record for context pack, routing trace, provenance, and invalidation record with provenance and invalidation triggers.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `behavior-context-engineering-validate-immediately`

Context Engineering: Validate Immediately. Run retrieval relevance, source verification, token size, freshness, and task outcomes or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Context Engineering, this behavior operates on context pack, routing trace, provenance, and invalidation record, uses retrieval relevance, source verification, token size, freshness, and task outcomes, and protects 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

## Failure (6)

### `failure-context-engineering-boundary-violation`

Context Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for model context selection and freshness.

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

### `failure-context-engineering-evidence-overclaim`

Context Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

### `failure-context-engineering-premature-action`

Context Engineering: Premature Action. large or convenient context replaces selective verified evidence

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

### `failure-context-engineering-silent-failure`

Context Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

### `failure-context-engineering-stale-context`

Context Engineering: Stale Context. The state of context pack, routing trace, provenance, and invalidation record changed while routing continued from a stale checkpoint.

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

### `failure-context-engineering-unbounded-loop`

Context Engineering: Unbounded Repair Loop. Failures do not trigger a reset of large or convenient context replaces selective verified evidence.

**Domain delta:** In Context Engineering, this failure threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' through large or convenient context replaces selective verified evidence.

## Signal (4)

### `signal-context-engineering-constraint-risk`

Context Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data' for model context selection and freshness.

**Domain delta:** For Context Engineering, this signal observes model context selection and freshness through context pack, routing trace, provenance, and invalidation record while rejecting stale or untrusted substitutes.

### `signal-context-engineering-explicit-mission`

Context Engineering: Explicit Mission Signal. The current user request explicitly concerns model context selection and freshness and states an observable outcome.

**Domain delta:** For Context Engineering, this signal observes model context selection and freshness through context pack, routing trace, provenance, and invalidation record while rejecting stale or untrusted substitutes.

### `signal-context-engineering-repository-evidence`

Context Engineering: Repository Evidence Signal. Current source or accepted documentation identifies context pack, routing trace, provenance, and invalidation record as the owning surface for model context selection and freshness.

**Domain delta:** For Context Engineering, this signal observes model context selection and freshness through context pack, routing trace, provenance, and invalidation record while rejecting stale or untrusted substitutes.

### `signal-context-engineering-runtime-failure`

Context Engineering: Runtime Failure Signal. A reproducible observation shows large or convenient context replaces selective verified evidence in context pack, routing trace, provenance, and invalidation record.

**Domain delta:** For Context Engineering, this signal observes model context selection and freshness through context pack, routing trace, provenance, and invalidation record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-context-engineering-escalate-and-contain`

Context Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route invalidate stale context, rebuild from authoritative sources, and enforce a budget to the accountable owner

**Domain delta:** For Context Engineering, recovery targets large or convenient context replaces selective verified evidence in context pack, routing trace, provenance, and invalidation record and exits only with retrieval relevance, source verification, token size, freshness, and task outcomes.

### `recovery-context-engineering-isolate-and-repair`

Context Engineering: Isolate And Repair. Reduce to the smallest failing path in context pack, routing trace, provenance, and invalidation record Apply one bounded repair Run retrieval relevance, source verification, token size, freshness, and task outcomes Check adjacent invariants

**Domain delta:** For Context Engineering, recovery targets large or convenient context replaces selective verified evidence in context pack, routing trace, provenance, and invalidation record and exits only with retrieval relevance, source verification, token size, freshness, and task outcomes.

### `recovery-context-engineering-reset-and-reconstruct`

Context Engineering: Reset And Reconstruct. Stop mutation Re-read context pack, routing trace, provenance, and invalidation record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Context Engineering, recovery targets large or convenient context replaces selective verified evidence in context pack, routing trace, provenance, and invalidation record and exits only with retrieval relevance, source verification, token size, freshness, and task outcomes.

## Decision (2)

### `decision-context-engineering-build-versus-test`

Context Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Context Engineering, this model decides include, summarize, retrieve, isolate, expire, or exclude using retrieval relevance, source verification, token size, freshness, and task outcomes and the constraint 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

### `decision-context-engineering-local-versus-systemic`

Context Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening derived context routes back to authoritative current evidence and excludes unnecessary sensitive data.

**Domain delta:** For Context Engineering, this model decides include, summarize, retrieve, isolate, expire, or exclude using retrieval relevance, source verification, token size, freshness, and task outcomes and the constraint 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data'.

## Mental Model (2)

### `mental-model-context-engineering-feedback-loop`

Context Engineering: Feedback Loop. Actions on model context selection and freshness change context pack, routing trace, provenance, and invalidation record, which changes the next evidence and decision environment.

**Domain delta:** For Context Engineering, this model maps reasoning quality depends on relevance and provenance, not maximum context volume onto model context selection and freshness and context pack, routing trace, provenance, and invalidation record.

### `mental-model-context-engineering-weakest-link`

Context Engineering: Weakest Link And Bottleneck. End-to-end quality for model context selection and freshness is limited by the least trustworthy boundary in the path through context pack, routing trace, provenance, and invalidation record.

**Domain delta:** For Context Engineering, this model maps reasoning quality depends on relevance and provenance, not maximum context volume onto model context selection and freshness and context pack, routing trace, provenance, and invalidation record.

## Governance (1)

### `governance-context-engineering-evidence-authority-policy`

Context Engineering: Evidence And Authority Policy. Work on model context selection and freshness must preserve 'derived context routes back to authoritative current evidence and excludes unnecessary sensitive data', cite retrieval relevance, source verification, token size, freshness, and task outcomes, and remain within 'authoritative source versus memory, summary, index, and untrusted retrieved text'.

**Domain delta:** For Context Engineering, this policy enforces untrusted text becomes instruction or sensitive context is persisted without need at authoritative source versus memory, summary, index, and untrusted retrieved text.

