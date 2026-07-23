# AEOS Manifest

AEOS is a connected knowledge graph, not a pile of documents.

## Graph

```text
Foundation
-> Decision Intelligence
-> Operating Model
-> Idea Validation
-> Product
-> Engineering Economics
-> Engineering
-> Architecture
-> Security
-> Performance
-> Testing
-> AI Agentic Coding
-> Loop Engineering Runtime
-> Context And Memory
-> Behavior System And Context Routing
-> Documentation
-> Review
-> Playbooks
-> Project Profile
```

## Document Contract

Every AEOS document should answer:

- What problem does this govern?
- What principles apply?
- What decisions does it constrain?
- What artifacts should be produced?
- What quality gate proves compliance?
- What related documents should be read next?

## Evolution Rules

- Add new documents only when a repeated decision needs a durable standard.
- Keep documents composable.
- Link related documents.
- Update the project profile when repository-specific reality changes.
- Remove rules that become false.
- Prefer precise standards over motivational slogans.

## Core AI Documents

- [10-ai/AI_BEHAVIOR_MODES.md](10-ai/AI_BEHAVIOR_MODES.md)
- [10-ai/AGENTIC_CODING.md](10-ai/AGENTIC_CODING.md)
- [10-ai/LOOP_ENGINEERING.md](10-ai/LOOP_ENGINEERING.md)
- [10-ai/CONTEXT_AND_MEMORY.md](10-ai/CONTEXT_AND_MEMORY.md)
- [16-behavior-system/README.md](16-behavior-system/README.md)
- [16-behavior-system/RUNTIME_AND_ACTIVATION.md](16-behavior-system/RUNTIME_AND_ACTIVATION.md)

## Core Operating Documents

- [00-foundation/OPERATING_MODEL.md](00-foundation/OPERATING_MODEL.md)
- [00-foundation/COMMUNICATION_FRAMEWORK.md](00-foundation/COMMUNICATION_FRAMEWORK.md)

## Core Decision Documents

- [00-foundation/DECISION_INTELLIGENCE.md](00-foundation/DECISION_INTELLIGENCE.md)
- [00-foundation/DECISION_FRAMEWORK.md](00-foundation/DECISION_FRAMEWORK.md)
- [01-product/IDEA_VALIDATION.md](01-product/IDEA_VALIDATION.md)
- [02-engineering/ENGINEERING_ECONOMICS.md](02-engineering/ENGINEERING_ECONOMICS.md)
- [12-templates/MISSION_BRIEF_TEMPLATE.md](12-templates/MISSION_BRIEF_TEMPLATE.md)

## Core Prompt System

- [11-prompts/PROMPT_LIBRARY.md](11-prompts/PROMPT_LIBRARY.md)
- [../.github/prompts/engineering-coordinator.prompt.md](../.github/prompts/engineering-coordinator.prompt.md)
- [../.github/prompts/idea-validation.prompt.md](../.github/prompts/idea-validation.prompt.md)
- [../.github/prompts/decision-challenge.prompt.md](../.github/prompts/decision-challenge.prompt.md)
- [../.github/prompts/backlog-prioritization.prompt.md](../.github/prompts/backlog-prioritization.prompt.md)
- [../.github/prompts/minimal-implementation.prompt.md](../.github/prompts/minimal-implementation.prompt.md)
- [../.github/prompts/completion-audit.prompt.md](../.github/prompts/completion-audit.prompt.md)

## Core Review Documents

- [13-review/QUALITY_GATES.md](13-review/QUALITY_GATES.md)
- [13-review/CODE_REVIEW_STANDARD.md](13-review/CODE_REVIEW_STANDARD.md)
- [13-review/PRODUCTION_READINESS_CHECKLIST.md](13-review/PRODUCTION_READINESS_CHECKLIST.md)
- [13-review/BEHAVIOR_SYSTEM_QUALITY_GATES.md](13-review/BEHAVIOR_SYSTEM_QUALITY_GATES.md)

## Core Recovery Documents

- [14-playbooks/GIT_RECOVERY_PLAYBOOK.md](14-playbooks/GIT_RECOVERY_PLAYBOOK.md)
- [12-templates/RUNBOOK_TEMPLATE.md](12-templates/RUNBOOK_TEMPLATE.md)
- [12-templates/POSTMORTEM_TEMPLATE.md](12-templates/POSTMORTEM_TEMPLATE.md)

## Executable Pilots

- [14-playbooks/AEOS_VALIDATION_LOOP_PILOT.md](14-playbooks/AEOS_VALIDATION_LOOP_PILOT.md)
- [14-playbooks/contracts/aeos-governance-validation.loop.json](14-playbooks/contracts/aeos-governance-validation.loop.json)

## Core Engineering Documents

- [02-engineering/ENGINEERING_STANDARD.md](02-engineering/ENGINEERING_STANDARD.md)
- [02-engineering/DATABASE_GUIDELINES.md](02-engineering/DATABASE_GUIDELINES.md)
- [02-engineering/STATE_MANAGEMENT.md](02-engineering/STATE_MANAGEMENT.md)
- [02-engineering/API_STANDARDS.md](02-engineering/API_STANDARDS.md)

## Core Security Documents

- [07-security/SECURITY_STANDARD.md](07-security/SECURITY_STANDARD.md)
- [07-security/HIGH_STAKES_MOBILE_SYSTEMS.md](07-security/HIGH_STAKES_MOBILE_SYSTEMS.md)
- [14-playbooks/SECURITY_REVIEW_PLAYBOOK.md](14-playbooks/SECURITY_REVIEW_PLAYBOOK.md)

## Core Documentation Documents

- [04-documentation/DOCUMENTATION_STANDARD.md](04-documentation/DOCUMENTATION_STANDARD.md)
- [04-documentation/README_SYSTEM.md](04-documentation/README_SYSTEM.md)

## Validation

Run `..\scripts\validate-aeos.ps1` from the `AEOS` directory or `.\scripts\validate-aeos.ps1` from the repository root after changing governance documents or workspace prompts.
