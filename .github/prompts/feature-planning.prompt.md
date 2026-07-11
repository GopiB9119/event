---
name: "Feature Planning"
description: "Convert one understood feature into small safe implementation tasks with risks, dependencies, and verification criteria."
argument-hint: "Feature request plus discovery report or acceptance criteria"
agent: "agent"
---

# Feature Planning

Act as the engineering planner. Do not implement.

Read [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [Engineering Economics](../../AEOS/02-engineering/ENGINEERING_ECONOMICS.md), [decision framework](../../AEOS/00-foundation/DECISION_FRAMEWORK.md), [feature delivery playbook](../../AEOS/14-playbooks/FEATURE_DELIVERY_PLAYBOOK.md), [testing standard](../../AEOS/09-testing/TESTING_STANDARD.md), and the relevant discovery evidence.

First verify a decision state and Definition of Ready. Material product or architecture work requires a supported `BUILD` verdict. For `TEST`, produce only the smallest experiment plan. For `STOP` or `DEFER`, do not produce an implementation backlog. If the requirement, controlling path, data ownership, failure modes, acceptance criteria, or value evidence are unclear, identify the exact discovery work still required.

Label material planning claims `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`. Do not hide an unknown inside an implementation task.

For a ready feature, produce:

- goal, non-goals, assumptions, and constraints
- affected files/modules and dependency order
- smallest independently verifiable implementation tasks
- data/migration/security/privacy implications
- creation, recurring/support, migration/rollback, lock-in, and opportunity costs
- validation after each task
- regression, negative, restart, offline, permission, duplicate, and time-based checks as applicable
- rollback strategy and release risk
- review triggers and evidence that would stop or redirect later tasks
- definition of done mapped to evidence

Prefer the smallest shippable change. Reject scope that only makes the product bigger. Do not design for a maturity stage or scale the evidence has not earned.