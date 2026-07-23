# Technical Communication

Exchange decisions, evidence, uncertainty, and action clearly for the intended audience.

- **Domain ID:** `communication`
- **Boundary:** informing and recommending versus deciding or persuading through manipulation
- **Invariant:** communication is truthful, proportionate, actionable, and free of fabricated certainty
- **Default evidence:** audience comprehension, accurate references, action clarity, and feedback
- **Risk classes:** communication, trust

## Behavior (10)

### `behavior-communication-choose-falsifier`

Technical Communication: Choose Cheapest Falsifier. Choose the lowest-cost check of decision packet, status update, explanation, or incident message that could disprove the current hypothesis.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-communicate-uncertainty`

Technical Communication: Communicate Uncertainty. State confidence, missing evidence, failure impact 'misunderstanding, hidden risk, wasted effort, and damaged trust', and the next discriminating check.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-establish-state`

Technical Communication: Establish Current State. Inspect decision packet, status update, explanation, or incident message and record the current behavior before proposing change.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-identify-owner`

Technical Communication: Identify Owner And Boundary. Name the owner of decision packet, status update, explanation, or incident message, the boundary 'informing and recommending versus deciding or persuading through manipulation', and who may decide or mutate it.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-minimize-change`

Technical Communication: Make The Smallest Useful Change. Change only the owning slice of decision packet, status update, explanation, or incident message needed to protect 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-protect-invariant`

Technical Communication: Protect The Domain Invariant. Reject an option that can violate 'communication is truthful, proportionate, actionable, and free of fabricated certainty' without an approved mitigation.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-stop-and-escalate`

Technical Communication: Stop And Escalate. Stop mutation, preserve evidence, and route 'simplify, restate decisions and unknowns, verify understanding, and correct the record' to the accountable owner.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-surface-assumptions`

Technical Communication: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-update-memory`

Technical Communication: Update Durable Knowledge. Update the decision or memory record for decision packet, status update, explanation, or incident message with provenance and invalidation triggers.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `behavior-communication-validate-immediately`

Technical Communication: Validate Immediately. Run audience comprehension, accurate references, action clarity, and feedback or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Technical Communication, this behavior operates on decision packet, status update, explanation, or incident message, uses audience comprehension, accurate references, action clarity, and feedback, and protects 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

## Failure (6)

### `failure-communication-boundary-violation`

Technical Communication: Boundary Violation. A local optimization bypasses the domain ownership model for technical and product communication.

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

### `failure-communication-evidence-overclaim`

Technical Communication: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

### `failure-communication-premature-action`

Technical Communication: Premature Action. language optimizes fluency or agreement instead of shared meaning and evidence

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

### `failure-communication-silent-failure`

Technical Communication: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

### `failure-communication-stale-context`

Technical Communication: Stale Context. The state of decision packet, status update, explanation, or incident message changed while routing continued from a stale checkpoint.

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

### `failure-communication-unbounded-loop`

Technical Communication: Unbounded Repair Loop. Failures do not trigger a reset of language optimizes fluency or agreement instead of shared meaning and evidence.

**Domain delta:** In Technical Communication, this failure threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' through language optimizes fluency or agreement instead of shared meaning and evidence.

## Signal (4)

### `signal-communication-constraint-risk`

Technical Communication: Constraint Or Risk Signal. A current constraint or risk threatens 'communication is truthful, proportionate, actionable, and free of fabricated certainty' for technical and product communication.

**Domain delta:** For Technical Communication, this signal observes technical and product communication through decision packet, status update, explanation, or incident message while rejecting stale or untrusted substitutes.

### `signal-communication-explicit-mission`

Technical Communication: Explicit Mission Signal. The current user request explicitly concerns technical and product communication and states an observable outcome.

**Domain delta:** For Technical Communication, this signal observes technical and product communication through decision packet, status update, explanation, or incident message while rejecting stale or untrusted substitutes.

### `signal-communication-repository-evidence`

Technical Communication: Repository Evidence Signal. Current source or accepted documentation identifies decision packet, status update, explanation, or incident message as the owning surface for technical and product communication.

**Domain delta:** For Technical Communication, this signal observes technical and product communication through decision packet, status update, explanation, or incident message while rejecting stale or untrusted substitutes.

### `signal-communication-runtime-failure`

Technical Communication: Runtime Failure Signal. A reproducible observation shows language optimizes fluency or agreement instead of shared meaning and evidence in decision packet, status update, explanation, or incident message.

**Domain delta:** For Technical Communication, this signal observes technical and product communication through decision packet, status update, explanation, or incident message while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-communication-escalate-and-contain`

Technical Communication: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route simplify, restate decisions and unknowns, verify understanding, and correct the record to the accountable owner

**Domain delta:** For Technical Communication, recovery targets language optimizes fluency or agreement instead of shared meaning and evidence in decision packet, status update, explanation, or incident message and exits only with audience comprehension, accurate references, action clarity, and feedback.

### `recovery-communication-isolate-and-repair`

Technical Communication: Isolate And Repair. Reduce to the smallest failing path in decision packet, status update, explanation, or incident message Apply one bounded repair Run audience comprehension, accurate references, action clarity, and feedback Check adjacent invariants

**Domain delta:** For Technical Communication, recovery targets language optimizes fluency or agreement instead of shared meaning and evidence in decision packet, status update, explanation, or incident message and exits only with audience comprehension, accurate references, action clarity, and feedback.

### `recovery-communication-reset-and-reconstruct`

Technical Communication: Reset And Reconstruct. Stop mutation Re-read decision packet, status update, explanation, or incident message and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Technical Communication, recovery targets language optimizes fluency or agreement instead of shared meaning and evidence in decision packet, status update, explanation, or incident message and exits only with audience comprehension, accurate references, action clarity, and feedback.

## Decision (2)

### `decision-communication-build-versus-test`

Technical Communication: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Technical Communication, this model decides inform, ask, challenge, recommend, teach, warn, summarize, or remain silent using audience comprehension, accurate references, action clarity, and feedback and the constraint 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

### `decision-communication-local-versus-systemic`

Technical Communication: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening communication is truthful, proportionate, actionable, and free of fabricated certainty.

**Domain delta:** For Technical Communication, this model decides inform, ask, challenge, recommend, teach, warn, summarize, or remain silent using audience comprehension, accurate references, action clarity, and feedback and the constraint 'communication is truthful, proportionate, actionable, and free of fabricated certainty'.

## Mental Model (2)

### `mental-model-communication-feedback-loop`

Technical Communication: Feedback Loop. Actions on technical and product communication change decision packet, status update, explanation, or incident message, which changes the next evidence and decision environment.

**Domain delta:** For Technical Communication, this model maps communication succeeds when sender and receiver converge on meaning, not when text is merely delivered onto technical and product communication and decision packet, status update, explanation, or incident message.

### `mental-model-communication-weakest-link`

Technical Communication: Weakest Link And Bottleneck. End-to-end quality for technical and product communication is limited by the least trustworthy boundary in the path through decision packet, status update, explanation, or incident message.

**Domain delta:** For Technical Communication, this model maps communication succeeds when sender and receiver converge on meaning, not when text is merely delivered onto technical and product communication and decision packet, status update, explanation, or incident message.

## Governance (1)

### `governance-communication-evidence-authority-policy`

Technical Communication: Evidence And Authority Policy. Work on technical and product communication must preserve 'communication is truthful, proportionate, actionable, and free of fabricated certainty', cite audience comprehension, accurate references, action clarity, and feedback, and remain within 'informing and recommending versus deciding or persuading through manipulation'.

**Domain delta:** For Technical Communication, this policy enforces public, legal, security, or incident claims exceed evidence or authority at informing and recommending versus deciding or persuading through manipulation.

