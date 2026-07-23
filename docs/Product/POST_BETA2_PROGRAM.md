# Post-Beta.2 Product Program

Last reviewed: 13 July 2026

## Status

This is the proposed execution program after `0.2.0-beta.2`. It is not a production-release approval, cloud-resource approval, legal opinion, or delivery guarantee.

The active first-horizon weekly sequence and performance targets are detailed in the [Four-Month Execution Plan](FOUR_MONTH_EXECUTION_PLAN.md).

Planning durations are `ASSUMPTION` estimates for sequencing. They must be recalibrated from completed-slice evidence. Quality gates, not elapsed time, decide when a milestone is done.

Decision state:

- `BUILD`: the smallest local-product quality, accessibility, localization, documentation, and verification slices.
- `TEST`: an authenticated server-event contract in local Firebase emulators after the owner decision packet and independent security review.
- `DEFER`: production synchronization, Play production, cloud billing/resource creation, receipt-image upload, and synchronized-ledger claims until their gates pass.
- `STOP`: cosmetic sync, unverified private-event claims, weakened receipt gates, dummy financial data, cloud/LLM OCR fallback, and broad rewrites justified only as polish.

## Accountability And Approval

- **Accountable product and publisher owner:** Gopi Banoth. He chooses product scope, release priority, supported countries, budget, and whether a reviewed candidate may be published.
- **Engineering coordinator:** sequences slices, enforces readiness, and integrates evidence; this role cannot approve its own material risk exception.
- **Implementation owner:** may change only the files and behavior named by one approved slice.
- **Independent verifier:** may fail acceptance using a violated invariant, test, security rule, accessibility criterion, or documented requirement; it does not silently repair the work it judges.
- **Qualified human gates:** legal scope and translated legal text require qualified legal review; Telugu and Hindi financial/safety terminology require fluent human reviewers named by the publisher; production security/privacy risk acceptance requires accountable human owners.
- **External actions:** cloud creation, billing, permanent region selection, deployment, protected-branch merge, GitHub release, Pages deployment, Play submission, data deletion, and secret/key changes require separate current approval.

If an owner or reviewer is not named, the affected side effect remains `BLOCKED`; silence never becomes approval.

## Problem Contract

### Goal

Move Community Ledger from a verified but limited local beta to a professional, multilingual, accessible event-ledger product, then add real shared events without sacrificing ledger integrity, privacy, or explainability.

### Beneficiary

Community and event organizers who need a clear record of money in, money out, members, and reviewed receipt evidence. A later shared-event product also serves invited contributors and viewers with explicit roles.

### Current State

- The signed beta.2 app, GitHub release, website, update manifest, Room migration, receipt review, and event-copy identity are verified.
- Each installation still owns an independent ledger.
- Event-copy links copy metadata only; they do not create membership or synchronize data.
- Local email is an ownership/uploader label, not authentication.
- Only `app_name` uses Android string resources; the primary Compose UI contains more than 150 directly embedded English strings.
- The five-page website is English-only.
- No export, restore, account recovery, server authorization, or cloud backup exists.
- Complete two-phone physical evidence and qualified legal review remain open.

### Desired State

1. The local product is calm, consistent, professional, and understandable on small and large Android screens.
2. Dashboard, balance, events, public/private markers, members, messages, receipt scanning, review, Trust Center, and help use one coherent interaction and visual system.
3. The app supports English, Telugu, and Hindi without clipping, mixed-language leftovers, or untranslated critical states.
4. Public marketing/help pages support the same languages. English remains the authoritative legal source until qualified reviewers approve translated legal text.
5. Receipt extraction remains ML Kit-only, evidence-driven, and human-reviewed.
6. A future shared event uses verified accounts, server authorization, revocable membership, idempotent financial writes, visible pending/conflict state, and immutable audit history.
7. No release calls a feature synchronized, private, secure, current, or production-ready without matching executable and runtime evidence.

### Acceptance

The program succeeds only through milestone-specific evidence. The final shared-event claim requires two clean authenticated devices to converge on the same server-confirmed metadata, members, activity, entries, totals, and audit history while unauthorized and revoked access fails closed.

## Conservative Defaults While Owner Input Is Unavailable

- Improve the local product and its verification foundation before introducing remote writes.
- Keep Material 3 and a restrained palette. Use color for meaning, not decoration.
- Use a Firebase server-authoritative design as the first shared-event candidate, but create no cloud resources until ownership, billing, region, retention, abuse, recovery, and incident decisions are approved.
- Use Google Sign-In as the first authentication candidate.
- Synchronize reviewed structured evidence only. Receipt images remain local in the first shared release.
- Translate the app plus public product/help pages into English, Telugu, and Hindi. Keep legal pages English-authoritative until reviewed translations exist.
- Keep local event copies and future shared invitations as visibly separate actions and data models.

## Product Truth That Must Remain Visible

### Local Event Copy

- Opens an independent local shell.
- Does not add a member.
- Does not update later.
- Public/private remains a local marker.
- Uses the current opaque copy key only for collision-safe local identity.

### Future Shared Invitation

- Requires sign-in before event details are disclosed.
- Uses a server-issued, expiring, revocable secret.
- Creates server-side membership with a role.
- Never uses a Room ID, local event-copy key, email label, or title as authority.
- Does not become public until the full shared-ledger acceptance contract passes.

### Coexistence And Migration

- Existing beta.2 event-copy links and imported shells remain local-only and continue to use their current validation rules.
- A future shared invitation uses a different server-issued route and token contract. It never upgrades a local-copy URL into membership.
- Local events and imported shells are never matched to server events by title, email label, Room ID, or local copy key.
- Moving a local ledger to a shared event is a separate, explicit action with a field/entry preview, cancellation, server confirmation, and duplicate-safe migration evidence.
- Deprecating local-copy links requires a later owner decision and migration/support plan; no cutoff is assumed here.

## Horizon A: Professional Multilingual Local Product

Planning estimate: weeks 1-20. This is the first shippable program and does not depend on a backend.

### Milestone 0: Governance And Baseline

Planning estimate: week 1.

Outcomes:

- Establish `MIT OR Apache-2.0` dual licensing and align contribution terms.
- Preserve beta.2 artifact, signing, migration, and live-channel evidence.
- Capture baseline screenshots and critical-path behavior before visual changes.
- Record the screen inventory, hardcoded-string inventory, accessibility gaps, and current physical-device evidence.

Done when:

- License selector, canonical texts, metadata, README, contribution policy, and governance docs agree.
- AEOS validation and documentation diagnostics pass.
- No source behavior changes are mixed into the licensing slice.

### Milestone 1: UX Contract And Design Foundation

Planning estimate: weeks 2-4.

Outcomes:

- Define typography, spacing, elevation, shape, semantic colors, icons, focus, disabled, loading, empty, success, warning, and error states.
- Define one primary action per screen and reduce competing card/button emphasis.
- Split reusable screen sections from the large `Screens.kt` only when a vertical slice proves the boundary; do not perform a broad component rewrite.
- Establish a screenshot matrix for phone widths, font scaling, light/dark mode, and all three languages.

Done when:

- The design contract is documented and represented by reusable theme tokens.
- Existing behavior remains unchanged.
- Baseline dashboard and event-card screenshot tests or deterministic captures exist.
- Normal text contrast is at least 4.5:1; large text and meaningful non-text controls are at least 3:1 unless a stricter platform rule applies.
- Interactive targets are at least 48dp where the platform permits, have meaningful TalkBack labels, and expose visible focus/disabled state.
- Each screen or dialog has one highest-emphasis action; secondary, dismiss, and destructive actions use distinct lower/semantic emphasis.
- Screen code uses approved theme tokens instead of new decorative raw color literals. The palette is neutral surfaces plus one brand accent and semantic success/warning/error roles.
- At the smallest test viewport and 200% font scale, no critical value or action clips, overlaps, or becomes unreachable.

### Milestone 2: Localization And Accessibility Foundation

Planning estimate: weeks 5-7.

Outcomes:

- Move user-facing strings from Compose code into Android resources.
- Add English, Telugu, and Hindi resources with parameterized strings and plurals.
- Add an app-language control with a system-default option.
- Define plain-language terminology for Money in, Money out, Balance, Member, Receipt, Local copy, Pending, Confirmed, and Shared event.
- Complete TalkBack labeling and traversal for the first localized vertical slice: first-use disclosure, local identity, Dashboard, event cards, and Create Event.

Done when:

- No mixed English remains in the selected vertical slice under Telugu or Hindi.
- Long strings, 200% font scale, screen readers, and smallest supported viewport do not hide actions or values.
- Locale switching survives restart and does not mutate ledger data.
- All safety warnings, destructive confirmations, ledger-impact labels, validation errors, privacy limitations, and primary actions are release-blocking strings; untranslated critical text blocks that locale from the candidate.
- Financial and safety meaning is identical across languages and signed off by one fluent Telugu reviewer and one fluent Hindi reviewer named by the publisher before release. Machine translation alone is not acceptance evidence.
- English remains the source text. Translation changes record the English source revision they reviewed so later copy changes invalidate stale translations.

### Milestone 3: Dashboard, Balance, And Event Cards

Planning estimate: weeks 8-10.

Outcomes:

- Make Available Balance the clear primary number, with Total Collected and Total Spent as supporting facts.
- Improve empty, filtered-empty, loading, and error states.
- Make public/private markers visually distinct without implying server access control.
- Improve event-card hierarchy, date/duration readability, destructive-action placement, and touch targets.
- Keep filters compact and stable as counts or translations change.

Done when:

- All/public/private filtering and event deletion still preserve current behavior.
- Card content fits at the smallest viewport and 200% text scale in all languages.
- Balance and event counts match Room-derived data after restart.
- Dark/light screenshots pass visual review without one-color dominance or inaccessible contrast.

### Milestone 4: Create Event And Event Details

Planning estimate: weeks 11-13.

Outcomes:

- Clarify required and optional fields and inline validation.
- Improve custom-field entry, editing, removal, and narrow-screen layout.
- Organize Event Details into balance, members, activity, and event information without nested-card clutter.
- Add transaction search/filter only if the repeated-ledger navigation test proves it is needed.
- Preserve current local-only semantics and Room ownership.

Done when:

- Create, cancel, validation failure, save, reopen, edit-related display, and delete paths are exercised.
- Custom fields remain associated with the correct event.
- Event totals, utilization, member counts, and transaction counts reconcile with persisted rows.
- Process restart returns to a truthful recoverable state even if active navigation is not restored yet.

### Milestone 5: Members, Activity, Invite Copy, And Messages

Planning estimate: weeks 14-15.

Outcomes:

- Improve member cards and profiles around role, confirmed receipt count, Money in, Money out, and related activity.
- Use anonymized/minimized identifiers where full contact details are unnecessary.
- Rewrite event-copy messages in clear, short language for all three locales.
- Keep `Share local copy` separate from future `Invite to shared event` in wording, iconography, and help content.
- Improve link expiry, copy, WhatsApp/share-sheet success, invalid, expired, and unsupported-app states.

Done when:

- Reopening one opaque-key link returns to one shell and cannot replace another ledger.
- An expired or malformed link fails with a useful localized message.
- No current message promises membership, authorization, live updates, or organizer verification.
- Member statistics reconcile with persisted transactions.

### Milestone 6: Receipt Scan, Processing, And Review

Planning estimate: weeks 16-17.

Outcomes:

- Keep receipt processing calm and neutral, with no fake terminal activity or artificial delay.
- Improve image guidance, read-only amount emphasis, evidence source, missing-field explanation, Money in/out selection, ledger-person attribution, duplicate, cancel, retry, and interruption states.
- Broaden private real-image coverage across supported payment apps and poor-image conditions.
- Measure OCR time and memory before optimizing; do not weaken amount or duplicate gates to increase apparent success.

Done when:

- Every saved amount comes from reliable receipt-derived evidence and remains read-only during review.
- App-private JSON is written before the Room mutation; failure leaves totals unchanged.
- Reliable labelled, weak/unlabelled, manually sourced, duplicate, non-receipt, missing-amount, process-death, and database-failure paths pass focused checks.
- Receipt image/text and payment identifiers never enter public screenshots, logs, repository fixtures, or analytics.

### Milestone 7: Trust Center, Help, Brand Assets, And Website

Planning estimate: weeks 18-19.

Outcomes:

- Reorganize Trust Center into concise About, Data and privacy, Updates, Limitations, Help, and Support content while keeping legal text accurate.
- Add visual receipt guidance and task-based help using privacy-safe screenshots.
- Refine the existing launcher mark and create a consistent, small icon/asset set rather than decorative illustration overload.
- Expand the website with product truth, install/update help, receipt guidance, shared-event status, accessibility, security reporting, and release evidence.
- Localize public product/help pages into English, Telugu, and Hindi with proper language navigation and metadata.

Done when:

- Every link, asset, locale route, manifest field, and download fallback passes static validation.
- Mobile and desktop screenshots show no overflow or overlap.
- App and site make the same claims.
- Privacy, Terms, and Data safety translations are not presented as legally reviewed until qualified reviewers approve them.
- The asset inventory identifies source, purpose, dimensions/density, alt text or content description, privacy review, and where each app/help/site asset is used.

### Milestone 8: Device Matrix And Release Candidate

Planning estimate: week 20.

Outcomes:

- Run the exact signed candidate through two physical-device critical-path matrices.
- Exercise clean install, beta.2 upgrade, locale switching, image/text share, receipt review/save/cancel, restart, link handling, deletion, update check, offline behavior, and accessibility.
- Complete an independent completion, security, privacy, and release audit.

Done when:

- Required compile, JVM, instrumentation, static-site, AEOS, and artifact checks pass.
- Two real-device records exist with no unexplained crash, ANR, data loss, silent mutation, wrong total, or blocked upgrade.
- No open severity-0 or severity-1 finding remains.
- Remaining limitations are visible in-app, on the site, and in release notes.
- The publisher separately approves any GitHub release, Pages deployment, or Play submission.

## Horizon B: Authenticated Shared-Event Alpha

Planning estimate: weeks 21-32. This horizon remains blocked from production resources until owner approvals are recorded.

### Milestone 9: Owner Decision Packet And Emulator Contract

Planning estimate: weeks 21-24.

Required owner decisions:

- Firebase project and billing owner.
- Permanent data region and initial launch countries.
- Google Sign-In recovery and device/session revocation policy.
- Organizer, contributor, and viewer role rules.
- Account, audit, idempotency, and structured-evidence retention.
- Deletion, export, recovery, abuse limits, cost alerts, support, and incident owners.

Build only after approval:

- Firebase Emulator Suite configuration without production credentials.
- Test identity provider and authentication abstraction.
- One server-created event, organizer membership, authorized read projection, and fail-closed Security Rules tests.

Done when unauthenticated, non-member, revoked, and direct protected writes fail in emulator tests and an independent security reviewer accepts the contract.

### Milestone 10: Account, Server Event, And Read-Only Membership

Planning estimate: weeks 25-28.

- Add authenticated account/session UI and secure token handling.
- Add server event identity and local Room cache mapping through an explicit migration.
- Show server-confirmed event metadata, active role, member count, confirmed-entry count, last-confirmed time, and offline/stale state.
- Preserve local-only events and local event-copy links without automatic merging.

Done when two clean authenticated devices see the same authorized server event and revoked/non-member reads fail closed.

### Milestone 11: Revocable Invitations And Member Visibility

Planning estimate: weeks 29-32.

- Add server-generated, expiring, revocable invites.
- Require authentication before acceptance or private metadata disclosure.
- Create server membership atomically with invite use count.
- Show role, member list/count, join/role/revocation audit activity, and clear permission errors.

Done when organizer invitation, acceptance, expiry, reuse, revocation, sole-organizer protection, and role boundaries pass on two devices and emulator tests.

## Horizon C: Shared Ledger Beta

Planning estimate: weeks 33-44.

### Milestone 12: Reviewed Entry Synchronization

Planning estimate: weeks 33-38.

- Persist an operation UUID before the first network attempt.
- Submit reviewed structured evidence through a bounded server mutation.
- Atomically write idempotency result, duplicate reservation, redacted/private projections, audit event, revision, and server aggregates.
- Keep receipt images and raw OCR text local.
- Keep contributor uploads pending until server confirmation; do not include them in confirmed totals early.

Done when retry, two-device duplicate, transaction failure, correction, void, and authorization tests cannot double-count or partially commit money/audit state.

### Milestone 13: Activity, Confirmed Totals, And Audit UX

Planning estimate: weeks 39-41.

- Show who submitted and reviewed each entry without confusing authenticated user, ledger person, and OCR counterparty.
- Show confirmed member receipt counts, Money in/out, event totals, balance, utilization, redacted activity, and organizer audit history according to role.
- Surface corrections as revisions instead of deleting financial history silently.

Done when organizer, contributor, and viewer projections match the role matrix and all authorized devices converge on server-derived values.

### Milestone 14: Offline Queue, Conflicts, And Revocation

Planning estimate: weeks 42-44.

- Persist pending operations separately from confirmed cache.
- Use bounded idempotent retries.
- Show stale listener state, conflicts, blocked operations, and last server-confirmed time.
- Recheck active membership inside every server transaction.
- Purge synchronized cache after learned revocation under the approved privacy policy.

Done when restart, reconnect, stale revision, duplicate key, listener replacement, revocation, and blocked-operation tests remain explicit and never silently alter confirmed totals.

## Horizon D: Production Consideration

Planning estimate: weeks 45-52 or later.

Outcomes:

- Existing-ledger upload preview and cancellation, with no automatic migration.
- Versioned export/restore and duplicate-safe recovery if the product decision becomes `BUILD`.
- Reconciliation verifier for aggregates and audit history.
- Cost, rate-limit, abuse, backup, deletion, incident, and account-recovery runbooks.
- Qualified legal/privacy review for intended jurisdictions.
- Play Data safety, Privacy, Terms, and support operations updated for the actual backend.
- Three-device shared-ledger acceptance and closed beta.

Production remains `DEFER` until every applicable gate passes and the accountable publisher approves deployment separately.

## Version And Channel Strategy

- `0.2.x` remains the historical local beta line, including beta.2.
- The first post-beta.2 local UX/localization candidate is provisionally `0.3.0-beta.1`; the exact version is chosen only at release planning and must increment Android `versionCode`.
- Authenticated shared-event work uses an explicitly labelled alpha/closed-test channel and may not reuse local-beta wording such as `Share local copy` for server membership.
- `1.0.0` is reserved until the accountable owner chooses whether the stable product promise is local-only or shared and the matching broader-reliance gates pass.
- Every release title, Trust Center version label, site download, manifest, changelog entry, and Play listing must say `Local-only` or `Shared-event alpha/beta` as applicable.

## Screen Acceptance Map

| Surface | Required evidence before completion |
|---|---|
| Dashboard | Correct Room/server-confirmed values, empty/filter/error states, small-screen and 200% text screenshots |
| Event cards | Clear hierarchy, truthful local/public/private status, stable touch targets, no overflow in three languages |
| Create event | Required/optional validation, custom fields, cancel/save/reopen, no duplicate mutation |
| Event details | Reconciled balance/totals/utilization, members/activity/info, offline/stale disclosure where applicable |
| Members | Correct role and per-person counts, minimized contact data, revoke/permission states in shared mode |
| Invite/copy | Local copy and shared invitation never confused; expiry, invalid, revoked, reused, and unsupported paths tested |
| Receipt processing | Real ML Kit input, bounded work, cancel/retry/interruption, no fake progress or public sensitive data |
| Receipt review | Reliable read-only amount and ledger impact clear; weak/manual/missing and duplicate gates; compact responsive UI |
| Trust Center | Version, data handling, limits, updates, support, license, and shared-event status match implementation |
| Website | Download and manifest integrity, locale navigation, responsive rendering, accessibility, no claim drift |

## Verification Contract

Every behavior slice uses:

```text
Baseline
-> smallest implementation
-> focused falsifying check
-> repair only the same slice if needed
-> relevant broader suite
-> runtime interaction and screenshots when user-visible
-> independent completion review
-> documentation and memory update
```

Minimum Android source gate:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
```

Relevant broader gates:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDirectDebugUnitTest :app:testPlayDebugUnitTest
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDirectDebug :app:assemblePlayDebug
.\gradlew.bat --no-daemon --no-configuration-cache :app:connectedDirectDebugAndroidTest :app:connectedPlayDebugAndroidTest
node scripts/validate-site.mjs site
.\scripts\validate-aeos.ps1
```

Receipt changes additionally require the project OCR/search/data/UX/security gates and private real-image tests. Shared-event changes additionally require Firebase emulator authorization, function, idempotency, duplicate, conflict, and multi-device acceptance tests.

## Security And Supply-Chain Program

- Keep secrets, service-account credentials, signing keys, receipt fixtures, and personal data outside the repository and model memory.
- Keep push protection, secret scanning, dependency alerts, protected branches, and required CI enabled where the repository account supports them.
- Add dependency review and code scanning only with accurate language/build configuration and an owner for findings; scanner output never self-approves a release.
- Review direct and transitive dependency licenses before introducing new libraries.
- Preserve permanent Android signing lineage and independent key backup.
- Keep all remote confirmed-ledger writes behind server validation; Security Rules and App Check do not replace function authorization.
- Log no receipt text, payment reference, invite secret, member contact, auth token, or structured evidence payload.
- Treat legal compliance and translated legal text as qualified-human gates, not model claims.

## Autonomous Work Contract

Autonomous execution is allowed only inside one approved milestone slice at a time.

- Discovery and specialist audits remain read-only.
- The implementer receives one outcome, owned files, protected invariants, verification command, and stop condition.
- Each retry must test a changed hypothesis from new evidence.
- Iteration, time, and cost budgets are set per slice before unattended execution; unknown budgets require owner input.
- External resources, billing, production configuration, release publication, Pages deployment, Play submission, protected-branch merge, data deletion, and secret rotation always require current explicit approval.
- A fresh reviewer challenges substantial, security-sensitive, data-sensitive, or release work.
- Checkpoints record current revision/state, changed files, verification evidence, blockers, and next owner.

Enforcement is layered:

- deterministic compile, test, schema, static-site, and governance scripts enforce machine-checkable gates
- focused runtime/device checks enforce user journeys and lifecycle behavior
- independent review enforces architecture, security, privacy, accessibility, and claim-to-evidence alignment
- the publisher enforces external release approval after all required gates pass

The system does not need hundreds of agents. It needs the smallest relevant capabilities, deterministic checks, clear ownership, and one integration coordinator.

## Stop Conditions

Stop the affected slice immediately on:

- silent or unexplained ledger mutation
- wrong or irreconcilable totals
- invented receipt/member/payment data
- duplicate counting
- authorization or role bypass
- receipt or identity data exposure
- failed migration, blocked upgrade, or unrecoverable data loss
- unsupported legal/security claim
- untranslated or clipped critical action in a release locale
- repeated identical validation failure without new evidence
- missing owner approval for cloud, billing, deployment, release, or risk acceptance

## Timeline Honesty

- A professional multilingual local release candidate is a roughly 20-week planning program, not one autonomous turn.
- An authenticated read-only shared-event alpha is roughly 28-32 weeks from program start if owner decisions and backend staffing are available.
- A shared-ledger beta with idempotent reviewed entries, role projections, audit history, and explicit offline conflicts is roughly 40-44 weeks from program start.
- Production consideration is roughly 52 weeks or later and remains evidence-gated.

These estimates are not promises. Missing owner decisions, legal review, physical devices, translation review, backend operations, or failed acceptance gates extend the schedule. More elapsed time does not make unsafe work ready.

## Immediate Next Slice

After the license baseline, start Milestone 1 with a read-only screen-state inventory and screenshot baseline, then implement only the first dashboard/event-card design-token slice. Do not begin backend code, cloud setup, or broad string extraction in the same change.
