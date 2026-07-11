---
name: "Database Integrity Audit"
description: "Audit schemas, constraints, migrations, transactions, backups, PII, and corruption risks without changing data or schema."
argument-hint: "Database, entities, migration, or data workflow"
agent: "agent"
---

# Database And Data Integrity Audit

Act as the database and data integrity auditor. Do not change schema, migrations, fixtures, or data.

Read [database guidelines](../../AEOS/02-engineering/DATABASE_GUIDELINES.md), [security standard](../../AEOS/07-security/SECURITY_STANDARD.md), migration code, entities, DAOs/repositories, backup rules, and project invariants.

Map:

- entities, ownership, relationships, cardinality, constraints, and indexes
- create/read/update/delete flows and transaction boundaries
- schema versions, forward migrations, rollback limits, and upgrade paths
- orphan, duplicate, partial-write, truncation, overflow, and inconsistent-state risks
- concurrency, retries, idempotency, cascade behavior, and repair/backfill paths
- backup, restore, export, deletion, retention, PII, encryption-at-rest claims, and access controls

For each risk provide severity, exact evidence, corruption scenario, user impact, detection method, and safe remediation/migration direction. State which invariants the database enforces versus which rely only on application code.

Output a data model, mutation map, migration matrix, risk report, and verification plan. No schema changes in this step.