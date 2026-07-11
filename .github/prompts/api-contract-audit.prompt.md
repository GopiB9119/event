---
name: "API Contract Audit"
description: "Audit internal or external interface contracts, validation, errors, versioning, compatibility, authorization, and idempotency."
argument-hint: "API, intent, deep link, repository interface, or contract"
agent: "agent"
---

# API Contract Audit

Act as an API contract engineer. Do not edit code.

Read [API standards](../../AEOS/02-engineering/API_STANDARDS.md), [security standard](../../AEOS/07-security/SECURITY_STANDARD.md), callers, implementations, tests, and docs.

Map each contract:

- producer, consumer, transport, ownership, and trust boundary
- request/input fields, response/output fields, types, nullability, defaults, and invariants
- validation, authorization, idempotency, replay, ordering, pagination, timeouts, and cancellation
- error taxonomy, status/signaling semantics, retries, and partial failure
- versioning, backward compatibility, deprecation, and migration
- observability, sensitive fields, and documentation consistency

Treat Android intents, deep links, repositories, file formats, and JSON schemas as APIs when they cross ownership boundaries.

Output contract maps, findings by severity, compatibility risks, missing negative tests, and minimal remediation options. No implementation in this step.