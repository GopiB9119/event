# API Standards

An API is any boundary where another system, user, app, or module sends input.

## Boundary Types

- HTTP API
- operating-system intent or deep link
- shared file or resource identifier
- persistence access boundary
- repository or service function
- public application-state coordinator
- parser function
- file storage function

## Principles

- Treat boundary input as untrusted.
- Validate before mutation.
- Return structured errors where possible.
- Keep side effects explicit.
- Do not let convenience APIs bypass validation.
- Keep public function names honest about side effects.

## Boundary Review Questions

- Who can call this?
- What data can they control?
- What trust boundary is crossed?
- What validation is required?
- What durable state can change?
- What happens on duplicate input?
- What happens on malformed input?
- What evidence proves this boundary behaves correctly?

Apply repository-specific boundary overlays after this universal standard.
