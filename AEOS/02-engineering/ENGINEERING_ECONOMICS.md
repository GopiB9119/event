# Engineering Economics

## Purpose

Engineering decisions consume money, attention, operational capacity, and future flexibility. "Can build" is not the same as "should build."

Use economics to compare alternatives, not to manufacture precise ROI from unknown inputs.

## Cost Model

For each option, consider:

### Creation Cost

- product discovery and design
- implementation and integration
- migration and compatibility
- test fixtures and realistic validation
- security, privacy, accessibility, and legal review
- documentation and launch preparation

### Recurring Cost

- cloud, storage, bandwidth, AI/API, messaging, and observability
- support, moderation, abuse handling, and incident response
- dependency updates and platform policy changes
- backups, recovery, retention, deletion, and compliance operations
- on-call burden and manual operational work

### Change Cost

- data migration and rollback
- vendor lock-in and switching cost
- user retraining and support
- compatibility with old clients or exported data
- permanent promises created by public APIs or behavior

### Opportunity Cost

- which higher-value problem is delayed?
- what learning could be obtained more cheaply?
- does the work strengthen the core idea or only expand surface area?
- what maintenance capacity is consumed after launch?

## Value Model

Name the expected outcome without inventing numbers:

- task success or time saved
- fewer errors or safer data
- improved trust or comprehension
- retention, conversion, referral, or acquisition hypothesis
- reduced support or operational burden
- regulatory, platform, or security necessity
- strategic learning that resolves a major uncertainty

Distinguish measured value from expected value. If no outcome can be observed, the proposal is not economically testable.

## Comparison Method

Use ordinal ratings only when exact data is unavailable:

| Dimension | Low | Medium | High |
|---|---|---|---|
| User value | marginal convenience | meaningful workflow improvement | solves urgent/core problem |
| Evidence confidence | intuition | indirect evidence | observed behavior or validated test |
| Delivery cost | small local change | cross-module work | new system/migration/operations |
| Recurring burden | near zero | periodic maintenance | continuous infrastructure/support |
| Risk | reversible/local | contained but material | data, security, legal, or lock-in risk |
| Reversibility | easy rollback | migration required | durable commitment or data loss risk |

Do not total these into a magic score. Explain why one option dominates or why a test is needed.

## Required Options

Every analysis compares:

1. do nothing
2. remove or simplify
3. manual/operational solution
4. buy or use a proven platform capability
5. smallest custom implementation
6. larger custom system only when justified

## Infrastructure And AI Rules

- Estimate request volume, storage growth, retention, egress, and failure cost before selecting cloud services.
- Treat free tiers as temporary pricing, not architecture.
- For AI APIs, include model cost, retries, latency, privacy, evaluation, hallucination handling, and vendor changes.
- Prefer local or deterministic processing when it meets the product need and reduces sensitive-data exposure.
- Do not introduce a backend merely to make the architecture look production-like.

## Build/Buy/Defer Gate

Choose custom implementation only when it provides necessary product differentiation, data control, reliability, or economics that existing capabilities cannot provide.

Choose `TEST` when the dominant uncertainty is user value or technical feasibility. Choose `DEFER` when another item has stronger evidence-adjusted value. Choose `STOP` when lifetime burden exceeds plausible benefit or contradicts the product idea.

## Required Economic Note

```text
Decision:
Expected user/business outcome:
Evidence confidence:
Options compared:
Creation cost:
Recurring/support cost:
Migration/rollback cost:
Opportunity cost:
Lock-in and exit path:
Cheapest learning experiment:
Recommendation and why:
Review trigger:
```

## Quality Gate

An economic recommendation is acceptable only when it includes no-build and simpler alternatives, separates known costs from assumptions, includes recurring burden and opportunity cost, and names what evidence would change the decision.

## Related Documents

- [Decision Intelligence](../00-foundation/DECISION_INTELLIGENCE.md)
- [Idea Validation](../01-product/IDEA_VALIDATION.md)
- [Decision Framework](../00-foundation/DECISION_FRAMEWORK.md)