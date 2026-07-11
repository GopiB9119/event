# Security Policy

Community Ledger handles local event-money records, member contact details, and receipt evidence. Security and privacy reports should not be posted publicly.

## Supported Versions

No permanent public release exists yet. The current `0.2.0-beta` artifact is a debug-signed trusted beta supported on a best-effort basis. After public release, only the latest published release line will receive security fixes unless a release note states otherwise.

## Report Privately

Email **banothgopikrishna19@gmail.com** with the subject:

```text
[Community Ledger Security] Short description
```

Include only what is necessary:

- affected app version and Android version
- affected component or workflow
- redacted reproduction steps
- observed and potential impact
- whether data may have been exposed or changed
- suggested mitigation, if known

Do not send passwords, signing keys, verification codes, unredacted receipt images, phone numbers, email addresses, UPI IDs, or transaction references. If sensitive evidence is necessary, first ask how to share a redacted or safer reproduction.

## Public Issues

Use [GitHub Issues](https://github.com/GopiB9119/event/issues) only for non-sensitive bugs. Do not open a public issue for a suspected vulnerability before private coordination.

## Scope

Useful reports include vulnerabilities involving:

- unauthorized ledger or receipt access
- unintended data exposure through files, logs, backups, links, or screenshots
- event-copy link validation or local ledger replacement
- update-manifest or APK download validation
- unsafe input parsing or ledger mutation
- vulnerable dependencies with a demonstrated project impact

Documented product limitations are not hidden security guarantees: local identity is not authentication, public/private is not access control, devices do not synchronize, and rooted/debuggable devices weaken local protections. A defect that exceeds or contradicts those disclosed limits is still reportable.

## Response And Disclosure

This independent project has no guaranteed response-time or bug-bounty program. Reports will be reviewed as capacity allows. Please allow reasonable time to investigate and prepare a fix before public disclosure. The publisher will not ask for passwords, private keys, verification codes, or payment.

See the [project security playbook](.ai/SECURITY_PLAYBOOK.md) for implemented controls and known limits.