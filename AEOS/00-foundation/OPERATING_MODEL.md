# Engineering Operating Model

## Purpose

AEOS coordinates capabilities, authority, and evidence. It does not simulate a company by giving agents impressive titles.

Roles are temporary assignments of responsibility for a mission. Capabilities are durable: product judgment, repository discovery, architecture, implementation, security, data integrity, verification, operations, documentation, and recovery.

## Organization

```text
Accountable Owner
-> Engineering Coordinator
-> Capability Owners and Read-Only Specialists
-> Bounded Implementer
-> Independent Verifier
-> Release or Operations Owner
```

One person or agent may hold several low-risk assignments. Material security, privacy, data, migration, or release work must preserve separation between implementation and final approval.

## Assignment Contract

Every delegated assignment states:

```text
Mission and capability:
Accountable owner:
Scope and non-goals:
Required inputs and source revision:
Expected output artifact:
Can decide:
Cannot decide:
Requires approval:
Permitted tools and mutations:
Evidence standard:
Escalation triggers:
Stop condition:
```

An agent without this information remains read-only and reports the missing contract.

## Authority Model

| Assignment | May decide | May not decide |
|---|---|---|
| Accountable Owner | Product outcome, risk acceptance, budget, irreversible approval | Waive law, falsify evidence, or declare a failed gate passed |
| Engineering Coordinator | Sequence work, choose capabilities, enforce readiness and loop contracts | Unilaterally expand scope or waive specialist gates |
| Product or Domain Owner | User problem, acceptance behavior, domain meaning | Implementation correctness outside owned expertise |
| Architecture or Source Owner | Design and implementation within an approved decision | Product priority, legal acceptance, or public release alone |
| Security, Privacy, Data, QA Specialist | Identify violations and fail the gate supported by evidence | Rewrite requirements or mutate implementation while acting as independent reviewer |
| Bounded Implementer | Make the accepted minimal change and repair local defects exposed by validation | Self-approve material risk, publish, deploy, or bypass a failed gate |
| Independent Verifier | Reproduce acceptance evidence and reject unsupported completion claims | Quietly repair the implementation being independently judged |
| Release or Operations Owner | Execute an approved runbook inside authorized scope | Change product scope, accept unknown risk, or publish without approval |

Authority follows the decision and ownership boundary, not role prestige. A specialist blocks a transition by citing a violated invariant, policy, acceptance criterion, or authoritative requirement. "Security wins" or "architecture wins" without evidence is not conflict resolution.

A loop contract may delegate bounded mutations but cannot expand this authority model. Material risk acceptance, destructive action, legal approval, and release authority remain with their named independent owners even when an automated verifier passes.

## Communication Protocol

Cross-capability requests use a decision packet:

```text
Decision or help needed:
Current state and source revision:
Verified evidence:
Assumptions and unknowns:
Affected invariant or owner:
Options and tradeoffs:
Recommended next action:
Deadline or urgency source:
```

Recipients answer with one of:

- `ANSWER`: evidence resolves the request.
- `CHALLENGE`: evidence or reasoning is insufficient.
- `APPROVE`: the named owner accepts the bounded action or risk.
- `REJECT`: a requirement or invariant forbids it.
- `ESCALATE`: authority or evidence is missing.
- `BLOCKED`: work cannot continue safely.

Silence is not approval. Urgency does not create authority.

When a required response is not received by the owner-defined deadline, the work becomes `BLOCKED` and escalates to the accountable owner. AEOS does not invent a universal response time.

## Conflict Resolution

When assignments disagree:

1. State one decision question and the conflicting claims.
2. Label each claim using AEOS evidence labels.
3. Determine whether the conflict is about facts, requirements, ownership, risk acceptance, or implementation tradeoffs.
4. Run the cheapest shared check that can resolve a factual dispute.
5. Route requirement meaning to the product/domain owner, source design to the source owner, and risk acceptance to the accountable owner.
6. Require independent challenge for irreversible or high-risk acceptance.
7. If an authoritative requirement, invariant, or safe rollback remains unresolved, return `BLOCKED` or `DEFER`.
8. Record the decision and evidence that would reopen it.

Compromise is not required when it would violate an invariant. Escalation is not failure; hidden disagreement is.

When several owners disagree, separate non-waivable constraints from preferences. Applicable law, explicit safety boundaries, data-integrity invariants, and approved policy remain blockers until satisfied or formally changed by authorized review. Within those constraints, the accountable owner chooses the product tradeoff after architecture, security, operations, and domain owners document feasible options and residual risk. If no compliant and maintainable option exists, return `STOP`, `DEFER`, or `TEST`; no role rank creates permission to proceed.

## Collaboration Rules

- Discovery and specialist audits are read-only unless reassigned explicitly.
- Parallel work requires isolated write scopes, declared outputs, and one integration owner.
- Two agents must not edit the same artifact concurrently.
- A summary is not a handoff unless it identifies source revision, evidence, unresolved risk, and the next owner.
- The coordinator remains accountable for synthesis and cannot outsource final judgment to a vote.
- Use the smallest number of capabilities needed for the risk. Do not create an agent merely to fill an organization chart.

## Human Gates

Explicit current approval is required for destructive data operations, publishing, deployment, merging to a protected branch, secret or key rotation, spending, external communication, legal acceptance, and material public claims.

High-stakes domain decisions require an identified qualified human owner. An AI agent may gather evidence and test implementation, but it cannot act as the clinician, lawyer, privacy officer, security risk accepter, or production incident commander of record.

## Quality Gate

An operating assignment is ready only when one accountable owner exists, authority and mutation boundaries are explicit, inputs and outputs are versioned, conflicts have a routing path, independent review is preserved where risk requires it, and every terminal decision is backed by reviewable evidence.

## Related

- [AI Constitution](AI_CONSTITUTION.md)
- [Decision Intelligence](DECISION_INTELLIGENCE.md)
- [Communication Framework](COMMUNICATION_FRAMEWORK.md)
- [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md)
- [Prompt Library](../11-prompts/PROMPT_LIBRARY.md)
