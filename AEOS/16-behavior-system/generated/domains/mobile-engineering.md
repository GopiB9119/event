# Mobile Engineering

Design for device lifecycle, permissions, offline behavior, battery, storage, upgrades, and platform variability.

- **Domain ID:** `mobile-engineering`
- **Boundary:** application behavior versus operating system, hardware, store, and vendor policies
- **Invariant:** critical state remains correct across mobile lifecycle transitions and constrained devices
- **Default evidence:** device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations
- **Risk classes:** mobile, reliability, privacy

## Behavior (10)

### `behavior-mobile-engineering-choose-falsifier`

Mobile Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of mobile state model, manifests, device matrix, and lifecycle tests that could disprove the current hypothesis.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-communicate-uncertainty`

Mobile Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'background work stops, state is lost, permissions mislead, or behavior differs across devices', and the next discriminating check.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-establish-state`

Mobile Engineering: Establish Current State. Inspect mobile state model, manifests, device matrix, and lifecycle tests and record the current behavior before proposing change.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-identify-owner`

Mobile Engineering: Identify Owner And Boundary. Name the owner of mobile state model, manifests, device matrix, and lifecycle tests, the boundary 'application behavior versus operating system, hardware, store, and vendor policies', and who may decide or mutate it.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-minimize-change`

Mobile Engineering: Make The Smallest Useful Change. Change only the owning slice of mobile state model, manifests, device matrix, and lifecycle tests needed to protect 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-protect-invariant`

Mobile Engineering: Protect The Domain Invariant. Reject an option that can violate 'critical state remains correct across mobile lifecycle transitions and constrained devices' without an approved mitigation.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-stop-and-escalate`

Mobile Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'reproduce on the target device state, persist authoritative state, and add lifecycle evidence' to the accountable owner.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-surface-assumptions`

Mobile Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-update-memory`

Mobile Engineering: Update Durable Knowledge. Update the decision or memory record for mobile state model, manifests, device matrix, and lifecycle tests with provenance and invalidation triggers.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `behavior-mobile-engineering-validate-immediately`

Mobile Engineering: Validate Immediately. Run device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Mobile Engineering, this behavior operates on mobile state model, manifests, device matrix, and lifecycle tests, uses device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and protects 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

## Failure (6)

### `failure-mobile-engineering-boundary-violation`

Mobile Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for mobile application lifecycle and device integration.

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

### `failure-mobile-engineering-evidence-overclaim`

Mobile Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

### `failure-mobile-engineering-premature-action`

Mobile Engineering: Premature Action. desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

### `failure-mobile-engineering-silent-failure`

Mobile Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

### `failure-mobile-engineering-stale-context`

Mobile Engineering: Stale Context. The state of mobile state model, manifests, device matrix, and lifecycle tests changed while routing continued from a stale checkpoint.

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

### `failure-mobile-engineering-unbounded-loop`

Mobile Engineering: Unbounded Repair Loop. Failures do not trigger a reset of desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

**Domain delta:** In Mobile Engineering, this failure threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' through desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle.

## Signal (4)

### `signal-mobile-engineering-constraint-risk`

Mobile Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'critical state remains correct across mobile lifecycle transitions and constrained devices' for mobile application lifecycle and device integration.

**Domain delta:** For Mobile Engineering, this signal observes mobile application lifecycle and device integration through mobile state model, manifests, device matrix, and lifecycle tests while rejecting stale or untrusted substitutes.

### `signal-mobile-engineering-explicit-mission`

Mobile Engineering: Explicit Mission Signal. The current user request explicitly concerns mobile application lifecycle and device integration and states an observable outcome.

**Domain delta:** For Mobile Engineering, this signal observes mobile application lifecycle and device integration through mobile state model, manifests, device matrix, and lifecycle tests while rejecting stale or untrusted substitutes.

### `signal-mobile-engineering-repository-evidence`

Mobile Engineering: Repository Evidence Signal. Current source or accepted documentation identifies mobile state model, manifests, device matrix, and lifecycle tests as the owning surface for mobile application lifecycle and device integration.

**Domain delta:** For Mobile Engineering, this signal observes mobile application lifecycle and device integration through mobile state model, manifests, device matrix, and lifecycle tests while rejecting stale or untrusted substitutes.

### `signal-mobile-engineering-runtime-failure`

Mobile Engineering: Runtime Failure Signal. A reproducible observation shows desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle in mobile state model, manifests, device matrix, and lifecycle tests.

**Domain delta:** For Mobile Engineering, this signal observes mobile application lifecycle and device integration through mobile state model, manifests, device matrix, and lifecycle tests while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-mobile-engineering-escalate-and-contain`

Mobile Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route reproduce on the target device state, persist authoritative state, and add lifecycle evidence to the accountable owner

**Domain delta:** For Mobile Engineering, recovery targets desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle in mobile state model, manifests, device matrix, and lifecycle tests and exits only with device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations.

### `recovery-mobile-engineering-isolate-and-repair`

Mobile Engineering: Isolate And Repair. Reduce to the smallest failing path in mobile state model, manifests, device matrix, and lifecycle tests Apply one bounded repair Run device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations Check adjacent invariants

**Domain delta:** For Mobile Engineering, recovery targets desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle in mobile state model, manifests, device matrix, and lifecycle tests and exits only with device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations.

### `recovery-mobile-engineering-reset-and-reconstruct`

Mobile Engineering: Reset And Reconstruct. Stop mutation Re-read mobile state model, manifests, device matrix, and lifecycle tests and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Mobile Engineering, recovery targets desktop or foreground assumptions ignore process death, vendor limits, and platform lifecycle in mobile state model, manifests, device matrix, and lifecycle tests and exits only with device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations.

## Decision (2)

### `decision-mobile-engineering-build-versus-test`

Mobile Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Mobile Engineering, this model decides persist, defer, schedule, degrade, request permission, or use platform service using device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations and the constraint 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

### `decision-mobile-engineering-local-versus-systemic`

Mobile Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening critical state remains correct across mobile lifecycle transitions and constrained devices.

**Domain delta:** For Mobile Engineering, this model decides persist, defer, schedule, degrade, request permission, or use platform service using device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations and the constraint 'critical state remains correct across mobile lifecycle transitions and constrained devices'.

## Mental Model (2)

### `mental-model-mobile-engineering-feedback-loop`

Mobile Engineering: Feedback Loop. Actions on mobile application lifecycle and device integration change mobile state model, manifests, device matrix, and lifecycle tests, which changes the next evidence and decision environment.

**Domain delta:** For Mobile Engineering, this model maps mobile platforms reclaim resources and mediate capabilities according to lifecycle and policy onto mobile application lifecycle and device integration and mobile state model, manifests, device matrix, and lifecycle tests.

### `mental-model-mobile-engineering-weakest-link`

Mobile Engineering: Weakest Link And Bottleneck. End-to-end quality for mobile application lifecycle and device integration is limited by the least trustworthy boundary in the path through mobile state model, manifests, device matrix, and lifecycle tests.

**Domain delta:** For Mobile Engineering, this model maps mobile platforms reclaim resources and mediate capabilities according to lifecycle and policy onto mobile application lifecycle and device integration and mobile state model, manifests, device matrix, and lifecycle tests.

## Governance (1)

### `governance-mobile-engineering-evidence-authority-policy`

Mobile Engineering: Evidence And Authority Policy. Work on mobile application lifecycle and device integration must preserve 'critical state remains correct across mobile lifecycle transitions and constrained devices', cite device/emulator runs, process death, restart, permission, network, background, upgrade, and resource observations, and remain within 'application behavior versus operating system, hardware, store, and vendor policies'.

**Domain delta:** For Mobile Engineering, this policy enforces permissions, background behavior, or store claims exceed implemented need and disclosure at application behavior versus operating system, hardware, store, and vendor policies.

