---
name: "Feature Discovery"
description: "Understand one feature end to end before coding: owning paths, flows, dependencies, state, risks, and acceptance criteria."
argument-hint: "Feature or behavior to discover"
agent: "agent"
---

# Feature Discovery

Act as the project intelligence team. This is read-only: do not edit code, docs, configuration, or data.

Read-only discovery may precede a decision when repository evidence is needed to evaluate the idea. It never authorizes planning or implementation. If no `STOP / DEFER / TEST / BUILD` state exists, finish with the evidence needed by Idea Validation and explicitly route the mission there; do not declare the feature ready to build.

Read [project instructions](../../AGENTS.md), [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [thinking framework](../../AEOS/00-foundation/THINKING_FRAMEWORK.md), [product system](../../AEOS/01-product/PRODUCT_SYSTEM.md), and the project profile.

Investigate the feature from its real entrypoint to its final side effects. Map:

- user goal and current user journey
- product promise, maturity stage, evidence that the problem exists, and consequence of doing nothing
- owning files, symbols, modules, and call chain
- inputs, outputs, state, persistence, and external dependencies
- data flow and mutation points
- trust boundaries and sensitive data
- loading, empty, error, permission, offline, restart, duplicate, and time-based states
- existing tests, docs, known limitations, and conflicting behavior
- facts versus assumptions and unresolved questions
- removal, simplification, manual, documentation, and platform alternatives when they could avoid product surface
- implementation, recurring, support, migration, privacy, and opportunity-cost signals visible in the repository

Do not map unrelated repository areas. Follow the controlling path until evidence is sufficient.

Label material findings `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`. Use repository history only when it can answer a concrete design-intent question; do not invent the original author's reasons.

Output:

1. Feature summary and current behavior
2. User-flow and system-flow maps
3. Dependency and data/state maps
4. Invariants and failure modes
5. Evidence with file/symbol references
6. Gaps, contradictions, and unknowns
7. Acceptance criteria
8. Decision unknowns, no-build/simpler alternatives, and cheapest falsification test
9. Definition of Ready verdict