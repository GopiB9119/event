# Signing And Distribution

## Current State

`0.2.0-beta.1` established the release-certificate lineage for the historical package `com.aistudio.communityledger.yrtqwx`. `0.2.0-beta.2` uses the same certificate. That lineage remains valid only for updates to that historical package.

Current source uses the separate package `com.communityledger.app`. Its long-term app-signing lineage and distribution relationship are not yet approved. Do not publish a release build of the new package until the owner records the decision below. Debug APKs remain development artifacts and must not be presented as a public release channel.

## New-Package Decision Gate

The `direct` and `play` flavors currently share `com.communityledger.app`. Android permits an installed app to update only when both the application ID and app-signing lineage are compatible. A Play upload key authenticates an AAB upload; it does not sign the APKs delivered to devices.

Before either new-package channel is published, the owner must approve one strategy:

1. **Play-only:** let Play create the app-signing key and do not publicly distribute a direct APK using `com.communityledger.app`.
2. **Compatible cross-channel lineage:** establish an eligible owner-controlled app-signing key before Play enrollment, complete the supported Play App Signing import process, and sign direct APKs with that same compatible lineage. Keep a separate upload key for AAB uploads.
3. **Separate package identities:** give one channel a different application ID before publication so Android treats the channels as separate apps with separate data and update histories.

Do not infer approval from the current Gradle configuration. A local release keystore can sign a direct APK and an uploaded AAB, but Play-delivered APKs are signed with the Play app-signing key. If Play creates a different app-signing key, users cannot update or switch between those same-package installations in place.

## Release Key Rule

For the historical package, preserve its existing release keystore and independent backup. For the new package, create or register keys only after the decision gate is approved. Store every owner-controlled key and password outside the repository with two secure, independent backups. Losing an app-signing key can prevent future direct updates.

Do not:

- commit a keystore, password, token, or generated credentials
- send signing passwords through chat
- replace the release key between versions
- publish a release built from an unreviewed workspace

## Local Key Generation

Run this only after the approved strategy calls for a new upload key. Enter secrets directly in a secure terminal:

```powershell
keytool -genkeypair -v -keystore C:\secure\community-ledger-upload.jks -alias upload -keyalg RSA -keysize 4096 -validity 10000
```

Then set these environment variables only in the build environment:

```text
KEYSTORE_PATH
STORE_PASSWORD
KEY_PASSWORD
```

The existing Gradle release configuration reads those values. It does not need secrets in source control. These variables do not establish or prove the Play app-signing lineage.

## Build Outputs

For direct APK distribution:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleDirectRelease
```

Do not distribute this new-package output until the decision gate permits direct distribution and its signing certificate has been verified against that decision.

For Google Play after store readiness is complete:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:bundlePlayRelease
```

Verify every public APK with `apksigner`, `zipalign`, SHA-256, installation, cold launch, OCR review, and persisted ledger checks.

## Hosting

Use GitHub Releases over HTTPS for APKs. Each release should include:

- a release-signed APK named `community-ledger-<versionName>.apk` using only letters, digits, dots, hyphens, and underscores
- version name and version code
- SHA-256 checksum
- signing certificate SHA-256 fingerprint
- release notes and known limitations
- privacy policy and terms links

The website should link to GitHub Releases. Do not copy APKs into the GitHub Pages source tree.

Example candidate asset name:

```text
community-ledger-0.2.0-beta.2.apk
```

## Rollback

Installing an older APK may fail when its version code is lower. Uninstalling to roll back deletes local data. Until export/restore exists, there is no safe rollback for user-created ledgers.
