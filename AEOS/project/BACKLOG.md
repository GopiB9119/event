# Community Ledger Evidence-Based Backlog

Last review: 11 July 2026

This backlog is ordered by current constraint, not feature excitement. Evidence labels follow [Decision Intelligence](../00-foundation/DECISION_INTELLIGENCE.md). Reorder only when new evidence changes user value, risk, dependencies, or opportunity cost.

## NOW

### 1. Physical-device launch matrix

- Problem: `UNKNOWN` final picker/share/review/save/restart behavior on the publisher's exact phone and a second Android device.
- Current status (11 July 2026): `BLOCKED` after a partial Samsung Galaxy A54 run. Candidate install/hash, cold restart persistence, Trust Center navigation, one persisted receipt/member/transaction, and six instrumentation tests passed. During the run, Samsung Package Installer displayed uninstall UI and Android fully removed the app; the initiator is `UNKNOWN`, no shell uninstall was issued, and uninstall deleted the disposable ledger. Reinstall requires explicit confirmation and a coordinated no-touch rerun.
- Why now: It is the cheapest test of the most dangerous launch assumption.
- Smallest action: Run the [Physical Device Launch Matrix](../../docs/Testing/PHYSICAL_DEVICE_LAUNCH_MATRIX.md) on two devices and record Android versions and pass/fail evidence.
- Acceptance evidence: event creation, real receipt share/import, review/save/cancel, force-stop/restart, totals, interruption notice, event-copy fallback, and reinstall/update behavior are explicit.
- Stop condition: silent ledger mutation, data loss, unexplainable total, blocked update, or misleading synchronized/access-control expectation.

### 2. Permanent release signing and recovery custody

- Problem: `VERIFIED` current APK is debug-signed and cannot establish a permanent public update lineage.
- Dependency: Publisher must create secrets directly in a secure terminal and maintain two independent backups.
- Smallest action: Follow [Signing And Distribution](../../docs/Release/SIGNING_AND_DISTRIBUTION.md); record only certificate fingerprint and backup confirmation, never secrets.
- Acceptance evidence: release APK builds, verifies, aligns, installs over the same release lineage, and survives cold launch/ledger smoke tests.
- Opportunity cost avoided: prevents publishing an APK that cannot receive trusted updates.

### 3. Hosted CI, Pages, and release-candidate evidence

- Problem: `UNKNOWN` local workflows have not been proven on hosted runners or deployed HTTPS.
- Dependency: explicit approval before push, deployment, release, or publication.
- Smallest action: push the prepared governance/workflow files; obtain green Android CI and Site CI runs; manually deploy Pages; verify every route; keep `available=false` until the signed release passes.
- Acceptance evidence: hosted logs, improved Community Profile, configured repository rules/security settings, HTTPS 200 checks, valid JSON, no broken assets, and fail-closed download behavior.

### License and contribution terms

- Problem: `VERIFIED` the repository is public but grants no open-source license; external code merging is intentionally closed.
- Decision state: `DEFER` license selection to Gopi Banoth; do not infer a license from Gradle/dependency headers.
- Smallest action: choose source-available/all-rights-reserved, Apache-2.0, MIT, GPL-3.0, or another reviewed license before accepting external code contributions or describing the project as open source.
- Acceptance evidence: root `LICENSE` when applicable, README/Contributing text matches it, and contribution rights are explicit.

### 4. Export/restore decision before wider reliance

- Problem: `VERIFIED` uninstall/device loss permanently removes financial records.
- Decision state: `TEST`, not automatic implementation.
- Smallest action: discover which organizer-controlled export format and restore workflow users understand; threat-model PII leakage, tampering, versioning, and duplicate restore.
- Acceptance evidence: a reviewed `STOP / DEFER / TEST / BUILD` record and, only if `BUILD`, a minimal migration-safe plan.

### 5. Define financial-purpose semantics before adding investment language

- Problem: `VERIFIED` current types group `Donated/Credit` as Money in and `Debit/Expense` as Money out, but they do not model investment ownership, loan repayment, expected return, or equity.
- Decision state: `TEST`; do not add an Investment type or marketing claim until the user's real accounting job and legal/trust consequences are understood.
- Smallest action: document concrete personal-work and event-donation scenarios, required totals, ownership/repayment rules, and what final report must prove.
- Acceptance evidence: reviewed vocabulary/data model, migration plan for existing types, calculation invariants, UI comprehension test, and legal/product review as applicable.

### 6. Export and report format discovery

- Problem: `VERIFIED` users cannot export or restore; `UNKNOWN` whether their first need is PDF presentation, spreadsheet analysis, or machine-readable backup.
- Decision state: `TEST`.
- Smallest action: define report audiences and tasks separately: organizer audit, member statement, public event summary, and restore backup. Compare PDF, CSV, and versioned JSON without treating one format as all four solutions.
- Acceptance evidence: privacy/redaction policy, per-member/event totals, receipt-reference inclusion rules, tamper/verification limits, restore versioning/duplicate behavior, and owner-approved format priority.

## TEST

### 7. Problem and retention validation with target organizers

- Assumption: organizers need a dedicated local ledger instead of spreadsheets, notebooks, or messaging threads.
- Test: evidence-focused interviews and observed task use with a small relevant cohort; ask about recent behavior and workarounds, not feature opinions.
- Predeclare: what repeated problem/action permits continued investment, and what result triggers `STOP` or repositioning.
- Privacy: do not collect real receipt/member PII for product research.

### 8. Event-copy comprehension

- Assumption: users understand that event-copy links do not synchronize or verify organizers.
- Test: task-based comprehension check using exact in-app/site copy.
- Failure threshold: any participant expects shared balances, private access control, organizer verification, or automatic updates across phones.

### 9. OCR breadth and poor-image conditions

- Evidence: `VERIFIED` six private fixtures pass; broader payment apps and image conditions remain unproven.
- Test: consented private fixtures covering low light, crop, rotation, large image, missing fields, and supported app families.
- Gate: never lower confidence or duplicate protections merely to raise extraction rate.

## LATER

### 10. Google Play distribution

- Dependency: public-beta evidence, permanent signing, privacy URL, data-safety declarations, store assets, and current testing-policy compliance.
- Reason later: direct trusted beta can validate the product before adding store process cost.

### 11. Synchronized shared events

- Decision state: `DEFER`.
- Reason: no recorded demand evidence justifies authentication, server IDs, authorization, sync, conflict resolution, audit operations, and moderation cost.
- Revisit trigger: repeated observed need for concurrent multi-device use after local beta, with willingness to adopt account-based workflows.
- Required architecture: [Future Shared Events](../../docs/Architecture/SHARED_EVENTS_FUTURE.md).

### 12. Public event discovery/search

- Decision state: `DEFER` with shared-event architecture.
- Reason: public search without verification, authorization, moderation, abuse reporting, and privacy controls would increase impersonation and fraud risk.

## STOP FOR CURRENT PRODUCT

### Cloud/LLM receipt fallback

- Reason: contradicts local-first privacy, adds sensitive-data transfer/cost, and has no evidence that it is required.
- Reconsider only through an explicit privacy/security/product decision and real comparative evaluation.

### Money custody, payment execution, or investment returns

- Reason: outside the product promise and creates severe legal, security, abuse, and trust obligations.
- UI/site/marketing must continue stating that Community Ledger records evidence and does not move or invest money.

### Silent background update or APK installation

- Reason: violates the current privacy/trust posture and Android consent model.
- Keep update checks user-triggered and installation controlled by Android/user action.

### Publishing the debug-signed APK as the permanent public channel

- Reason: creates an impermanent update lineage and contradicts the release-signing gate.

## Review Triggers

Review this backlog when any of the following occurs:

- physical-device or hosted validation changes launch confidence
- a real organizer supplies repeated behavior evidence
- a security/privacy incident or platform policy changes urgency
- export/restore discovery changes the safe launch boundary
- release signing or legal review identifies a new blocker
- the publisher chooses repository license/contribution terms
- current milestone changes