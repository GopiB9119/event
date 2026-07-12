# AEOS: AI Engineering Operating System

AEOS is a version-controlled operating manual for AI collaborators.

It is not a prompt collection. It is a composable engineering knowledge base that tells any AI assistant how to think, write, architect, review, test, and build with the discipline of a serious engineering organization.

## Purpose

AEOS exists to make AI outputs consistent, reviewable, and durable across models and projects.

AEOS v0.3 adds a bounded runtime and operating layer: explicit loop contracts, capability-based authority, deterministic terminal states, and provenance-bearing context. See [Loop Engineering](10-ai/LOOP_ENGINEERING.md), [Operating Model](00-foundation/OPERATING_MODEL.md), and [Context And Memory](10-ai/CONTEXT_AND_MEMORY.md).

It is designed to be:

- Model-agnostic
- Project-agnostic
- Language-agnostic
- Version-controlled
- Composable
- Long-lived
- Evidence-oriented
- Continuously improved

## Operating Model

Every AI collaborator should use AEOS as the default reasoning and delivery system:

```text
Understand
-> Model the system
-> Question assumptions
-> Identify risks
-> Compare options
-> Make the smallest useful change
-> Validate
-> Document consequences
```

## Directory Map

- [MANIFEST.md](MANIFEST.md) defines the knowledge graph.
- [00-foundation](00-foundation/) defines identity, principles, decisions, thinking, communication, and vocabulary.
- [01-product](01-product/) defines product strategy and feature lifecycle.
- [02-engineering](02-engineering/) defines engineering standards.
- [03-architecture](03-architecture/) defines architecture decision discipline.
- [04-documentation](04-documentation/) defines documentation systems.
- [05-writing](05-writing/) defines language and communication standards.
- [06-ui-ux](06-ui-ux/) defines interface principles.
- [07-security](07-security/) defines security and privacy posture.
- [08-performance](08-performance/) defines efficiency and scalability standards.
- [09-testing](09-testing/) defines test strategy.
- [10-ai](10-ai/) defines AI behavior modes.
- [11-prompts](11-prompts/) contains reusable prompt systems.
- [12-templates](12-templates/) contains delivery templates.
- [13-review](13-review/) contains quality gates.
- [14-playbooks](14-playbooks/) contains execution playbooks.
- [15-reference](15-reference/) contains vocabulary, patterns, and examples.
- [project](project/) binds AEOS to the current repository.

## Deterministic Validation

After changing AEOS, workspace prompts, root agent instructions, or project AI overlays, run:

```powershell
.\scripts\validate-aeos.ps1
```

The validator checks required core documents, the anchored machine-readable loop contract and runner self-test, prompt frontmatter and unique names, unresolved prompt inputs, product-specific leakage into universal AEOS documents, and local links across the governance corpus. Run `.\scripts\validate-aeos.ps1 -SelfTest` to verify its frontmatter parser guardrails.

## v0.3 Boundaries

AEOS v0.3 specifies and validates operating contracts. It does not yet ship a scheduler/event listener, MCP or LSP repository indexer, metrics service, machine-readable loop ledger, or jurisdiction-specific compliance package. Add those implementations only when a real project supplies an owner, threat model, operating environment, success predicate, and lifecycle cost.

## Executable Pilot

The first bounded adapter is the local [AEOS Governance Validation Loop](14-playbooks/AEOS_VALIDATION_LOOP_PILOT.md), governed by a [versioned machine-readable contract](14-playbooks/contracts/aeos-governance-validation.loop.json): one iteration, a 60-second watchdog, fixed verifier allowlist, no repair authority, and ignored JSON run evidence.

```powershell
.\scripts\invoke-aeos-governance-loop.ps1 -SelfTest
.\scripts\invoke-aeos-governance-loop.ps1
```

## First Rule

Powerful language must follow powerful implementation. Do not use authority as decoration. Use it to make truth sharper.
