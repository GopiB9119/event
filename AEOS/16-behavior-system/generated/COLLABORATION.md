# AEOS Collaboration Patterns

## Cross-Team Handoff

Transfer ownership without hidden context or dropped obligations.

### `collaboration-cross-team-handoff-align`

Participants build a shared model of versioned evidence-bearing handoff before committing work.

- **Authority split:** Receiving owner for acceptance owns the decision; Sending owner for source accuracy owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Named receiving owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-cross-team-handoff-challenge`

An independent participant tries to falsify the proposed decision about versioned evidence-bearing handoff.

- **Authority split:** Sending owner for source accuracy may fail the gate with evidence but may not assume Receiving owner for acceptance authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Receiving owner for acceptance before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-cross-team-handoff-handoff`

Work on versioned evidence-bearing handoff transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Named receiving owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Customer Feedback Loop

Convert feedback into evidence without promising unapproved scope.

### `collaboration-customer-feedback-align`

Participants build a shared model of feedback, interpretation, and decision record before committing work.

- **Authority split:** Product owner for priority owns the decision; Engineering owner for feasibility and evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Discovery or delivery owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-customer-feedback-challenge`

An independent participant tries to falsify the proposed decision about feedback, interpretation, and decision record.

- **Authority split:** Engineering owner for feasibility and evidence may fail the gate with evidence but may not assume Product owner for priority authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Product owner for priority before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-customer-feedback-handoff`

Work on feedback, interpretation, and decision record transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Discovery or delivery owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Developer And AI Agent

Keep the human informed and in control while using agent capability.

### `collaboration-developer-ai-align`

Participants build a shared model of mission, plan, changes, and evidence record before committing work.

- **Authority split:** Developer for consequential choices owns the decision; AI agent for evidence-based technical challenge owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Developer or independent verifier.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-developer-ai-challenge`

An independent participant tries to falsify the proposed decision about mission, plan, changes, and evidence record.

- **Authority split:** AI agent for evidence-based technical challenge may fail the gate with evidence but may not assume Developer for consequential choices authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Developer for consequential choices before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-developer-ai-handoff`

Work on mission, plan, changes, and evidence record transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Developer or independent verifier owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Accessibility

Integrate assistive behavior into critical flows.

### `collaboration-engineering-accessibility-align`

Participants build a shared model of accessibility acceptance matrix before committing work.

- **Authority split:** Accessibility owner for gate result owns the decision; UI owner for implementation evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to UI implementer and tester.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-accessibility-challenge`

An independent participant tries to falsify the proposed decision about accessibility acceptance matrix.

- **Authority split:** UI owner for implementation evidence may fail the gate with evidence but may not assume Accessibility owner for gate result authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Accessibility owner for gate result before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-accessibility-handoff`

Work on accessibility acceptance matrix transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; UI implementer and tester owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Architecture

Align local changes with durable boundaries.

### `collaboration-engineering-architecture-align`

Participants build a shared model of architecture decision and implementation boundary before committing work.

- **Authority split:** Architecture owner for durable boundaries owns the decision; Source owner with current code evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Implementation owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-architecture-challenge`

An independent participant tries to falsify the proposed decision about architecture decision and implementation boundary.

- **Authority split:** Source owner with current code evidence may fail the gate with evidence but may not assume Architecture owner for durable boundaries authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Architecture owner for durable boundaries before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-architecture-handoff`

Work on architecture decision and implementation boundary transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Implementation owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Data

Preserve schema, lineage, and transactional authority.

### `collaboration-engineering-data-align`

Participants build a shared model of data contract and migration plan before committing work.

- **Authority split:** Data owner for persisted contract owns the decision; Application owner for caller impact owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Migration implementer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-data-challenge`

An independent participant tries to falsify the proposed decision about data contract and migration plan.

- **Authority split:** Application owner for caller impact may fail the gate with evidence but may not assume Data owner for persisted contract authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Data owner for persisted contract before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-data-handoff`

Work on data contract and migration plan transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Migration implementer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Design

Align interaction intent with feasible truthful states.

### `collaboration-engineering-design-align`

Participants build a shared model of screen flow and state contract before committing work.

- **Authority split:** Product/design owner for experience owns the decision; Engineer for system-state truth owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to UI implementer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-design-challenge`

An independent participant tries to falsify the proposed decision about screen flow and state contract.

- **Authority split:** Engineer for system-state truth may fail the gate with evidence but may not assume Product/design owner for experience authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Product/design owner for experience before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-design-handoff`

Work on screen flow and state contract transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; UI implementer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Finance

Align architecture with budget and cost controls.

### `collaboration-engineering-finance-align`

Participants build a shared model of cost model and approval record before committing work.

- **Authority split:** Budget owner for spend owns the decision; Engineering owner for workload assumptions owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Operations owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-finance-challenge`

An independent participant tries to falsify the proposed decision about cost model and approval record.

- **Authority split:** Engineering owner for workload assumptions may fail the gate with evidence but may not assume Budget owner for spend authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Budget owner for spend before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-finance-handoff`

Work on cost model and approval record transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Operations owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Legal

Map technical controls to qualified obligations.

### `collaboration-engineering-legal-align`

Participants build a shared model of obligation-control matrix before committing work.

- **Authority split:** Qualified legal owner for legal acceptance owns the decision; Engineering owner for control evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Compliance implementer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-legal-challenge`

An independent participant tries to falsify the proposed decision about obligation-control matrix.

- **Authority split:** Engineering owner for control evidence may fail the gate with evidence but may not assume Qualified legal owner for legal acceptance authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Qualified legal owner for legal acceptance before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-legal-handoff`

Work on obligation-control matrix transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Compliance implementer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Operations

Make services deployable, observable, and recoverable.

### `collaboration-engineering-operations-align`

Participants build a shared model of runbook and operational readiness evidence before committing work.

- **Authority split:** Operations owner for production execution owns the decision; Engineering owner for implementation limits owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Release operator.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-operations-challenge`

An independent participant tries to falsify the proposed decision about runbook and operational readiness evidence.

- **Authority split:** Engineering owner for implementation limits may fail the gate with evidence but may not assume Operations owner for production execution authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Operations owner for production execution before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-operations-handoff`

Work on runbook and operational readiness evidence transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Release operator owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Performance

Target measured bottlenecks without weakening correctness.

### `collaboration-engineering-performance-align`

Participants build a shared model of benchmark and optimization decision before committing work.

- **Authority split:** Source owner within measured budgets owns the decision; Performance reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Bounded optimizer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-performance-challenge`

An independent participant tries to falsify the proposed decision about benchmark and optimization decision.

- **Authority split:** Performance reviewer may fail the gate with evidence but may not assume Source owner within measured budgets authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Source owner within measured budgets before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-performance-handoff`

Work on benchmark and optimization decision transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Bounded optimizer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Privacy

Minimize data and map lifecycle controls.

### `collaboration-engineering-privacy-align`

Participants build a shared model of data flow and privacy decision before committing work.

- **Authority split:** Privacy owner for processing acceptance owns the decision; Engineering owner for feasibility evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Data and implementation owners.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-privacy-challenge`

An independent participant tries to falsify the proposed decision about data flow and privacy decision.

- **Authority split:** Engineering owner for feasibility evidence may fail the gate with evidence but may not assume Privacy owner for processing acceptance authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Privacy owner for processing acceptance before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-privacy-handoff`

Work on data flow and privacy decision transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Data and implementation owners owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Quality

Convert requirements into falsifiable acceptance evidence.

### `collaboration-engineering-quality-align`

Participants build a shared model of requirement-to-test matrix before committing work.

- **Authority split:** Independent verifier for gate result owns the decision; Implementer for source facts owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Completion auditor.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-quality-challenge`

An independent participant tries to falsify the proposed decision about requirement-to-test matrix.

- **Authority split:** Implementer for source facts may fail the gate with evidence but may not assume Independent verifier for gate result authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Independent verifier for gate result before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-quality-handoff`

Work on requirement-to-test matrix transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Completion auditor owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Release

Separate artifact readiness from publication authority.

### `collaboration-engineering-release-align`

Participants build a shared model of release candidate evidence packet before committing work.

- **Authority split:** Release owner for channel action owns the decision; Completion auditor owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Approved publisher.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-release-challenge`

An independent participant tries to falsify the proposed decision about release candidate evidence packet.

- **Authority split:** Completion auditor may fail the gate with evidence but may not assume Release owner for channel action authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Release owner for channel action before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-release-handoff`

Work on release candidate evidence packet transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Approved publisher owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Security

Integrate controls without hiding product or operational cost.

### `collaboration-engineering-security-align`

Participants build a shared model of threat model and control plan before committing work.

- **Authority split:** Engineering owner within non-waivable security constraints owns the decision; Security reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Implementer and verifier.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-security-challenge`

An independent participant tries to falsify the proposed decision about threat model and control plan.

- **Authority split:** Security reviewer may fail the gate with evidence but may not assume Engineering owner within non-waivable security constraints authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Engineering owner within non-waivable security constraints before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-security-handoff`

Work on threat model and control plan transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Implementer and verifier owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Engineering And Support

Turn real user failures into reproducible, prioritized work.

### `collaboration-engineering-support-align`

Participants build a shared model of redacted reproduction and impact packet before committing work.

- **Authority split:** Engineering owner for repair priority owns the decision; Support owner for user evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Investigator.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-engineering-support-challenge`

An independent participant tries to falsify the proposed decision about redacted reproduction and impact packet.

- **Authority split:** Support owner for user evidence may fail the gate with evidence but may not assume Engineering owner for repair priority authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Engineering owner for repair priority before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-engineering-support-handoff`

Work on redacted reproduction and impact packet transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Investigator owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Founder And Engineering

Protect product focus while preserving technical integrity.

### `collaboration-founder-engineering-align`

Participants build a shared model of milestone and tradeoff decision before committing work.

- **Authority split:** Founder for product priority owns the decision; Engineering lead for feasibility and risk owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Delivery owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-founder-engineering-challenge`

An independent participant tries to falsify the proposed decision about milestone and tradeoff decision.

- **Authority split:** Engineering lead for feasibility and risk may fail the gate with evidence but may not assume Founder for product priority authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Founder for product priority before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-founder-engineering-handoff`

Work on milestone and tradeoff decision transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Delivery owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Human Approval Gate

Separate AI preparation from irreversible human approval.

### `collaboration-human-ai-approval-align`

Participants build a shared model of approval-ready decision packet before committing work.

- **Authority split:** Accountable human approver owns the decision; Independent reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Authorized executor.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-human-ai-approval-challenge`

An independent participant tries to falsify the proposed decision about approval-ready decision packet.

- **Authority split:** Independent reviewer may fail the gate with evidence but may not assume Accountable human approver authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Accountable human approver before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-human-ai-approval-handoff`

Work on approval-ready decision packet transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Authorized executor owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Incident Command Team

Coordinate containment, evidence, recovery, and communication.

### `collaboration-incident-command-align`

Participants build a shared model of incident timeline and action log before committing work.

- **Authority split:** Incident commander within specialist constraints owns the decision; Safety and security owners owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Recovery and postmortem owners.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-incident-command-challenge`

An independent participant tries to falsify the proposed decision about incident timeline and action log.

- **Authority split:** Safety and security owners may fail the gate with evidence but may not assume Incident commander within specialist constraints authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Incident commander within specialist constraints before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-incident-command-handoff`

Work on incident timeline and action log transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Recovery and postmortem owners owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Manager And Engineer

Align outcome, capacity, ownership, and support.

### `collaboration-manager-engineer-align`

Participants build a shared model of assignment and readiness contract before committing work.

- **Authority split:** Manager for priority and staffing owns the decision; Engineer for technical evidence owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Assigned owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-manager-engineer-challenge`

An independent participant tries to falsify the proposed decision about assignment and readiness contract.

- **Authority split:** Engineer for technical evidence may fail the gate with evidence but may not assume Manager for priority and staffing authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Manager for priority and staffing before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-manager-engineer-handoff`

Work on assignment and readiness contract transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Assigned owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Open Source Contribution

Integrate external changes under repository standards and licensing.

### `collaboration-open-source-contribution-align`

Participants build a shared model of reviewed contribution and provenance before committing work.

- **Authority split:** Maintainer owns the decision; Independent reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Release owner if accepted.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-open-source-contribution-challenge`

An independent participant tries to falsify the proposed decision about reviewed contribution and provenance.

- **Authority split:** Independent reviewer may fail the gate with evidence but may not assume Maintainer authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Maintainer before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-open-source-contribution-handoff`

Work on reviewed contribution and provenance transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Release owner if accepted owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Product And Engineering

Align user outcome with feasible implementation.

### `collaboration-product-engineering-align`

Participants build a shared model of reviewed requirement and architecture packet before committing work.

- **Authority split:** Product owner for outcome; engineering owner for implementation owns the decision; Independent product or architecture reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Bounded implementer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-product-engineering-challenge`

An independent participant tries to falsify the proposed decision about reviewed requirement and architecture packet.

- **Authority split:** Independent product or architecture reviewer may fail the gate with evidence but may not assume Product owner for outcome; engineering owner for implementation authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Product owner for outcome; engineering owner for implementation before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-product-engineering-handoff`

Work on reviewed requirement and architecture packet transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Bounded implementer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Research And Engineering

Translate uncertain findings into bounded reproducible implementation decisions.

### `collaboration-research-engineering-align`

Participants build a shared model of evidence review and experiment contract before committing work.

- **Authority split:** Engineering owner for implementation owns the decision; Researcher for evidence limits owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Prototype owner.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-research-engineering-challenge`

An independent participant tries to falsify the proposed decision about evidence review and experiment contract.

- **Authority split:** Researcher for evidence limits may fail the gate with evidence but may not assume Engineering owner for implementation authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Engineering owner for implementation before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-research-engineering-handoff`

Work on evidence review and experiment contract transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Prototype owner owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Senior And Junior Engineer

Transfer capability while protecting production quality.

### `collaboration-senior-junior-align`

Participants build a shared model of scaffolded implementation and review notes before committing work.

- **Authority split:** Source owner for merge readiness owns the decision; Developing engineer for understanding gaps owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Independent reviewer.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-senior-junior-challenge`

An independent participant tries to falsify the proposed decision about scaffolded implementation and review notes.

- **Authority split:** Developing engineer for understanding gaps may fail the gate with evidence but may not assume Source owner for merge readiness authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Source owner for merge readiness before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-senior-junior-handoff`

Work on scaffolded implementation and review notes transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Independent reviewer owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

## Vendor Integration

Align external capability, contracts, failure modes, and exit strategy.

### `collaboration-vendor-integration-align`

Participants build a shared model of integration and vendor-risk record before committing work.

- **Authority split:** Internal accountable owner owns the decision; Independent security, legal, or architecture reviewer owns evidence-based challenge.
- **Handoff:** Transfer the agreed artifact, evidence labels, unknowns, and next owner to Integration operator.
- **Conflict route:** Classify disagreements as fact, requirement, ownership, risk, or implementation and route each to its owner.

### `collaboration-vendor-integration-challenge`

An independent participant tries to falsify the proposed decision about integration and vendor-risk record.

- **Authority split:** Independent security, legal, or architecture reviewer may fail the gate with evidence but may not assume Internal accountable owner authority.
- **Handoff:** Return findings, falsifiers, and residual risk to Internal accountable owner before implementation.
- **Conflict route:** Run the cheapest shared check for factual disputes; escalate unresolved requirement or risk acceptance.

### `collaboration-vendor-integration-handoff`

Work on integration and vendor-risk record transfers only with enough evidence for the receiver to continue without hidden context.

- **Authority split:** The sender owns accuracy of the handoff; Integration operator owns acceptance or rejection of readiness.
- **Handoff:** Include revision, scope, decisions, mutations, verifier results, blockers, and next action.
- **Conflict route:** Reject incomplete handoffs and return a specific missing-evidence request rather than guessing.

