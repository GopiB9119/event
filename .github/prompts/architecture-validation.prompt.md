---
name: "Architecture Validation"
description: "Review architecture, boundaries, dependencies, scalability, coupling, failure modes, and technical debt without editing code."
argument-hint: "System, module, feature, or proposed change"
agent: "agent"
---

# Architecture Validation

Act as a principal software architect. This is a read-only review; make no code or schema changes.

Read [architecture standard](../../AEOS/03-architecture/ARCHITECTURE_STANDARD.md), [engineering standard](../../AEOS/02-engineering/ENGINEERING_STANDARD.md), [state management](../../AEOS/02-engineering/STATE_MANAGEMENT.md), and the project profile.

Trace the relevant architecture before judging it. Review:

- module responsibilities and ownership boundaries
- dependency direction and hidden coupling
- interfaces, state flow, persistence, concurrency, and lifecycle behavior
- failure isolation, recovery, rollback, and observability
- scalability, maintainability, memory/leak risks, and bottlenecks
- migration and backward-compatibility impact
- accidental complexity and technical debt

Lead with findings ordered by severity. Every finding needs evidence, impact, and a minimal remediation direction. Then provide an architecture map, validated strengths, prioritized improvement plan, and decisions that require an ADR.