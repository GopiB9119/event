import { spawnSync } from "node:child_process";

const repository = "GopiB9119/event";
const sourceBranch = "new-changes";
const sourceCommit = "e47dcc7";
const apply = process.argv.includes("--apply");

function issue(
  title,
  priority,
  labels,
  problem,
  scope,
  acceptance,
  validation,
  security,
  dependencies,
  evidence
) {
  return { title, priority, labels, problem, scope, acceptance, validation, security, dependencies, evidence };
}

const issues = [
  issue(
    "Approve production Firebase ownership, billing, region, and operating responsibilities",
    "P0",
    ["question", "documentation"],
    "Production shared mode cannot be enabled until ownership and irreversible cloud choices are explicit.",
    "Record the owning Google account, billing owner, Firestore/RTDB/Functions regions, budget alerts, incident owner, retention owner, and go/no-go authority.",
    ["Every accountable owner is named", "Permanent regions and cost thresholds are approved", "No credential or secret value enters the repository", "Create/deploy runbooks contain explicit approval gates"],
    ["Independent architecture and security review", "Dry-run review that creates no cloud resource"],
    "Billing, regions, credentials, and deployment remain human approval gates.",
    "None; this blocks all production cloud work.",
    "firebase/README.md: Production Deployment Inputs; shared-ledger plan: Current Blocker"
  ),
  issue(
    "Implement production Firebase backend provider and Android configuration",
    "P0",
    ["enhancement"],
    "Release builds intentionally use DisabledSharedBackend and cannot synchronize.",
    "Add an owner-approved production provider, environment-specific Firebase configuration, safe build separation, and failure-visible initialization.",
    ["Release never references emulator hosts", "Debug emulator isolation remains intact", "Missing production configuration fails closed", "Approved project and regions are used"],
    ["Direct and Play compile/test gates", "Release manifest and dependency inspection", "Two-device staging smoke test"],
    "Do not commit service-account keys; include only client-safe configuration.",
    "Production Firebase ownership decision.",
    "app/src/release/.../SharedBackendProvider.kt; app/build.gradle.kts"
  ),
  issue(
    "Enforce Firebase App Check with Play Integrity in production",
    "P0",
    ["enhancement"],
    "Emulator App Check evidence does not protect a deployed backend.",
    "Configure Play Integrity, token refresh, callable enforcement, rollout metrics, and a controlled enforcement switch.",
    ["Valid release clients succeed", "Missing or invalid tokens fail closed", "Debug emulator tokens cannot reach production", "Enforcement rollback is documented"],
    ["Staging positive and negative tests", "Expired token and clock-skew tests", "Rules and Functions audit"],
    "Prevent unauthorized clients without locking out legitimate users during rollout.",
    "Production Firebase provider and Play app registration.",
    "ADR-0003; Firebase callable verification contract"
  ),
  issue(
    "Design account recovery and identity linking for anonymous users",
    "P0",
    ["enhancement"],
    "Anonymous identities are lost after uninstall or app-data clearing, risking membership loss.",
    "Choose and implement account linking, recovery, reauthentication, device replacement, and organizer-loss handling.",
    ["Memberships can be recovered on a new install", "Unrelated identities never merge silently", "Organizer-loss behavior is explicit", "Recovery events are auditable"],
    ["Uninstall/reinstall and second-device tests", "Collision, takeover, and revocation tests"],
    "Threat-model account takeover, enumeration, and recovery abuse.",
    "Production authentication decision.",
    "Shared-ledger account recovery risk; project known risks"
  ),
  issue(
    "Run physical two-phone shared-ledger acceptance on separate networks",
    "P0",
    ["enhancement"],
    "API 36 convergence used two clients in one emulator, not two physical phones.",
    "Validate two clean phones and identities over Wi-Fi and mobile data for invite, membership, presence, pending review, history, and totals.",
    ["Both phones converge on membership, history, and totals", "Presence wording remains approximate", "Receipt images stay on the originating phone", "Redacted evidence is retained"],
    ["Complete the physical-device matrix", "Repeat across separate networks and app restarts", "Inspect crash and ANR records"],
    "Use disposable synthetic event data; never record real receipts or PII.",
    "Owner-approved staging or production backend.",
    "docs/Testing/PHYSICAL_DEVICE_LAUNCH_MATRIX.md; TEST_STRATEGY highest blocker"
  ),
  issue(
    "Verify offline, reconnect, process restart, and stale-state behavior for shared events",
    "P0",
    ["enhancement"],
    "Local replay exists, but real network interruption and recovery remain insufficiently proven.",
    "Test airplane mode, packet loss, process death, listener replacement, stale revisions, retry budgets, and reconnect convergence.",
    ["Pending work stays visibly pending", "Local state never changes confirmed totals", "Restart reuses idempotency keys", "Revoked operations stop with recovery guidance"],
    ["Two-device network fault matrix", "Process-kill and server-revision conflict tests"],
    "Never move blocked work to another event or retain protected state after revocation.",
    "Physical staging acceptance.",
    "SharedWorkspaceController; shared-ledger offline queue plan"
  ),
  issue(
    "Add organizer role-change controls and server mutation",
    "P1",
    ["enhancement"],
    "Organizers cannot change contributor or viewer roles after joining.",
    "Add an authorized role-change callable, audit/revision updates, durable client replay, confirmation UI, and listener reconciliation.",
    ["Only authorized organizers can change roles", "The last-organizer invariant is protected", "All clients observe the new role", "Retries do not duplicate audit events"],
    ["Authorization and idempotency tests", "Two-client UI and presence-mirror tests"],
    "Prevent privilege escalation and organizer lockout.",
    "Membership API foundation.",
    "firebase/README.md: Not Implemented; shared plan: Current Blocker"
  ),
  issue(
    "Add organizer member removal and immediate access revocation",
    "P1",
    ["enhancement"],
    "There is no product flow to remove a member even though client revocation cleanup exists.",
    "Implement removal mutation, invite/session invalidation, presence purge, blocked queue behavior, audit, and organizer UI.",
    ["Removed members promptly lose reads and writes", "Protected live state is cleared", "Pending operations become blocked with explanation", "Removal is idempotent and audited"],
    ["Rules and Functions negative tests", "Open-app revocation and restart tests"],
    "Fail closed during delayed Firestore and RTDB trigger delivery.",
    "Role and membership API foundation.",
    "SharedWorkspaceController revocation handling; missing removal callable"
  ),
  issue(
    "Add member leave-event flow",
    "P1",
    ["enhancement"],
    "Members cannot voluntarily leave a shared event.",
    "Add leave mutation, last-organizer constraints, cache/presence cleanup, pending-work handling, and confirmation UI.",
    ["Contributor and viewer can leave", "Organizer cannot orphan an event", "Local shell handling is explicit", "Audit and counts converge"],
    ["Backend transaction tests", "Client restart and two-client count tests"],
    "Explain any pending work that will be discarded; avoid silent data loss.",
    "Membership API foundation.",
    "Shared-ledger Current Blocker"
  ),
  issue(
    "Add close and archive lifecycle for shared events",
    "P1",
    ["enhancement"],
    "Shared events have no supported terminal lifecycle.",
    "Define active, closed, and archived states, write restrictions, discovery behavior, retention, reopen policy, and UI.",
    ["Closed events reject new financial submissions", "History remains readable per policy", "Discovery excludes closed events", "Actions are confirmed and audited"],
    ["State-transition contract tests", "Rules, query, and UI state tests"],
    "Do not delete financial history as a shortcut for closure.",
    "Retention policy and organizer authorization.",
    "firebase/README.md: Not Implemented; shared event backlog"
  ),
  issue(
    "Add invite revocation, rotation, and active-invite management",
    "P1",
    ["enhancement"],
    "Organizers cannot inspect or revoke outstanding private invites.",
    "Implement safe invite metadata listing, revoke and rotate mutations, UI, and audit without exposing raw tokens.",
    ["Unused invites can be revoked", "Revoked and expired tokens fail closed", "Only hashes are persisted", "Rotation preserves memberships"],
    ["Callable and rules tests", "Race, replay, and UI tests"],
    "Never log or persist raw invite tokens beyond the necessary response.",
    "Invite authority and membership APIs.",
    "Firebase shared membership implementation; remaining capabilities"
  ),
  issue(
    "Implement confirmed-entry correction and void audit workflow",
    "P0",
    ["enhancement"],
    "Confirmed money cannot be corrected safely; direct editing would destroy auditability.",
    "Add corrected and voided revisions, reason/evidence requirements, aggregate deltas, duplicate policy, and UI history.",
    ["Confirmed entries are never overwritten", "Correction and void are append-only", "Totals update atomically", "Clients converge after retries and conflicts"],
    ["Rollback and idempotency tests", "Aggregate recomputation and two-client tests"],
    "Require authorization and preserve original evidence and history.",
    "Server revision and audit model.",
    "Shared plan: corrected and voided states"
  ),
  issue(
    "Add paginated shared history and bounded member/event queries",
    "P1",
    ["enhancement"],
    "Current listeners stop at hard limits and cannot expose large histories.",
    "Define cursor contracts, stable ordering, loading/error states, deduplication, indexes, and cache policy.",
    ["Records beyond current limits are reachable", "Pages contain no duplicates or gaps", "Live updates do not corrupt ordering", "Memory and network use stay bounded"],
    ["Large synthetic dataset tests", "Cursor mutation and Compose scroll tests"],
    "Rules must constrain query shape and prevent broad unauthorized reads.",
    "Stable server ordering and indexes.",
    "MAX_ENTRIES/MAX_MEMBERS; shared plan pagination blocker"
  ),
  issue(
    "Add clear failed-operation and retry controls to shared UI",
    "P1",
    ["enhancement"],
    "Durable pending operations exist, but users lack complete failed and blocked operation management.",
    "Expose pending, running, failed, and blocked states with retry, discard, safe export, and reason-specific guidance.",
    ["Each state has accessible distinct UI", "Retry preserves the idempotency key", "Permanent failures cannot loop", "Discard requires confirmation"],
    ["Robolectric and UI tests", "Restart, reconnect, and accessibility tests"],
    "Do not expose receipt references, invite tokens, or private evidence in generic errors.",
    "Offline and reconnect contract.",
    "SharedSyncModels; remaining UI capabilities"
  ),
  issue(
    "Complete shared screen loading, empty, stale, offline, and permission-loss UX",
    "P1",
    ["enhancement"],
    "Shared screens need production-grade non-happy states, not only successful workflows.",
    "Audit discovery, details, members, history, receipt intake, evidence review, reconnect, and permission loss.",
    ["Every screen has loading, empty, error, offline, and stale handling", "One primary action stays clear", "Permission loss removes protected state", "Presence copy avoids exact claims"],
    ["Screen-state matrix", "Compose tests and phone/tablet screenshots"],
    "Redact evidence for unauthorized roles and after revocation.",
    "Stable shared workflows.",
    "SharedScreens.kt; UI quality gates"
  ),
  issue(
    "Add explicit local-ledger to shared-event migration with preview and rollback",
    "P1",
    ["enhancement"],
    "Existing local ledgers cannot be safely promoted to shared authority.",
    "Design opt-in preview, field and entry selection, duplicate handling, cancellation, server confirmation, and rollback boundaries.",
    ["No silent upload occurs", "Preview shows exact synchronized fields", "Retries cannot double-count", "Failure leaves the local ledger unchanged"],
    ["Migration, duplicate, conflict, cancellation, and process-death tests"],
    "Receipt images remain local and synchronized identifiers are minimized.",
    "Production backend and privacy policy.",
    "Shared plan: Existing-Ledger Migration"
  ),
  issue(
    "Implement shared-data export for authorized users",
    "P1",
    ["enhancement"],
    "Users cannot export shared event data for portability, review, or recovery.",
    "Define role-scoped export, canonical format, evidence redaction, integrity metadata, and progress/failure handling.",
    ["Export is deterministic and bounded", "Role-specific private fields are enforced", "Receipt images are excluded unless approved", "Interrupted export leaves no corrupt final file"],
    ["Round-trip, authorization, large-export, and cancellation tests"],
    "Use app-private staging and Storage Access Framework; never expose secrets.",
    "Data classification and retention policy.",
    "Shared capabilities backlog; encrypted backup foundation"
  ),
  issue(
    "Implement shared-event deletion, retention, and user data rights",
    "P0",
    ["enhancement"],
    "Production has no approved deletion, export, or retention behavior.",
    "Define close versus deletion, member data deletion, tombstones, audit retention, backups, legal holds, and user requests.",
    ["Policy maps to executable backend behavior", "Deletion is authorized and auditable", "History invariants remain valid", "User timelines and limitations are explicit"],
    ["Emulator lifecycle tests", "Operational rehearsal and privacy review"],
    "Irreversible deletion requires explicit approval and recovery analysis.",
    "Owner privacy and legal decisions.",
    "firebase/README.md: Production Deployment Inputs"
  ),
  issue(
    "Build aggregate reconciliation and integrity verifier",
    "P0",
    ["enhancement"],
    "Transactional totals still need independent detection of drift or failed triggers.",
    "Implement read-only recomputation of event and member aggregates, mismatch alerts, bounded repair approval, and evidence retention.",
    ["Verifier recomputes from confirmed revisions", "Mismatch never silently rewrites production", "Runs are idempotent and bounded", "Alerts avoid private evidence"],
    ["Seeded corruption and large-event tests", "Runbook dry run"],
    "Repair requires owner approval and least-privilege access.",
    "Monitoring and operations ownership.",
    "Shared plan reconciliation requirement"
  ),
  issue(
    "Add production monitoring, crash reporting, observability, and cost alerts",
    "P0",
    ["enhancement"],
    "No production telemetry or cost controls exist for shared mode.",
    "Define privacy-bounded Android/backend metrics, errors, latency, rule denials, queue health, spend alerts, dashboards, and escalation.",
    ["Critical failures generate actionable alerts", "Budgets and owners are configured", "Telemetry excludes receipt content", "Every alert links to a runbook"],
    ["Synthetic alert tests", "Cost-threshold dry run", "Telemetry privacy review"],
    "Collect only necessary telemetry with documented retention.",
    "Production ownership and billing decision.",
    "Firebase production inputs; operations backlog"
  ),
  issue(
    "Add callable rate limits, abuse controls, and operational quotas",
    "P0",
    ["enhancement"],
    "Auth and App Check do not stop abusive authenticated clients or runaway cost.",
    "Define per-user, event, and device limits, invite/submission abuse controls, payload caps, backoff, and operator visibility.",
    ["Abusive bursts are bounded", "Legitimate retries remain idempotent", "Limits return stable safe errors", "Overrides are audited"],
    ["Load and abuse tests", "False-positive and cost-model tests"],
    "Do not rely on client-only throttling or enable user enumeration.",
    "Monitoring and production backend.",
    "Shared plan abuse limits and event limits"
  ),
  issue(
    "Update privacy policy, terms, and Play Data Safety for shared mode",
    "P0",
    ["documentation"],
    "Current public legal text describes a local-only beta and would be false for cloud shared events.",
    "Document Auth identifiers, event/member/financial fields, ML Kit diagnostics, processors, retention, deletion/export, and security limits.",
    ["Policy matches production data flow", "Play declarations match SDK evidence", "Local-copy and shared-event wording remain distinct", "Qualified review is recorded"],
    ["Data inventory trace", "Manifest/SDK inspection", "Legal and privacy review"],
    "Never claim no collection, secure, or guaranteed beyond implemented controls.",
    "Final architecture and retention decisions.",
    "docs/Legal; Play launch plan; ML Kit disclosure"
  ),
  issue(
    "Complete physical local-app workflow matrix on Samsung and second phone",
    "P0",
    ["enhancement"],
    "Emulator tests pass, but the current package needs coordinated physical-device evidence.",
    "Run create, save, restart, share, cancel, OCR interruption, backup boundaries, updates, and exit-record checks on two phones.",
    ["Exact candidate hash is recorded", "Disposable ledger survives expected restarts", "No unexplained uninstall or data loss occurs", "A second phone repeats critical flows"],
    ["Redacted physical-device matrix", "Package installer and crash/ANR evidence"],
    "Use disposable synthetic data and coordinated no-touch windows.",
    "Approved candidate build.",
    "PUBLIC_LAUNCH_CHECKLIST; AEOS PRIMARY OPEN GATE"
  ),
  issue(
    "Investigate Samsung Package Installer uninstall incident",
    "P0",
    ["bug"],
    "A prior physical run ended with the app removed by an UNKNOWN initiator.",
    "Reproduce safely, capture package installer, activity, and policy logs, identify the initiator, and document containment.",
    ["Initiator or bounded unknown state is evidenced", "Tests cannot uninstall a physical app", "Reproduction uses disposable data", "Runbook prevents recurrence"],
    ["Controlled install/update/uninstall matrix", "ADB package history and exit records"],
    "Do not retry on the user phone without explicit coordinated approval.",
    "Physical-device plan.",
    "AEOS/project/BACKLOG.md: PRIMARY OPEN GATE"
  ),
  issue(
    "Expand OCR coverage across payment apps and difficult image conditions",
    "P1",
    ["enhancement"],
    "Six private receipts pass, but payment-app and poor-image breadth remains limited.",
    "Add private real screenshots for supported apps, low light, crop, rotation, blur, mixed scripts, and misleading numbers plus sanitized parser regressions.",
    ["No dummy, filename, or cloud fallback is used", "Positive amount and receipt-context gates stay enforced", "Missing fields remain null", "Private reports stay ignored"],
    ["Focused device OCR test", "JVM parser suite", "No-PII tracking scan"],
    "Never commit real receipt images or raw PII.",
    "User-provided private fixtures.",
    "TEST_STRATEGY highest blocker; OCR backlog"
  ),
  issue(
    "Run comprehensive accessibility audit for all local and shared screens",
    "P1",
    ["enhancement"],
    "Automated tests cover some semantics, but assistive-technology validation is incomplete.",
    "Audit TalkBack, focus order, targets, contrast, font scaling, reduced motion, switch/keyboard access, localization, and error announcements.",
    ["Critical flows work with TalkBack", "Large fonts do not clip or overlap", "Color is not the sole signal", "Errors and pending states are announced"],
    ["Manual TalkBack matrix", "Compose semantics tests", "Viewport screenshots"],
    "Accessibility findings may block release; do not claim legal conformance automatically.",
    "Stable screen flows.",
    "TEST_STRATEGY accessibility matrix; UI gates"
  ),
  issue(
    "Add adaptive layouts for tablets, foldables, landscape, and large fonts",
    "P2",
    ["enhancement"],
    "Current phone-centric screens lack verified adaptive behavior.",
    "Define window-size classes, panes, navigation behavior, stable dimensions, landscape reachability, and fold posture handling.",
    ["No incoherent overlap at target sizes", "Primary actions remain reachable", "Large screens improve density without nested cards", "State survives configuration changes"],
    ["Screenshot matrix", "Rotation/fold simulation", "Compose tests"],
    "Do not reveal additional private data merely because more space exists.",
    "UX inventory and accessibility audit.",
    "Android UI production recommendations"
  ),
  issue(
    "Add localization for Telugu and Hindi with RTL-safe layout support",
    "P2",
    ["enhancement"],
    "Multilingual support is planned but not implemented or reviewed.",
    "Externalize copy, add Telugu and Hindi resources, plural/date/currency handling, pseudo-localization, and RTL checks.",
    ["No hardcoded copy remains in target flows", "Translations receive human review", "Long strings and plurals fit", "RTL preserves financial meaning"],
    ["Pseudo-locale tests", "Screenshot and font-scale matrix", "Translation review"],
    "Do not machine-translate legal or financial copy without qualified review.",
    "Stable UX copy.",
    "Post-beta localization horizon"
  ),
  issue(
    "Add startup, Compose jank, memory, battery, and baseline-profile benchmarks",
    "P1",
    ["enhancement"],
    "Correctness is measured, but production performance claims lack device benchmarks.",
    "Add Macrobenchmark, Baseline Profiles, startup timing, frame traces, recomposition checks, large-ledger scrolling, OCR memory, and battery/network budgets.",
    ["Benchmarks run on representative devices", "Budgets and regressions are explicit", "Shared listeners remain bounded", "No claim relies on a correctness fixture"],
    ["Macrobenchmark runs", "Perfetto traces", "Memory and battery profiling"],
    "Profiling output must exclude receipt content and PII.",
    "Stable candidate build.",
    "TEST_STRATEGY performance gap; lint advisories"
  ),
  issue(
    "Clean release lint warnings without unrelated dependency churn",
    "P2",
    ["enhancement"],
    "Release lint passes but reports 46 warnings and 3 hints per variant.",
    "Triage dependency, KTX, unused-resource, allocation, preference, version, inlined-API, and AGP findings.",
    ["Each finding is fixed, justified, or explicitly deferred", "No risky broad upgrade is bundled", "Release lint stays free of errors and fatals", "Behavior remains stable"],
    ["Direct and Play lint", "Tests for touched code", "Dependency compatibility review"],
    "Dependency upgrades require supply-chain and privacy review.",
    "Split risky upgrades into separate work.",
    "app/build/reports/lint-results-*Release.xml"
  ),
  issue(
    "Stabilize headless KSP AWT side-thread warning",
    "P2",
    ["bug", "java"],
    "Cold headless builds emit a nonfatal IntelliJ/KSP AWT NullPointerException.",
    "Identify the KSP, Room, or compiler trigger and remove the warning without hiding failures.",
    ["All debug/release builds finish without the exception", "KSP output remains correct", "Incremental and cache stability do not regress"],
    ["Cold compile matrix", "Room schema comparison", "Hosted CI run"],
    "Do not suppress JVM errors globally.",
    "Dependency and version investigation.",
    "Observed during compile and assemble gates"
  ),
  issue(
    "Run current new-changes Android CI and preserve hosted evidence",
    "P0",
    ["github_actions"],
    "Local gates pass, but the current branch needs hosted reproducibility.",
    "Run the required workflow for new-changes, retain logs and artifacts, fix environment-only failures, and document exact commit evidence.",
    ["Compile, unit suites, artifacts, and policy checks pass", "No secret is printed", "Timeout and resource budgets are respected", "Evidence names the tested commit"],
    ["GitHub Actions required check", "Artifact manifest inspection"],
    "Use GitHub secrets only through repository controls and never echo them.",
    "Pushed new-changes branch.",
    ".github/workflows/android-ci.yml; TEST_STRATEGY hosted-run gap"
  ),
  issue(
    "Decide new package signing lineage and produce owner-approved signed candidate",
    "P0",
    ["question", "documentation"],
    "The new package has no approved long-term signing relationship or available release inputs.",
    "Record Play App Signing, upload-key custody and backup, rotation/recovery, and build procedure; then create a signed candidate without committing secrets.",
    ["Decision is owner-approved", "Keystore has an independent secure backup", "Signed APK/AAB verifies for com.communityledger.app", "No secret enters history or logs"],
    ["apksigner, jarsigner, and zipalign verification", "Fresh and update install matrix"],
    "Signing keys and passwords stay outside repository and model-visible tools.",
    "Publisher and Play ownership decision.",
    "SIGNING_AND_DISTRIBUTION.md; release gate"
  ),
  issue(
    "Complete Play Console declarations, assets, closed testing, and rollout plan",
    "P1",
    ["enhancement", "documentation"],
    "Play production remains blocked by store, policy, testing, and rollout work.",
    "Prepare listing copy, icon, feature graphic, screenshots, Data Safety/App Content forms, closed testing, staged rollout, rollback, and support readiness.",
    ["Declarations match code and data flow", "Assets show current UI", "Closed testing evidence is retained", "Rollback and support owners are named"],
    ["Play pre-launch report", "Policy checklist", "Internal and closed-track smoke tests"],
    "Do not upload debug/emulator builds or make misleading synchronized claims.",
    "Signing, privacy, and production backend decisions.",
    "PUBLIC_LAUNCH_CHECKLIST; GOOGLE_PLAY_LAUNCH_PLAN"
  ),
  issue(
    "Implement production notification strategy without sensitive payloads",
    "P2",
    ["enhancement"],
    "Shared-event notification delivery is deferred and undefined.",
    "Define opt-in events, channels, deep links, deduplication, offline handling, permissions, and privacy-safe payloads.",
    ["Payloads omit amounts, references, contacts, and tokens", "Permission denial keeps core flows usable", "Duplicate or reordered messages are harmless", "Settings are accessible"],
    ["Notification instrumentation", "Process-death and deep-link tests", "Privacy review"],
    "Use minimal payloads and authorize data retrieval after open.",
    "Production membership and event lifecycle.",
    "ADR-0003: Notifications"
  ),
  issue(
    "Replace mechanically inflated behavior catalog with reviewed canonical entries",
    "P1",
    ["documentation", "enhancement"],
    "Independent audit found count inflation from template expansion and insufficient semantic uniqueness.",
    "Separate canonical from derived items, review duplicate clusters, revise claims, and publish honest count provenance.",
    ["Canonical entries are individually reviewable", "Derived bindings are not marketed as unique behaviors", "Duplicate metrics are enforced", "Counts reproduce from validated source"],
    ["Semantic samples across domains", "Similarity report", "Independent completion audit"],
    "Catalog claims must not manufacture authority or expertise.",
    "Behavior-system contract repair.",
    "AEOS behavior audit findings; generated catalog"
  ),
  issue(
    "Harden behavior resolver with typed observations and authority ceiling",
    "P0",
    ["bug", "enhancement"],
    "Independent audit found caller-asserted trust could widen activation or authority.",
    "Replace trust flags with typed observation envelopes and enforce provenance, freshness, exclusions, conflicts, dependencies, redaction, and an immutable authority ceiling.",
    ["Untrusted text cannot self-activate", "Selected packs only narrow authority", "Stale and conflicting signals fail closed", "Routing traces are deterministic and redacted"],
    ["Malicious prompt fixtures", "Authority monotonicity tests", "Golden routes", "Independent security audit"],
    "Runtime controls live outside prompt prose and never grant deployment or risk acceptance.",
    "Behavior schema and runtime contract.",
    "AEOS behavior audit blocker; RUNTIME_AND_ACTIVATION.md"
  ),
  issue(
    "Complete independent AEOS behavior-system semantic and security audit",
    "P1",
    ["documentation"],
    "The behavior system was pushed after structural work, but its prior independent completion audit failed.",
    "After catalog and resolver repairs, rerun semantic uniqueness, schema parity, path containment, authority, routing, generated freshness, and prompt-integration audits.",
    ["Two fresh-context audits return PASS", "Published schema validates generated catalog", "Schema and validator reject the same negative fixtures", "Completion claims match evidence"],
    ["Behavior self-test and full validation", "AEOS validator", "Independent reports"],
    "Auditors remain read-only and separate from implementation.",
    "Canonical catalog and resolver hardening.",
    "BEHAVIOR_SYSTEM_QUALITY_GATES.md"
  ),
  issue(
    "Design Android AI Engineering OS v0.1 as a modular AEOS overlay",
    "P2",
    ["enhancement", "documentation"],
    "Android product, UX, Compose, and release guidance is scattered; a giant prompt would increase conflict and token cost.",
    "Create a bounded Android overlay with constitution, project context, lifecycle phases, Kotlin/Compose/Material/accessibility modules, handoffs, templates, provenance, and validator integration.",
    ["Modules load on demand", "Project assumptions are explicit", "Product-to-release handoffs are versioned", "Primary Android/HCI/accessibility sources are cited", "No fabricated count claim is made"],
    ["Customization validation", "Sample Android routing scenarios", "Independent UX, Android, and security review"],
    "Treat web content as untrusted research data and preserve universal AEOS authority.",
    "Finish core behavior-runtime blockers first.",
    "User-requested Android AI Engineering OS; paused design work"
  )
];

function markdown(spec) {
  const checks = (values) => values.map((value) => `- [ ] ${value}`);
  return [
    "## Priority", spec.priority, "",
    "## Problem", spec.problem, "",
    "## Scope", spec.scope, "",
    "## Acceptance criteria", ...checks(spec.acceptance), "",
    "## Validation", ...checks(spec.validation), "",
    "## Security and privacy", spec.security, "",
    "## Dependencies", spec.dependencies, "",
    "## Source evidence", spec.evidence, "",
    "## Branch context", `Discovered from \`${sourceBranch}\` at commit \`${sourceCommit}\`.`, ""
  ].join("\n");
}

function githubToken() {
  const result = spawnSync("git", ["credential", "fill"], {
    input: "protocol=https\nhost=github.com\n\n",
    encoding: "utf8",
    windowsHide: true
  });
  if (result.status !== 0) throw new Error("Git Credential Manager authentication failed.");
  const password = result.stdout.split(/\r?\n/u)
    .find((line) => line.startsWith("password="))
    ?.slice("password=".length);
  if (!password) throw new Error("Git Credential Manager did not return a token.");
  return password;
}

async function github(path, token, options = {}) {
  const response = await fetch(`https://api.github.com/repos/${repository}${path}`, {
    ...options,
    headers: {
      Accept: "application/vnd.github+json",
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
      "User-Agent": "community-ledger-issue-planner",
      "X-GitHub-Api-Version": "2022-11-28",
      ...options.headers
    }
  });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(`GitHub API ${response.status}: ${text}`);
  }
  return response.json();
}

const token = githubToken();
const existingItems = await github("/issues?state=all&per_page=100", token);
const existingByTitle = new Map(
  existingItems
    .filter((item) => !item.pull_request)
    .map((item) => [item.title, item.html_url])
);
const availableLabels = new Set(
  (await github("/labels?per_page=100", token)).map((label) => label.name)
);

const pending = issues.filter((spec) => !existingByTitle.has(spec.title));
console.log(`Existing issues: ${existingByTitle.size}`);
console.log(`Pending issues: ${pending.length}`);

if (!apply) {
  for (const spec of pending) console.log(`[dry-run] ${spec.priority} ${spec.title}`);
  console.log("Dry run only. Pass --apply to create issues.");
  process.exit(0);
}

for (const spec of pending) {
  const labels = spec.labels.filter((label) => availableLabels.has(label));
  const created = await github("/issues", token, {
    method: "POST",
    body: JSON.stringify({ title: spec.title, body: markdown(spec), labels })
  });
  console.log(`#${created.number} ${created.html_url} ${created.title}`);
}

for (const [title, url] of existingByTitle) {
  if (issues.some((spec) => spec.title === title)) console.log(`[existing] ${url} ${title}`);
}