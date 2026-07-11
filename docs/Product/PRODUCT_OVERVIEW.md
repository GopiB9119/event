# Product Overview

## One-Sentence Product

Community Ledger helps a trusted organizer maintain a local event-money ledger backed by reviewed payment receipt evidence.

## Target User

The current product serves one organizer or one shared physical device used by a trusted community group. It does not yet provide a synchronized ledger shared across multiple phones.

## Implemented User Journeys

| Journey | Current state |
|---|---|
| Acknowledge beta limits | Required once; persists locally after acceptance |
| Set local identity | Valid normalized email label saved locally; required before ownership/uploader actions; not authentication |
| Create event | Title, duration, custom fields, public/private marker |
| Filter events | All, public, or private on the local dashboard |
| Add members | Persisted per event |
| Import receipt | Picker or Android share intent |
| Extract receipt | On-device ML Kit Latin + Devanagari |
| Review and save | JSON review, confidence warning, duplicate block |
| View ledger | Collected, spent, balance, members, transactions |
| Share event copy | Expiring convenience link with checksum |
| Add event copy | Creates or opens an independent event metadata shell on that device |
| Delete event | Creator-email comparison plus confirmation flow |

## Product Truth

### What works

- Local event creation and ledger persistence
- Receipt OCR, parsing, review, evidence storage, and calculation
- Duplicate protection for the selected local event
- Local member linking
- Import from payment-app share sheets
- APK installation and offline use after ML Kit availability is confirmed
- Event creation, event-copy sharing, receipt scans/shares, and receipt replacement require a valid local identity label
- If the app process dies during OCR or review, no receipt is saved and the next launch tells the user to rescan

### What does not exist

- Real login or account recovery
- Server-enforced private events
- Cross-device ledger synchronization
- Event-copy revocation or membership authorization
- Cloud backup, export, restore, or conflict resolution
- Payment processing

## Public And Private

Public/private controls labels, dashboard filtering, and the marker copied in event-copy links. Anyone who receives a valid link can add the event shell to their device. It must not be presented as confidentiality or access control.

## Success Criteria For Friend Beta

1. A new user understands before use that data is local and does not sync.
2. Event creation, receipt import, review, save, and totals work on a real device.
3. Invalid or duplicate receipt evidence cannot mutate totals.
4. Event-copy link collisions fail without changing existing data.
5. App restart preserves saved events and transactions.
6. Known limitations are visible in-app and in release notes.

The first-use disclosure now blocks entry until the user accepts the no-sync, no-access-control, no-recovery, and uninstall data-loss limits.

## Next Product Decision

Choose one direction before expanding features:

1. **Local organizer tool:** prioritize export/restore, auditability, and simple UX.
2. **Shared multi-device ledger:** design authentication, server-issued event IDs, authorization, synchronization, conflict resolution, and privacy before writing sync code.

Trying to imply multi-device sharing without that architecture will damage trust.