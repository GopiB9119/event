# Contributing To Community Ledger

Thank you for helping improve Community Ledger. This project protects local event-money records, so correctness, privacy, and evidence matter more than change volume.

## Contribution Status And License

The repository is public, but no open-source license has been granted yet. Public visibility does not grant permission to copy, redistribute, or create derivative versions.

Until the publisher chooses explicit license and contribution terms, external code contributions are **not accepted for merging**. You may still open non-sensitive bug reports, propose ideas, or suggest documentation corrections. Open an issue before preparing substantial work.

## Before Opening An Issue

- Read [README.md](README.md), [SUPPORT.md](SUPPORT.md), and [SECURITY.md](SECURITY.md).
- Search existing issues.
- Remove all receipt/member/payment personal data.
- For a feature, describe the observed problem and current workaround before proposing a solution.
- Do not treat an issue as approval to build; product decisions use `STOP / DEFER / TEST / BUILD` evidence gates.

## Development Environment

- JDK 17
- Android SDK 36
- Android build-tools 36.1.0
- Windows PowerShell commands below; use `./gradlew` on Linux/macOS

Required local validation:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDebugKotlin
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDebugUnitTest
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDebug
```

High-risk Room/share/OCR work also requires the applicable Android instrumentation tests and real private receipt fixtures. Never commit real receipt images or personal data.

## Change Standard

- One problem per change.
- Read the owning code path and project instructions first.
- Preserve architecture and public behavior unless the decision record explicitly changes them.
- Do not add dummy financial data, cloud/Gemini OCR, filename extraction, or unsupported security claims.
- Add or update focused tests when behavior changes.
- Update documentation and project memory when verified reality changes.
- State residual risk and checks that were not run.

## Pull Requests

Pull Requests are currently for publisher/authorized collaborator work while licensing is unresolved. A PR must link its issue/decision, explain scope and non-goals, include validation evidence, and disclose security/privacy/data impact.

By participating, you agree to follow [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md). Do not place secrets or personal financial data in commits, branches, CI logs, Issues, or Pull Requests.