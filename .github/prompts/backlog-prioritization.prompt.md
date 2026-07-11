---
name: "Backlog Prioritization"
description: "Prioritize competing work by evidence-adjusted user value, product fit, urgency, learning, lifetime cost, risk, and dependencies without fake scoring. Read-only."
argument-hint: "Backlog items, current milestone, constraints, evidence, and available capacity"
agent: "agent"
---

# Backlog Prioritization

Act as the product and engineering portfolio review. This is read-only: do not implement backlog items or silently change scope.

Read [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [Idea Validation](../../AEOS/01-product/IDEA_VALIDATION.md), [Engineering Economics](../../AEOS/02-engineering/ENGINEERING_ECONOMICS.md), the project profile, current milestone, risks, and verified validation evidence.

For every item, separate facts from assumptions and compare:

- target user and observable problem
- alignment with the one-sentence product promise and current milestone
- urgency from data loss, security, legal, platform, reliability, or blocked learning
- expected user outcome and how it can be observed
- evidence confidence
- creation, migration, recurring, support, infrastructure, and opportunity costs
- security, privacy, accessibility, ethics, and abuse risk
- dependencies, reversibility, and smallest useful slice
- value of learning versus value of shipping
- consequence of doing nothing now

Do not total dimensions into a magic score. Do not prioritize feature count, novelty, executive enthusiasm, or speculative scale over evidence.

Output:

1. Current product idea, maturity, milestone, and capacity constraints
2. Evidence and unknowns affecting the portfolio
3. Item-by-item comparison with evidence labels
4. Ordered groups:
   - `NOW`: blocks safety, launch, core value, or critical learning
   - `TEST`: needs a cheap experiment before planning
   - `LATER`: valuable but not the current constraint
   - `STOP`: weak, harmful, duplicative, or economically unjustified
5. Explicit cuts and the opportunity cost they avoid
6. Dependency-aware sequence for `NOW`
7. Acceptance evidence for the first item only
8. Review trigger that would reorder the backlog

Prefer one concrete next action over a large roadmap. Protect capacity by cutting work that does not sharpen the idea.