# AI Agent Engineering

Design bounded agents that reason, use tools, collaborate, stop, and report evidence predictably.

- **Domain ID:** `ai-agent-engineering`
- **Boundary:** model reasoning versus deterministic runtime, authority, and human approval
- **Invariant:** agent autonomy remains bounded, observable, reversible, and aligned with the mission
- **Default evidence:** task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates
- **Risk classes:** ai, governance, cost

## Behavior (10)

### `behavior-ai-agent-engineering-choose-falsifier`

AI Agent Engineering: Choose Cheapest Falsifier. Choose the lowest-cost check of agent contract, tool policy, checkpoints, and evaluation suite that could disprove the current hypothesis.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-communicate-uncertainty`

AI Agent Engineering: Communicate Uncertainty. State confidence, missing evidence, failure impact 'agents drift, fabricate success, misuse tools, consume resources, or exceed authority', and the next discriminating check.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-establish-state`

AI Agent Engineering: Establish Current State. Inspect agent contract, tool policy, checkpoints, and evaluation suite and record the current behavior before proposing change.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-identify-owner`

AI Agent Engineering: Identify Owner And Boundary. Name the owner of agent contract, tool policy, checkpoints, and evaluation suite, the boundary 'model reasoning versus deterministic runtime, authority, and human approval', and who may decide or mutate it.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-minimize-change`

AI Agent Engineering: Make The Smallest Useful Change. Change only the owning slice of agent contract, tool policy, checkpoints, and evaluation suite needed to protect 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-protect-invariant`

AI Agent Engineering: Protect The Domain Invariant. Reject an option that can violate 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' without an approved mitigation.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-stop-and-escalate`

AI Agent Engineering: Stop And Escalate. Stop mutation, preserve evidence, and route 'stop the loop, restore the contract, inspect the first failed step, and reroute' to the accountable owner.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-surface-assumptions`

AI Agent Engineering: Surface Dangerous Assumptions. Separate verified facts from assumptions and identify which assumption could violate 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-update-memory`

AI Agent Engineering: Update Durable Knowledge. Update the decision or memory record for agent contract, tool policy, checkpoints, and evaluation suite with provenance and invalidation triggers.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `behavior-ai-agent-engineering-validate-immediately`

AI Agent Engineering: Validate Immediately. Run task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates or the cheapest stronger check before opening another edit slice.

**Domain delta:** For AI Agent Engineering, this behavior operates on agent contract, tool policy, checkpoints, and evaluation suite, uses task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and protects 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

## Failure (6)

### `failure-ai-agent-engineering-boundary-violation`

AI Agent Engineering: Boundary Violation. A local optimization bypasses the domain ownership model for agent goals, capabilities, loops, and lifecycle.

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

### `failure-ai-agent-engineering-evidence-overclaim`

AI Agent Engineering: Evidence Overclaim. Compilation, generated output, or a sampled check is treated as full behavioral proof.

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

### `failure-ai-agent-engineering-premature-action`

AI Agent Engineering: Premature Action. model fluency is treated as control, memory, verification, or governance

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

### `failure-ai-agent-engineering-silent-failure`

AI Agent Engineering: Silent Failure. Errors are swallowed, outputs are not observed, or success predicates are incomplete.

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

### `failure-ai-agent-engineering-stale-context`

AI Agent Engineering: Stale Context. The state of agent contract, tool policy, checkpoints, and evaluation suite changed while routing continued from a stale checkpoint.

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

### `failure-ai-agent-engineering-unbounded-loop`

AI Agent Engineering: Unbounded Repair Loop. Failures do not trigger a reset of model fluency is treated as control, memory, verification, or governance.

**Domain delta:** In AI Agent Engineering, this failure threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' through model fluency is treated as control, memory, verification, or governance.

## Signal (4)

### `signal-ai-agent-engineering-constraint-risk`

AI Agent Engineering: Constraint Or Risk Signal. A current constraint or risk threatens 'agent autonomy remains bounded, observable, reversible, and aligned with the mission' for agent goals, capabilities, loops, and lifecycle.

**Domain delta:** For AI Agent Engineering, this signal observes agent goals, capabilities, loops, and lifecycle through agent contract, tool policy, checkpoints, and evaluation suite while rejecting stale or untrusted substitutes.

### `signal-ai-agent-engineering-explicit-mission`

AI Agent Engineering: Explicit Mission Signal. The current user request explicitly concerns agent goals, capabilities, loops, and lifecycle and states an observable outcome.

**Domain delta:** For AI Agent Engineering, this signal observes agent goals, capabilities, loops, and lifecycle through agent contract, tool policy, checkpoints, and evaluation suite while rejecting stale or untrusted substitutes.

### `signal-ai-agent-engineering-repository-evidence`

AI Agent Engineering: Repository Evidence Signal. Current source or accepted documentation identifies agent contract, tool policy, checkpoints, and evaluation suite as the owning surface for agent goals, capabilities, loops, and lifecycle.

**Domain delta:** For AI Agent Engineering, this signal observes agent goals, capabilities, loops, and lifecycle through agent contract, tool policy, checkpoints, and evaluation suite while rejecting stale or untrusted substitutes.

### `signal-ai-agent-engineering-runtime-failure`

AI Agent Engineering: Runtime Failure Signal. A reproducible observation shows model fluency is treated as control, memory, verification, or governance in agent contract, tool policy, checkpoints, and evaluation suite.

**Domain delta:** For AI Agent Engineering, this signal observes agent goals, capabilities, loops, and lifecycle through agent contract, tool policy, checkpoints, and evaluation suite while rejecting stale or untrusted substitutes.

## Recovery (3)

### `recovery-ai-agent-engineering-escalate-and-contain`

AI Agent Engineering: Escalate And Contain. Stop mutation Contain further effects Preserve redacted evidence Route stop the loop, restore the contract, inspect the first failed step, and reroute to the accountable owner

**Domain delta:** For AI Agent Engineering, recovery targets model fluency is treated as control, memory, verification, or governance in agent contract, tool policy, checkpoints, and evaluation suite and exits only with task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates.

### `recovery-ai-agent-engineering-isolate-and-repair`

AI Agent Engineering: Isolate And Repair. Reduce to the smallest failing path in agent contract, tool policy, checkpoints, and evaluation suite Apply one bounded repair Run task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates Check adjacent invariants

**Domain delta:** For AI Agent Engineering, recovery targets model fluency is treated as control, memory, verification, or governance in agent contract, tool policy, checkpoints, and evaluation suite and exits only with task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates.

### `recovery-ai-agent-engineering-reset-and-reconstruct`

AI Agent Engineering: Reset And Reconstruct. Stop mutation Re-read agent contract, tool policy, checkpoints, and evaluation suite and current evidence Rebuild one falsifiable hypothesis Resume only with a discriminating check

**Domain delta:** For AI Agent Engineering, recovery targets model fluency is treated as control, memory, verification, or governance in agent contract, tool policy, checkpoints, and evaluation suite and exits only with task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates.

## Decision (2)

### `decision-ai-agent-engineering-build-versus-test`

AI Agent Engineering: Build Versus Test. Choose BUILD only when the owner, outcome, boundary, acceptance evidence, and rollback are known; otherwise choose the least costly state that resolves uncertainty.

**Domain delta:** For AI Agent Engineering, this model decides deterministic tool, single agent, specialist, multi-agent, human gate, or no AI using task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates and the constraint 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

### `decision-ai-agent-engineering-local-versus-systemic`

AI Agent Engineering: Local Versus Systemic Intervention. Choose the narrowest intervention that removes the verified cause without duplicating policy or weakening agent autonomy remains bounded, observable, reversible, and aligned with the mission.

**Domain delta:** For AI Agent Engineering, this model decides deterministic tool, single agent, specialist, multi-agent, human gate, or no AI using task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates and the constraint 'agent autonomy remains bounded, observable, reversible, and aligned with the mission'.

## Mental Model (2)

### `mental-model-ai-agent-engineering-feedback-loop`

AI Agent Engineering: Feedback Loop. Actions on agent goals, capabilities, loops, and lifecycle change agent contract, tool policy, checkpoints, and evaluation suite, which changes the next evidence and decision environment.

**Domain delta:** For AI Agent Engineering, this model maps reliable agency emerges from constrained feedback loops around an uncertain model onto agent goals, capabilities, loops, and lifecycle and agent contract, tool policy, checkpoints, and evaluation suite.

### `mental-model-ai-agent-engineering-weakest-link`

AI Agent Engineering: Weakest Link And Bottleneck. End-to-end quality for agent goals, capabilities, loops, and lifecycle is limited by the least trustworthy boundary in the path through agent contract, tool policy, checkpoints, and evaluation suite.

**Domain delta:** For AI Agent Engineering, this model maps reliable agency emerges from constrained feedback loops around an uncertain model onto agent goals, capabilities, loops, and lifecycle and agent contract, tool policy, checkpoints, and evaluation suite.

## Governance (1)

### `governance-ai-agent-engineering-evidence-authority-policy`

AI Agent Engineering: Evidence And Authority Policy. Work on agent goals, capabilities, loops, and lifecycle must preserve 'agent autonomy remains bounded, observable, reversible, and aligned with the mission', cite task traces, tool outcomes, stop-state tests, adversarial fixtures, and human correction rates, and remain within 'model reasoning versus deterministic runtime, authority, and human approval'.

**Domain delta:** For AI Agent Engineering, this policy enforces agent prompts grant authority or bypass deterministic enforcement at model reasoning versus deterministic runtime, authority, and human approval.

