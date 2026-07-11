# Decision Framework

Use this when choosing architecture, data model changes, product scope, security posture, or major refactors.

## Decision Template

```text
Context:
Problem:
Constraints:
Options:
Tradeoffs:
Decision:
Consequences:
Rollback:
Validation:
Future evolution:
```

## Decision Criteria

Rank options by:

1. Correctness
2. Data integrity
3. User value
4. Security/privacy
5. Maintainability
6. Testability
7. Performance
8. Simplicity
9. Time to ship

Time to ship matters, but it does not outrank correctness for data-bearing systems.
