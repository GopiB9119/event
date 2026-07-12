# Community Ledger Privacy Policy

Effective date: 11 July 2026

## Scope

This policy describes the Community Ledger Android beta. Community Ledger is a local-first event finance record. It does not collect, hold, invest, transfer, or process money.

Community Ledger is independently published by **Gopi Banoth**. Private support and privacy questions can be sent to [banothgopikrishna19@gmail.com](mailto:banothgopikrishna19@gmail.com). Public identity links: [GitHub](https://github.com/GopiB9119) and [LinkedIn](https://www.linkedin.com/in/gopib-960965243/).

Non-sensitive app bugs can also be reported through the [public project support page](https://github.com/GopiB9119/event/issues). Do not post receipts, personal or financial data, passwords, signing keys, or verification codes in a public issue.

## Data Stored On Your Device

The app can store:

- event names, dates, visibility markers, and custom fields
- member names, phone numbers, email addresses, and roles entered by an organizer
- donation, credit, expense, and debit records
- payment references, UPI IDs, payee information, and OCR warnings retained in reviewed receipt JSON
- a self-declared local email label used for local ownership and uploader checks
- app preferences and non-sensitive interruption markers

The Room database, receipt JSON, and preferences use app-private storage and are excluded from Android backup and device-transfer rules.

## Receipt Images And OCR

When you select or share a receipt image, the app reads it on the device using Google ML Kit Latin and Devanagari text recognition. OCR can be wrong. Extracted values do not affect ledger totals until a person reviews and saves them.

The app does not send receipt images to a Community Ledger server. It stores reviewed JSON evidence rather than a cloud OCR result. The source image remains under the control of the gallery, payment app, or other source that supplied it.

## Network Use

The beta has no account, synchronization, advertising, crash-reporting, or transaction server.

Google's ML Kit Android SDK disclosure states that all ML Kit features collect device and application information, bundled per-installation identifiers, performance metrics, API configuration such as image format and resolution, input/output sizes, feature versions, event types, and error codes for diagnostics and usage analytics. Google states that this data is encrypted in transit with HTTPS and is not transferred to third parties. The disclosure does not list receipt image contents or recognized text as collected data for bundled text recognition; OCR execution remains on-device.

The manual update checker contacts a static HTTPS release manifest only when the user taps **Check for updates**. That request can expose ordinary connection metadata such as IP address, request time, and user agent to GitHub Pages and its infrastructure. There is no automatic background update check or silent installation.

ML Kit disclosure source, checked 12 July 2026: https://developers.google.com/ml-kit/android-data-disclosure

## Sharing And Event-Copy Links

Android share actions send only content selected by the user. An event-copy link can contain an opaque copy key, event title, expiry, visibility marker, and the organizer's self-declared local email label. A recipient can forward that link.

An event-copy link does not authenticate the organizer, enforce private access, or connect devices to one synchronized ledger. Each device receives an independent local event shell.

## Data Disclosure

Community Ledger does not sell personal information. Because there is no Community Ledger backend, the project cannot view or retrieve ledger content stored on a user's device.

Users can disclose data themselves by sharing screenshots, receipt text, event-copy links, exports from other apps, or device access. Those disclosures are controlled by the user and the receiving service's terms.

## Retention And Deletion

Local data remains until the user deletes an event, clears app data, or uninstalls the app. Uninstalling deletes the ledger from that device. There is currently no export, restore, or account recovery.

## Organizer Responsibilities

Organizers should collect only information needed for their event, obtain any required consent, avoid recording unnecessary contact or payment details, and follow the laws that apply to their event, donations, and record keeping.

## Security Limits

The app uses app-private storage, reviewed OCR, duplicate checks, and validated ledger writes. These controls do not make a device tamper-proof. A rooted device, debug build, forwarded link, shared screenshot, or person with device access can expose data.

## Your Choices

You can:

- decline to select or share receipt images
- avoid entering member contact details
- delete an event and its associated local records
- clear app data or uninstall the app
- stop using event-copy links

## Changes

Material policy changes should update the effective date and be included in release notes. The in-app policy and hosted policy should remain consistent.

## Contact

- Publisher: Gopi Banoth, independent publisher of Community Ledger
- Private support and privacy email: [banothgopikrishna19@gmail.com](mailto:banothgopikrishna19@gmail.com)
