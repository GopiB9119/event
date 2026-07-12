# Loop Engineering Runtime

## Purpose

Loop engineering turns an engineering workflow into a bounded state machine. An agent may repeat work only while an explicit contract permits another iteration.

AEOS uses loops to reduce manual prompting, not to remove accountability. A loop is successful when it produces the required evidence or stops honestly. Continued activity is not success.

## Decision

AEOS loop definitions are tool-agnostic. Commands such as `/goal`, `/loop`, scheduled routines, CI jobs, and webhook workers are possible runtime adapters, not the architecture itself.

Architecture, feature delivery, quality review, incident response, and release are workflows. They may execute inside one or more loop types, but they are not separate trigger types.

## Loop Contract

No autonomous loop may start without a recorded contract containing:

```text
Mission:
Decision state: STOP | DEFER | TEST | BUILD
Trigger: user | goal | time | event
Scope and non-goals:
Entry evidence:
Protected invariants:
Permitted mutations and tools:
Success predicate:
Verification command or observation:
Iteration budget:
Time or cost budget:
Checkpoint location:
Human approval gates:
Contract owner and amendment authority:
Failure and escalation owner:
```

Unknown budget values require owner input for unattended execution. Do not invent limits merely to fill the contract.

## Runtime States

| State | Meaning | May mutate? |
|---|---|---|
| `READY` | Contract and Definition of Ready pass. | No |
| `RUNNING` | One bounded iteration is executing. | Within the contract |
| `BLOCKED` | Required evidence, access, decision, or safe recovery path is missing. | No |
| `PASSED` | The success predicate passed and evidence was captured. | No |
| `FAILED` | A terminal safety, invariant, or acceptance check failed. | No |
| `EXHAUSTED` | The iteration, time, or cost budget was reached. | No |
| `CANCELLED` | An accountable human or controlling system stopped the loop. | No |

Only `READY` may enter `RUNNING`. Every iteration must leave `RUNNING` by recording new evidence and choosing another state. `BLOCKED`, `PASSED`, `FAILED`, `EXHAUSTED`, and `CANCELLED` are terminal for that run. Starting again requires a new or amended contract.

Only the original contract owner or the accountable owner may authorize a restart or amendment. The implementer cannot amend its own authority, scope, budget, approval gates, or success predicate. An amendment must cite the terminal run, the new evidence that permits re-entry, changed terms, and a reset budget. If failure evidence challenges the original `STOP / DEFER / TEST / BUILD` decision, return to Decision Intelligence before issuing another contract.

## Trigger Types

| Type | Trigger | Stop condition | Appropriate work |
|---|---|---|---|
| Turn-based | A user request | Outcome verified, context required, or current turn ends | Local defects, small changes, investigation, judgment |
| Goal-based | A user or system supplies a measurable predicate | Predicate passes or a budget/terminal rule fires | Compilation, tests, migrations in a disposable environment, measurable quality targets |
| Time-based | A clock or interval | Each run terminates independently; the schedule stops by owner action or policy | Reports, dependency review, queue inspection, maintenance |
| Event-driven | A trusted event, webhook, queue item, or incident | Each spawned run terminates independently; the listener has a circuit breaker | Triage, CI failure handling, bounded remediation, operational response |

Use the simplest type that fits. A short task does not become safer by adding more agents or a scheduler.

## Iteration Protocol

Each iteration follows the same sequence:

```text
Read checkpoint and current evidence
-> choose one falsifiable hypothesis or bounded action
-> execute only permitted mutations
-> run the cheapest discriminating verifier
-> classify the result
-> record evidence and changed state
-> stop or begin the next budgeted iteration
```

After a failed verifier, another attempt is allowed only when the failure produced new evidence and the next action tests a changed hypothesis. Repeating the same action without new evidence is a blind retry and must stop.

## Verification Rules

- Prefer deterministic scripts, compilers, tests, schemas, policy checks, and runtime observations over agent judgment.
- A successful edit, command exit, or generated artifact is not enough when behavior can be exercised directly.
- Subjective quality requires an explicit rubric and, for material risk, an independent reviewer with fresh context.
- The implementing agent cannot waive a failed gate or certify its own exception.
- Flaky checks must be classified and bounded. Repeated execution must not be used to manufacture a pass.
- Evidence must identify the exact source revision, artifact, environment, and command or observation when those facts affect reproducibility.

## Side-Effect Safety

Unattended loops must be safe to repeat:

- Default to read-only discovery and dry runs.
- Use least-privilege credentials and the narrowest tool allowlist.
- Require idempotency or deduplication for external writes.
- Isolate speculative code changes in a branch, worktree, sandbox, or disposable environment.
- Checkpoint before migration, bulk rewrite, dependency upgrade, or generated-file replacement.
- Never deploy, publish, merge, delete data, rotate secrets, spend money, contact users, or change production infrastructure without the required explicit approval.
- Stop on an invariant violation, unexpected repository state, secret exposure, data-integrity risk, or unavailable rollback path.

A loop contract delegates execution, not risk acceptance. It cannot override the Operating Model, expand the assignee's authority, waive independent review, or convert a prohibited/destructive action into an autonomous one.

## Scheduled And Event-Driven Controls

Long-running routines additionally require:

- authenticated trigger provenance
- concurrency and duplicate-event policy
- per-run isolation and an auditable run identifier
- backoff for external failures
- queue and rate limits
- a circuit breaker and owner-controlled disable mechanism
- dead-letter or manual-review handling for exhausted work
- monitoring for stale, stuck, and repeatedly failing runs

A routine may remain enabled indefinitely; its individual runs may not remain unbounded.

## Evidence Record

Every run records:

```text
Run identifier and trigger:
Contract version:
Starting revision/state:
Iterations used:
Mutations performed:
Verifier results:
Terminal state:
Artifacts and logs:
Residual risk:
Follow-up owner:
```

Store only durable, non-sensitive facts in project memory. Logs, secrets, private data, and transient traces belong in access-controlled operational storage with an explicit retention policy.

## Anti-Patterns

- "Continue until it looks good."
- An agent choosing its own success threshold after seeing results.
- Unlimited retries, agents, wall time, tokens, or external calls.
- Treating architecture or quality as a trigger category.
- Polling when a reliable event exists.
- Spawning parallel agents without isolated state or merge ownership.
- Allowing a knowledge graph, summary, or prior chat to override current source evidence.
- Turning a failed safety gate into a prompt to bypass the gate.

## Quality Gate

A loop is ready only when its trigger is authenticated as needed, its success predicate is observable, budgets and terminal states are explicit, mutations are bounded, retries are evidence-driven, side effects are reversible or approved, and the final claim can be reproduced from captured evidence.

## Related

- [Agentic Coding](AGENTIC_CODING.md)
- [Decision Intelligence](../00-foundation/DECISION_INTELLIGENCE.md)
- [Quality Gates](../13-review/QUALITY_GATES.md)
- [Feature Delivery Playbook](../14-playbooks/FEATURE_DELIVERY_PLAYBOOK.md)
- [Runbook Template](../12-templates/RUNBOOK_TEMPLATE.md)