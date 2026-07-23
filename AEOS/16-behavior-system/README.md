# AEOS Behavior System

The AEOS Behavior System is a model-agnostic specification for selecting a small, relevant set of engineering behaviors from a broad reviewed catalog.

It does not place the full catalog into every prompt. The always-active kernel remains the [AI Constitution](../00-foundation/AI_CONSTITUTION.md), [Operating Model](../00-foundation/OPERATING_MODEL.md), [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md), [Context And Memory System](../10-ai/CONTEXT_AND_MEMORY.md), and [Quality Gates](../13-review/QUALITY_GATES.md). This system adds typed knowledge, deterministic routing, bounded context packs, and auditable activation records under those authorities.

## Required Catalog Floors

| Kind | Minimum canonical active items |
|---|---:|
| Knowledge domains | 50 |
| Engineering behaviors | 500 |
| Failure patterns | 300 |
| Context signals | 200 |
| Recovery strategies | 150 |
| Decision models | 100 |
| Mental models | 100 |
| Collaboration patterns | 75 |
| Governance policies | 50 |

These are quality floors, not permission to manufacture aliases or permutations. Only explicitly bound, active, reviewed, provenance-bearing items count.

## Source And Derived Artifacts

```text
source/catalog-blueprint.json
  -> scripts/validate-aeos-behavior-system.ps1
  -> scripts/generate-aeos-behavior-system.ps1
  -> generated/COUNTS.json
  -> generated/INDEX.md
  -> generated/domains/*.md
```

The JSON catalog is authoritative. Generated files are derived indexes and never count as additional knowledge items. Generation must be deterministic and may write only under `generated/`.

## Runtime Boundary

A context resolution may activate at most:

- two knowledge domains;
- twelve catalog items;
- one primary collaboration mode;
- one governance overlay in addition to always-active kernel rules.

Ambiguous routing, missing evidence, stale signals, authority conflicts, or budget overflow fail closed. Catalog entries may narrow authority but can never grant tools, mutations, deployment rights, destructive actions, or risk acceptance beyond the kernel and project overlays.

## Claims

Keep these claims separate:

1. **Structurally valid**: schema, references, counts, relationships, and deterministic generation pass.
2. **Semantically reviewed**: item-level editorial review found the distinctions useful and non-duplicative.
3. **Runtime verified**: positive, negative, ambiguous, malicious, stale, and budget-exhausted routing fixtures pass.

None of these claims implies the next.

## Related

- [Catalog Contract](CATALOG_CONTRACT.md)
- [Runtime And Activation](RUNTIME_AND_ACTIVATION.md)
- [Catalog Quality Gates](../13-review/BEHAVIOR_SYSTEM_QUALITY_GATES.md)
- [Catalog Authoring Playbook](../14-playbooks/BEHAVIOR_SYSTEM_AUTHORING_PLAYBOOK.md)
