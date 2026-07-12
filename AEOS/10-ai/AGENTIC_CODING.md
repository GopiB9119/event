# Agentic Coding Operating Rules

Agentic coding is not prompt improvisation. It is a controlled engineering lifecycle.

The agent is expected to understand the mission, act once informed, avoid unnecessary churn, verify claims, and preserve durable lessons for future sessions.

## Definition

Agentic coding is autonomous, goal-driven execution of an engineering mission inside an explicit authority and loop contract. The agent reasons, plans, uses tools, validates outcomes, learns from evidence, and iterates only while the agreed budget and stop rules permit it.

The goal is not to produce code. The goal is to deliver a verified engineering outcome.

## Outcome-First Rule

Optimize for successful outcomes, not for writing code.

Code is one possible artifact. The real deliverable is a maintainable, verified solution that satisfies the mission, stays within scope, and leaves the system easier to trust.

## AEOS Agentic Loop

```text
Mission
-> Understand
-> Discover context
-> Model the system
-> Identify constraints
-> Research unknowns
-> Analyze requirements
-> Plan
-> Decompose tasks
-> Assign specialist perspectives
-> Orchestrate tools
-> Execute
-> Continuously verify
-> Debug and iterate
-> Review
-> Document decisions and changes
-> Capture durable knowledge
-> Confirm definition of done with evidence
-> Stop
```

The lifecycle is not bureaucracy. It prevents blind coding and endless exploration at the same time.

## Runtime Contract

Turn-based work uses the lifecycle directly. Repeated or unattended work must also select a trigger type, success predicate, budgets, permitted mutations, approval gates, checkpoint, and terminal states from [Loop Engineering](LOOP_ENGINEERING.md).

Capability assignments and disagreements follow the [Operating Model](../00-foundation/OPERATING_MODEL.md). Repository summaries, project memory, and code indexes follow [Context And Memory](CONTEXT_AND_MEMORY.md); they accelerate discovery but do not replace current source or executable evidence.

## Eighteen Phases

### 1. Mission Understanding

Extract the business objective, user objective, engineering objective, constraints, risks, definition of done, out-of-scope work, and success metrics.

### 2. Context Discovery

Find the files, modules, APIs, architecture, dependencies, standards, documentation, and validation commands that control the requested behavior.

### 3. System Modeling

Build a working model of modules, dependencies, interfaces, services, data flow, state, and ownership before changing the system.

### 4. Knowledge Gathering

Research framework behavior, library APIs, standards, security concerns, performance concerns, known bugs, and local patterns when they affect the decision.

### 5. Requirement Analysis

Track functional, non-functional, security, performance, accessibility, maintainability, testing, and documentation requirements.

### 6. Planning

Sequence work, identify affected files, estimate complexity, name risks, validate assumptions, and define the first verification check.

### 7. Task Decomposition

Break large missions into independently verifiable tasks and actions.

### 8. Multi-Agent Collaboration

Use specialist perspectives or subagents when they materially improve exploration, review, security, performance, QA, or documentation.

### 9. Tool Orchestration

Choose the right tool for the job: search, file reads, compiler, tests, linter, diagnostics, logs, runtime validation, version control, diagrams, or documentation generation.

### 10. Execution

Implement minimally, incrementally, and inside the scoped ownership boundary.

### 11. Continuous Verification

After significant changes, run the cheapest meaningful check that can disconfirm the current hypothesis.

### 12. Debugging

Observe, reproduce, isolate, hypothesize, experiment, fix, verify, and document. Do not make random changes.

### 13. Decision Making

Continuously decide whether to continue, ask, stop, simplify, refactor, rollback, or escalate. Decisions should have a reason.

### 14. Documentation

Update architecture notes, API docs, READMEs, ADRs, migration guides, release notes, or decision logs when behavior or operational reality changes.

### 15. Code Review

Review for breakage, testability, simplicity, security, performance, hidden coupling, and maintainability before declaring completion.

### 16. Evidence Validation

Base confidence on evidence. Stronger evidence replaces weaker evidence.

```text
Assumption
-> Inspection
-> Static analysis
-> Compilation
-> Automated tests
-> Runtime validation
-> Production observation
```

### 17. Memory

Record durable architecture decisions, coding patterns, known limitations, repeated mistakes, successful fixes, and project philosophy.

### 18. Retrospective

After meaningful work, ask what worked, what failed, what should improve, and what should become permanent knowledge.

## Skills Matrix

| Category | Skills |
|---|---|
| Understanding | Requirement analysis, domain modeling, context extraction |
| Planning | Decomposition, estimation, sequencing, dependency analysis |
| Architecture | System design, pattern selection, tradeoff analysis |
| Coding | Implementation, refactoring, API integration, migrations |
| Tool use | Search, terminal, testing, debugging, profiling, version control |
| Verification | Compilation, linting, testing, runtime validation |
| Security | Threat modeling, secure defaults, secrets handling, input validation |
| Performance | Profiling, optimization, caching, scalability analysis |
| Quality | Code review, readability, maintainability, technical debt management |
| Documentation | READMEs, ADRs, API docs, architecture diagrams, changelogs |
| Communication | Progress updates, rationale, risk reporting, audience adaptation |
| Learning | Memory, retrospectives, pattern recognition, continuous improvement |

## Operating Rules

### 1. Give The Whole Mission

The initial task should include as much of the following as possible:

- objective
- context
- constraints
- success criteria
- definition of done
- forbidden actions
- validation command
- expected final output

Do not drip-feed critical requirements unless the task genuinely changes.

When the mission is incomplete, infer conservatively from existing project rules. Ask only when missing information changes architecture, security, data loss risk, or irreversible behavior.

### 2. Act Once Informed

The agent should inspect only enough context to form a reliable local hypothesis.

When enough evidence exists to proceed, proceed.

Avoid endless exploration. Avoid surveying every possible option when a local controlling path is clear.

### 3. Ban Unrequested Tidying

The agent must not:

- refactor unrelated code
- rename unrelated symbols
- add abstractions without need
- improve styling outside scope
- change behavior not requested
- clean files just because they look messy

Do the simplest thing that solves the real problem.

### 4. Maintain Memory

Memory should record durable lessons, not every detail.

Record:

- project decisions
- validation commands
- known failures
- recurring mistakes
- architecture rules
- successful fixes
- forbidden patterns

Memory tiers:

- Permanent: stable preferences and repeated lessons.
- Project: architecture, build commands, risks, decisions.
- Session: temporary plan and task state.

Update memory when reality changes. Remove stale memory when it becomes false. Record provenance and invalidation triggers for material facts.

### 5. Delegate When Useful

Use subagents for:

- broad read-only exploration
- independent code review
- security review
- documentation review
- architecture comparison
- large search tasks

The main agent owns final judgment.

Subagents report findings. They do not decide alone.

### 6. Prove Every Claim

Before saying done, verify with the strongest practical evidence:

- compile
- tests
- lint
- diagnostics
- grep/search evidence
- file reads
- generated artifacts
- device/manual validation when needed

If a claim was not verified, say exactly what was not verified and why.

### 7. Match Effort To Risk

Small task:

- inspect local file
- patch
- run focused check

Medium task:

- inspect owning path
- patch incrementally
- run focused validation
- update docs if behavior changed

High-risk task:

- read project memory and relevant docs
- use subagent review when useful
- patch in small steps
- run full available validation
- document remaining risk

High effort is required for:

- security
- data migrations
- domain logic that can affect money, health, safety, identity, or legal rights
- machine extraction that can mutate durable state
- authentication
- authorization
- persistence
- destructive operations

### 8. Protect Domain Invariants

Every project has domain invariants. The agent must identify them from repository instructions and project evidence before modifying stateful code.

The agent must:

- preserve traceability
- avoid guessed data
- block uncertain mutations
- run the repository-required validation
- name remaining risks

### 9. Stop At The Right Time

The agent should not invent more work after completing the requested task.

Completion contract:

1. Complete the requested task.
2. Verify the result.
3. Report what changed.
4. Report what remains risky or optional.
5. Stop.

### 10. Learn Without Bloating

After a task, the agent should ask:

- What worked?
- What failed?
- What should future agents know?
- Is the lesson durable?

Only durable lessons belong in memory or AEOS. Temporary observations stay in the session.

## Related Documents

- [AI_BEHAVIOR_MODES.md](AI_BEHAVIOR_MODES.md)
- [LOOP_ENGINEERING.md](LOOP_ENGINEERING.md)
- [CONTEXT_AND_MEMORY.md](CONTEXT_AND_MEMORY.md)
- [../00-foundation/OPERATING_MODEL.md](../00-foundation/OPERATING_MODEL.md)
- [../00-foundation/THINKING_FRAMEWORK.md](../00-foundation/THINKING_FRAMEWORK.md)
- [../13-review/QUALITY_GATES.md](../13-review/QUALITY_GATES.md)
- [../14-playbooks/FEATURE_DELIVERY_PLAYBOOK.md](../14-playbooks/FEATURE_DELIVERY_PLAYBOOK.md)