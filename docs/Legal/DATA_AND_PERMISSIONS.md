# Data And Permissions Inventory

This inventory is the source for privacy copy and future store data-safety declarations.

| Data or capability | Purpose | Storage or transmission | User control |
|---|---|---|---|
| Events and custom fields | Define a local event ledger | Room database on device | Create/delete events |
| Member name/contact/role | Link people to contributions or expenses | Room database on device | Optional entry; member/event deletion |
| Transactions | Calculate collected, spent, and balance totals | Room database on device | Explicit reviewed save; deletion rules |
| Receipt image | Input to OCR | Read temporarily from a selected/shared URI; not sent to a Community Ledger server | Select/share/cancel |
| Receipt JSON evidence | Explain a saved receipt transaction | App-private files on device | Saved only after review; removed with associated local data/uninstall |
| Local email label | Local ownership/uploader checks | SharedPreferences and event/transaction records | User enters or changes it |
| Event-copy link fields | Add or reopen an independent event shell using an opaque copy key, metadata, expiry, and checksum | Shared through an app chosen by the user | User explicitly copies/shares link |
| ML Kit SDK diagnostics | Diagnose and measure ML Kit feature use | Google documents device/app information, bundled per-installation identifiers, performance, API configuration, input/output sizes, feature versions/events, and errors sent with HTTPS | Occurs as part of using the bundled OCR SDK; the current app exposes no telemetry control |
| Update request metadata | User-triggered version check | HTTPS request to GitHub Pages; normal network metadata may be logged by hosting infrastructure | Runs only when user taps check |

## Android Permissions

| Permission | Status | Reason |
|---|---|---|
| Internet | Used by ML Kit diagnostics/usage telemetry and the manual update check | No silent installation; update checks remain user-triggered |
| Camera | Not requested | Receipts come from Android picker/share flows |
| Location | Not requested | Not needed |
| Contacts | Not requested | Member details are entered manually |
| Microphone | Not requested | Not needed |
| SMS/call log | Not requested | Not needed |
| Broad photo/storage access | Not requested | Android grants access only to selected/shared content URIs |
| Install packages | Not requested | APK downloads open in the browser; Android handles installation consent |

## SDK And Service Inventory

- Google ML Kit Latin text recognition: on-device OCR
- Google ML Kit Devanagari text recognition: on-device OCR
- AndroidX Room: local database
- Jetpack Compose and Material 3: user interface
- No separate general-purpose product analytics SDK; ML Kit includes diagnostics and usage analytics documented by Google
- No advertising SDK
- No Firebase backend, authentication, Crashlytics, or cloud database
- No Gemini or other cloud OCR/LLM extraction

ML Kit's current Android disclosure says its collected SDK metadata is encrypted in transit and is not transferred to third parties. It does not list receipt image contents or recognized text as collected data for bundled text recognition. Source checked 12 July 2026: https://developers.google.com/ml-kit/android-data-disclosure

## Backup Posture

Android backup and device-transfer configuration excludes:

- `community_ledger_db` and related database files
- receipt evidence files
- `app_prefs` preferences

This protects against unintended cloud backup but means there is no recovery after uninstall or device loss.
