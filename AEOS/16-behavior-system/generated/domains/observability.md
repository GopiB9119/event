# Observability

Make system state, failures, latency, cost, and agent behavior diagnosable without exposing sensitive content.

- **Domain ID:** `observability`
- **Boundary:** operational telemetry versus user content, secrets, and privacy
- **Invariant:** critical failures are detectable and attributable without leaking protected data
- **Default evidence:** signal coverage, redaction tests, alert exercises, trace correlation, and incident use
- **Risk classes:** operations, privacy, reliability

## Behavior (10)

### `behavior-observability-choose-falsifier`

Observability: Choose Cheapest Falsifier. Choose the lowest-cost check of observability schema, dashboards, alerts, and diagnostic runbook that could disprove the current hypothesis.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-communicate-uncertainty`

Observability: Communicate Uncertainty. State confidence, missing evidence, failure impact 'silent failures persist, incidents take longer, or sensitive data enters logs', and the next discriminating check.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-establish-state`

Observability: Establish Current State. Inspect observability schema, dashboards, alerts, and diagnostic runbook and record the current behavior before proposing change.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-identify-owner`

Observability: Identify Owner And Boundary. Name the owner of observability schema, dashboards, alerts, and diagnostic runbook, the boundary 'operational telemetry versus user content, secrets, and privacy', and who may decide or mutate it.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-minimize-change`

Observability: Make The Smallest Useful Change. Change only the owning slice of observability schema, dashboards, alerts, and diagnostic runbook needed to protect 'critical failures are detectable and attributable without leaking protected data'.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-protect-invariant`

Observability: Protect The Domain Invariant. Reject an option that can violate 'critical failures are detectable and attributable without leaking protected data' without an approved mitigation.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-stop-and-escalate`

Observability: Stop And Escalate. Stop mutation, preserve evidence, and route 'add the minimum diagnostic signal at the first uncertain boundary and verify alert-to-action flow' to the accountable owner.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-surface-assumptions`

Observability: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'critical failures are detectable and attributable without leaking protected data'.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-update-memory`

Observability: Update Durable Knowledge. Update the decision or memory record for observability schema, dashboards, alerts, and diagnostic runbook with provenance and invalidation triggers.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

### `behavior-observability-validate-immediately`

Observability: Validate Immediately. Run signal coverage, redaction tests, alert exercises, trace correlation, and incident use or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Observability, this behavior operates on observability schema, dashboards, alerts, and diagnostic runbook, uses signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and protects 'critical failures are detectable and attributable without leaking protected data'.

## Failure (6)

### `failure-observability-boundary-violation`

Observability: Boundary Violation. A local optimization bypasses the domain ownership model for logs, metrics, traces, events, and health signals.

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

### `failure-observability-evidence-overclaim`

Observability: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

### `failure-observability-premature-action`

Observability: Premature Action. systems emit raw volume without actionable correlation, ownership, or retention

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

### `failure-observability-silent-failure`

Observability: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

### `failure-observability-stale-context`

Observability: Stale Context. The state of observability schema, dashboards, alerts, and diagnostic runbook changed while routing continued from a stale checkpoint.

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

### `failure-observability-unbounded-loop`

Observability: Unbounded Repair Loop. Failures do not trigger a reset of systems emit raw volume without actionable correlation, ownership, or retention.

**Domain delta:** In Observability, this failure threatens 'critical failures are detectable and attributable without leaking protected data' through systems emit raw volume without actionable correlation, ownership, or retention.

## Signal (4)

### `signal-observability-constraint-risk`

Observability: Constraint Or Risk Signal. A current constraint or risk threatens 'critical failures are detectable and attributable without leaking protected data' for logs, metrics, traces, events, and health signals.

**Domain delta:** For Observability, this signal observes logs, metrics, traces, events, and health signals through observability schema, dashboards, alerts, and diagnostic runbook while rejecting stale or untrusted substitutes.

### `signal-observability-explicit-mission`

Observability: Explicit Mission Signal. The current user request explicitly concerns logs, metrics, traces, events, and health signals and states an observable outcome.

**Domain delta:** For Observability, this signal observes logs, metrics, traces, events, and health signals through observability schema, dashboards, alerts, and diagnostic runbook while rejecting stale or untrusted substitutes.

### `signal-observability-repository-evidence`

Observability: Repository Evidence Signal. Current source or accepted documentation identifies observability schema, dashboards, alerts, and diagnostic runbook as the owning surface for logs, metrics, traces, events, and health signals.

**Domain delta:** For Observability, this signal observes logs, metrics, traces, events, and health signals through observability schema, dashboards, alerts, and diagnostic runbook while rejecting stale or untrusted substitutes.

### `signal-observability-runtime-failure`

Observability: Runtime Failure Signal. A reproducible observation shows systems emit raw volume without actionable correlation, ownership, or retention in observability schema, dashboards, alerts, and diagnostic runbook.

**Domain delta:** For Observability, this signal observes logs, metrics, traces, events, and health signals through observability schema, dashboards, alerts, and diagnostic runbook while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-observability-escalate-and-contain`

Observability: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route add the minimum diagnostic signal at the first uncertain boundary and verify alert-to-action flow to the accountable owner

**Domain delta:** For Observability, recovery targets systems emit raw volume without actionable correlation, ownership, or retention in observability schema, dashboards, alerts, and diagnostic runbook and exits only with signal coverage, redaction tests, alert exercises, trace correlation, and incident use.

### `recovery-observability-isolate-and-repair`

Observability: Isolate And Repair. Reduce to the smallest failing path in observability schema, dashboards, alerts, and diagnostic runbook Apply one bounded repair Run signal coverage, redaction tests, alert exercises, trace correlation, and incident use Check adjacent invariants

**Domain delta:** For Observability, recovery targets systems emit raw volume without actionable correlation, ownership, or retention in observability schema, dashboards, alerts, and diagnostic runbook and exits only with signal coverage, redaction tests, alert exercises, trace correlation, and incident use.

### `recovery-observability-reset-and-reconstruct`

Observability: Reset And Reconstruct. Stop mutation Re-read observability schema, dashboards, alerts, and diagnostic runbook and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Observability, recovery targets systems emit raw volume without actionable correlation, ownership, or retention in observability schema, dashboards, alerts, and diagnostic runbook and exits only with signal coverage, redaction tests, alert exercises, trace correlation, and incident use.

## Decision (2)

### `decision-observability-build-versus-test`

Observability: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Observability, this model decides log, metric, trace, event, sample, redact, alert, or omit using signal coverage, redaction tests, alert exercises, trace correlation, and incident use and the constraint 'critical failures are detectable and attributable without leaking protected data'.

### `decision-observability-local-versus-systemic`

Observability: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening critical failures are detectable and attributable without leaking protected data.

**Domain delta:** For Observability, this model decides log, metric, trace, event, sample, redact, alert, or omit using signal coverage, redaction tests, alert exercises, trace correlation, and incident use and the constraint 'critical failures are detectable and attributable without leaking protected data'.

## Mental Model (2)

### `mental-model-observability-feedback-loop`

Observability: Feedback Loop. Actions on logs, metrics, traces, events, and health signals change observability schema, dashboards, alerts, and diagnostic runbook, which changes the next evidence and decision environment.

**Domain delta:** For Observability, this model maps observability reduces uncertainty by connecting state transitions across time and boundaries onto logs, metrics, traces, events, and health signals and observability schema, dashboards, alerts, and diagnostic runbook.

### `mental-model-observability-weakest-link`

Observability: Weakest Link And Bottleneck. End-to-end quality for logs, metrics, traces, events, and health signals is limited by the least trustworthy boundary in the path through observability schema, dashboards, alerts, and diagnostic runbook.

**Domain delta:** For Observability, this model maps observability reduces uncertainty by connecting state transitions across time and boundaries onto logs, metrics, traces, events, and health signals and observability schema, dashboards, alerts, and diagnostic runbook.

## Governance (1)

### `governance-observability-evidence-authority-policy`

Observability: Evidence And Authority Policy. Work on logs, metrics, traces, events, and health signals must preserve 'critical failures are detectable and attributable without leaking protected data', cite signal coverage, redaction tests, alert exercises, trace correlation, and incident use, and remain within 'operational telemetry versus user content, secrets, and privacy'.

**Domain delta:** For Observability, this policy enforces sensitive payloads, credentials, or unbounded telemetry are collected without need and retention at operational telemetry versus user content, secrets, and privacy.

