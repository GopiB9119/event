# Git Disaster Recovery Playbook

## Purpose

Recover apparently lost Git work without destroying the evidence needed to find it. Missing branch history does not prove that commits or file content are gone.

This playbook is read-only until an accountable owner approves a specific recovery mutation.

## Stop Destructive Activity

During investigation, do not run:

- `git reset --hard`
- `git clean`
- `git gc`
- `git prune`
- destructive or interactive rebase
- branch deletion
- force push
- commands that expire reflogs or rewrite shared history

Stop automated writers, formatters, release jobs, and agents that can mutate the affected repository. Do not assume a remote is newer or safer than the local object database.

## Incident Record

Record before recovery:

```text
Repository and worktree path:
Observed problem:
Approximate last known good action/time:
Current branch and HEAD:
Working-tree and index state:
Recent command or automation history:
Remotes and fetch/push activity:
Potentially affected files/commits:
Evidence preservation location:
Recovery owner and approval boundary:
```

Never include credentials or private file content in the incident record.

## Preserve Evidence

1. Avoid changing the affected worktree.
2. Record `git status --short --branch`, `git rev-parse HEAD`, configured worktrees, branches, tags, remotes, and stashes.
3. Preserve a filesystem-level copy of the repository, including `.git`, on storage with sufficient access control and space.
4. Record the source repository path, copy time, and current HEAD for the preserved copy.
5. Perform deeper object inspection on the preserved copy when possible.

A normal clone or bundle may omit unreachable objects. Do not treat it as a complete forensic copy unless that limitation is acceptable and verified.

## Recovery Order

### 1. Locate Named References

Inspect local branches, tags, worktrees, stashes, and remote-tracking references. The work may already be reachable under another name.

### 2. Inspect Reflogs

Use reflogs for `HEAD`, branches, and other local references to identify the state before reset, rebase, branch deletion, checkout, or amend operations. Reflogs are local and retention varies; do not assume another clone has the same entries.

For each candidate object:

- inspect metadata and parent chain
- inspect the patch or tree
- compare it with current and intended state
- record why it is a recovery candidate

Do not check out an unverified object over the affected worktree.

### 3. Anchor The Candidate

After review, create a clearly named recovery reference pointing at the candidate object, such as a recovery branch. Creating the reference prevents the object from remaining unreachable while evaluation continues.

Reference creation is a repository mutation and requires the mission's approval boundary. It must not replace or move the current protected branch.

### 4. Search Unreachable Objects

If reflogs do not contain the work, run read-only object consistency inspection on the preserved copy to list unreachable commits, trees, and blobs. Inspect candidates with object-reading commands before writing recovered files or references.

Do not run garbage collection or pruning. Uncommitted editor buffers, ignored files, and some working-tree changes may never have entered Git's object database; check editor local history, filesystem snapshots, backups, CI artifacts, and approved recovery systems separately.

### 5. Reintegrate Deliberately

Choose the smallest reviewed recovery method:

- keep the recovered branch for manual comparison
- cherry-pick a verified commit
- restore selected paths into a separate worktree
- reconstruct a commit from a verified tree
- revert a destructive shared change rather than rewriting published history

Do not merge, cherry-pick, reset, or force-push automatically. Preserve unrelated current work and use a separate worktree when histories or owners are ambiguous.

## Verification

Before declaring recovery complete:

- compare recovered content with the recorded last-known-good behavior
- inspect the complete diff and parent history
- run repository-required build, test, lint, security, and migration checks
- verify no unrelated current changes were overwritten
- verify secrets, private data, generated files, and ignored artifacts were not accidentally added
- record the new reference/commit, commands used, evidence location, and residual uncertainty

Recovery success means the intended work is reachable and verified. It does not mean history should be rewritten to look as if the incident never happened.

## Autonomous-Agent Rules

- Enter a bounded goal loop only for read-only discovery and verification.
- Stop as `BLOCKED` before the first recovery mutation unless the contract names the exact allowed action and owner approval.
- Treat unexpected worktree changes as potentially user-owned.
- Never delete an alternate candidate merely because another candidate appears correct.
- Never publish recovered history or alter a remote without explicit current approval.
- After resolution, update the incident record, durable lesson, and preventive guardrail; do not store private recovered content in general project memory.

## Quality Gate

Recovery is complete only when evidence was preserved before mutation, the chosen object/content was independently inspected, reintegration was explicitly approved and minimal, required repository validation passes, unrelated work remains intact, and the incident record explains what was recovered and what may still be missing.

## Related

- [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md)
- [Operating Model](../00-foundation/OPERATING_MODEL.md)
- [Context And Memory](../10-ai/CONTEXT_AND_MEMORY.md)
- [Runbook Template](../12-templates/RUNBOOK_TEMPLATE.md)
- [Postmortem Template](../12-templates/POSTMORTEM_TEMPLATE.md)
