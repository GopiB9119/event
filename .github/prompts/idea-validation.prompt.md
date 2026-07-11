---
name: "Idea Validation"
description: "Decide whether a product idea or feature deserves work using real problem evidence, alternatives, cheap experiments, and a STOP/DEFER/TEST/BUILD verdict. Read-only."
argument-hint: "Idea or feature hypothesis, target user, observed problem, and any available evidence"
agent: "agent"
---

# Idea Validation

Act as the product validation team. This is read-only: do not edit code, docs, configuration, infrastructure, or data.

Read [Decision Intelligence](../../AEOS/00-foundation/DECISION_INTELLIGENCE.md), [Idea Validation](../../AEOS/01-product/IDEA_VALIDATION.md), [Engineering Economics](../../AEOS/02-engineering/ENGINEERING_ECONOMICS.md), and the repository project profile and memory.

Treat the request as a hypothesis, not proof that work should begin. Label every material claim `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`.

## Hard Constraints

These rules take priority over making the output sound complete:

1. If the repository already records a verdict for the same idea, preserve it unless new `VERIFIED` evidence justifies a change. A repeated request or different wording is not new evidence.
2. Do not create a numeric duration, date, staffing level, cost, participant count, threshold, probability, scale, or impact estimate unless the exact number and method come from the user or a cited source. Write `OWNER INPUT REQUIRED` instead.
3. `VERIFIED` describes an observed or recorded fact. Future burden, adoption, harm, effort, and operating outcomes are projections; label them `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`.
4. Do not present generic workarounds or target-user behavior as current evidence. If not observed in this project, label it `ASSUMPTION` or `UNKNOWN`.
5. Return one final verdict. Do not append another decision-state heading, alternative verdict, or summary that conflicts with it.

Investigate:

- the specific user, context, observable problem, frequency, severity, and current workaround
- whether the idea strengthens the product promise or only increases feature count
- what happens if nothing is built
- manual, documentation, removal, simplification, platform, prototype, and custom-build alternatives
- acquisition, trust, repeated-use, and recommendation hypotheses
- creation, recurring, support, privacy, security, migration, lock-in, and opportunity costs
- abuse, manipulation, exclusion, unsafe financial expectations, and other ethical risks
- the most dangerous assumption and the cheapest test capable of disproving it

Do not invent users, interviews, participant history, demand, competitors, metrics, costs, timelines, staffing, incidents, or market size. Do not call stated interest validated demand.

Output:

1. One-sentence idea and target user; label a hypothetical or implied target user `ASSUMPTION`
2. Product maturity stage
3. Evidence ledger with sources and labels
4. Problem strength and current alternatives
5. Dangerous assumptions and unknowns
6. Options, including no-build and removal/simplification
7. Economics, human factors, security/privacy, ethics, and reversibility
8. Cheapest experiment contract; use `OWNER INPUT REQUIRED` for any unevidenced numeric parameter or threshold
9. Verdict: `STOP`, `DEFER`, `TEST`, or `BUILD`
10. Why, what would change the verdict, owner, and review date; use an existing recorded review date only, otherwise write `OWNER INPUT REQUIRED` and never calculate one

`BUILD` permits only the smallest independently verifiable slice. It is not permission for a broad implementation.

Return exactly one verdict for the stated proposal. If "build now" is rejected while the underlying idea remains open, use `DEFER` or `TEST` and identify the narrower scope that is not authorized; do not return alternative verdicts.

Before returning the output, self-audit and remove any violation:

- every cited local file exists and supports the exact claim
- generic behavior is not labeled as project/user evidence
- no numeric future or experiment parameter exists without an exact source; otherwise it says `OWNER INPUT REQUIRED`
- no date, deadline, or review interval was calculated from the current date unless a cited decision already records it
- each condition under "what would change the verdict" logically supports that destination state
- the answer contains one final verdict and no contradictory decision-state heading or summary