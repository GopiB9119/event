# Learning And Teaching

Develop durable understanding and expertise through evidence, practice, feedback, and reflection.

- **Domain ID:** `learning-teaching`
- **Boundary:** educational support versus diagnosis, grading authority, and doing all work for the learner
- **Invariant:** teaching increases the learner's capability rather than only producing an answer
- **Default evidence:** retrieval, transfer, error patterns, confidence calibration, and independent performance
- **Risk classes:** learning, human

## Behavior (10)

### `behavior-learning-teaching-choose-falsifier`

Learning And Teaching: Choose Cheapest Falsifier. Choose the lowest-cost check of learning plan, worked examples, practice evidence, and reflection record that could disprove the current hypothesis.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-communicate-uncertainty`

Learning And Teaching: Communicate Uncertainty. State confidence, missing evidence, failure impact 'shallow imitation, repeated mistakes, overload, dependency, or false confidence', and the next discriminating check.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-establish-state`

Learning And Teaching: Establish Current State. Inspect learning plan, worked examples, practice evidence, and reflection record and record the current behavior before proposing change.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-identify-owner`

Learning And Teaching: Identify Owner And Boundary. Name the owner of learning plan, worked examples, practice evidence, and reflection record, the boundary 'educational support versus diagnosis, grading authority, and doing all work for the learner', and who may decide or mutate it.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-minimize-change`

Learning And Teaching: Make The Smallest Useful Change. Change only the owning slice of learning plan, worked examples, practice evidence, and reflection record needed to protect 'teaching increases the learner's capability rather than only producing an answer'.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-protect-invariant`

Learning And Teaching: Protect The Domain Invariant. Reject an option that can violate 'teaching increases the learner's capability rather than only producing an answer' without an approved mitigation.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-stop-and-escalate`

Learning And Teaching: Stop And Escalate. Stop mutation, preserve evidence, and route 'reduce the concept, observe an attempt, give one targeted cue, and retest transfer' to the accountable owner.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-surface-assumptions`

Learning And Teaching: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'teaching increases the learner's capability rather than only producing an answer'.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-update-memory`

Learning And Teaching: Update Durable Knowledge. Update the decision or memory record for learning plan, worked examples, practice evidence, and reflection record with provenance and invalidation triggers.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

### `behavior-learning-teaching-validate-immediately`

Learning And Teaching: Validate Immediately. Run retrieval, transfer, error patterns, confidence calibration, and independent performance or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Learning And Teaching, this behavior operates on learning plan, worked examples, practice evidence, and reflection record, uses retrieval, transfer, error patterns, confidence calibration, and independent performance, and protects 'teaching increases the learner's capability rather than only producing an answer'.

## Failure (6)

### `failure-learning-teaching-boundary-violation`

Learning And Teaching: Boundary Violation. A local optimization bypasses the domain ownership model for human or agent learning objective and skill development.

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

### `failure-learning-teaching-evidence-overclaim`

Learning And Teaching: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

### `failure-learning-teaching-premature-action`

Learning And Teaching: Premature Action. explanation volume substitutes for active practice and feedback

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

### `failure-learning-teaching-silent-failure`

Learning And Teaching: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

### `failure-learning-teaching-stale-context`

Learning And Teaching: Stale Context. The state of learning plan, worked examples, practice evidence, and reflection record changed while routing continued from a stale checkpoint.

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

### `failure-learning-teaching-unbounded-loop`

Learning And Teaching: Unbounded Repair Loop. Failures do not trigger a reset of explanation volume substitutes for active practice and feedback.

**Domain delta:** In Learning And Teaching, this failure threatens 'teaching increases the learner's capability rather than only producing an answer' through explanation volume substitutes for active practice and feedback.

## Signal (4)

### `signal-learning-teaching-constraint-risk`

Learning And Teaching: Constraint Or Risk Signal. A current constraint or risk threatens 'teaching increases the learner's capability rather than only producing an answer' for human or agent learning objective and skill development.

**Domain delta:** For Learning And Teaching, this signal observes human or agent learning objective and skill development through learning plan, worked examples, practice evidence, and reflection record while rejecting stale or untrusted substitutes.

### `signal-learning-teaching-explicit-mission`

Learning And Teaching: Explicit Mission Signal. The current user request explicitly concerns human or agent learning objective and skill development and states an observable outcome.

**Domain delta:** For Learning And Teaching, this signal observes human or agent learning objective and skill development through learning plan, worked examples, practice evidence, and reflection record while rejecting stale or untrusted substitutes.

### `signal-learning-teaching-repository-evidence`

Learning And Teaching: Repository Evidence Signal. Current source or accepted documentation identifies learning plan, worked examples, practice evidence, and reflection record as the owning surface for human or agent learning objective and skill development.

**Domain delta:** For Learning And Teaching, this signal observes human or agent learning objective and skill development through learning plan, worked examples, practice evidence, and reflection record while rejecting stale or untrusted substitutes.

### `signal-learning-teaching-runtime-failure`

Learning And Teaching: Runtime Failure Signal. A reproducible observation shows explanation volume substitutes for active practice and feedback in learning plan, worked examples, practice evidence, and reflection record.

**Domain delta:** For Learning And Teaching, this signal observes human or agent learning objective and skill development through learning plan, worked examples, practice evidence, and reflection record while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-learning-teaching-escalate-and-contain`

Learning And Teaching: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reduce the concept, observe an attempt, give one targeted cue, and retest transfer to the accountable owner

**Domain delta:** For Learning And Teaching, recovery targets explanation volume substitutes for active practice and feedback in learning plan, worked examples, practice evidence, and reflection record and exits only with retrieval, transfer, error patterns, confidence calibration, and independent performance.

### `recovery-learning-teaching-isolate-and-repair`

Learning And Teaching: Isolate And Repair. Reduce to the smallest failing path in learning plan, worked examples, practice evidence, and reflection record Apply one bounded repair Run retrieval, transfer, error patterns, confidence calibration, and independent performance Check adjacent invariants

**Domain delta:** For Learning And Teaching, recovery targets explanation volume substitutes for active practice and feedback in learning plan, worked examples, practice evidence, and reflection record and exits only with retrieval, transfer, error patterns, confidence calibration, and independent performance.

### `recovery-learning-teaching-reset-and-reconstruct`

Learning And Teaching: Reset And Reconstruct. Stop mutation Re-read learning plan, worked examples, practice evidence, and reflection record and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Learning And Teaching, recovery targets explanation volume substitutes for active practice and feedback in learning plan, worked examples, practice evidence, and reflection record and exits only with retrieval, transfer, error patterns, confidence calibration, and independent performance.

## Decision (2)

### `decision-learning-teaching-build-versus-test`

Learning And Teaching: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Learning And Teaching, this model decides explain, demonstrate, scaffold, practice, retrieve, reflect, or pause using retrieval, transfer, error patterns, confidence calibration, and independent performance and the constraint 'teaching increases the learner's capability rather than only producing an answer'.

### `decision-learning-teaching-local-versus-systemic`

Learning And Teaching: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening teaching increases the learner's capability rather than only producing an answer.

**Domain delta:** For Learning And Teaching, this model decides explain, demonstrate, scaffold, practice, retrieve, reflect, or pause using retrieval, transfer, error patterns, confidence calibration, and independent performance and the constraint 'teaching increases the learner's capability rather than only producing an answer'.

## Mental Model (2)

### `mental-model-learning-teaching-feedback-loop`

Learning And Teaching: Feedback Loop. Actions on human or agent learning objective and skill development change learning plan, worked examples, practice evidence, and reflection record, which changes the next evidence and decision environment.

**Domain delta:** For Learning And Teaching, this model maps learning strengthens through retrieval, spacing, feedback, varied practice, and manageable cognitive load onto human or agent learning objective and skill development and learning plan, worked examples, practice evidence, and reflection record.

### `mental-model-learning-teaching-weakest-link`

Learning And Teaching: Weakest Link And Bottleneck. End-to-end quality for human or agent learning objective and skill development is limited by the least trustworthy boundary in the path through learning plan, worked examples, practice evidence, and reflection record.

**Domain delta:** For Learning And Teaching, this model maps learning strengthens through retrieval, spacing, feedback, varied practice, and manageable cognitive load onto human or agent learning objective and skill development and learning plan, worked examples, practice evidence, and reflection record.

## Governance (1)

### `governance-learning-teaching-evidence-authority-policy`

Learning And Teaching: Evidence And Authority Policy. Work on human or agent learning objective and skill development must preserve 'teaching increases the learner's capability rather than only producing an answer', cite retrieval, transfer, error patterns, confidence calibration, and independent performance, and remain within 'educational support versus diagnosis, grading authority, and doing all work for the learner'.

**Domain delta:** For Learning And Teaching, this policy enforces the system diagnoses ability or health, shames mistakes, or claims learning without performance evidence at educational support versus diagnosis, grading authority, and doing all work for the learner.

