# Decision Intelligence

## Purpose

AEOS must prevent two different failures:

1. building software incorrectly
2. building the wrong software correctly

Engineering execution begins only after the decision to act has earned enough evidence. A request is an input to investigate, not proof that a feature should exist.

## Decision States

Every meaningful product, architecture, or release decision ends in one state:

| State | Meaning |
|---|---|
| `STOP` | Evidence shows the work is harmful, irrelevant, misleading, or not worth its cost. |
| `DEFER` | The problem may matter, but a dependency, risk, or higher-value priority blocks it. |
| `TEST` | A dangerous assumption remains. Run the cheapest experiment that can change the decision. |
| `BUILD` | The problem, scope, expected outcome, and acceptance evidence are strong enough for planning. |

`BUILD` is not permission for a large implementation. It permits the smallest independently verifiable next slice.

Return exactly one state for the stated decision question. If the underlying idea may remain valuable but the current timing, scope, or prerequisites are wrong, use `DEFER` or `TEST` rather than writing competing verdicts such as "STOP, alternatively TEST." State separately what narrower proposal was rejected.

An existing reviewed state remains authoritative until new `VERIFIED` evidence changes it. Repeating a request, renaming the feature, or increasing urgency language does not count as new evidence.

## Evidence Labels

Label every material claim in a decision record:

| Label | Standard |
|---|---|
| `VERIFIED` | Directly supported by repository inspection, executable validation, production observation, user research, or primary documentation. Cite the evidence. |
| `SUPPORTED` | Indirect but credible evidence exists. State why it applies and what remains uncertain. |
| `ASSUMPTION` | Treated as true for planning but not validated. Name the failure if it is wrong. |
| `UNKNOWN` | Evidence is absent or contradictory. Do not silently convert it into an assumption. |

Do not invent users, metrics, incidents, costs, competitor behavior, or market demand. Words such as "likely," "simple," "secure," and "standard" are not evidence.

Evidence labels apply to the exact claim, not a nearby source. A document saying work is high-cost does not verify a duration, headcount, price, adoption rate, or return. Derived numeric estimates require an explicit source and method; otherwise omit the number or label the estimate `ASSUMPTION`.

Experiment designs are recommendations, not observed evidence. If the user or a cited source has not supplied numeric participant counts, duration, budget, or thresholds, write `OWNER INPUT REQUIRED` rather than inventing precision.

Do not calculate a review date or deadline merely because a template asks for one. Use a date already recorded by the accountable owner or write `OWNER INPUT REQUIRED`.

Distinguish current facts from future projections. A currently installed dependency or recorded incident may be `VERIFIED`; a predicted support burden, adoption effect, abuse scenario, delivery difficulty, or future operating cost is normally `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN` even when its underlying mechanism is documented.

## Decision Gate

Before committing meaningful effort, answer:

### Problem And User

- Who experiences the problem?
- What observable behavior or evidence shows it exists?
- How severe and frequent is it?
- What workaround is used today?
- What happens if nothing is built?

### Product Value

- Does the change sharpen the product promise or only increase feature count?
- Which user behavior should improve: task success, trust, retention, conversion, referral, or support burden?
- Can that outcome be observed without invasive analytics?
- Could copy, support, workflow removal, or a manual process solve it first?

### Alternatives

Always compare at least:

- do nothing
- remove or simplify existing behavior
- documentation or operational change
- prototype or reversible experiment
- smallest product change
- larger architecture change only when evidence requires it

### Economics

Consider implementation, infrastructure, migration, testing, security, privacy, support, operations, vendor lock-in, and opportunity cost. Do not use fake precision when inputs are unknown.

### Human Factors

- Will users understand the change without training?
- Does it add anxiety, cognitive load, another decision, or another failure state?
- Could one screen, click, field, permission, or concept be removed?
- What wrong expectation could the interface create?

### Ethics And Abuse

- Can the feature enable fraud, coercion, impersonation, surveillance, exclusion, manipulation, or unsafe financial behavior?
- Is data collection necessary and proportionate?
- Are limitations and uncertainty visible to affected users?
- Would the decision be defensible if explained publicly?

### Reversibility And Time

- Can the decision be rolled back without data loss or user lock-in?
- What breaks after six months, one year, and three years?
- Which future commitment does this create?
- Is the design appropriate for the current maturity stage rather than imagined scale?

## Meta-Reasoning Check

Before finalizing a recommendation, challenge the reasoning:

- Why this option instead of the strongest alternative?
- What evidence would change the verdict?
- Are novelty bias, action bias, sunk-cost bias, authority bias, or scale fantasy influencing the choice?
- Is the team optimizing an easy metric instead of the user outcome?
- What would a product, security, operations, accessibility, or domain expert dispute?
- If this decision fails, what is the most probable cause?

Before publishing the decision record, run an evidence-integrity check:

- every cited local path exists and supports the exact adjacent claim
- generic industry behavior is not presented as repository or user evidence
- every numeric future estimate has a source/method or is explicitly `ASSUMPTION`
- every proposed experiment parameter is separated from observed evidence
- every "evidence that changes the verdict" condition logically moves toward the named state
- exactly one final decision state is present for the stated question

## Required Decision Record

```text
Decision question:
User and problem:
Product promise affected:
Evidence ledger:
Unknowns and dangerous assumptions:
Options, including no-build:
User and business outcome:
Cost and opportunity cost:
Security, privacy, ethics, and abuse:
Reversibility and time horizons:
Cheapest falsification test:
Verdict: STOP | DEFER | TEST | BUILD
Why:
Evidence that would change the verdict:
Owner and review date:
```

## Stop Rules

Do not enter implementation when:

- the user or problem is invented or materially unclear
- success cannot be observed
- a cheaper test can resolve the central uncertainty
- the feature contradicts the product promise
- authorization, data ownership, migration, or irreversible risk is unresolved
- the expected value depends on fabricated scale, revenue, or retention claims
- the UI or marketing promise would exceed the implementation

## Quality Gate

A decision is ready only when evidence is distinguished from assumptions, the no-build option was considered, the smallest falsification test is named, costs and harms are visible, and the verdict can be reviewed independently.

## Related Documents

- [AI Constitution](AI_CONSTITUTION.md)
- [Decision Framework](DECISION_FRAMEWORK.md)
- [Thinking Framework](THINKING_FRAMEWORK.md)
- [Product System](../01-product/PRODUCT_SYSTEM.md)
- [Agentic Coding](../10-ai/AGENTIC_CODING.md)