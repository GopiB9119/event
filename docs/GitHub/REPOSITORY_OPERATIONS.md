# GitHub Repository Operations

This runbook defines how `GopiB9119/event` should operate as the public source and distribution-control repository for Community Ledger. Repository settings and publication remain owner-gated actions.

## Verified Public Snapshot

GitHub API check on 11 July 2026:

- visibility: public; default branch: `main`
- Issues and Projects features: enabled
- Wiki: enabled; Discussions: disabled
- stars: 0; forks: 1
- community profile health: 14%
- workflows visible remotely: 0; workflow runs: 0
- releases: 0; public issues: 0
- Pages API: not configured (`404`)
- detected license/security/contributing/conduct/issue/PR templates: none
- branch protection: not publicly verifiable (`401`)
- public tree: 98 entries, not truncated; no private receipt fixtures, local SDK properties, keystores, APKs, or app bundles detected
- remote `.env.example`: fake Gemini placeholder, not a real credential, but stale and misleading; corrected locally
- remote `metadata.json`: still declares server-side Gemini; corrected locally
- remote manifest: still declares `communityledger.com` with App Link verification; corrected locally
- remote app version: `1.0 (1)`; local candidate is newer

This snapshot becomes stale after the governance files are pushed. Re-run the checks before making release claims.

The live repository must not be described as matching the current workspace until these corrected truth surfaces and workflows are reviewed and pushed.

## Selective Publication Safety

The working tree contains application, governance, site, and machine-specific IDE changes. Never publish it with a blind `git add .`.

Before each commit:

1. Review `git status --short --untracked-files=all`.
2. Stage only named project paths for the intended change.
3. Exclude `.idea/deviceManager.xml`, `.idea/deploymentTargetSelector.xml`, local deployment targets, build outputs, `.env`, `local.properties`, debug keystores, and private receipt fixtures.
4. Run `git check-ignore -v` for every private fixture directory.
5. Inspect the staged path list and staged diff before committing.
6. Run secret/PII and repository-required validation against the staged content.

Current private-fixture evidence: all real files under `app/src/androidTest/assets/receipt-images-private/` are ignored; only `.gitkeep` is intended for publication.

## Repository Purpose

GitHub owns:

- public source and reviewed history
- Issues and product evidence intake
- Pull Request review and CI evidence
- GitHub Project planning views
- security reporting policy and support routing
- GitHub Pages website source/deployment
- permanent release-signed APKs, checksums, fingerprints, and release notes

GitHub does not own user ledgers, receipt images, member data, signing secrets, passwords, or verification codes.

## License State

Community Ledger is dual-licensed under the MIT License or Apache License 2.0, at the recipient's option. The root `LICENSE` selects the two canonical texts in `LICENSE-MIT` and `LICENSE-APACHE`; `metadata.json` uses SPDX expression `MIT OR Apache-2.0`.

External contributions may be reviewed, but merge acceptance remains at the publisher's discretion. Unless explicitly stated otherwise, contributions intentionally submitted for inclusion use the same dual-license terms without additional conditions.

License changes are owner-gated release decisions. Do not silently replace, narrow, or expand these terms from dependency headers or generated metadata.

Do not infer the app's license from Gradle wrapper headers or dependency licenses.

## Owner Settings Checklist

### General

- Set description to: `Local-first Android event finance ledger with reviewed on-device receipt evidence.`
- Add topics: `android`, `kotlin`, `jetpack-compose`, `room`, `ml-kit`, `local-first`, `receipt-ocr`.
- After Pages deployment, set website to `https://gopib9119.github.io/event/`.
- Keep Issues and Projects enabled.
- Disable Wiki while documentation is versioned in-repository.
- Keep Discussions disabled until moderation/support capacity exists.
- Prefer squash merging and automatically delete merged branches.

### Actions

- Set default workflow permissions to read repository contents.
- Allow write permissions only inside workflows that declare them, such as Pages deployment.
- Run `Android CI` and `Site CI`; record the first green hosted runs.
- Keep `Deploy launch website` manual until the owner approves publication.
- Never store release passwords in repository files or workflow logs.

### Security

- Enable dependency graph, Dependabot alerts, and Dependabot security updates.
- Enable secret scanning and push protection when available.
- Keep private vulnerability reporting enabled if GitHub offers it for the repository.
- Route researchers through [SECURITY.md](../../SECURITY.md), not public Issues.

### Main Branch Ruleset

For a solo maintainer, avoid a rule that permanently requires another reviewer. Recommended baseline:

- require a Pull Request before merge
- require conversation resolution
- require `Android CI` for Android changes and `Site CI` for website changes after each has a green baseline
- block force pushes and branch deletion
- allow an explicit owner bypass for emergencies, with a follow-up issue and evidence

Revisit approval count when another active maintainer exists.

## Labels

Use a small stable label set:

| Label | Purpose |
|---|---|
| `bug` | Reproducible defect |
| `enhancement` | Product hypothesis, not automatic build approval |
| `documentation` | Documentation-only change |
| `security` | Public tracking only after private disclosure is safe |
| `receipt-ocr` | OCR/parser/evidence workflow |
| `data-integrity` | Ledger, Room, migration, duplicate, or calculation risk |
| `release` | Signing, CI, Pages, APK, or store work |
| `decision-needed` | Requires `STOP / DEFER / TEST / BUILD` review |
| `blocked` | Cannot proceed until named evidence/dependency exists |
| `good first issue` | Small, documented, non-sensitive work after licensing opens contributions |

Do not create labels for every component. Use Project fields for priority, status, and decision state.

## GitHub Project

Create one owner-level or repository Project named **Community Ledger Delivery**.

Fields:

| Field | Values |
|---|---|
| Status | Inbox, Decision needed, Ready, In progress, Review, Blocked, Done |
| Priority | P0 Integrity, P1 Launch, P2 Product, P3 Later |
| Decision | STOP, DEFER, TEST, BUILD |
| Area | Product, Android, OCR, Data, Security, Website, Release, Docs |
| Evidence | Unknown, Assumption, Supported, Verified |
| Milestone | Public beta, Post-beta learning, Future shared events |

Saved views:

1. **Launch Gate:** P0/P1 items not Done, grouped by Status.
2. **Decision Queue:** Decision needed or Evidence not Verified.
3. **Engineering Board:** BUILD items grouped by Status, excluding Future shared events.
4. **Security And Data:** Security/Data areas, sorted by Priority.
5. **Later / Deferred:** DEFER and STOP items, hidden from active delivery views.
6. **Completed Evidence:** Done items with validation links.

Automation:

- new Issues enter Inbox
- Pull Requests enter Review
- merged Pull Requests move linked items to Done only after required checks pass
- closed-without-build issues retain their STOP/DEFER decision instead of being deleted

Use [AEOS project backlog](../../AEOS/project/BACKLOG.md) as the initial source. Do not create a Project item for speculative features without a problem/evidence issue.

## Issue And Pull Request Flow

Development model: trunk-based. Create one short-lived branch per issue/decision, open a Pull Request into `main`, require applicable checks, squash merge, and delete the merged branch. Do not maintain parallel long-lived release/develop branches until a real need exists.

### Bug

Issue form -> reproduce with synthetic/redacted data -> severity/data-integrity assessment -> smallest fix -> focused test -> Pull Request -> CI/review -> close with evidence.

### Product Idea

Feature form -> Idea Validation -> `STOP / DEFER / TEST / BUILD` -> Project decision field -> only BUILD enters implementation planning.

### Security

Private email/policy -> triage privately -> prepare fix/advisory -> coordinated disclosure -> public issue only when safe.

### Pull Request

Every PR links its issue/decision, states scope/non-goals, lists data/security impact, and supplies executable evidence. Repository owner review does not replace CI.

## Website Deployment

1. Push the governance/site workflows.
2. Confirm `Site CI` passes on GitHub.
3. In repository Settings -> Pages, select **GitHub Actions** as the source.
4. Run **Deploy launch website** manually.
5. Verify Home, Privacy, Terms, Contact, event-copy fallback, screenshots, `releases/latest.json`, and `releases/community-ledger-app.json` over HTTPS.
6. Set repository website URL only after the checks pass.

Each package-specific release manifest must remain `available=false` until a permanent release-signed APK with that exact application ID is uploaded and verified. Never repoint the old-package `latest.json` to `com.communityledger.app`.

## Dependency Review

- Dependabot opens bounded weekly Gradle and GitHub Actions Pull Requests.
- Security updates are reviewed before the next public release and immediately when verified impact threatens a published artifact or user data.
- Regular updates are grouped by evidence and compatibility risk; do not merge automatically merely because CI passes.
- Run the applicable Android and website gates before merge.
- Record why a vulnerable dependency is accepted, mitigated, or deferred.

## Release Gate

Do not create a public release from the debug APK.

1. Confirm Mission Brief permits the release and all physical-device gates pass.
2. Build with the permanent release key entered through a secure environment.
3. Verify tests, APK signature, certificate fingerprint, alignment, SHA-256, install/update, cold launch, OCR review, and persisted ledger.
4. Prepare release notes and [CHANGELOG.md](../../CHANGELOG.md).
5. Create a draft GitHub Release and upload the stable APK asset.
6. Verify the final asset URL and checksum.
7. Update only the manifest assigned to the release package: `site/releases/latest.json` for the historical old package or `site/releases/community-ledger-app.json` for `com.communityledger.app`.
8. Re-run Site CI and manually deploy Pages.
9. Verify the in-app manual update check against the live manifest.

Publishing, tagging, deploying, or changing signing secrets requires explicit owner approval in the current turn.

## Insights And Review Cadence

Review monthly or before each release:

- Actions failures and duration trends
- dependency updates and security alerts
- issue age, reopen rate, and unresolved P0/P1 items
- release downloads only after a release exists
- contributor activity without interpreting stars/forks as product demand
- Project items that remain In progress or Blocked without new evidence

Do not invent retention, adoption, reliability, or market conclusions from repository traffic alone.

Positive evidence comes from completed physical-device matrices, reproducible Issues, test/release history, support patterns with privacy preserved, and consented organizer research. Stars, forks, views, and clone counts are repository-interest signals only.

## Incident Response

Before enabling a public release, review [Security And Data-Integrity Incident Response](../Security/INCIDENT_RESPONSE.md). A security incident may require pausing downloads or Pages, but installed APKs cannot be remotely disabled and uninstall rollback deletes local data.