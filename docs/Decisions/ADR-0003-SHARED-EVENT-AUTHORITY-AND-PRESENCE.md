# ADR-0003: Shared Event Authority And Presence

Date: 14 July 2026; implementation evidence updated 17 July 2026

Status: Accepted for local emulator implementation; production deployment deferred

## Context

The current event-copy link creates an independent Room shell. It does not create membership, synchronize entries, or connect devices. Users reasonably expect a joined event to show the same people, reviewed entries, totals, history, and later changes on every authorized phone.

Synchronizing local Room files or trusting client-calculated totals would allow divergent balances, duplicate submissions, role escalation, and silent financial-history replacement. Exact online/offline claims are also unreliable on mobile networks and can expose unnecessary behavioral data.

## Decision

Build shared events as a separate server-authoritative mode with these provisional choices:

- Silent anonymous Firebase Authentication identifies each current debug installation without a visible login. Production recovery/account-linking remains an explicit decision gate.
- Cloud Firestore listeners provide authorized real-time read projections.
- Callable Cloud Functions are the only mutation path for confirmed events, memberships, entries, totals, invites, and audit records.
- Room becomes an offline confirmed-data cache plus a separate pending-operation queue. It is never the cross-device authority.
- Reviewed structured receipt details may synchronize in v1. Receipt screenshots remain on the uploader's device.
- Firebase Realtime Database is reserved for presence because its disconnect primitives fit mobile presence better than Firestore. Presence is visible only to active event members.
- Presence copy is limited to `Active now`, `Recently active`, or `Unavailable`. It is approximate, has a short time-to-live, and is not an exact last-seen guarantee.
- India is the first intended operating region. No deployment region is pinned by the emulator code. A permanent Firebase/Firestore/Functions location remains unselected until the publisher confirms account ownership, billing, and available region choices.
- The emulator project ID is `demo-community-ledger`. The `demo-` prefix prevents accidental access to live Firebase resources.

## Membership Rules

- Every shared-ledger reader must have an authenticated active membership.
- Public events expose only a reviewed public profile. A signed-in user must explicitly choose `Join event` before receiving member projections or ledger data.
- Private events are undiscoverable beyond minimum invite acceptance metadata. A signed-in user must accept a valid, unexpired, unrevoked server invite.
- Accepting a join or invite creates one server membership with role `organizer`, `contributor`, or `viewer`.
- Opening the existing local event-copy link never creates shared membership.
- Revoked and non-member accounts cannot read or mutate the shared event.

## Ledger Rules

- The server stores money as integer minor units and calculates all confirmed totals.
- Client retries use persisted idempotency keys and cannot double-count.
- Confirmed financial corrections append revisions and audit events; they do not silently overwrite history.
- Pending offline submissions remain separate from confirmed totals and are visibly pending.
- Every authorized device listens to the same server projections and transactionally refreshes its Room cache.
- Listener disconnects show stale state and the last server-confirmed time. Cached data is never labelled live while disconnected.

## Presence And Activity

- Presence is not part of financial authority and cannot affect membership or totals.
- A client writes only its own connection state after authentication.
- Realtime Database rules require mirrored active event access before presence read/write.
- Active membership grants a short server presence lease. An authenticated/App Check-protected refresh rechecks Firestore membership before extending it; stale leases and stale presence records become unreadable. The Android debug client publishes lifecycle-bound approximate states and treats inaccessible presence as `Unavailable`.
- The UI may show joined count, role counts, active-now count, and recently-active count. It must not expose precise activity timestamps to non-members or claim guaranteed offline detection.
- A redacted activity feed comes from immutable server audit events, not presence heartbeats.

## Notifications

Push notifications are a later bounded slice. They may announce invite acceptance, role changes, reviewed-entry state, corrections, and revocation, but never include receipt references, contact details, amounts, or evidence in lock-screen payloads by default.

## Consequences

- Real shared events require cloud operations, privacy disclosures, account recovery, deletion/export, monitoring, abuse limits, and incident response.
- The local beta remains truthful and usable without a backend, but its copy link cannot be marketed as join or sync.
- Production remains blocked until physical two-phone convergence, billing ownership, permanent region, recovery, retention, support, privacy, operations, and legal gates pass.

## Emulator Evidence

As of 17 July 2026, 51 tests across the Auth, Firestore, Functions, and Realtime Database emulators prove:

1. authenticated/App Check-protected event, invite, join, entry, review, and presence-lease callables;
2. public-profile isolation, explicit public viewer join, private invite acceptance, joined/role counts, and malformed/revoked/non-member denial;
3. pending versus confirmed entries, organizer review/rejection, reliable receipt-derived amount provenance, confirmed-only member projections, private evidence scope, strong/fallback duplicates, idempotency, stale-revision rejection, and aggregate rollback;
4. member-only approximate presence, own-record writes, lease expiry, current-membership rechecks, and stale-trigger-safe Firestore membership grant/revoke mirroring;
5. no production project, credentials, billing, deployment, or receipt-image upload exists.

An API 36 Android convergence test additionally proves two isolated anonymous clients can create/invite/join, see two active members and approximate presence, submit pending entries, load private evidence, confirm them, and converge on equal final history and totals. Production deployment and public shared-mode claims remain deferred.
