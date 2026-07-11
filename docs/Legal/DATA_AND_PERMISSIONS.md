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
| Event-copy link fields | Add an independent event shell on another device | Shared through an app chosen by the user | User explicitly copies/shares link |
| Update request metadata | User-triggered version check | HTTPS request to GitHub Pages; normal network metadata may be logged by hosting infrastructure | Runs only when user taps check |

## Android Permissions

| Permission | Status | Reason |
|---|---|---|
| Internet | Required for the manual update check and opening hosted release information | No background polling or silent installation |
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
- No analytics SDK
- No advertising SDK
- No Firebase backend, authentication, Crashlytics, or cloud database
- No Gemini or other cloud OCR/LLM extraction

## Backup Posture

Android backup and device-transfer configuration excludes:

- `community_ledger_db` and related database files
- receipt evidence files
- `app_prefs` preferences

This protects against unintended cloud backup but means there is no recovery after uninstall or device loss.
