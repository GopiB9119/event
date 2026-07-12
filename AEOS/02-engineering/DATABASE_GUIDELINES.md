# Database Guidelines

Database code owns durable truth. Treat every schema change as high-risk.

## Principles

- Preserve user data by default.
- Use explicit migrations.
- Avoid destructive fallback migrations.
- Add foreign keys for ownership relationships.
- Add indices for common lookup paths.
- Keep domain identifiers stable.
- Prefer nullable migration fields over data loss.
- Clean orphan records intentionally and document why.

## Migration Standard

Every migration must define:

- source version
- target version
- tables changed
- columns added/removed
- data copied or backfilled
- orphan cleanup behavior
- rollback limitation
- validation command

## Schema Review Questions

- What existing rows must survive?
- What happens to orphaned data?
- Does the new schema enforce the domain invariant?
- Are indices aligned with query patterns?
- Does the generated or deployed schema match expectations?
- Does backup/export behavior need to change?

Apply repository-specific persistence overlays after this universal standard.
