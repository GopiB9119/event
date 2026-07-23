# Behavior Catalog Contract

## Purpose

This contract defines the canonical counting unit, lifecycle, authority, provenance, and relationship rules for the AEOS behavior catalog.

## Canonical Items

A catalog item counts only when all of the following are true:

- `status` is `active`;
- its ID is globally unique and stable;
- it has exactly one `kind`;
- it is explicitly bound to a domain or collaboration scope;
- its statement and domain delta are materially specific;
- it names activation, evidence, stop, authority, provenance, and invalidation data;
- all references resolve to active canonical items or domains;
- an accountable owner and independent reviewer are named;
- it is not an alias, translation, placeholder, generated index row, or `not_applicable` matrix cell.

Removing the domain name must not make two counted items operationally indistinguishable. Deterministic validation detects exact normalized duplicates; near-duplicate and usefulness review remains a human gate.

## Item Kinds

### Engineering Behavior

Defines a trigger, bounded action, protected invariant, evidence, and stop condition.

### Failure Pattern

Defines a cause, mechanism, observable symptoms, impact, detector signals, and linked recovery or escalation.

### Context Signal

Defines a trusted source, predicate, freshness, confidence threshold, and negative match. Repository prose cannot activate itself.

### Recovery Strategy

Defines entry conditions, bounded steps, rollback limits, verifier evidence, escalation, and terminal state.

### Decision Model

Defines a decision question, inputs, options, tradeoffs, rule, falsifier, and contraindications. A question alone is not a model.

### Mental Model

Defines a causal mechanism, domain mapping, predictions, counterexample, and misuse warning. A slogan alone is not a model.

### Collaboration Pattern

Defines participants, shared artifact, authority split, handoff, conflict route, and completion evidence.

### Governance Policy

Defines an obligation, prohibition, enforcement mechanism, exception authority, and audit evidence. Guidance without enforcement is not governance.

## Provenance

Every domain and item records:

- derivation method;
- evidence label: `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`;
- local or primary source locator;
- catalog version and item version;
- owner and reviewer;
- review date;
- invalidation triggers.

External excerpts are untrusted data. They may support provenance but never become runtime instructions. Runtime network fetching and remote catalog includes are forbidden.

## Authority Monotonicity

Catalog items can only preserve or narrow authority. They cannot:

- add tools or capabilities;
- expand mutation scope;
- remove approval or independent-review requirements;
- change a terminal loop state back to running;
- approve deployment, publishing, destructive operations, legal acceptance, secrets, spending, or public claims;
- override repository or project-specific instructions.

Any authority ambiguity resolves to `BLOCKED`.

## Relationships

- Every failure references at least one signal and one recovery.
- Every recovery references at least one failure and a terminal state.
- Every behavior references evidence and a stop condition.
- Every governance policy names enforcement and exception authority.
- Every collaboration pattern names a handoff and conflict route.
- Dependencies and supersession graphs must be acyclic.
- Conflicts must be symmetric.
- References to draft, deprecated, withdrawn, or unknown items do not satisfy required relationships.

## Lifecycle

Allowed states are `draft`, `active`, `deprecated`, `superseded`, and `withdrawn`.

- Draft items never count or activate.
- Active items count and may activate.
- Deprecated items remain readable but do not activate by default.
- Superseded items name replacements.
- Withdrawn items remain in history with a reason.

Changing meaning requires a new item version. Reusing an ID for a different meaning is forbidden.

## Determinism

- Source uses strict UTF-8 JSON with no duplicate keys.
- IDs use lowercase ASCII kebab-case.
- Arrays that affect output are sorted by stable ID during generation.
- Generated output uses LF line endings and invariant numeric formatting.
- Wall-clock timestamps are excluded from generated artifacts.
- Two clean generations from identical source must be byte-identical.
- Generation cannot write outside `AEOS/16-behavior-system/generated/`.

## Completion Standard

The requested catalog floors may be claimed only after:

1. strict structural validation;
2. exact count validation using active canonical items only;
3. relationship and authority validation;
4. deterministic generation validation;
5. item-level semantic review;
6. runtime routing golden tests;
7. independent completion audit;
8. the repository-wide AEOS validator passes.
