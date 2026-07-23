# Product Discovery

Establish whether a real user problem deserves product effort.

- **Domain ID:** `product-discovery`
- **Boundary:** problem evidence versus solution preference
- **Invariant:** features sharpen a validated product promise instead of merely increasing scope
- **Default evidence:** direct user, workflow, or production evidence that can change the product decision
- **Risk classes:** product, economics

## Behavior (10)

### `behavior-product-discovery-choose-falsifier`

Product Discovery: Choose Cheapest Falsifier. Choose the lowest-cost check of product evidence ledger that could disprove the current hypothesis.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-communicate-uncertainty`

Product Discovery: Communicate Uncertainty. State confidence, missing evidence, failure impact 'engineering effort accumulates around an unvalidated or weak idea', and the next discriminating check.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-establish-state`

Product Discovery: Establish Current State. Inspect product evidence ledger and record the current behavior before proposing change.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-identify-owner`

Product Discovery: Identify Owner And Boundary. Name the owner of product evidence ledger, the boundary 'problem evidence versus solution preference', and who may decide or mutate it.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-minimize-change`

Product Discovery: Make The Smallest Useful Change. Change only the owning slice of product evidence ledger needed to protect 'features sharpen a validated product promise instead of merely increasing scope'.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-protect-invariant`

Product Discovery: Protect The Domain Invariant. Reject an option that can violate 'features sharpen a validated product promise instead of merely increasing scope' without an approved mitigation.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-stop-and-escalate`

Product Discovery: Stop And Escalate. Stop mutation, preserve evidence, and route 'run the cheapest user or workflow falsification test' to the accountable owner.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-surface-assumptions`

Product Discovery: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'features sharpen a validated product promise instead of merely increasing scope'.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-update-memory`

Product Discovery: Update Durable Knowledge. Update the decision or memory record for product evidence ledger with provenance and invalidation triggers.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

### `behavior-product-discovery-validate-immediately`

Product Discovery: Validate Immediately. Run direct user, workflow, or production evidence that can change the product decision or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Product Discovery, this behavior operates on product evidence ledger, uses direct user, workflow, or production evidence that can change the product decision, and protects 'features sharpen a validated product promise instead of merely increasing scope'.

## Failure (6)

### `failure-product-discovery-boundary-violation`

Product Discovery: Boundary Violation. A local optimization bypasses the domain ownership model for the proposed user problem and value hypothesis.

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

### `failure-product-discovery-evidence-overclaim`

Product Discovery: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

### `failure-product-discovery-premature-action`

Product Discovery: Premature Action. solution enthusiasm substitutes for evidence of user need

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

### `failure-product-discovery-silent-failure`

Product Discovery: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

### `failure-product-discovery-stale-context`

Product Discovery: Stale Context. The state of product evidence ledger changed while routing continued from a stale checkpoint.

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

### `failure-product-discovery-unbounded-loop`

Product Discovery: Unbounded Repair Loop. Failures do not trigger a reset of solution enthusiasm substitutes for evidence of user need.

**Domain delta:** In Product Discovery, this failure threatens 'features sharpen a validated product promise instead of merely increasing scope' through solution enthusiasm substitutes for evidence of user need.

## Signal (4)

### `signal-product-discovery-constraint-risk`

Product Discovery: Constraint Or Risk Signal. A current constraint or risk threatens 'features sharpen a validated product promise instead of merely increasing scope' for the proposed user problem and value hypothesis.

**Domain delta:** For Product Discovery, this signal observes the proposed user problem and value hypothesis through product evidence ledger while rejecting stale or untrusted substitutes.

### `signal-product-discovery-explicit-mission`

Product Discovery: Explicit Mission Signal. The current user request explicitly concerns the proposed user problem and value hypothesis and states an observable outcome.

**Domain delta:** For Product Discovery, this signal observes the proposed user problem and value hypothesis through product evidence ledger while rejecting stale or untrusted substitutes.

### `signal-product-discovery-repository-evidence`

Product Discovery: Repository Evidence Signal. Current source or accepted documentation identifies product evidence ledger as the owning surface for the proposed user problem and value hypothesis.

**Domain delta:** For Product Discovery, this signal observes the proposed user problem and value hypothesis through product evidence ledger while rejecting stale or untrusted substitutes.

### `signal-product-discovery-runtime-failure`

Product Discovery: Runtime Failure Signal. A reproducible observation shows solution enthusiasm substitutes for evidence of user need in product evidence ledger.

**Domain delta:** For Product Discovery, this signal observes the proposed user problem and value hypothesis through product evidence ledger while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-product-discovery-escalate-and-contain`

Product Discovery: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route run the cheapest user or workflow falsification test to the accountable owner

**Domain delta:** For Product Discovery, recovery targets solution enthusiasm substitutes for evidence of user need in product evidence ledger and exits only with direct user, workflow, or production evidence that can change the product decision.

### `recovery-product-discovery-isolate-and-repair`

Product Discovery: Isolate And Repair. Reduce to the smallest failing path in product evidence ledger Apply one bounded repair Run direct user, workflow, or production evidence that can change the product decision Check adjacent invariants

**Domain delta:** For Product Discovery, recovery targets solution enthusiasm substitutes for evidence of user need in product evidence ledger and exits only with direct user, workflow, or production evidence that can change the product decision.

### `recovery-product-discovery-reset-and-reconstruct`

Product Discovery: Reset And Reconstruct. Stop mutation Re-read product evidence ledger and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Product Discovery, recovery targets solution enthusiasm substitutes for evidence of user need in product evidence ledger and exits only with direct user, workflow, or production evidence that can change the product decision.

## Decision (2)

### `decision-product-discovery-build-versus-test`

Product Discovery: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Product Discovery, this model decides stop, defer, test, or build using direct user, workflow, or production evidence that can change the product decision and the constraint 'features sharpen a validated product promise instead of merely increasing scope'.

### `decision-product-discovery-local-versus-systemic`

Product Discovery: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening features sharpen a validated product promise instead of merely increasing scope.

**Domain delta:** For Product Discovery, this model decides stop, defer, test, or build using direct user, workflow, or production evidence that can change the product decision and the constraint 'features sharpen a validated product promise instead of merely increasing scope'.

## Mental Model (2)

### `mental-model-product-discovery-feedback-loop`

Product Discovery: Feedback Loop. Actions on the proposed user problem and value hypothesis change product evidence ledger, which changes the next evidence and decision environment.

**Domain delta:** For Product Discovery, this model maps product learning is a feedback loop between hypotheses and observed behavior onto the proposed user problem and value hypothesis and product evidence ledger.

### `mental-model-product-discovery-weakest-link`

Product Discovery: Weakest Link And Bottleneck. End-to-end quality for the proposed user problem and value hypothesis is limited by the least trustworthy boundary in the path through product evidence ledger.

**Domain delta:** For Product Discovery, this model maps product learning is a feedback loop between hypotheses and observed behavior onto the proposed user problem and value hypothesis and product evidence ledger.

## Governance (1)

### `governance-product-discovery-evidence-authority-policy`

Product Discovery: Evidence And Authority Policy. Work on the proposed user problem and value hypothesis must preserve 'features sharpen a validated product promise instead of merely increasing scope', cite direct user, workflow, or production evidence that can change the product decision, and remain within 'problem evidence versus solution preference'.

**Domain delta:** For Product Discovery, this policy enforces the user, problem, or success outcome is invented at problem evidence versus solution preference.

