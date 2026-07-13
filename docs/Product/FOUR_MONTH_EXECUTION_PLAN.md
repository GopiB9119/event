# Four-Month Execution Plan

Last reviewed: 13 July 2026

## Mission

Deliver a materially smoother, clearer, more professional successor to `0.2.0-beta.2` without rewriting the product or weakening ledger integrity. Preserve the existing local-first behavior, improve the full critical journey, add English/Telugu/Hindi foundations, prove large-ledger behavior on named devices, and produce a signed release candidate only after all gates pass.

This plan starts from the old version because its core behavior is good and verified. It changes one independently testable slice at a time.

## Four-Month Boundary

### Included

- Dashboard and event-card visual hierarchy.
- Balance, collected, spent, utilization, empty, loading, and error states.
- Create Event, Event Details, member cards/profiles, activity, transaction list, and local-copy messages.
- Receipt selection, OCR processing, amount review, duplicate/error/retry/cancel/interruption states.
- Trust Center, task-based help, privacy-safe screenshots, launcher/brand refinement, and website improvements.
- English, Telugu, and Hindi app resources; public product/help page localization.
- Accessibility, small-screen, dark/light, text-scaling, startup, Room, list, OCR-memory, and lifecycle verification.
- A load-test contract and emulator architecture for a future server-authoritative shared ledger.

### Excluded From The Four-Month Release Claim

- Real synchronized membership, entries, balances, or receipt activity.
- A claim that 10,000 concurrent shared users are supported.
- Production Firebase/cloud creation, billing, permanent region selection, or deployment.
- Receipt-image upload or cloud OCR.
- Play production, GitHub release publication, or Pages deployment without a separate current approval.

## Scale Contract

The phrase "10,000 users" represents three different engineering problems. They must not be merged into one claim.

### 1. Ten Thousand Independent App Users

The current app is local-first. Independent installations do not send ledger traffic to one application server, so 10,000 people using separate phones does not create 10,000-way backend concurrency. Distribution, update hosting, support, crash monitoring, policy, and release operations still need capacity planning.

Four-month acceptance:

- Signed candidate installs and upgrades on the supported Android range.
- GitHub/Pages download and update paths are externally verified only after release approval.
- No app claim says global user count was load-tested when it was not.

### 2. Ten Thousand Local Ledger Records

This is testable in the current app. The stress profile is one event with 10,000 transactions and representative members, plus a Dashboard with a large event list.

Target budgets, to be confirmed or revised after Week 1 baselines:

- No crash, ANR, out-of-memory error, wrong total, or data loss at 10,000 transactions.
- Opening Event Details with a preloaded 10,000-row ledger reaches usable content within 2 seconds on the agreed reference device.
- Warm Dashboard reaches usable content within 1 second with 1,000 events on the reference device.
- Critical list interactions show no repeated visible freeze over 100 ms during the scripted flow.
- Room read and aggregate measurements record p50 and p95, not only one best run.
- Confirmed totals exactly reconcile with persisted transactions after restart.
- Receipt OCR peak memory and duration are measured on private representative images; limits are set from Week 9 evidence rather than guessed here.

These are product targets, not current verified results.

### 3. Ten Thousand Concurrent Shared Users

This cannot be proven because no shared backend exists. Before such a claim, the system needs server authority, authenticated roles, idempotent writes, duplicate reservation, audit history, rate limits, cost limits, observability, incident ownership, and a production-like load environment.

Four-month output:

- A versioned workload model covering read listeners, invite acceptance, reviewed-entry writes, retries, conflicts, revocation, and reconnect storms.
- Staged targets for 100, 1,000, and 10,000 virtual users.
- Error-rate, p95/p99 latency, duplicate-count, aggregate-reconciliation, cost, queue-depth, and recovery acceptance criteria.
- No production concurrency claim until the later shared-ledger program passes this workload against approved infrastructure.

## Non-Negotiable Product Rules

- No dummy, random, filename-derived, Gemini, or cloud OCR receipt values.
- No amount changes totals before positive/evidence/duplicate/review gates pass.
- No local-copy link is described as authentication, membership, access control, or synchronization.
- No visual redesign changes ledger calculations, Room ownership, signing lineage, or release behavior implicitly.
- No optimization is accepted without a comparable baseline and correctness check.
- No performance pass is manufactured by repeating a flaky check.
- No sensitive receipt/member/payment data enters source control, public screenshots, logs, metrics, or project memory.

## Reference Environments

The named device matrix is finalized in Week 1 from available hardware. At minimum:

- API 24 or the lowest practical supported emulator profile.
- API 36 reference emulator with 4 GB RAM and stable host GPU configuration.
- Samsung Galaxy A54 physical device if explicitly available and approved for disposable test data.
- A second physical Android device from a different vendor/version before release approval.

Performance numbers always identify device, API, build type, dataset, run count, and thermal/power state. Emulator measurements route regressions; physical release measurements support user-facing claims.

## Month 1: Baseline, Design Foundation, And Proven Hot Paths

### Week 1: Baselines And Current-State Evidence

Outcomes:

- Capture current Dashboard, event cards, Event Details, member cards, receipt flow, Trust Center, and website at target viewports.
- Record cold/warm startup, first Dashboard content, Event Details open, 1,000-event filter, 10,000-transaction summary, Room query plan, and OCR duration/memory baselines where the environment permits.
- Define the reference-device matrix and deterministic fixture generator using synthetic non-PII ledger data only in tests.
- Preserve beta.2 behavior and screenshots as comparison evidence.

Done when:

- Every baseline names the environment and command.
- Unknown metrics remain `UNKNOWN`; no estimate is presented as observation.
- Existing compile, JVM, Android, site, and governance gates still pass.

### Week 2: Restrained Visual Tokens And Event Cards

Outcomes:

- Define 8dp card radius, 48dp minimum actions, semantic colors, typography hierarchy, spacing, focus, disabled, loading, empty, success, warning, and error states.
- Replace decorative event-card accents/chips with one outlined information surface.
- Use truthful `Public marker` / `Private marker` language until server authorization exists.
- Add card semantics, callback, target-size, and visual regression checks.

Done when:

- Card open and delete actions remain independent.
- Delete has a 48dp target and a specific TalkBack label.
- Two-line titles and long dates do not hide controls.
- No old `Invite Only` claim remains on local cards.

### Week 3: One-Pass Ledger Presentation Summary

Outcomes:

- Replace repeated per-member transaction scans with a pure one-pass event/member presentation summary.
- Preserve linked-member priority and current unlinked name/phone/email fallback behavior.
- Reuse the same summary for member cards and member profiles.

Done when:

- The 10,000-transaction fixture has exact collected, spent, balance, member counts, and assignment totals.
- Linked transactions never leak into another matching member.
- Focused JVM tests and the complete unit suite pass.

### Week 4: Dashboard Hierarchy And List Behavior

Outcomes:

- Replace decorative banner graphics and empty badge rows with a compact local-status surface.
- Keep Create Event as the one primary Dashboard action.
- Make filters stable under translations and dynamic counts.
- Add real empty and filtered-empty states.
- Measure 1,000-event list/filter behavior before choosing pagination or query-backed filtering.

Done when:

- All/public/private results remain correct after create/delete/restart.
- No filter or action clips at 200% text scale.
- Dashboard visual regression covers light/dark and compact width.

## Month 2: Core Event Journey And Localization

### Week 5: Create Event

- Clarify required/optional fields and inline errors.
- Make custom fields easy to add, review, remove, and use on narrow screens.
- Keep privacy wording explicit: local marker, not access control.
- Verify create, cancel, validation failure, save, reopen, and multiple custom fields.

### Week 6: Event Details And Financial Summary

- Make Available Balance primary; Collected and Spent supporting.
- Remove fake `REAL-TIME` or live implications from local Room data.
- Simplify nested cards and decorative gradients.
- Correctly represent overspending and utilization above 100% where applicable.
- Verify totals from persisted rows after restart.

### Week 7: Members, Activity, And Large Lists

- Refine member cards/profiles around role, confirmed receipt count, Money in/out, and related entries.
- Remove emoji-dependent labels and crowded two-column strings.
- Profile transaction/member list rendering with large fixtures.
- Choose query-backed paging/search only if measurements show the full-list path misses targets.

### Week 8: Localization And Accessibility Foundation

- Extract the first critical vertical slice into Android resources.
- Add English, Telugu, and Hindi resources with plurals and parameters.
- Add system/default language control and restart persistence.
- Complete TalkBack labels/order and 200% text checks for onboarding, identity, Dashboard, event cards, Create Event, and critical errors.

Month 2 gate:

- A fluent Telugu reviewer and fluent Hindi reviewer must approve financial/safety terms before those locales enter a release candidate.
- Machine translation alone is not release evidence.

## Month 3: Receipt Reliability, Memory, And Recovery

### Week 9: OCR Performance Baseline

- Measure each ML Kit pass, decoded/scaled/processed bitmap dimensions, heap change, and total duration on private fixtures.
- Inspect cancellation and timeout behavior.
- Do not add `recycle()` or skip passes blindly; first prove bitmap ownership and whether later passes materially improve extraction.

### Week 10: Bounded OCR Pipeline

- Apply only measured memory/latency improvements.
- Bound decode dimensions and work count without reducing successful evidence extraction.
- Keep Latin/Devanagari behavior and private fixture coverage.
- Verify low-memory/error/cancel paths leave the ledger unchanged.

### Week 11: Receipt Review UX

- Improve amount hierarchy, evidence source, missing details, Money in/out, ledger person, duplicate warning, confirmation, save, retry, and cancel.
- Keep raw JSON private and terminal-style progress absent.
- Verify labelled, unlabelled, edited, duplicate, missing-amount, and non-receipt inputs.

### Week 12: Lifecycle And Offline Reliability

- Preserve the current fail-closed interruption guarantee.
- Decide from threat/reliability evidence whether resuming URI/review state is safe; otherwise improve restart guidance without persisting sensitive URI/text.
- Test force-stop, process death, rotation, no network, permission denial, full storage, and database/file failure.

Month 3 gate:

- No receipt path may save partial evidence or silently change a confirmed total.
- Private fixtures remain ignored and outside reports/screenshots.

## Month 4: Trust, Website, Security, And Release Candidate

### Week 13: Trust Center, Help, And Assets

- Reorganize About, Data and privacy, Updates, Limits, Help, Support, and License into concise task-oriented sections.
- Add privacy-safe visual guidance for selecting and reviewing receipts.
- Refine the existing launcher mark and icon usage; avoid decorative asset sprawl.

### Week 14: Website And Three-Language Public Help

- Improve product truth, install/update help, receipt guidance, local-copy limits, shared-event status, accessibility, support, release evidence, and license links.
- Add English, Telugu, and Hindi product/help routes and metadata.
- Keep legal pages English-authoritative until qualified reviewed translations exist.
- Verify desktop/mobile overflow, links, assets, manifest fallback, and download integrity.

### Week 15: Performance, Security, And Device Matrix

- Run startup, 1,000-event, 10,000-transaction, Room, scroll, OCR, memory, process-death, and upgrade checks on the named environments.
- Run secret/dependency/static checks supported by the repository and manually review changed trust boundaries.
- Run two physical-device critical paths with disposable synthetic data.
- Compare current measurements against Week 1 and record regressions as blockers.

### Week 16: Independent Audit And Candidate Decision

- Complete independent code, product, accessibility, security, privacy, performance, documentation, and release reviews.
- Reconcile every completion claim with exact evidence.
- Fix only release blockers and rerun affected gates.
- Prepare versioned release notes, screenshots, checksum/signature procedure, rollback/data-loss wording, and update manifest as drafts.

Release-candidate done criteria:

- No open severity-0 or severity-1 finding.
- No unexplained crash, ANR, out-of-memory error, data loss, wrong total, duplicate count, blocked upgrade, or sensitive-data leak in the required matrix.
- Compile, full JVM suite, relevant Android suite, APK assembly, static site, AEOS, diagnostics, signing, alignment, and artifact hash checks pass.
- English critical journey is complete; Telugu/Hindi are included only if critical-string, clipping, TalkBack, and fluent-review gates pass.
- Remaining limitations are identical in app, website, Trust Center, and release notes.
- Publication remains a separate publisher approval.

## Performance Engineering Sequence

Use this order; do not optimize from intuition:

1. Reproduce and measure.
2. Extract pure calculations where correctness can be tested cheaply.
3. Remove repeated work and unnecessary recomposition.
4. Profile Room query plans before adding indexes or migrations.
5. Page/query large lists only when measured full-list behavior misses targets.
6. Profile bitmap ownership and ML Kit pass value before changing OCR memory handling.
7. Add Baseline Profiles/Macrobenchmark only after the critical journeys stabilize.
8. Compare release builds on physical hardware before making performance claims.

## Verification Matrix

| Claim | Required evidence |
|---|---|
| Event card is better | Semantics/callback test, 48dp target, compact/light/dark screenshots, long-title check |
| Ledger summary is smoother | One-pass implementation, 10,000-row correctness test, before/after profile on reference device |
| Dashboard handles large data | 1,000-event fixture, filter correctness, frame/interaction trace, no ANR |
| Event Details handles 10,000 rows | Room/query timing, usable-content timing, scroll trace, exact total reconciliation |
| OCR is smoother | Per-pass timing, peak-memory comparison, same private-fixture acceptance, failure/cancel test |
| Three languages work | Critical-string inventory, fluent review, screenshots at 200% text, TalkBack journey |
| Website is ready | Static validator, responsive screenshots, locale links, console/network errors, live checks after approval |
| 10,000 concurrent users work | Production-like backend load test with defined workload, p95/p99/error/cost/reconciliation evidence; not part of this local release |

## Stop Conditions

Stop the affected slice on:

- wrong or unexplained totals
- duplicate counting or member misattribution
- migration or upgrade failure
- crash, ANR, out-of-memory, or repeated visible freeze
- OCR accuracy regression on accepted private fixtures
- sensitive data in logs, screenshots, fixtures, analytics, or memory
- misleading local-copy/private/sync language
- untranslated or clipped critical action in an enabled locale
- performance target changed after seeing results without owner review
- missing approval for cloud, production, release, deployment, data deletion, or key changes

## Work Started On 13 July 2026

- Event cards changed from decorative elevated/chip-heavy surfaces to restrained 8dp outlined cards.
- Delete target increased to 48dp with event-specific accessibility text.
- Local visibility labels changed to `Public marker` and `Private marker`.
- Added a pure one-pass ledger presentation summary shared by Event Details member cards and profiles.
- Added matching/correctness coverage including a synthetic 10,000-transaction fixture.

These changes are implementation progress, not a release or performance-capacity claim.