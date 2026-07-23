# Shared Event Emulator Foundation

This directory contains the server-authority contract and its isolated Firebase Emulator Suite tests. It does not configure, deploy, or connect to a production Firebase project.

## Boundary

- Test project ID: `demo-community-ledger`. Firebase reserves the `demo-` prefix for emulator-only projects, so a missing emulator cannot fall through to a live service.
- No default Firebase project alias is stored. Every production command must name the real owner-approved project explicitly.
- No service-account key, API key, Google Services file, production database URL, billing account, or cloud region is stored here.
- Android debug builds use these services for isolated shared-event development. Release builds keep the shared backend disabled, and the published beta remains local-only.
- Reviewed structured receipt details are modeled; receipt screenshots are never uploaded.

## Prerequisites

- Node.js 22.23.1 is installed as a pinned development dependency and used by the npm scripts for parity with the declared Functions runtime.
- Java 21 for Firebase emulators. On the current Windows workstation, Android Studio's bundled JBR 21 is used only for the command session; the global Java installation is not changed.
- Dependencies installed with `npm ci --prefix firebase/functions`.

## Validation

From the repository root:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\firebase\functions\node_modules\node\bin\node.exe .\firebase\functions\scripts\runEmulators.js
```

The command uses Java 21 only in that PowerShell session, plus the pinned Node runtime and Firebase CLI. It forces npm registry checks offline, generates fresh in-memory test HMAC keys, sanitizes inherited cloud credentials and production database URLs, pins a local-only function region, starts Auth, Firestore, Functions, and Realtime Database emulators, runs all syntax/rules/mutation tests, restores local parameter state, and stops the emulators. The current contract passes 51 tests, including rejection of weak, unlabelled, or manually sourced receipt amounts, correct Money in/out aggregate direction, evidence-gated confirmation, strict public discovery, and stale-trigger-safe presence revocation. On Windows, the runner also terminates only test-suite Java emulator processes created during its own invocation if Firebase CLI leaves them behind. Never replace the explicit test project with a real project ID.

The Android convergence gate requires the prebuilt Direct debug app/test APKs and one ready emulator. Its runner ignores physical-device serials:

```powershell
.\firebase\functions\node_modules\node\bin\node.exe .\firebase\functions\scripts\runEmulators.js android
```

On API 36, this gate passes one two-client test covering anonymous authentication, invite acceptance, joined-member visibility, approximate presence, pending receipt submissions, private-evidence review, confirmation, and equal final history/totals.

## Production Deployment Inputs

Production deployment remains blocked until the accountable owner supplies and approves:

- the real Firebase/Google Cloud project ID and billing owner;
- the permanent Firestore, Realtime Database, and Functions region;
- Secret Manager values for `INVITE_TOKEN_KEY` and `DUPLICATE_HASH_KEY` generated from at least 32 random bytes each;
- retention, deletion/export, recovery, rate-limit, cost-alert, monitoring, incident-response, privacy, and legal decisions;
- Android Auth, App Check/Play Integrity, listener/cache, offline conflict, revocation, and two-device convergence evidence.

Functions use `defineSecret` for both HMAC keys and `FUNCTIONS_REGION` has no production default. Do not put secret values in `.env` files, source control, shell history, or documentation.

## Implemented In Emulators

- Auth and App Check enforcement at callable boundaries.
- Server-created public/private events with organizer membership.
- Strict anonymous public profile plus explicit public viewer join.
- Single-use expiring private invites with hash-only secret storage.
- Organizer/contributor/viewer read authorization and deny-by-default client writes.
- Pending reviewed-entry submission and organizer confirm/reject.
- Server-confirmed event/member totals using integer minor units.
- Strong and fallback duplicate reservations plus idempotent retries.
- Confirmed-only member projections; pending/rejected details restricted to organizer and submitter.
- Private evidence readable only by organizers or its submitter.
- Approximate member-only presence using expiring server leases.
- Android debug shared-event screens, silent anonymous Auth, authorized Firestore listeners, Room event mapping/pending operations, restart replay, revocation cleanup, and lifecycle presence.
- API 36 two-client convergence through the real debug Android client and local Firebase services.

## Not Implemented

- Membership revocation/role-change callable, correction/void workflow, notification delivery, reconciliation job, or production monitoring.
- Production project ownership, billing, permanent region, managed secrets, production App Check/Play Integrity, account recovery, rate limits, retention, deletion/export, incident response, legal review, physical two-phone acceptance, or deployment.

Passing emulator and API 36 tests prove the debug integration, not a deployed or publicly available synchronization service.