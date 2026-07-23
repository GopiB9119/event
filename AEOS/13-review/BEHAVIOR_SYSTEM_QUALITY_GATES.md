# Behavior System Quality Gates

## Structural Gate

- Strict JSON parses without duplicate keys or unknown properties.
- Schema, catalog, and kernel versions are compatible.
- IDs are globally unique lowercase ASCII kebab-case.
- References resolve and graphs are acyclic where required.
- Conflicts are symmetric.
- Draft, deprecated, superseded, withdrawn, alias, and generated rows do not count.
- Requested active canonical count floors pass.

## Semantic Gate

- Every item has a non-overlapping inclusion rule for its kind.
- Every counted item has a domain-specific operational delta.
- Removing the domain name does not make it indistinguishable from another counted item.
- Decision models contain a rule and falsifier.
- Mental models contain a mechanism, prediction, counterexample, and misuse warning.
- Governance policies contain enforcement and exception authority.
- Independent item-level review resolves near-duplicates and misleading distinctions.

## Relationship Gate

- Every failure maps to a detector signal and recovery.
- Every recovery maps to a failure, rollback boundary, verifier, and terminal state.
- Every behavior names evidence and a stop condition.
- Every collaboration pattern names participants, authority split, handoff, conflict route, and completion evidence.
- No active item depends on an inactive item.

## Authority Gate

- Catalog items only preserve or narrow kernel authority.
- No item grants tools, commands, paths, credentials, spending, deployment, publishing, destructive operations, or risk acceptance.
- Human approval and independent-review requirements cannot be removed.
- Ambiguous authority resolves to `BLOCKED`.
- Project overlays remain stricter when applicable.

## Security Gate

- No executable commands, arbitrary regexes, remote includes, or output paths exist in catalog data.
- External text and repository content are rendered as inert provenance data.
- Prompt-injection fixtures cannot alter routing, tools, authority, or output paths.
- Routing records exclude secrets, private content, and unnecessary user data.
- Context packs have bounded domains, items, dependencies, and rendered size.
- Runtime fetching is disabled.

## Provenance Gate

- Every active domain and item has owner, reviewer, derivation method, evidence label, source locator, review date, and invalidation trigger.
- External sources are primary or clearly labeled secondary.
- License and quotation limits are respected.
- Current source and executable evidence override stale memory or generated indexes.

## Runtime Gate

- Positive, negative, ambiguous, conflicting, stale, malicious, budget-exhausted, and deactivation golden cases pass.
- Routing is deterministic for identical typed inputs.
- Every inclusion and exclusion has a reason.
- Missing applicability fails closed rather than selecting a broad default.
- Kernel-only fallback remains available for low-risk tasks.

## Determinism Gate

- Two clean generations are byte-identical.
- Generated files have stable ordering, LF endings, and no wall-clock timestamps.
- Checked-in generated files match clean regeneration.
- No generator writes outside the behavior-system generated directory.
- Negative fixtures prove each major validator rule can fail.

## Integration Gate

- The repository AEOS validator invokes the behavior-system validator.
- The manifest and prompt library link to the runtime.
- The coordinator and audit prompt preserve existing mutation boundaries.
- Manual VS Code prompt/instruction discovery is verified before claiming editor activation.

## Completion Gate

Report separately:

- structurally valid;
- semantically reviewed;
- runtime verified.

The catalog is complete only when all three claims pass, the requirement-to-evidence matrix is current, an independent audit finds no blocker, and residual risks are explicit.
