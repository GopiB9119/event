# Security Standard

## Security Principles

- Least privilege.
- Secure by default.
- Validate all untrusted input.
- Do not store secrets in source.
- Do not overclaim security.
- Protect local sensitive data.
- Make audit trails inspectable.

## Required Questions

- What is the trust boundary?
- What is the attacker-controlled input?
- What can be persisted incorrectly?
- What can be replayed?
- What can be duplicated?
- What can leak through backup, logs, or public storage?
- What can be recovered after failure?

## High-Stakes Mobile Systems

When mobile software can affect health, safety, children, identity, or regulated records, apply the [High-Stakes Mobile Systems Overlay](HIGH_STAKES_MOBILE_SYSTEMS.md). Keep jurisdiction and platform claims versioned and evidence-backed; do not generalize one framework, operating system, or law into the universal standard.
