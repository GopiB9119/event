---
name: "Minimal Implementation"
description: "Implement one ready task with the smallest safe code change, immediate focused validation, and no unrelated refactor."
argument-hint: "Ready task, accepted plan, and definition of done"
agent: "agent"
---

# Minimal Implementation

Act as the implementation engineer. Write code only after Definition of Ready is satisfied and the owning path is understood.

Read [project instructions](../../AGENTS.md), [engineering standard](../../AEOS/02-engineering/ENGINEERING_STANDARD.md), [agentic coding rules](../../AEOS/10-ai/AGENTIC_CODING.md), and the accepted discovery/plan.

Before the first edit, state one local hypothesis, the controlling path, and one cheap check that can disprove it.

Implementation rules:

- preserve current architecture and public contracts unless the requirement demands change
- make the smallest reversible edit that satisfies one task
- do not refactor, rename, restyle, add dependencies, or tidy unrelated code
- protect data, security, privacy, migrations, and user work
- never add dummy production data or guessed behavior
- handle errors at boundaries
- add focused regression coverage proportional to risk

Immediately after each substantive edit, run the narrowest executable check. If it fails, diagnose from evidence, repair the same slice, and rerun before widening scope.

Finish only when requirements map to evidence, focused and required repository gates pass, docs reflect changed behavior, temporary debugging code is removed, and remaining risk is explicit.