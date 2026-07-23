# Prompt Library

Workspace prompt files live in [../../.github/prompts](../../.github/prompts/). They are focused operating modes, not one oversized instruction block.

## Operating Rules

- One prompt owns one job.
- Roles are capability assignments governed by the Operating Model, not product-specific personas.
- The coordinator owns the mission and final judgment.
- Material product/architecture work passes Decision Intelligence before implementation planning.
- `STOP`, `DEFER`, and `TEST` are valid outcomes; code volume is not success.
- Discovery and audit prompts are read-only.
- Only Minimal Implementation owns code changes by default.
- Completion, release, and deployment require evidence.
- Deployment, publishing, tagging, pushing, and destructive actions require explicit approval.
- Project Memory Sync stores only durable verified facts.
- Repeated or unattended work requires the Loop Engineering contract and terminal states.
- Derived memory and repository indexes must preserve provenance and route agents back to current evidence.
- Behavior-system routing selects at most two domains and twelve items from typed trusted signals; the full catalog is never an always-on prompt.

## Invocation

In VS Code Chat, type `/`, select a workspace prompt, and provide the task described by its `argument-hint`. The same prompts are available through `Chat: Run Prompt...`.

Workspace prompts use invocation text directly. Do not add unverified `${input:...}` variables; this repository targets the prompt syntax documented by the installed VS Code customization reference.

## Verified Editor Support

The installed VS Code build was inspected directly:

- `.github/prompts` is a built-in workspace prompt location.
- `workbench.action.chat.run.prompt` is registered for running prompt files.
- `workbench.action.chat.configure.prompts` is registered for prompt configuration.
- `chat.promptFilesLocations` supports additional prompt locations when needed.
- GitHub Copilot's `github.copilot.chat.promptFileContextProvider.enabled` setting defaults to `true`.

This workspace uses the built-in location, so no `.vscode/settings.json` override is required. Prompt-picker rendering still requires a manual `/` check because the current automation tools cannot open that UI in an existing workspace.

## Mutation Boundaries

| Role | Default authority |
|---|---|
| Engineering Coordinator | Orchestrates the mission; enters Minimal Implementation explicitly when changes are required |
| Minimal Implementation | May make the smallest accepted code/config/docs change and must validate immediately |
| Project Memory Sync | May update durable project memory and project profile only when evidence changed |
| Discovery, research, planning, review, and specialist audits | Read-only |
| Completion Audit | Read-only independent verification |
| Release Manager | May prepare evidence and release artifacts; cannot publish, tag, push, or deploy without approval |
| DevOps Deployment Gate | Readiness and runbook only; cannot mutate infrastructure or deploy without approval |

## Recommended Sequence

```text
Engineering Coordinator
-> Idea Validation when value or direction is uncertain
-> Independent Decision Challenge for irreversible/high-risk decisions or assumption-dependent BUILD verdicts
-> Feature Discovery / Product Understanding
-> Backlog Prioritization when multiple items compete
-> Architecture and applicable specialist audits
-> Feature Planning
-> Minimal Implementation
-> Senior Code Review / Test Strategy QA
-> Production Scenario Walkthrough for material user/release flows
-> Completion Audit
-> Documentation Steward / Project Memory Sync
-> Release Manager
-> DevOps Deployment Gate (only when approved)
```

Root Cause Debugging replaces discovery when the mission begins with a failure. Production Pattern Research is used only when an external framework or product pattern materially affects the decision.

Self-review does not replace independent challenge. The author of a material decision cannot certify its own evidence quality as the only gate to implementation.

## Decide And Prioritize

- [Idea Validation](../../.github/prompts/idea-validation.prompt.md)
- [Decision Challenge](../../.github/prompts/decision-challenge.prompt.md)
- [Backlog Prioritization](../../.github/prompts/backlog-prioritization.prompt.md)

## Coordination

- [Engineering Coordinator](../../.github/prompts/engineering-coordinator.prompt.md)
- [Behavior System Audit](../../.github/prompts/behavior-system-audit.prompt.md)

## Understand And Plan

- [Feature Discovery](../../.github/prompts/feature-discovery.prompt.md)
- [Product Understanding](../../.github/prompts/product-understanding.prompt.md)
- [Architecture Validation](../../.github/prompts/architecture-validation.prompt.md)
- [Feature Planning](../../.github/prompts/feature-planning.prompt.md)
- [Production Pattern Research](../../.github/prompts/production-pattern-research.prompt.md)

## Diagnose, Implement, And Verify

- [Root Cause Debugging](../../.github/prompts/root-cause-debugging.prompt.md)
- [Minimal Implementation](../../.github/prompts/minimal-implementation.prompt.md)
- [Senior Code Review](../../.github/prompts/senior-code-review.prompt.md)
- [Completion Audit](../../.github/prompts/completion-audit.prompt.md)
- [Production Scenario Walkthrough](../../.github/prompts/production-scenario-walkthrough.prompt.md)

## Specialist Audits

- [Security Threat Model](../../.github/prompts/security-threat-model.prompt.md)
- [Performance Reliability Audit](../../.github/prompts/performance-reliability-audit.prompt.md)
- [Database Integrity Audit](../../.github/prompts/database-integrity-audit.prompt.md)
- [API Contract Audit](../../.github/prompts/api-contract-audit.prompt.md)
- [Test Strategy QA](../../.github/prompts/test-strategy-qa.prompt.md)
- [Frontend UX Accessibility Audit](../../.github/prompts/frontend-ux-accessibility-audit.prompt.md)

## Operations And Knowledge

- [DevOps Deployment Gate](../../.github/prompts/devops-deployment-gate.prompt.md)
- [Documentation Steward](../../.github/prompts/documentation-steward.prompt.md)
- [Release Manager](../../.github/prompts/release-manager.prompt.md)
- [Project Memory Sync](../../.github/prompts/project-memory-sync.prompt.md)

## Definition Of Done

A mission is complete only when requirements are mapped to evidence, the smallest safe change exists, focused and repository-required checks pass, security/privacy and data risks are reviewed, documentation and memory match reality, rollback limits are clear, residual risk is named, and any loop ended in a recorded terminal state rather than an unbounded retry.

Core runtime contracts:

- [Operating Model](../00-foundation/OPERATING_MODEL.md)
- [Loop Engineering](../10-ai/LOOP_ENGINEERING.md)
- [Context And Memory](../10-ai/CONTEXT_AND_MEMORY.md)
- [Behavior Runtime And Activation](../16-behavior-system/RUNTIME_AND_ACTIVATION.md)
