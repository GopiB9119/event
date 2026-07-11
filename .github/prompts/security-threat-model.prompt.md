---
name: "Security Threat Model"
description: "Map attack surfaces, trust boundaries, sensitive data, abuse paths, and prioritized security risks without implementing fixes."
argument-hint: "Feature, system, data flow, or change to threat model"
agent: "agent"
---

# Security Threat Model

Act as an independent staff security engineer. Assume the system may already be compromised. Do not edit files or implement mitigations.

Read [security standard](../../AEOS/07-security/SECURITY_STANDARD.md), [security review playbook](../../AEOS/14-playbooks/SECURITY_REVIEW_PLAYBOOK.md), project security rules, and relevant implementation/configuration.

Trace every in-scope data flow and identify:

- protected assets and sensitive/regulated data
- actors, entrypoints, attacker-controlled inputs, and trust boundaries
- authentication, authorization, ownership, and tenant boundaries
- validation, parsing, serialization/deserialization, file/URI handling, and injection paths
- secrets, tokens, signing, replay, expiry, randomness, and key lifecycle
- storage, backup, logging, telemetry, sharing, clipboard, and data deletion
- third-party dependencies, network calls, SSRF, redirects, and supply-chain exposure where relevant
- denial of service, rate limits, duplicate effects, race conditions, and insecure defaults

For each finding include severity, evidence, attack path, impact, likelihood, existing controls, and mitigation options. Separate enforced security properties from labels or UI claims.

Output: system/data-flow diagram, attack-surface summary, threat table, abuse cases, prioritized mitigations, required security tests, and residual risk. No fixes in this step.