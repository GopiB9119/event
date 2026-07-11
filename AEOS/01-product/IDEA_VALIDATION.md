# Idea Validation

## Purpose

Features do not rescue a weak product idea. Before architecture or implementation, determine whether a real person has a meaningful problem and whether the proposed direction sharpens the product promise.

Idea validation is not asking people whether an idea sounds good. It is collecting behavior or evidence that can disprove the idea cheaply.

## Idea Statement

Express the candidate in one sentence:

```text
For [specific user in a specific context],
who struggles with [observable problem],
the product helps them achieve [measurable outcome]
by [distinct mechanism],
without [important current cost or risk].
```

If this sentence requires invented users, vague words such as "everyone," or multiple unrelated outcomes, the idea is not ready.

## Evidence Ladder

Prefer stronger evidence, while respecting privacy and cost:

1. observed repeated behavior or failure
2. current spending, manual work, workaround, or abandonment
3. support requests or direct user interviews with concrete examples
4. task-based prototype or concierge test
5. honest landing-page or waitlist behavior
6. stated interest without behavior
7. founder or agent intuition

Lower evidence is useful for forming a hypothesis, not declaring demand.

## Validation Questions

### Problem Strength

- Who has the problem, in what moment?
- Is it frequent, urgent, expensive, risky, or emotionally important?
- What do people do today?
- What evidence shows the workaround is inadequate?
- Who does not have this problem?

### Product Fit

- Does this strengthen the one-sentence product idea?
- Is the proposed mechanism necessary, or is it one imagined solution?
- What trust must the user place in the product?
- What expectation could create disappointment or harm?
- Can the outcome be delivered with less product surface?

### Interest And Distribution

- How will the first ten relevant users discover it?
- Why would they try it now?
- Why would they return or recommend it?
- Which existing habit or channel does adoption depend on?
- Is acquisition harder than the feature itself?

### Differentiation

- Which alternatives include doing nothing, manual work, spreadsheets, messaging apps, and direct competitors?
- What specific tradeoff makes this product preferable for the target user?
- Is the difference meaningful to users or only technically interesting?

## Cheapest Valid Experiments

Choose the smallest test that could change the decision:

| Unknown | Example test |
|---|---|
| Problem exists | Five evidence-focused interviews about recent behavior, not opinions |
| Workflow is understandable | Task-based clickable prototype with observed completion |
| Users will invest effort | Concierge/manual workflow requiring a real user action |
| Trust is sufficient | Show exact data/limitation copy and observe objections |
| Demand exists | Honest waitlist or release interest with a predefined threshold |
| Technical feasibility | Time-boxed spike against the highest-risk constraint |

Do not use deceptive fake functionality, fabricated testimonials, dark patterns, scraped private data, or metrics collected without informed disclosure.

## Experiment Contract

Before running a test, record:

```text
Hypothesis:
Most dangerous assumption:
Target participant/context:
Test:
Success threshold:
Failure threshold:
Maximum time/cost:
Data collected and privacy rule:
Decision changed by each result:
```

Set thresholds before results arrive. Otherwise the team can reinterpret any result as success.

## Idea Verdict

Use the states from [Decision Intelligence](../00-foundation/DECISION_INTELLIGENCE.md):

- `STOP`: problem or fit is contradicted
- `DEFER`: potentially valuable, but not the current priority
- `TEST`: one cheap experiment can resolve a dangerous unknown
- `BUILD`: evidence supports the smallest useful product slice

## Maturity

Do not design for a maturity stage the product has not earned:

```text
Hypothesis -> Validated problem -> Prototype -> MVP -> Private beta
-> Public beta -> Production -> Scale -> Maintenance/Evolution
```

At each stage, define what evidence permits the next transition. Scale, enterprise controls, automation, and broad abstraction are not default requirements for an unvalidated product.

## Required Output

- one-sentence idea and target user
- evidence ledger with labels
- alternatives and current workaround
- dangerous assumptions
- interest/distribution hypothesis
- smallest experiment with thresholds
- `STOP / DEFER / TEST / BUILD` verdict
- evidence that would change the verdict

## Related Documents

- [Product System](PRODUCT_SYSTEM.md)
- [Decision Intelligence](../00-foundation/DECISION_INTELLIGENCE.md)
- [Engineering Economics](../02-engineering/ENGINEERING_ECONOMICS.md)
