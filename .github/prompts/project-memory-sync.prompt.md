---
name: "Project Memory Sync"
description: "Synchronize verified architecture, decisions, risks, commands, milestones, and knowledge maps into durable project memory."
argument-hint: "Session, feature, decision, or repository scope to synchronize"
agent: "agent"
---

# Project Memory And Knowledge Graph Sync

Act as the project memory steward. Preserve verified knowledge so future agents do not rediscover or contradict the project.

Read repository instructions, `/memories/repo/project-decisions.md`, [project profile](../../AEOS/project/PROJECT_PROFILE.md), [project knowledge graph](../../AEOS/project/KNOWLEDGE_GRAPH.md) when present, architecture/docs, current implementation, validation output, and recent decisions.

Build or refresh a concise project knowledge graph covering:

- product idea, target user, current milestone, and definition of done
- modules/screens, ownership, dependencies, interfaces, and entrypoints
- entities, persistence, state, data flow, and external services
- build/test/deploy commands and evidence quality
- architecture/security/product decisions with rationale
- decision states, evidence labels, dangerous assumptions, rejected alternatives, review triggers, and what would change each material decision
- known bugs, limitations, risks, forbidden patterns, and unresolved unknowns
- successful fixes and recurring failure patterns

Only store durable, verified facts. Preserve `ASSUMPTION` and `UNKNOWN` labels when uncertainty itself is durable; never save them as fact. Do not copy temporary logs, speculative plans, private data, credentials, or conversation narration. Remove or replace stale memory when evidence disproves it.

Update project memory, project profile, and knowledge graph only when reality changed. Report exactly what was added, changed, removed, what remains uncertain, and the evidence date/source for material updates.