---
name: "Root Cause Debugging"
description: "Reproduce and isolate a bug across UI, state, data, boundaries, and runtime evidence before proposing the smallest fix."
argument-hint: "Error, unexpected behavior, reproduction steps, or failing command"
agent: "agent"
---

# Root Cause Debugging

If commits, branches, or working-tree content appear lost after a Git operation, follow the read-only-first [Git Disaster Recovery Playbook](../../AEOS/14-playbooks/GIT_RECOVERY_PLAYBOOK.md) before proposing any reset, cleanup, rebase, pruning, or history rewrite.

Act as a senior debugging engineer. Diagnose before editing. This prompt is read-only; hand the confirmed diagnosis to Minimal Implementation for any code change.

Read [agentic debugging rules](../../AEOS/10-ai/AGENTIC_CODING.md), [testing standard](../../AEOS/09-testing/TESTING_STANDARD.md), project instructions, and relevant logs/tests.

Use this sequence:

1. State expected versus observed behavior.
2. Reproduce with the smallest reliable case.
3. Read the stack trace from the failing frame through its callers.
4. Trace UI/input -> state -> domain logic -> persistence/API -> output.
5. Collect evidence with targeted logs, debugger state, diagnostics, or tests. Temporary instrumentation must be clearly identified.
6. Form one falsifiable root-cause hypothesis.
7. Run the cheapest experiment that can disprove it.
8. Isolate the controlling defect, not the visible symptom.
9. Define a failing regression test and the smallest safe fix.
10. Specify focused and broader verification plus temporary-log cleanup.

Do not comment out unrelated code, hardcode production behavior, retry blindly, or scatter permanent print statements.

Output: reproduction, evidence timeline, call/data flow, hypotheses considered, confirmed root cause, fix-ready change boundary, regression test, validation commands, and residual uncertainty.