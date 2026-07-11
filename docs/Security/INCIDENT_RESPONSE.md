# Security And Data-Integrity Incident Response

Use this runbook after a suspected vulnerability, incorrect ledger mutation, exposed receipt/member data, compromised release asset, or signing-key concern. It does not authorize destructive actions or public disclosure without owner approval.

## Principles

- Protect people and ledger evidence before protecting reputation.
- Preserve facts; do not invent scope, affected users, or root cause.
- Minimize collection of receipt/member/payment data during investigation.
- Separate containment from permanent repair.
- Never recommend uninstalling without warning that uninstall deletes the local ledger.
- Existing installed APKs cannot be remotely disabled or silently updated.

## Intake

Private reports go to `banothgopikrishna19@gmail.com` under [SECURITY.md](../../SECURITY.md). Record:

- reporter contact preference
- affected version/device/path
- exact observed behavior and time
- potential confidentiality, integrity, or availability impact
- whether public exploitation or data exposure is suspected
- redacted reproduction evidence

Do not copy sensitive evidence into public Issues, project boards, CI logs, or chat transcripts.

## Triage

Classify the dominant risk without fake precision:

| Severity | Examples | Default posture |
|---|---|---|
| Critical | signing-key compromise, public malicious update path, repeatable unauthorized ledger replacement, widespread sensitive-data exposure | Stop publication/download promotion; owner-led private investigation and fix validation |
| High | silent incorrect totals, cross-event data mutation, receipt/member exposure outside intended app storage | Block affected release path; prepare minimal repair and focused regression |
| Medium | bounded validation bypass, denial of service, sensitive logging requiring local/device access | Fix before next release unless evidence raises impact |
| Low | hardening opportunity with no demonstrated exploit or data impact | Track with evidence and review trigger |

Severity may change as evidence changes. Record `VERIFIED`, `SUPPORTED`, `ASSUMPTION`, and `UNKNOWN` claims separately.

## Containment

Choose the smallest reversible control that reduces harm:

- keep `site/releases/latest.json` at `available=false` or point it only to a verified safe release
- unpublish/mark a GitHub Release as draft when its asset is unsafe, with owner approval
- pause Pages deployment when public copy or download metadata is wrong
- disable a compromised workflow/secret in GitHub settings
- revoke exposed tokens immediately; never paste replacements into Issues or commits
- preserve affected artifacts, hashes, logs, and commit IDs privately

Removing a download does not remove installed copies. The app has no force-update or remote kill switch.

## Investigation

Map the complete path:

```text
trigger/input
  -> UI/intent/network boundary
  -> validation/state
  -> Room/files/preferences
  -> ledger/user-visible effect
  -> release/update/distribution effect
```

Identify root cause, affected versions, required preconditions, data touched, and why existing tests/gates did not detect it. Use synthetic or redacted fixtures whenever possible.

## Repair And Verification

1. Create a private or minimally disclosed tracking item.
2. Implement the smallest root-cause repair.
3. Add a regression that fails before and passes after the repair.
4. Run compile, full unit tests, applicable Android instrumentation, physical-device flow, and artifact verification.
5. Verify migrations, restart, offline/permission/time boundaries, rollback limits, and update behavior as applicable.
6. Independently review security/privacy claims and release evidence.
7. Update Security, Privacy, Changelog, release notes, and project memory when facts changed.

## Release And Communication

For a corrected public APK:

- use the same uncompromised permanent signing lineage
- increment version code
- publish exact SHA-256 and signing certificate fingerprint
- explain affected versions, user action, known limits, and whether local data is at risk
- update the release manifest only after the final asset URL/hash are verified
- verify in-app manual update detection and HTTPS Pages routes

Communication template:

```text
Issue:
Affected versions:
Who may be affected:
What is VERIFIED / UNKNOWN:
User action:
Data-loss warning:
Fixed version and verification:
Remaining risk:
Support contact:
```

Do not claim no exploitation occurred unless evidence supports it.

## Signing-Key Concern

If the permanent signing key may be exposed:

- stop release publication and preserve evidence
- do not generate or announce a replacement casually
- assess whether existing users can receive a valid update under Android's signing rules
- obtain platform/security advice before choosing rotation, new application identity, or migration
- communicate update/install consequences honestly

The keystore and passwords must never enter the repository, support email attachments, or agent/chat context.

## Post-Incident Review

After containment and release:

- document timeline from evidence
- record root cause and contributing conditions
- identify detection/support signals that were absent
- update tests, runbooks, decision records, and release gates
- assign owners/review triggers without invented deadlines
- remove temporary containment only after verification

Use [AEOS Postmortem Template](../../AEOS/12-templates/POSTMORTEM_TEMPLATE.md). Public disclosure must exclude sensitive exploit and user data until it is safe.