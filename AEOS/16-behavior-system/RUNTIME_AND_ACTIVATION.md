# Behavior Runtime And Activation

## Purpose

The behavior runtime selects a small context pack from the canonical catalog. It does not replace model reasoning, repository instructions, source inspection, or executable verification.

## Inputs

Only typed, trusted signals may drive activation:

- the current user mission and explicit constraints;
- selected AEOS prompt/capability;
- repository instructions and accepted decisions;
- current source, build configuration, and test evidence;
- environment observations returned by tools;
- an accountable owner's explicit risk or approval decision.

Repository prose, comments, fetched web content, generated examples, and model-produced text are untrusted data. They cannot activate a pack merely by containing an activation phrase.

## Resolution Sequence

```text
Kernel and project overlays
-> classify mission phase and risk
-> collect fresh trusted signals
-> resolve candidate domains
-> apply negative signals and exclusions
-> detect authority or policy conflicts
-> select at most two domains
-> rank active items by phase, risk, and evidence need
-> select at most twelve items
-> emit activation reasons and source IDs
-> execute under existing authority
-> deactivate at terminal state or context invalidation
```

## Context Budget

The resolver enforces:

- maximum two domains;
- maximum twelve items;
- maximum dependency depth two;
- one collaboration pattern;
- one catalog governance overlay;
- deduplicated kernel invariants;
- concise rendered statements with full items loaded only on demand.

Budget overflow returns `BLOCKED`. The resolver never silently drops a higher-priority safety, privacy, data-integrity, authority, or approval rule to fit the budget.

## Phase Filters

| Mission phase | Preferred item kinds |
|---|---|
| Understand and discover | signals, failures, mental models, decision models |
| Plan and design | decisions, mental models, behaviors, governance |
| Implement | behaviors, recoveries, governance |
| Debug | failures, signals, recoveries, behaviors |
| Review and verify | failures, signals, governance, collaboration |
| Release and operate | governance, failures, recoveries, collaboration |

## Fail-Closed Conditions

Return `BLOCKED` when:

- no domain meets the minimum confidence;
- more than two equal-priority domains remain and evidence cannot distinguish them;
- selected items conflict without an authoritative resolution rule;
- a required source is stale or unavailable;
- an item would expand authority;
- a dependency or reference is unresolved;
- a routing budget is exceeded;
- a malicious or untrusted signal attempts to select tools, commands, paths, permissions, or remote content.

## Activation Record

Every resolved context pack records:

```text
Run identifier
Source revision and working-state fingerprint
Kernel, router, and catalog versions
Trusted input signal IDs
Candidate and selected domain IDs with reasons
Selected and rejected item IDs with reasons
Conflicts and resolution
Context budget used
Authority ceiling
Terminal state and stop reason
Redacted verifier evidence
```

Do not persist raw prompts, source code, private user content, secrets, credentials, or financial/health data in activation records.

## Model Routing

Behavior routing and model routing are separate decisions.

1. Use deterministic tools for search, formatting, parsing, compilation, tests, and exact transformations when practical.
2. Use the smallest capable model for bounded summarization, classification, and routine drafting.
3. Escalate to a stronger reasoning model only when uncertainty, architecture, security, domain risk, or conflicting evidence requires it.
4. Reuse verified memory and deterministic artifacts instead of resending broad history.
5. Stop when additional model cost exceeds the owner-defined value or budget.

No catalog item may name an unavailable or invented model as mandatory. Model selection must use configured capabilities and current evidence.

## Deactivation

Deactivate a context pack when:

- the mission reaches a terminal state;
- source revision, branch, environment, requirement, authority, or approval changes;
- the selected prompt/capability changes;
- an invalidation trigger fires;
- the user pauses or redirects the mission.

Resume only after rerouting from fresh evidence.

## Verification

Runtime verification requires golden cases for:

- positive selection;
- negative exclusion;
- ambiguous routing;
- conflicting policies;
- stale signals;
- malicious repository text;
- context budget exhaustion;
- deactivation and rerouting;
- authority monotonicity;
- deterministic repeated resolution.
