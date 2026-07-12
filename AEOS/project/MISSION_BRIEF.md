# Community Ledger Mission Brief

Last decision review: 12 July 2026

Owner: Gopi Banoth

## Mission

Decision question: May Community Ledger move from a small trusted-device beta to broad public Android distribution?

Requested outcome: Ship a trustworthy local organizer ledger with a permanent signed update path, truthful public website, and evidence that core financial-record flows survive real devices and lifecycle failures.

Why now: The signed limited prerelease and public trust package are live. The next decision is whether evidence supports broader reliance beyond voluntary beta testing.

## Product Context

One-sentence product idea: Community Ledger helps a trusted organizer maintain a local event-money ledger backed by reviewed payment receipt evidence.

Target user and context: One community/event organizer or one shared physical device maintaining a local record of money in, money out, and supporting receipt evidence.

Current maturity: Limited public beta prerelease.

Product promise affected: Honest, local-first event records with evidence before ledger mutation.

Current milestone: Beta Reliability Validation.

## Evidence Ledger

| Claim | Label | Evidence | Consequence if wrong |
|---|---|---|---|
| The debug app compiles and all current JVM tests pass. | `VERIFIED` | [Test Strategy](../../docs/Testing/TEST_STRATEGY.md) and [Friend Beta Readiness](../../docs/Release/FRIEND_BETA_READINESS.md) | Public builds may contain known regressions. |
| The permanent release-signed `0.2.0-beta.2` candidate verifies, installs fresh and over beta.1, launches, and cold-relaunches on API 36. | `VERIFIED` | [Beta.2 Release Notes](../../docs/Release/0.2.0-beta.2.md) and [Friend Beta Readiness](../../docs/Release/FRIEND_BETA_READINESS.md) | The staged artifact, migration, or signing lineage may be invalid before physical testing. |
| API 36 Room/share/OCR tests pass, including private real-image fixtures. | `VERIFIED` | [Friend Beta Readiness](../../docs/Release/FRIEND_BETA_READINESS.md) | Ledger or OCR behavior may fail on the tested environment. |
| Branding, Trust Center, legal/data documents, support identity, and responsive site exist locally. | `VERIFIED` | [Public Launch Checklist](../../docs/Release/PUBLIC_LAUNCH_CHECKLIST.md) | Public trust claims or contact paths may be missing. |
| Public downloads resolve only to the release-signed `0.2.0-beta.2` GitHub asset, whose public bytes match the recorded SHA-256. | `VERIFIED` | [Release manifest](../../site/releases/latest.json) and [Beta.2 Release Notes](../../docs/Release/0.2.0-beta.2.md) | Users could install an unintended or corrupted artifact. |
| The final flow works on the publisher's physical phone. | `UNKNOWN` | Physical-device gate not recorded. | Users may hit picker/share/restart failures not reproduced on the emulator. |
| The final flow works across a second Android device/version. | `UNKNOWN` | Second-device gate not recorded. | Compatibility claims exceed evidence. |
| Hosted CI, Pages, and GitHub prerelease behave as designed. | `VERIFIED` | Green Android/Site/Pages runs, HTTPS route checks, anonymous APK download, and public release/tag metadata recorded on 12 July 2026. | Public download, update, or event-copy fallback can fail after launch. |
| Target organizers repeatedly need and will adopt this local workflow. | `ASSUMPTION` | No durable interview, behavior, retention, or support evidence is recorded. | The team may polish a product without demand. |
| Current legal text is sufficient for every intended launch jurisdiction. | `UNKNOWN` | No qualified legal review is recorded. | Publisher obligations or disclosures may be incomplete. |

## Alternatives

1. Do nothing: keep the verified APK private and avoid creating a public support/update obligation.
2. Continue trusted beta only: collect physical-device and user-behavior evidence before permanent distribution.
3. Publish the debug APK: rejected because it creates an impermanent signing lineage and weakens trust.
4. Public beta with release signing, physical tests, hosted checks, and limited claims: preferred next launch path.
5. Build synchronized shared events before launch: deferred; it is a separate high-cost product requiring authentication, authorization, sync, conflict resolution, privacy operations, and validation.

## Economics And Risk

Expected outcome: A repeatable, supportable public beta install/update path and observed evidence that organizers can safely complete the local ledger workflow.

Creation cost: release-key ceremony, physical-device tests, hosted CI/Pages/Release verification, release notes/checksums, and jurisdiction review.

Recurring cost: support inbox/issues, dependency/platform updates, release signing custody, privacy/legal maintenance, and incident response for distributed APKs.

Opportunity cost: launch work delays export/restore and broader OCR coverage; synchronized features would delay core local reliability much more.

Security/privacy/ethics: Never imply organizer verification, synchronized membership, money custody, investment, access control, or guaranteed OCR accuracy. Do not publish signing secrets or receipt PII.

Reversibility: Website copy and an unpublished manifest are reversible. The first permanent signing key and public APK create a durable compatibility commitment. Uninstall rollback deletes local data.

## Decision

Most dangerous assumption: Emulator evidence predicts the complete real-phone share, review, save, restart, and update experience.

Cheapest falsification test: Install the exact candidate on one physical phone; create an event, import/share a real receipt, review/save/cancel, force-stop/restart, verify totals and evidence, then repeat the smoke path on a second device/version. Any silent mutation, data loss, blocked install/update, or misleading state fails the gate.

Verdict: `DEFER` broad public distribution.

Permitted next action: `TEST` the exact signed release on physical devices, obtain qualified legal review for intended jurisdictions, and collect consented beta-use evidence. Do not expand public claims beyond the limited-beta wording.

Why: Core local integrity and public infrastructure have strong evidence, while real-device breadth, legal scope, and user-demand assumptions remain unverified.

Evidence that changes the broad-distribution verdict to `BUILD`: completed physical-device matrix, signed install/update verification on real devices, documented legal review scope, and observed target-user value.

Independent Decision Challenge required before changing this verdict to `BUILD`: **yes**. The reviewer must reopen the evidence above and may not rely on the proposing agent's self-audit.

## Definition Of Ready For Public Release Work

- [x] Product scope and non-synchronized event-copy behavior are explicit.
- [x] Current architecture, data ownership, trust boundaries, and release flow are mapped.
- [x] Public claims and prohibited claims are documented.
- [x] Automated debug and API 36 evidence is recorded.
- [x] Permanent release key custody and backup are confirmed.
- [ ] Physical-device acceptance matrix is complete.
- [x] Hosted CI/deployment evidence is complete.
- [ ] Target launch jurisdictions and legal review scope are explicit.

## Definition Of Done

- [ ] Exact release-signed APK passes all automated and physical-device gates.
- [x] GitHub prerelease contains APK, version, SHA-256, signing fingerprint, notes, and limitations.
- [x] Pages, Privacy, Terms, Contact, event-copy fallback, and update manifest pass over HTTPS.
- [x] Installed public APK manual update check read the live manifest after an explicit tap and reported `0.2.0-beta.2` current; no background polling or silent install occurred.
- [x] Documentation, decision log, knowledge graph, and project memory match the public artifact.
- [x] Rollback/data-loss limits and residual risks are visible to users.