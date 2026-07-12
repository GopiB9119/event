# AEOS Governance Validation Loop Pilot

## Purpose

This is the first executable adapter for the [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md). It proves a narrow goal-based loop before AEOS adds schedulers, event listeners, remediation agents, or external side effects.

The pilot validates governance. It does not repair failures.

## Contract

The version-controlled source of truth is [aeos-governance-validation.loop.json](contracts/aeos-governance-validation.loop.json). The runner rejects unknown or duplicate properties, scalar substitutes for arrays, malformed budgets, path changes, retries, and verifier IDs outside its hardcoded catalog. It also pins the reviewed Windows PowerShell 5.1-canonical semantic SHA-256, so an authority or policy amendment requires an explicit runner-anchor update. This is a local acceptance anchor, not a digital signature or a cross-platform canonicalization format. Contract JSON never supplies executable commands or output regular expressions.

```text
Mission: Validate the AEOS governance corpus with deterministic local checks.
Decision state: TEST
Trigger: goal, manually invoked
Scope: AEOS documents, workspace prompt contracts, local links, universal-core product leakage
Non-goals: repair, source edits, network access, scheduling, publishing, deployment
Entry evidence: validate-aeos.ps1 exists and previously passed interactively
Protected invariants: read-only verification, unchanged HEAD/status/content fingerprint, fixed verifier allowlist, safe persisted output, no blind retry
Permitted tools: Windows PowerShell, Git read-only status/revision, validate-aeos.ps1
Permitted mutation: one ignored JSON evidence record under build/aeos-loop-runs/
Success predicate: validator self-test and full validator both exit 0
Iteration budget: 1
Time budget: 60 seconds for the complete run
Checkpoint: the generated JSON evidence record
Human approval gates: none for this local read-only run; any repair is a separate mission
Contract owner: repository owner
Amendment authority: repository owner or explicitly delegated accountable owner
Failure owner: engineering coordinator
```

The one-iteration budget is deliberate. This adapter has no evidence-producing remediation step, so rerunning the same failed verifier would be a blind retry. A failed run terminates and hands the evidence to a separate turn-based diagnosis mission.

The 60-second watchdog is specific to this small local validator. It is not an AEOS default for other loops.

## Commands

Validate the runner state machine and watchdog:

```powershell
.\scripts\invoke-aeos-governance-loop.ps1 -SelfTest
```

Run the pilot:

```powershell
.\scripts\invoke-aeos-governance-loop.ps1
```

## Terminal States And Exit Codes

| State | Exit | Meaning |
|---|---:|---|
| `PASSED` | 0 | Both allowlisted checks passed and evidence was written. |
| `FAILED` | 2 | A validator returned a non-zero result. No retry or repair occurred. |
| `EXHAUSTED` | 3 | The fixed time/iteration budget was exhausted. |
| `BLOCKED` | 4 | Preconditions such as repository state, verifier, or PowerShell runtime were unavailable. |

Any other state exits with code 5 and is a runner defect.

## Evidence

Each run creates an ignored local JSON record containing:

- contract and run identifiers
- source HEAD and a hash of working-tree status, without listing file names
- a content fingerprint over tracked and non-ignored untracked files, without persisting names or content
- matching before/after source fingerprints; unexpected drift terminates as BLOCKED
- start/completion times and duration
- iteration count and check outputs
- terminal state and residual risk

Raw stdout is stored only when it matches that allowlisted verifier's safe-success pattern and stderr is empty. Otherwise the record stores only output hashes and character counts; diagnosis reruns the validator interactively as a separate turn-based mission. Future verifiers require an explicit safe-output pattern and security review before joining the allowlist.

The runner verifies with `git check-ignore` that repository evidence is excluded. If that precondition fails, the run is `BLOCKED` and its record falls back to the operating-system temp directory.

## Failure Handling

- `FAILED`: inspect output hashes/lengths, rerun the fixed validator interactively in a bounded diagnosis mission, and do not rerun the loop until evidence or source state changes.
- `EXHAUSTED`: inspect which check exceeded the watchdog; the owner decides whether to optimize the check or amend the contract.
- `BLOCKED`: restore the missing precondition or escalate to the contract owner.
- Runner defect: stop using the adapter until its self-test and focused review pass.

## Boundaries

This pilot does not implement a general command runner. The JSON contract selects only versioned verifier IDs; paths, arguments, and safe-output patterns remain fixed in source to prevent command injection and authority expansion. A dirty working tree is allowed, but tracked and non-ignored untracked content must remain unchanged during the run. The watchdog terminates the immediate verifier process only; future verifiers that spawn child processes require process-tree cleanup. The pilot does not run in CI, on a schedule, or from an event. Those adapters require authenticated triggers, concurrency policy, cancellation, retention, and operational ownership first.

## Related

- [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md)
- [Operating Model](../00-foundation/OPERATING_MODEL.md)
- [Context And Memory](../10-ai/CONTEXT_AND_MEMORY.md)
- [Quality Gates](../13-review/QUALITY_GATES.md)
