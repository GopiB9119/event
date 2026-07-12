# High-Stakes Mobile Systems Overlay

## Purpose

This overlay applies when mobile software can affect health, safety, children, identity, regulated records, or other high-impact outcomes. It supplements the AEOS core; it does not turn generic agents into clinicians, lawyers, privacy officers, or security risk owners.

Healthcare-specific implementation begins only after qualified owners define the intended use, safety boundary, jurisdictions, regulated data, and release authority.

## Overlay Contract

Before design or implementation, record:

```text
Intended use and explicit non-medical/non-safety claims:
Users, caregivers, and affected non-users:
Qualified domain owner:
Security and privacy risk owners:
Jurisdictions and distribution channels:
Current primary legal/policy sources and effective dates:
Data categories, purposes, processors, and retention:
Clinical or safety hazards and acceptable degraded behavior:
Supported OS/device versions:
Offline, permission, reboot, time-change, and update behavior:
Incident, support, and user-recovery paths:
Independent validation and release authority:
```

If the intended use, qualified owner, or applicable legal scope is unresolved, return `BLOCKED`, `DEFER`, or `TEST`. Do not infer them from a product name such as "care," "health," or "medical."

## Safety Boundary

- State whether the product is informational, administrative, wellness, clinical decision support, or a regulated medical function based on qualified review.
- Do not claim that a reminder guarantees medication adherence, delivery, dosage correctness, or a health outcome.
- Define what happens when the app, device, operating system, battery, permission, clock, or network fails.
- Provide a visible degraded state and a safe user recovery path.
- Avoid a single point of failure for outcomes whose loss could cause harm. The accountable domain owner decides whether an independent reminder or care process is required.
- Keep medication instructions and schedule changes traceable to an authorized human or authoritative care source; an AI agent must not invent them.

## Scheduled Reminder Model When Applicable

Apply this section only when the product sends time-dependent reminders or alerts. Other high-stakes mobile systems continue at the security baseline.

A reminder schedule must distinguish:

- the user-entered local wall-clock rule
- the associated IANA time zone when travel or daylight-saving changes matter
- the next computed instant
- recurrence rules and exceptions
- start/end bounds
- schedule version and source
- last reconciliation result

Persist the schedule as product data. Treat operating-system notifications or alarms as a derived queue that can be rebuilt idempotently.

Reconcile desired and scheduled reminders after creation, edit, deletion, app update, app launch, relevant permission change, and platform time/time-zone events. On Android, reboot rescheduling and exact-alarm access depend on the current API level, declared capabilities, user settings, and store policy. On iOS, use supported notification APIs and do not invent an Android-style boot receiver or assume background execution will run on demand.

Exact delivery is a platform capability decision, not a copywriting promise. The UI must disclose when only inexact or unavailable scheduling is active. Remote push may improve some delivery paths, but it introduces network, backend, authentication, privacy, outage, and provider dependencies; it is not an automatic fallback.

## Mobile Security Baseline

Use a current OWASP MASVS profile and applicable MASTG tests as a review baseline, then add domain threats. A checklist does not replace a threat model.

Required controls include:

- inventory protected assets and data flows across device, backend, notification provider, analytics, support, export, backup, and deletion paths
- keep third-party service credentials and confidential client secrets out of distributable mobile artifacts
- treat mobile OAuth clients as public clients and use current authorization best practices such as PKCE where the provider and flow require it
- store user-bound tokens and cryptographic keys through platform security APIs with access controls appropriate to the threat model
- verify whether keys are hardware-backed on the actual device before making that claim; Keychain, Keystore, TEE, Secure Enclave, and StrongBox are not interchangeable guarantees
- minimize lock-screen notification content and give users control over sensitive previews
- prevent sensitive data from leaking through logs, crash reports, screenshots, clipboard, backups, analytics, test artifacts, or support channels
- define key generation, rotation, invalidation, device migration, logout, account recovery, and deletion behavior
- validate server authorization for every protected object and action; local biometrics or device possession do not replace server authorization
- decide certificate pinning from the threat model and operational recovery design; pin rotation and outage risk must be handled before adoption
- verify dependency provenance, maintenance, advisories, permissions, and data collection before adding a mobile library

## Privacy And Regulatory Evidence

Create a jurisdiction-specific obligations matrix from current primary sources and qualified review. It must identify:

- accountable legal roles and processors
- purpose and lawful processing basis
- notice and affirmative consent requirements where applicable
- withdrawal and product behavior after withdrawal
- access, correction, deletion, nomination, grievance, and appeal paths where applicable
- child/guardian rules and age thresholds
- retention requirements, deletion exceptions, backups, and derived data
- cross-border or localization restrictions
- security safeguards, audit duties, breach notification recipients, content, and clocks
- records required to demonstrate compliance

Do not encode "immediate," "72 hours," age thresholds, penalty amounts, mandatory Consent Manager usage, erasure scope, or Significant Data Fiduciary/controller status as universal facts. Record the exact provision, rule version, effective date, applicability analysis, and qualified owner. Legal retention or safety duties may limit deletion; the product must explain the actual outcome rather than promise absolute erasure.

## Autonomous-Agent Restrictions

Agents working on this domain must:

- use discovery-first, product-agnostic role contracts
- keep health and identity data out of prompts, general memory, public logs, screenshots, and unapproved external tools
- use synthetic data for routine tests and access-controlled, consented fixtures only when real data is necessary
- require human approval for medical content, consent language, legal conclusions, production data access, security risk acceptance, and release
- stop on ambiguous dosage/schedule semantics, unauthorized data, missing rollback, failed safety gates, or unsupported platform claims
- preserve an audit trail of sources, decisions, code revision, test environment, and reviewer approval

## Verification Matrix

At minimum, verify the applicable combinations of:

- fresh install, upgrade, force-stop, process death, reboot, and app data removal
- permission granted, denied, revoked, and settings changed outside the app
- exact capability available/unavailable and inexact degradation
- offline, provider outage, backend outage, delayed delivery, and duplicate delivery
- device clock, time zone, daylight-saving transition, travel, and recurrence boundaries
- edit, skip, snooze, cancel, duplicate schedule, and stale schedule revision
- lock-screen privacy, accessibility, large text, locale, and caregiver workflows
- rooted/jailbroken or otherwise untrusted-device posture according to policy
- token expiry, logout, account recovery, device migration, data export, withdrawal, deletion, and backup expiry
- security tests mapped to the selected MASVS profile and domain threat model

Use physical devices representative of the supported population. Emulator success alone cannot establish reminder reliability or hardware-backed security.

## Release Gate

A high-stakes mobile release is blocked until the overlay contract is complete, hazards and trust boundaries are reviewed, sensitive data has an owned lifecycle, degraded reminder behavior is visible, platform/store claims are verified against supported versions, applicable legal conclusions have qualified approval, independent security and domain validation pass, incident/recovery paths are rehearsed, and residual risk is accepted by the named human owner.

## Related

- [Security Standard](SECURITY_STANDARD.md)
- [Loop Engineering Runtime](../10-ai/LOOP_ENGINEERING.md)
- [Operating Model](../00-foundation/OPERATING_MODEL.md)
- [Testing Standard](../09-testing/TESTING_STANDARD.md)
- [Production Readiness Checklist](../13-review/PRODUCTION_READINESS_CHECKLIST.md)
- [Security Review Playbook](../14-playbooks/SECURITY_REVIEW_PLAYBOOK.md)
