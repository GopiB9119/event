# Public Launch Checklist

## Product Truth

- [x] App describes itself as a local event finance ledger.
- [x] App states that it does not collect, hold, transfer, or invest money.
- [x] Event links are described as independent local copies, not synchronized membership.
- [x] Public/private is described as a marker, not access control.
- [ ] Decide whether synchronized shared events are a later product milestone.

## Trust And Legal

- [x] Original launcher mark replaces the Android Studio template.
- [x] In-app About, Privacy, and Terms are reachable after onboarding.
- [x] Privacy policy, beta terms, and data/permission inventory exist in the repository.
- [x] Manual update checks validate release metadata and allow only official GitHub Release APK assets.
- [x] Add a monitored private support email and final legal publisher identity.
- [x] Prepare a security and data-integrity incident-response runbook.
- [ ] Have final public legal text reviewed for target launch jurisdictions.
- [ ] Choose and document the repository's source-code license before accepting external code contributions.

## GitHub Repository

- [x] Prepare Security, Support, Conduct, Contributing, Changelog, issue forms, PR template, CODEOWNERS, and Dependabot configuration locally.
- [x] Add reusable static-site validation and non-deploying Site CI.
- [x] Push governance/workflow files and record Community Profile improvement (14% to 85%).
- [x] Stage publication selectively; exclude machine `.idea` state and confirm every real private receipt fixture remains ignored.
- [x] Confirm corrected remote `.env.example`, metadata, manifest, README, and version truth surfaces after push.
- [x] Record the first green hosted Android CI and Site CI runs.
- [x] Configure the active default-branch ruleset with strict `Compile, test, and assemble` status checks.
- [x] Enable vulnerability alerts/automated security fixes and disable the unused Wiki.
- [ ] Create the Community Ledger Delivery Project using [repository operations](../GitHub/REPOSITORY_OPERATIONS.md).

## Distribution

- [x] Create and securely back up the Android release keystore.
- [x] Increase version code for every public update.
- [x] Build and verify a release-signed APK.
- [x] Create a GitHub prerelease with APK, checksum, certificate fingerprint, notes, and limitations.
- [x] Enable and manually deploy the static GitHub Pages site.
- [x] Point the website's Download APK button and update manifest to the signed release.

## Website

- [x] Validate desktop and mobile layouts with browser screenshots and measured overflow checks.
- [x] Publish privacy, terms, data handling, and support pages.
- [x] Use real privacy-safe app screenshots; no receipt PII or fabricated user claims.
- [x] Verify all local links, scripts, JSON, release gating, and event-copy fallback before deployment.
- [x] Verify the deployed links, download, checksum, and event-copy fallback over HTTPS.

## App Verification

- [x] JVM/Robolectric suite passes.
- [x] API 36 instrumentation suite passes with private real-receipt OCR fixtures.
- [x] Process-death interruption leaves the ledger unchanged and displays recovery guidance.
- [ ] Run create/save/restart/share/cancel on a physical phone.
- [ ] Repeat the final smoke flow on a second physical device.
- [ ] Complete and retain the redacted [Physical Device Launch Matrix](../Testing/PHYSICAL_DEVICE_LAUNCH_MATRIX.md).
- [x] Record the first green hosted Android CI run.

## Play Store Later

- [ ] Build an Android App Bundle with Play App Signing.
- [x] Supply a public privacy-policy URL.
- [ ] Complete Data safety, Ads, App access, Target audience, Content rating, and Financial features declarations truthfully.
- [ ] Prepare store icon, feature graphic, phone screenshots, short description, and full description.
- [ ] Complete closed testing and current Google Play account/testing requirements before production rollout.
