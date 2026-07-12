# Signing And Distribution

## Current State

`0.2.0-beta.1` establishes the permanent Community Ledger release-certificate lineage and is published as a limited GitHub prerelease. Future Android updates must use the same signing lineage. Debug APKs remain development artifacts and must not be presented as the public release channel.

## Release Key Rule

Create one Android release keystore outside the repository. Store its file and passwords in two secure, independent backups. Losing the key can prevent users from installing future updates over the existing app.

Do not:

- commit a keystore, password, token, or generated credentials
- send signing passwords through chat
- replace the release key between versions
- publish a release built from an unreviewed workspace

## Local Key Generation

Run this yourself from a secure terminal and enter secrets directly when prompted:

```powershell
keytool -genkeypair -v -keystore C:\secure\community-ledger-upload.jks -alias upload -keyalg RSA -keysize 4096 -validity 10000
```

Then set these environment variables only in the build environment:

```text
KEYSTORE_PATH
STORE_PASSWORD
KEY_PASSWORD
```

The existing Gradle release configuration reads those values. It does not need secrets in source control.

## Build Outputs

For direct APK distribution:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:assembleRelease
```

For Google Play after store readiness is complete:

```powershell
.\gradlew.bat --no-daemon --no-configuration-cache :app:bundleRelease
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
community-ledger-0.2.0-beta.1.apk
```

## Rollback

Installing an older APK may fail when its version code is lower. Uninstalling to roll back deletes local data. Until export/restore exists, there is no safe rollback for user-created ledgers.
