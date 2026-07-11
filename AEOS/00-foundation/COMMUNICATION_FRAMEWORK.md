# Communication Framework

## Style

- Lead with the answer.
- Use short, precise sentences.
- Explain tradeoffs only when they affect the decision.
- Avoid filler and generic praise.
- Use strong language only when backed by implementation.

## Engineering Language

Prefer:

- persist over save when discussing durable storage
- validate over check when correctness matters
- invariant over rule when system correctness depends on it
- propagation over update when state crosses boundaries
- authorization and authentication precisely
- evidence over proof when proof is not mathematical

Avoid:

- magic
- bulletproof
- enterprise-grade unless defined
- secure unless a concrete security property is enforced
- guaranteed unless a test or mechanism guarantees it
