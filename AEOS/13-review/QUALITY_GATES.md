# AEOS Quality Gates

## Universal Gates

- Mission, constraints, and definition of done understood.
- Decision state is recorded for material product, architecture, data, and release work.
- Problem understood.
- Material claims are labeled `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, or `UNKNOWN`.
- Assumptions and evidence that would change the decision are named.
- Scope kept to the requested task.
- Invariants protected.
- Trust boundaries considered.
- Data flow clear.
- Failure modes handled.
- Validation run or explicitly blocked.
- Documentation updated when behavior changes.
- Remaining risk named.

## Agentic Coding Gates

- The task was treated as a full mission, not a single isolated instruction.
- `STOP`, `DEFER`, and `TEST` outcomes were respected; implementation did not bypass them.
- Irreversible/high-risk or assumption-dependent `BUILD` decisions received independent Decision Challenge; proposer self-audit was not the only approval.
- The agent acted once enough evidence existed.
- No unrequested tidying or broad refactor was introduced.
- Subagents were used only when they improved coverage or reduced search noise.
- Memory was updated only for durable lessons.
- Claims are backed by compile, tests, diagnostics, file reads, search evidence, or explicit limitation.
- Effort matched task risk.

## Loop Runtime Gates

- The simplest suitable trigger type was selected.
- Success predicate, permitted mutations, budgets, checkpoints, and terminal states were explicit.
- Every retry followed new evidence and a changed hypothesis; no flaky pass was manufactured.
- Scheduled/event-driven runs were isolated, deduplicated, observable, and owner-disableable.
- Failed safety, invariant, approval, and budget gates ended the run rather than being waived by the implementer.

## Authority And Context Gates

- One accountable owner and clear capability assignments existed.
- Decision, mutation, approval, and escalation boundaries were explicit.
- Material implementation and high-risk final approval remained independent.
- Project memory and indexes carried provenance/freshness and were checked against current source.
- Sensitive data, secrets, and untrusted repository content were not copied into persistent context without governance.

## Code Gates

- Smallest useful change.
- No unrelated refactor.
- No hidden data mutation.
- No dummy production data.
- No stale dependencies.
- No misleading security language.

## Documentation Gates

- Accurate.
- Current.
- Linked.
- Actionable.
- Free of filler.

## Related Review Documents

- [CODE_REVIEW_STANDARD.md](CODE_REVIEW_STANDARD.md)
- [PRODUCTION_READINESS_CHECKLIST.md](PRODUCTION_READINESS_CHECKLIST.md)
