# AEOS Behavior Catalog Index

Generated deterministically from the reviewed catalog blueprint. Generated rows are derived indexes; canonical counting semantics are governed by the catalog contract.

- **Catalog version:** `1.0.0`
- **Catalog SHA-256:** `4f1b95ffbc83a225920624a2dfea59124d3e6a9391cc9bbbc20f302ad3c25072`
- **Domains:** 50
- **Behaviors:** 500
- **Failure patterns:** 300
- **Context signals:** 200
- **Recovery strategies:** 150
- **Decision models:** 100
- **Mental models:** 100
- **Collaboration patterns:** 75
- **Governance policies:** 50

## Domains

| Domain | Purpose | Items |
|---|---|---:|
| [Accessibility](domains/accessibility.md) | Ensure people with diverse abilities can perceive, understand, navigate, and operate the product. | 28 |
| [AI Agent Engineering](domains/ai-agent-engineering.md) | Design bounded agents that reason, use tools, collaborate, stop, and report evidence predictably. | 28 |
| [AI And Engineering Governance](domains/governance.md) | Define authority, approval, audit, exceptions, lifecycle, and accountability outside model prompts. | 28 |
| [API And Interface Contracts](domains/api-contracts.md) | Make service and module interfaces explicit, bounded, versioned, and failure-aware. | 28 |
| [Business Strategy](domains/business-strategy.md) | Connect engineering choices to sustainable value, positioning, and opportunity cost. | 28 |
| [Codebase Navigation](domains/codebase-navigation.md) | Find the owning implementation, callers, tests, history, and constraints before editing. | 28 |
| [Cognitive And Human Factors](domains/cognitive-human-factors.md) | Adapt systems and workflows to attention, memory, fatigue, cognitive load, bias, and real human capability. | 28 |
| [Compliance And Legal Readiness](domains/compliance.md) | Map applicable obligations to owned controls and qualified approval without inventing legal conclusions. | 28 |
| [Context Engineering](domains/context-engineering.md) | Provide sufficient, relevant, fresh, isolated, economical, and provenance-bearing context to reasoning systems. | 28 |
| [Cost And Engineering Economics](domains/cost-economics.md) | Optimize total outcome cost across tokens, developer time, cloud, maintenance, support, and opportunity cost. | 28 |
| [Data Engineering](domains/data-engineering.md) | Design traceable ingestion, transformation, quality, lineage, and lifecycle for analytical or operational data. | 28 |
| [Database Integrity](domains/database-integrity.md) | Preserve ownership, constraints, transactions, migrations, and recoverability of persisted state. | 28 |
| [Debugging And Root Cause Analysis](domains/debugging.md) | Reproduce, classify, isolate, explain, and repair the first causal failure rather than its final symptom. | 28 |
| [Decision Science](domains/decision-science.md) | Make explicit choices under uncertainty using evidence, alternatives, value, risk, and reversibility. | 28 |
| [Delivery Management](domains/delivery-management.md) | Move accepted work through implementation, verification, release, and learning without hidden queues. | 28 |
| [DevOps And Operations](domains/devops-operations.md) | Operate build, deployment, configuration, and service workflows safely and repeatably. | 28 |
| [Distributed Systems](domains/distributed-systems.md) | Reason explicitly about partial failure, consistency, ordering, retries, identity, and network partitions. | 28 |
| [Documentation Engineering](domains/documentation.md) | Preserve accurate, navigable, audience-specific knowledge that remains useful after the conversation. | 28 |
| [Environment And Hardware Awareness](domains/environment-hardware.md) | Adapt engineering strategy to actual OS, hardware, storage, network, tools, and developer capacity. | 28 |
| [Ethics And Trust Engineering](domains/ethics-trust.md) | Design systems and collaboration that are honest, contestable, fair, accountable, and worthy of reliance. | 28 |
| [Frontend And User Experience](domains/frontend-ux.md) | Make critical user workflows understandable, efficient, responsive, and honest across states. | 28 |
| [Human-AI Collaboration](domains/human-collaboration.md) | Build shared understanding, constructive challenge, calibrated trust, and adaptive working relationships. | 28 |
| [Implementation](domains/implementation.md) | Translate an accepted design into the smallest readable, testable, maintainable change. | 28 |
| [Incident Response](domains/incident-response.md) | Detect, contain, communicate, recover, and learn from active production or data-integrity harm. | 28 |
| [Intent Understanding](domains/intent-understanding.md) | Recover the real requested outcome and protect it from conversational drift. | 28 |
| [Learning And Teaching](domains/learning-teaching.md) | Develop durable understanding and expertise through evidence, practice, feedback, and reflection. | 28 |
| [Memory And Knowledge Management](domains/memory-knowledge.md) | Preserve durable verified decisions and lessons while expiring stale or sensitive derived knowledge. | 28 |
| [Mobile Engineering](domains/mobile-engineering.md) | Design for device lifecycle, permissions, offline behavior, battery, storage, upgrades, and platform variability. | 28 |
| [Model Routing](domains/model-routing.md) | Select the least costly capable model or deterministic tool for each bounded reasoning need. | 28 |
| [Observability](domains/observability.md) | Make system state, failures, latency, cost, and agent behavior diagnosable without exposing sensitive content. | 28 |
| [Performance Engineering](domains/performance.md) | Measure and improve user-relevant latency, throughput, memory, energy, and capacity without guessing. | 28 |
| [Privacy Engineering](domains/privacy.md) | Minimize personal data and make collection, use, retention, access, and deletion proportionate and transparent. | 28 |
| [Product Discovery](domains/product-discovery.md) | Establish whether a real user problem deserves product effort. | 28 |
| [Project Planning](domains/project-planning.md) | Sequence bounded work around dependencies, risks, owners, and acceptance evidence. | 28 |
| [Prompt And Specification Engineering](domains/prompt-specification.md) | Encode focused task contracts and machine-readable rules without burying authority or context. | 28 |
| [Refactoring](domains/refactoring.md) | Improve internal structure while preserving externally observable behavior. | 28 |
| [Release Engineering](domains/release-engineering.md) | Produce traceable, reproducible, correctly identified artifacts and gate publication separately. | 28 |
| [Reliability Engineering](domains/reliability.md) | Keep critical user outcomes correct and available across faults, restarts, retries, and degradation. | 28 |
| [Requirement Engineering](domains/requirement-engineering.md) | Translate desired outcomes into testable requirements, non-requirements, and acceptance evidence. | 28 |
| [Risk And Safety Engineering](domains/risk-safety.md) | Identify hazards, controls, failure costs, and safe states before high-impact work proceeds. | 28 |
| [Security Engineering](domains/security.md) | Protect assets by designing threat-aware boundaries, least privilege, and fail-closed controls. | 28 |
| [Software Architecture](domains/software-architecture.md) | Define durable module boundaries, responsibilities, interfaces, and tradeoffs before scale. | 28 |
| [Startup Execution](domains/startup-execution.md) | Protect a young product from feature addiction, drift, and premature scale. | 28 |
| [Systems Engineering](domains/systems-engineering.md) | Optimize end-to-end lifecycle outcomes across components, humans, operations, and retirement. | 28 |
| [Team Coordination](domains/team-coordination.md) | Coordinate owners, dependencies, independent review, conflicts, and handoffs across a team. | 28 |
| [Technical Communication](domains/communication.md) | Exchange decisions, evidence, uncertainty, and action clearly for the intended audience. | 28 |
| [Testing](domains/testing.md) | Design layered checks that falsify defects and protect important behavior over time. | 28 |
| [Tool Orchestration](domains/tool-orchestration.md) | Choose, parameterize, sequence, observe, and stop tools safely according to their real capabilities. | 28 |
| [User Research](domains/user-research.md) | Ground design and prioritization in observed user context rather than imagined personas. | 28 |
| [Verification And Completion](domains/verification.md) | Require reproducible evidence before upgrading a claim from implemented to complete. | 28 |

## Runtime

The resolver loads at most two domains and twelve items. It accepts typed trusted signal IDs only and fails closed on ambiguity, stale or untrusted input, missing references, authority conflict, or budget overflow.

