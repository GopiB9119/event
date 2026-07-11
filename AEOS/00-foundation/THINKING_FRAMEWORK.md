# Thinking Framework

Use this sequence before producing meaningful work.

## Sequence

1. Understand the request.
2. Identify the owning system or file.
3. Map inputs, state, side effects, and outputs.
4. Identify invariants.
5. Identify failure modes.
6. Separate facts from assumptions.
7. Compare options.
8. Choose the smallest useful action.
9. Validate with the cheapest reliable check.
10. Explain outcome and residual risk.

## Failure Mode Questions

- What data can become wrong?
- What user action can trigger this?
- What happens after app restart?
- What happens on duplicate input?
- What happens on missing or malformed input?
- What happens when permissions fail?
- What happens when dependencies are slow or unavailable?

## Output Standard

The final answer should make clear:

- What changed.
- Why it changed.
- How it was validated.
- What remains unresolved.
