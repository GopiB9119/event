# Play Store Identity Migration

Last reviewed: 14 July 2026

## Approved Identity

Enter these values for the new Google Play app:

| Field | Value |
|---|---|
| App name | `Community Ledger` |
| Package name / Application ID | `com.communityledger.app` |
| Android namespace | `com.communityledger.app` |
| Default language | English (India) |
| App or game | App |
| Free or paid | Free |

The app name is 16 characters and fits Play's 30-character limit. The selected package contains no publisher name, personal name, or GitHub handle.

## Irreversible Consequence

The published beta uses `com.aistudio.communityledger.yrtqwx`. Android and Google Play treat `com.communityledger.app` as a completely different app even if both are signed with the same certificate.

Therefore:

- the new app cannot update the old beta;
- old app-private Room data, preferences, and receipt evidence do not move into the new app;
- both apps can be installed side by side;
- uninstalling the old beta still deletes its local ledger;
- users must keep the old beta installed until a separately tested export/import path exists or their old data is no longer needed.

Do not describe this package change as an upgrade or migration of existing beta data.

## Source Layout

The current source identity is:

```text
app/src/main/java/com/communityledger/app/
app/src/test/java/com/communityledger/app/
app/src/androidTest/java/com/communityledger/app/
```

Gradle uses:

```kotlin
namespace = "com.communityledger.app"
applicationId = "com.communityledger.app"
```

Historical release notes keep the old package because they document already-published APKs.

## Play Console Steps

1. Open **Play Console -> All apps -> Create app**.
2. Enter app name `Community Ledger`.
3. Select English (India), App, and Free.
4. Complete the declarations shown by Play Console.
5. When Play asks for a package or after the first AAB is uploaded, verify the package is exactly `com.communityledger.app`.
6. Use **Check availability**. If Play reports the package is unavailable, stop; do not invent a personal-name suffix or upload under another package without a new owner decision.
7. Remember that the package becomes permanent for that Play app after the first artifact is accepted.

Creating the Play Console record does not require publishing the app. Production remains deferred until shared-event, privacy, security, physical-device, and policy gates pass.

## Signing Choice

The old beta signing certificate belongs to the old package's update lineage. It is not required to preserve update compatibility for this new package because cross-package updates are impossible.

The new `direct` and `play` flavors currently share `com.communityledger.app`. The upload key is not the Play app-signing key. If Play creates a different app-signing key from the one used for direct APKs, Android cannot update or switch between those same-package installations in place.

Required owner decision before publication:

1. Use Play-only distribution and do not publicly distribute a direct APK with this package; or
2. establish an eligible owner-controlled app-signing key and complete Play's supported import process so direct and Play installations have a compatible lineage; or
3. assign separate application IDs before publication and accept separate app data and update histories.

After that decision, enroll `com.communityledger.app` in Play App Signing as applicable, create a separate upload key for AAB uploads, keep owner-controlled keys outside the repository with independent backups, and record only certificate fingerprints. See [Signing And Distribution](SIGNING_AND_DISTRIBUTION.md).

Do not publicly distribute a `com.communityledger.app` APK before the signing/channel strategy is final, because that can create another update-lineage problem.

## Versioning

The package is new, so Play does not require continuity with the old package's version code. The repository currently keeps the historical beta version while development continues. Before the first Play internal-test upload:

1. choose the approved candidate version name;
2. increment `versionCode` above the current source value;
3. build one AAB and promote that same tested artifact through testing tracks when possible.

Do not call the current emulator-only shared backend a finished Android synchronization feature.

## Build And Verify

Run the package migration checks first:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:compileDirectDebugKotlin :app:compilePlayDebugKotlin
.\gradlew.bat --no-daemon --no-configuration-cache :app:testDirectDebugUnitTest :app:testPlayDebugUnitTest :app:assembleDirectDebug :app:assemblePlayDebug
```

For a signed Play candidate, set signing secrets only in a secure terminal and build the release AAB:

```powershell
$env:KEYSTORE_PATH = 'C:\secure\community-ledger-upload.jks'
$env:STORE_PASSWORD = '<type in secure terminal>'
$env:KEY_PASSWORD = '<type in secure terminal>'
.\gradlew.bat --no-daemon --no-configuration-cache :app:lintPlayRelease :app:bundlePlayRelease
```

Before upload, verify:

- the AAB application ID is `com.communityledger.app`;
- the upload certificate matches the certificate registered in Play Console;
- version code is higher than the prior upload for this new package;
- tests, lint, Bundle Explorer, and pre-launch checks pass;
- store copy and Data safety describe the exact uploaded build;
- no screen claims live synchronization until Android is connected to the reviewed backend and two-device convergence passes.

## Firebase Registration Later

The local `demo-community-ledger` emulator does not need a Firebase login or Android app registration. When production Firebase is separately approved:

1. create the production Firebase project under the approved owner and billing account;
2. register the Android app as `com.communityledger.app`;
3. add the Play app-signing and upload/debug SHA certificate fingerprints required by the chosen authentication setup;
4. keep production configuration and secrets out of source control as required by the security design;
5. update Privacy, Data safety, retention, deletion/export, incident response, and support documentation before backend-enabled testing.

## Stop Conditions

Stop the Play setup or upload if:

- the package shown by Bundle Explorer is not exactly `com.communityledger.app`;
- Play reports the package is already in use;
- anyone expects the new package to update or recover the old beta automatically;
- signing ownership or backup is unclear;
- the uploaded build's data behavior differs from Privacy or Data safety answers;
- the app still implies synchronization that the Android build does not implement.
