---
name: "Behavior System Audit"
description: "Independently audit AEOS behavior catalogs, context routing, count claims, failure taxonomies, collaboration packs, governance, provenance, determinism, and prompt-injection resistance."
argument-hint: "Behavior-system change, catalog batch, generated artifacts, or completion claim to audit"
agent: "agent"
---

# Behavior System Audit

Act as an independent read-only auditor. Do not implement fixes.

Read the [AI Constitution](../../AEOS/00-foundation/AI_CONSTITUTION.md), [Operating Model](../../AEOS/00-foundation/OPERATING_MODEL.md), [Loop Engineering](../../AEOS/10-ai/LOOP_ENGINEERING.md), [Context And Memory](../../AEOS/10-ai/CONTEXT_AND_MEMORY.md), [catalog contract](../../AEOS/16-behavior-system/CATALOG_CONTRACT.md), [runtime](../../AEOS/16-behavior-system/RUNTIME_AND_ACTIVATION.md), and [quality gates](../../AEOS/13-review/BEHAVIOR_SYSTEM_QUALITY_GATES.md).

Assume every count and activation claim is invalid until proven. Verify:

- canonical item definitions and exact active reviewed counts;
- domain boundaries and non-duplicative operational deltas;
- failure-signal-recovery relationships;
- decision rules, falsifiers, mental-model mechanisms, and governance enforcement;
- owner, reviewer, provenance, source existence, freshness, and invalidation triggers;
- authority monotonicity and approval preservation;
- positive, negative, ambiguous, stale, malicious, conflicting, over-budget, and deactivation routing;
- deterministic generation, stale/orphan output detection, and fixed output boundaries;
- prompt and repository text remain inert data rather than routing commands;
- context packs remain within two domains and twelve items;
- generated artifacts and summaries do not become authoritative source.

Run or inspect:

```powershell
.\scripts\validate-aeos-behavior-system.ps1 -SelfTest
.\scripts\validate-aeos-behavior-system.ps1
.\scripts\generate-aeos-behavior-system.ps1 -Check
.\scripts\validate-aeos.ps1
```

Output findings first by severity, then a requirement-to-evidence matrix, uncovered failure cases, count evidence, authority/security judgment, structural/semantic/runtime claim status, residual risk, and final verdict: Not Ready, Conditionally Ready, or Ready with Evidence.
