# Google Play Launch Plan

Last verified: 14 July 2026

## Decision And Current Status

Decision: `BUILD` Play internal/closed-testing preparation. `DEFER` production rollout until the account path, signing continuity, Play-specific update behavior, physical-device evidence, store declarations, and legal scope are confirmed.

Community Ledger is not ready for a Play upload yet.

Already ready:

- application ID and namespace `com.communityledger.app`
- target and compile SDK 36
- minimum SDK 24
- public Privacy, Terms, Contact, and support URLs
- historical old-package 4096-bit RSA signing lineage with certificate SHA-256 `BC1415F8C2236009109CBDA483F351AB9F2C379B7E9A7661599D369E2FACA3CF`
- published beta evidence: 39 JVM tests, 8 Android instrumentation tests, release lint, signed APK install/relaunch, and hosted CI
- current unreleased source evidence: 169 JVM/Robolectric tests across 24 suites pass for each distribution flavor with zero failures, errors, or skips; the current 17-test Android instrumentation source compiles for both flavors, while its clean runtime pass remains pending
- compact receipt review with a read-only receipt-derived amount and fail-closed weak/manual amount policy
- implemented `direct` and `play` distribution flavors; the Play variant hides the GitHub APK checker and removes `android.permission.INTERNET`
- three privacy-safe 1080 x 2400 source captures for store exports
- no ads, account backend, cloud database, or restricted Android permissions; ML Kit diagnostics/usage telemetry is disclosed separately below

Still required:

- decide account ownership and confirm the new app's Production-access screen
- approve whether the new package is Play-only, uses a compatible cross-channel app-signing lineage, or uses separate package IDs
- finalize Play App Signing and upload-key custody for the approved new-package strategy
- create a separate Play upload key
- choose and increment the first Play-candidate version code before upload
- build, verify, and upload a signed Android App Bundle
- create a 512 x 512 Play icon and 1024 x 500 feature graphic
- create Play-compatible screenshot exports with a maximum 2:1 aspect ratio
- complete App content, Data safety, rating, audience, and financial-feature declarations
- complete the applicable testing track and production-access gate
- complete physical-device testing and qualified legal review before broad production rollout

### Product-Scope Gate

A reported beta expectation is that joined events share live members, receipt submissions, contributor counts, collected/spent/balance totals, utilization, event details, and custom information. The current app does not do that.

Provisional scope based on the reported requirement: **defer Play production until the shared-ledger acceptance contract passes**. Internal testing of the local collision fix may continue, but the production listing must not imply collaboration while joined events remain independent.

The bounded backend proposal is documented in the [Shared Ledger Implementation Plan](../Architecture/SHARED_LEDGER_IMPLEMENTATION_PLAN.md). It does not create cloud resources; backend ownership, billing, permanent region, retention, and incident-response ownership remain approval gates.

The collision-safe opaque event key is necessary for copies and future migration, but it is not synchronization, authentication, or authorization. A backend-enabled build would add account/member/financial collection beyond the ML Kit diagnostics already disclosed below and requires a complete new Data safety/privacy review.

## 1. Decide Who Owns The Play App

Using your brother's existing Play Console account means:

- his developer account is the publisher and policy owner of the app
- the developer name and contact details configured on that account appear on Google Play
- account suspensions, verification, tax/payment profile decisions, and policy history affect Community Ledger
- he controls user permissions, Play App Signing enrollment, releases, app transfer, and final publication

Do not share his Google password, recovery codes, or two-factor codes.

Preferred access model:

1. Your brother opens **Play Console -> Users and permissions**.
2. He invites your own Google account.
3. Grant only the permissions needed for Community Ledger: create/manage the app, store presence, app content, testing releases, production releases, and view app quality.
4. Keep account administration, payments, and unrelated apps restricted unless genuinely needed.
5. Use two-step verification on both accounts.

If you want **Gopi Banoth** rather than your brother/account organization to be the long-term developer of record, use your own verified developer account instead. App transfer may be possible later but has eligibility and operational requirements; do not treat transfer as guaranteed cleanup.

### Ask Your Brother Before Creating The App

Record these answers:

- Is the account **Personal** or **Organization**?
- Was it created before or after **13 November 2023**?
- What developer name and public contact details appear on its Store settings page?
- Is the account in good standing with identity/device verification complete?
- When a new app is created, is **Production** available, or does the Dashboard show **Apply for production**?

The official testing rule captured on 12 July 2026 applies to personal accounts created after 13 November 2023: a closed test needs at least 12 opted-in testers for 14 continuous days before applying for production access. Because your brother already has a production app, his account may already have production access. The new Community Ledger app's Dashboard is the source of truth; do not assume exemption.

## 2. Package And Signing Boundary

The approved Play identity is documented in [Play Store Identity Migration](PLAY_STORE_IDENTITY_MIGRATION.md):

```text
App name: Community Ledger
Package: com.communityledger.app
```

This is a new Android identity. It cannot update `com.aistudio.communityledger.yrtqwx` or inherit that app's private data, regardless of signing certificate. Keep the old beta installed while its data is needed.

1. Check `com.communityledger.app` availability before the first artifact upload.
2. Enroll the new package in Play App Signing.
3. Use a separate upload key and back it up independently.
4. Do not publicly distribute the new-package APK before the signing/channel decision is final.
5. Preserve the old signing key separately for the historical old-package beta.

Google's current guidance recommends separate upload and app-signing keys. The old certificate remains historical evidence, not an update bridge between different package names.

The new `direct` and `play` flavors currently have the same application ID. A Play upload key only authenticates bundle uploads; Play-delivered APKs use the app-signing key. If Play creates an app-signing key that differs from the direct APK lineage, Android cannot update or switch those same-package installations in place. Before the first public new-package artifact, the owner must approve one of the strategies in [Signing And Distribution](SIGNING_AND_DISTRIBUTION.md): Play-only distribution, a supported compatible cross-channel app-signing lineage, or separate package identities. No strategy is selected by this document.

## 3. Prepare A Play-Specific Build

The distribution split is implemented:

- Play build: updates are distributed by Google Play; no GitHub APK check or download action
- Direct build: keeps the existing user-triggered GitHub release manifest
- Play build: removes its own `INTERNET` permission
- external Privacy, Terms, support, or Play listing links should open through a user-initiated browser/Play action
- package ID remains `com.communityledger.app` across every new-package channel

Current Gradle shape:

```text
productFlavors
  direct -> GitHub manifest update channel
  play   -> Google Play update channel
```

Release version for the first Play upload is selected only when the candidate is ready. Increment above the current source value to keep candidate history unambiguous:

```text
versionCode: 5 or higher
versionName: next approved Play test version
```

Every later uploaded bundle requires a higher version code. Promote the same tested bundle from closed testing to production when possible instead of rebuilding it.

## 4. Build And Verify The Android App Bundle

After the Play variant and upload key exist:

```powershell
$env:KEYSTORE_PATH = 'C:\secure\community-ledger-play-upload.jks'
$env:STORE_PASSWORD = '<enter only in the secure terminal environment>'
$env:KEY_PASSWORD = '<enter only in the secure terminal environment>'
.\gradlew.bat --no-daemon --no-configuration-cache :app:bundlePlayRelease
```

Do not put passwords in Gradle files, shell history, repository files, or chat.

Verify before upload:

- AAB exists and is signed by the registered upload certificate
- package ID is exactly `com.communityledger.app`
- version code is greater than the prior upload for this package
- target SDK is accepted by the current Play Console policy warning
- release lint, unit tests, and applicable device tests pass
- Bundle Explorer/pre-launch report shows no unsupported devices or critical issues
- install a Play-generated APK set from internal testing or Bundle Explorer and verify a clean install plus side-by-side behavior with the old-package beta

Play App Signing is mandatory for new Play apps. Play generates device APKs from the AAB and signs those APKs with the app-signing key.

## 5. Create The App In Your Brother's Play Console

In **All apps -> Create app**:

- App name: `Community Ledger`
- Default language: English (India) or the language you will maintain first
- App or game: App
- Free or paid: Free
- Declare that the app follows Play policies and export requirements truthfully

Important:

- the package becomes fixed when the first AAB is uploaded
- do not create a second package for the same product
- free is appropriate for the current no-monetization build; check current Console rules before choosing if a paid download is a future requirement

## 6. Store Listing Copy

### App Name

```text
Community Ledger
```

16 characters; official limit is 30.

### Short Description

```text
Local event ledger with reviewed on-device receipt OCR and clear totals.
```

72 characters; official limit is 80.

### Full Description

```text
Community Ledger helps event organizers record money in, money out, members, and reviewed payment receipt evidence on one Android device.

Core features
- Create local event ledgers
- Record donations, credits, expenses, and debits
- Read shared or selected receipt images with on-device ML Kit OCR
- Review the read-only amount, payment mode, from, to, date, and reference before saving
- Block likely duplicate or low-confidence receipts
- Link transactions to persisted members
- View collected, spent, and available balance totals
- Keep reviewed receipt JSON in app-private storage

Important beta limits
- Local-only: no account, cloud sync, or shared live ledger
- Event-copy links create independent local copies and do not verify organizers
- Public/private is a local marker, not access control
- No export, restore, or recovery after uninstall or device loss
- OCR can be wrong; review every value before saving
- Community Ledger records evidence and does not collect, hold, transfer, or invest money

Keep an independent record for important or irreplaceable money data.
```

Current copy is about 1,100 characters and avoids ranking, awards, price claims, testimonials, and regulatory claims.

### Category And Contact

- Category: Finance is the closest fit; describe the app as local record-keeping, not payments or investment
- Support email: `banothgopikrishna19@gmail.com`
- Website: `https://gopib9119.github.io/event/`
- Privacy policy: `https://gopib9119.github.io/event/privacy/`

## 7. Store Graphics

Required official assets:

- Play icon: 512 x 512, 32-bit PNG with alpha, maximum 1024 KB
- Feature graphic: 1024 x 500, JPEG or 24-bit PNG without alpha
- Screenshots: at least 2; JPEG or 24-bit PNG, each dimension 320-3840 px, longest side no more than twice the shortest

Current status:

- logo source exists as `site/assets/community-ledger-icon.svg` with a 512 x 512 viewBox
- dedicated Play PNG icon is not created
- feature graphic is not created
- three privacy-safe 1080 x 2400 source captures exist, but their 20:9 aspect ratio exceeds Play's stated 2:1 maximum and cannot be uploaded unchanged

Export the existing captures without distorting the UI as 1080 x 2160 crops or 1200 x 2400 framed images, and visually verify that no UI is cut off or misleading. Before production, add a fourth matching screenshot showing receipt review with synthetic data. Four high-resolution portrait screenshots improve eligibility for recommendation surfaces.

Suggested screenshot order:

1. Dashboard and local-only ledger summary
2. Create Event
3. Reviewed receipt/attribution with synthetic data
4. Trust Center and privacy limits

Do not upload screenshots containing real receipt details, member data, phone numbers, emails, UPI IDs, or unsupported claims.

## 8. App Content And Policy Answers

Answer based on the Play build actually uploaded, not this document alone.

### Ads

```text
Contains ads: No
```

There is no advertising SDK.

### App Access

```text
All functionality is available without an account or login.
```

Explain that the first screen is a local-data limitation acknowledgement. The local email label is not authentication.

### Target Audience

Choose adults only, preferably 18 and over. The app handles event money records and member/payment identifiers and is not designed for children.

### Content Rating

Complete the IARC questionnaire truthfully. The app has no violence, sexual content, gambling, public social interaction, or purchases. User-entered local ledger text is not publicly distributed user-generated content.

### Financial Features Declaration

Select no regulated financial product or transaction feature. Community Ledger:

- does not transfer money
- does not provide banking, lending, investing, insurance, wallets, cryptocurrency, payments, or financial advice
- only records local evidence and totals

The Finance store category does not change that factual declaration.

### Account Deletion

The app does not create accounts. State that users delete local records in the app, clear app data, or uninstall. There is no server-side account or retained cloud ledger.

### Permissions

The packaged direct APK declares `android.permission.INTERNET` for its manual update request and a transitive `android.permission.ACCESS_NETWORK_STATE` from Google Data Transport. The packaged Play APK removes `INTERNET` and the GitHub checker but retains the normal network-state permission from that ML Kit dependency; network-state access alone does not authorize network traffic. The final AAB and its SDK behavior must still be reviewed against ML Kit's current disclosure before completing Data safety. No camera, location, contacts, microphone, SMS, call log, broad storage, or package-install permission declaration is needed.

## 9. Data Safety

Google defines collection as transmitting data off the device. Data accessed only on-device does not need to be declared as collection, and user-initiated sharing can qualify for an exclusion. However, the official ML Kit Android disclosure checked on 12 July 2026 says all ML Kit features collect SDK metadata for diagnostics and usage analytics.

Conservative Play-build answer:

```text
Does the app collect or share required user data types? Yes, data is collected.
Is the documented ML Kit data shared with third parties? No, according to Google.
```

Map the current Play form against Google's current data-type guide. At minimum, review:

- **Device or other IDs:** bundled per-installation identifiers not intended to identify a user or physical device
- **App info and performance / diagnostics:** device and app information, performance metrics, API configuration, input/output sizes, feature versions, event types, and error codes
- **Purpose:** diagnostics and usage analytics
- **Security:** encrypted in transit with HTTPS
- **Required/optional:** treat as required unless the final SDK/build provides and verifies an effective user choice
- **Ephemeral processing/deletion:** do not claim either without current Google evidence

Events, members, transactions, local identity, receipt image contents, recognized text, and receipt JSON remain on-device in the current architecture. Google's all-features list names image format/resolution and input/output size, not receipt image contents or recognized text, for bundled text recognition. Keep those distinctions explicit.

Ordinary GitHub request metadata applies only to the direct build; the current Play flavor does not contact the GitHub update manifest. A future shared-ledger backend would add account, member, receipt, and financial-record collection and requires a complete new Data safety review.

Official ML Kit source: https://developers.google.com/ml-kit/android-data-disclosure (last updated 28 January 2026; retrieved 12 July 2026).

Keep the Data safety form synchronized with:

- `docs/Legal/DATA_AND_PERMISSIONS.md`
- `docs/Legal/PRIVACY_POLICY.md`
- the final Play AAB's SDK and permission inventory

## 10. Testing Track Path

Always start with **Internal testing**, even if Production is already enabled:

1. Upload an AAB whose version code is higher than every prior upload for `com.communityledger.app`.
2. Resolve all Play pre-review and Bundle Explorer errors.
3. Add trusted testers.
4. Install through the Play opt-in link.
5. Test a clean install of `com.communityledger.app` and side-by-side installation with the old beta.
6. Verify the old beta remains untouched; do not claim its private data migrates.
7. Test receipt picker/share, read-only OCR amount review, duplicate block, Money in/out, deletion acknowledgement, restart, and Play-delivered updates.
8. Review the automated pre-launch report and Android vitals.

Then inspect the app Dashboard:

### Path A: Production Is Available

Your brother's account already has production access for this app/account. Run a meaningful closed test anyway, resolve feedback, complete every App content form, then promote the tested release to production.

### Path B: Dashboard Shows Apply For Production

Run the required closed test:

- at least 12 testers opted in
- at least 14 continuous days
- testers must remain opted in when applying
- retain tester feedback and record changes made
- complete the Console's production-access questions about testing, audience, expected use, feedback, and readiness

Do not fabricate testers, feedback, dates, usage, or production-readiness answers.

## 11. Production Submission

Before clicking **Send for review**:

- AAB package is exactly `com.communityledger.app`
- Play app-signing and upload-key ownership are recorded and backed up
- the new app clean-installs without modifying the old-package beta
- store listing and all App content forms are complete
- privacy policy matches the Play build
- Data safety matches actual code/SDK traffic
- no unresolved pre-review errors
- physical-device matrix is complete enough for the intended rollout
- qualified legal review covers the initial countries
- support channel is monitored
- release notes name beta limitations

Use Managed publishing if the account supports it, so review approval does not cause an unexpected go-live.

Initial production scope recommendation:

- free app
- India first
- limited staged rollout rather than immediate 100%
- monitor Android vitals, crashes/ANRs, reviews, support email, install/update failures, and OCR reports
- expand only after evidence, not because review approval exists

## 12. Stop Conditions

Do not submit or halt rollout if:

- AAB package differs from `com.communityledger.app`
- anyone expects cross-package upgrade or automatic old-beta data recovery
- Play build still promotes external APK installation
- Data safety or privacy answers do not match the uploaded AAB
- Play pre-review reports a policy, SDK, target API, or device-compatibility blocker
- physical-device tests show silent ledger mutation, unexplained totals, or data loss
- store copy implies sync, authentication, access control, money movement, investment, guaranteed OCR, recovery, or regulatory compliance

## 13. Official References Checked

Official pages refreshed on 12 July 2026:

- Target API requirements: https://support.google.com/googleplay/android-developer/answer/11926878
- New personal-account testing: https://support.google.com/googleplay/android-developer/answer/14151465
- Play App Signing: https://support.google.com/googleplay/android-developer/answer/9842756
- Data safety: https://support.google.com/googleplay/android-developer/answer/10787469
- Preview assets: https://support.google.com/googleplay/android-developer/answer/9866151
- Android App Bundles: https://developer.android.com/guide/app-bundle

The refreshed target API Help page still states the August 2025 API-35 minimum. Community Ledger targets API 36, which is above that documented requirement. Always obey any newer Play Console warning shown when the actual AAB is uploaded.
