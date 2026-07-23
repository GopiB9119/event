# Tool Orchestration

Choose, parameterize, sequence, observe, and stop tools safely according to their real capabilities.

- **Domain ID:** `tool-orchestration`
- **Boundary:** model intent versus tool capability and external side effects
- **Invariant:** tools operate only within declared scope and every result is inspected before the next mutation
- **Default evidence:** tool definitions, validated parameters, exit states, outputs, and side-effect checks
- **Risk classes:** tools, safety, security

## Behavior (10)

### `behavior-tool-orchestration-choose-falsifier`

Tool Orchestration: Choose Cheapest Falsifier. Choose the lowest-cost check of tool plan, parameter contract, and observed outputs that could disprove the current hypothesis.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-communicate-uncertainty`

Tool Orchestration: Communicate Uncertainty. State confidence, missing evidence, failure impact 'wrong parameters, hidden prompts, destructive actions, leaked secrets, or false success', and the next discriminating check.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-establish-state`

Tool Orchestration: Establish Current State. Inspect tool plan, parameter contract, and observed outputs and record the current behavior before proposing change.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-identify-owner`

Tool Orchestration: Identify Owner And Boundary. Name the owner of tool plan, parameter contract, and observed outputs, the boundary 'model intent versus tool capability and external side effects', and who may decide or mutate it.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-minimize-change`

Tool Orchestration: Make The Smallest Useful Change. Change only the owning slice of tool plan, parameter contract, and observed outputs needed to protect 'tools operate only within declared scope and every result is inspected before the next mutation'.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-protect-invariant`

Tool Orchestration: Protect The Domain Invariant. Reject an option that can violate 'tools operate only within declared scope and every result is inspected before the next mutation' without an approved mitigation.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-stop-and-escalate`

Tool Orchestration: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop the chain, inspect tool contracts and current state, then rerun one bounded action' to the accountable owner.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-surface-assumptions`

Tool Orchestration: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'tools operate only within declared scope and every result is inspected before the next mutation'.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-update-memory`

Tool Orchestration: Update Durable Knowledge. Update the decision or memory record for tool plan, parameter contract, and observed outputs with provenance and invalidation triggers.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `behavior-tool-orchestration-validate-immediately`

Tool Orchestration: Validate Immediately. Run tool definitions, validated parameters, exit states, outputs, and side-effect checks or the cheapest stronger check before opening another edit slice.

**Domain delta:** For Tool Orchestration, this behavior operates on tool plan, parameter contract, and observed outputs, uses tool definitions, validated parameters, exit states, outputs, and side-effect checks, and protects 'tools operate only within declared scope and every result is inspected before the next mutation'.

## Failure (6)

### `failure-tool-orchestration-boundary-violation`

Tool Orchestration: Boundary Violation. A local optimization bypasses the domain ownership model for tool selection and execution workflow.

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

### `failure-tool-orchestration-evidence-overclaim`

Tool Orchestration: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

### `failure-tool-orchestration-premature-action`

Tool Orchestration: Premature Action. the model invents tool behavior or chains actions without observing intermediate results

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

### `failure-tool-orchestration-silent-failure`

Tool Orchestration: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

### `failure-tool-orchestration-stale-context`

Tool Orchestration: Stale Context. The state of tool plan, parameter contract, and observed outputs changed while routing continued from a stale checkpoint.

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

### `failure-tool-orchestration-unbounded-loop`

Tool Orchestration: Unbounded Repair Loop. Failures do not trigger a reset of the model invents tool behavior or chains actions without observing intermediate results.

**Domain delta:** In Tool Orchestration, this failure threatens 'tools operate only within declared scope and every result is inspected before the next mutation' through the model invents tool behavior or chains actions without observing intermediate results.

## Signal (4)

### `signal-tool-orchestration-constraint-risk`

Tool Orchestration: Constraint Or Risk Signal. A current constraint or risk threatens 'tools operate only within declared scope and every result is inspected before the next mutation' for tool selection and execution workflow.

**Domain delta:** For Tool Orchestration, this signal observes tool selection and execution workflow through tool plan, parameter contract, and observed outputs while rejecting stale or untrusted substitutes.

### `signal-tool-orchestration-explicit-mission`

Tool Orchestration: Explicit Mission Signal. The current user request explicitly concerns tool selection and execution workflow and states an observable outcome.

**Domain delta:** For Tool Orchestration, this signal observes tool selection and execution workflow through tool plan, parameter contract, and observed outputs while rejecting stale or untrusted substitutes.

### `signal-tool-orchestration-repository-evidence`

Tool Orchestration: Repository Evidence Signal. Current source or accepted documentation identifies tool plan, parameter contract, and observed outputs as the owning surface for tool selection and execution workflow.

**Domain delta:** For Tool Orchestration, this signal observes tool selection and execution workflow through tool plan, parameter contract, and observed outputs while rejecting stale or untrusted substitutes.

### `signal-tool-orchestration-runtime-failure`

Tool Orchestration: Runtime Failure Signal. A reproducible observation shows the model invents tool behavior or chains actions without observing intermediate results in tool plan, parameter contract, and observed outputs.

**Domain delta:** For Tool Orchestration, this signal observes tool selection and execution workflow through tool plan, parameter contract, and observed outputs while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-tool-orchestration-escalate-and-contain`

Tool Orchestration: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop the chain, inspect tool contracts and current state, then rerun one bounded action to the accountable owner

**Domain delta:** For Tool Orchestration, recovery targets the model invents tool behavior or chains actions without observing intermediate results in tool plan, parameter contract, and observed outputs and exits only with tool definitions, validated parameters, exit states, outputs, and side-effect checks.

### `recovery-tool-orchestration-isolate-and-repair`

Tool Orchestration: Isolate And Repair. Reduce to the smallest failing path in tool plan, parameter contract, and observed outputs Apply one bounded repair Run tool definitions, validated parameters, exit states, outputs, and side-effect checks Check adjacent invariants

**Domain delta:** For Tool Orchestration, recovery targets the model invents tool behavior or chains actions without observing intermediate results in tool plan, parameter contract, and observed outputs and exits only with tool definitions, validated parameters, exit states, outputs, and side-effect checks.

### `recovery-tool-orchestration-reset-and-reconstruct`

Tool Orchestration: Reset And Reconstruct. Stop mutation Re-read tool plan, parameter contract, and observed outputs and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For Tool Orchestration, recovery targets the model invents tool behavior or chains actions without observing intermediate results in tool plan, parameter contract, and observed outputs and exits only with tool definitions, validated parameters, exit states, outputs, and side-effect checks.

## Decision (2)

### `decision-tool-orchestration-build-versus-test`

Tool Orchestration: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For Tool Orchestration, this model decides read tool, edit tool, terminal, browser, subagent, ask human, or no tool using tool definitions, validated parameters, exit states, outputs, and side-effect checks and the constraint 'tools operate only within declared scope and every result is inspected before the next mutation'.

### `decision-tool-orchestration-local-versus-systemic`

Tool Orchestration: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening tools operate only within declared scope and every result is inspected before the next mutation.

**Domain delta:** For Tool Orchestration, this model decides read tool, edit tool, terminal, browser, subagent, ask human, or no tool using tool definitions, validated parameters, exit states, outputs, and side-effect checks and the constraint 'tools operate only within declared scope and every result is inspected before the next mutation'.

## Mental Model (2)

### `mental-model-tool-orchestration-feedback-loop`

Tool Orchestration: Feedback Loop. Actions on tool selection and execution workflow change tool plan, parameter contract, and observed outputs, which changes the next evidence and decision environment.

**Domain delta:** For Tool Orchestration, this model maps tool reliability depends on explicit contracts, observability, sequencing, and side-effect control onto tool selection and execution workflow and tool plan, parameter contract, and observed outputs.

### `mental-model-tool-orchestration-weakest-link`

Tool Orchestration: Weakest Link And Bottleneck. End-to-end quality for tool selection and execution workflow is limited by the least trustworthy boundary in the path through tool plan, parameter contract, and observed outputs.

**Domain delta:** For Tool Orchestration, this model maps tool reliability depends on explicit contracts, observability, sequencing, and side-effect control onto tool selection and execution workflow and tool plan, parameter contract, and observed outputs.

## Governance (1)

### `governance-tool-orchestration-evidence-authority-policy`

Tool Orchestration: Evidence And Authority Policy. Work on tool selection and execution workflow must preserve 'tools operate only within declared scope and every result is inspected before the next mutation', cite tool definitions, validated parameters, exit states, outputs, and side-effect checks, and remain within 'model intent versus tool capability and external side effects'.

**Domain delta:** For Tool Orchestration, this policy enforces a tool performs destructive, external, privileged, or sensitive action without approval at model intent versus tool capability and external side effects.

