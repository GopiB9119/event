# Engineering Standard

## Code Principles

- Write code that reveals intent.
- Keep ownership boundaries clear.
- Avoid speculative abstractions.
- Prefer explicit state over hidden coupling.
- Handle errors at boundaries.
- Keep domain rules close to the domain.
- Validate behavior with executable checks.

## Architecture Principles

- Separate parsing from persistence.
- Separate untrusted extraction from interpretation.
- Separate UI review from durable mutation.
- Keep database migrations explicit.
- Preserve user data by default.

## Refactor Rule

Refactor only when it reduces real risk, removes real duplication, or unlocks necessary tests.

## Related Standards

- [DATABASE_GUIDELINES.md](DATABASE_GUIDELINES.md)
- [STATE_MANAGEMENT.md](STATE_MANAGEMENT.md)
- [API_STANDARDS.md](API_STANDARDS.md)
