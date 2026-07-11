# AEOS: AI Engineering Operating System

AEOS is a version-controlled operating manual for AI collaborators.

It is not a prompt collection. It is a composable engineering knowledge base that tells any AI assistant how to think, write, architect, review, test, and build with the discipline of a serious engineering organization.

## Purpose

AEOS exists to make AI outputs consistent, reviewable, and durable across models and projects.

AEOS v0.2 adds an upstream decision layer so agents can return `STOP`, `DEFER`, or `TEST` instead of treating every request as permission to build. See [Decision Intelligence](00-foundation/DECISION_INTELLIGENCE.md), [Idea Validation](01-product/IDEA_VALIDATION.md), and [Engineering Economics](02-engineering/ENGINEERING_ECONOMICS.md).

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

## First Rule

Powerful language must follow powerful implementation. Do not use authority as decoration. Use it to make truth sharper.
