# Architecture Standard

Every architecture decision must be documented as an ADR or equivalent design note.

## Required Sections

1. Problem
2. Context
3. Constraints
4. Goals
5. Non-goals
6. Options considered
7. Tradeoffs
8. Decision
9. Consequences
10. Rollback strategy
11. Validation plan
12. Future evolution

## Architecture Review Questions

- Does this change create a new source of truth?
- Does it cross a trust boundary?
- Does it change persistence?
- Does it affect compatibility or migration?
- Does it introduce new failure modes?
- Does it make testing easier or harder?
