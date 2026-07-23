# Behavior System Authoring Playbook

## Mission

Add or change behavior-system knowledge without inflating counts, weakening authority, creating prompt injection paths, or loading irrelevant context into every task.

## Entry Requirements

- A repeated engineering decision, failure, or context-routing need is evidenced.
- The owning domain and accountable owner are known.
- The proposed item kind is unambiguous.
- Existing items were searched for semantic overlap.
- A verifier or review method exists.

If these conditions are not met, keep the proposal as a draft note and do not count or activate it.

## Authoring Sequence

1. State the observed problem and exact evidence.
2. Choose one canonical kind using the [Catalog Contract](../16-behavior-system/CATALOG_CONTRACT.md).
3. Identify the domain-specific operational delta.
4. Define activation and explicit negative signals.
5. Define authority ceiling, protected invariants, and stop conditions.
6. Add type-specific mechanism and evidence fields.
7. Add provenance, owner, reviewer, review date, and invalidation triggers.
8. Connect failures, signals, recoveries, behaviors, decisions, and policies.
9. Run focused schema and relationship validation.
10. Run exact and near-duplicate review.
11. Add positive and negative routing fixtures when activation changes.
12. Regenerate derived documentation twice and compare bytes.
13. Run behavior-system self-tests and repository AEOS validation.
14. Request independent semantic review.

## Editorial Test

Ask:

> If the domain name is removed, does this item still prescribe a materially different action, detector, mechanism, decision rule, or obligation?

If not, merge, specialize, or reject it. Do not preserve it merely to satisfy a count floor.

## Failure Authoring

A failure requires:

- initiating cause;
- failure mechanism;
- observable symptoms;
- affected invariant and impact;
- detector signal IDs;
- recovery IDs or explicit escalation.

Do not define a failure solely as the absence of its corresponding behavior.

## Governance Authoring

A policy requires:

- obligation;
- prohibition;
- deterministic or reviewable enforcement;
- named exception authority;
- auditable evidence.

Catalog content cannot grant an exception. Only the named authority can approve one under the Operating Model.

## Deprecation

1. Mark the old item deprecated or superseded.
2. Preserve its ID and history.
3. Name replacement IDs and reason.
4. Update references and golden cases.
5. Verify old activation records remain interpretable.
6. Never silently reuse the old ID for new meaning.

## Batch Limits

Review catalog expansion in bounded batches. A batch should contain one coherent domain group and must pass structural, semantic, relationship, authority, provenance, runtime, and deterministic gates before the next batch begins.

## Stop Conditions

Stop and return to design when:

- three proposed items fail the editorial distinction test;
- a new kind overlaps an existing kind;
- count pressure drives placeholder or alias creation;
- routing requires untrusted free text;
- authority cannot be proven monotonic;
- generation becomes nondeterministic;
- no independent reviewer is available for a material batch.
