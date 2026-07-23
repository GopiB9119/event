package com.communityledger.app.shared

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class SharedFirebaseConvergenceInstrumentedTest {
    private lateinit var context: Context
    private lateinit var organizerBackend: FirebaseEmulatorSharedBackend
    private lateinit var contributorBackend: FirebaseEmulatorSharedBackend

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val suffix = UUID.randomUUID().toString().replace("-", "").take(12)
        organizerBackend = FirebaseEmulatorSharedBackend.create(context, "organizer-$suffix")
        contributorBackend = FirebaseEmulatorSharedBackend.create(context, "contributor-$suffix")
    }

    @After
    fun tearDown() {
        runCatching(organizerBackend::close)
        runCatching(contributorBackend::close)
    }

    @Test
    fun twoClientsConvergeOnMembershipPresenceEntriesAndTotals() = runBlocking {
        withTimeout(240_000L) {
            val organizer = organizerBackend.authenticate("Convergence Organizer")
            val contributor = contributorBackend.authenticate("Convergence Contributor")

            val created = organizerBackend.createEvent(
                CreateSharedEventRequest(
                    title = "Shared Convergence ${UUID.randomUUID().toString().take(8)}",
                    duration = "Instrumented acceptance",
                    isPrivate = true,
                    customInfo = emptyMap()
                )
            )
            val invite = organizerBackend.createInvite(
                eventId = created.eventId,
                role = SharedRole.CONTRIBUTOR,
                expiresInSeconds = 3600
            )
            val joined = contributorBackend.acceptInvite(invite.inviteToken)
            assertEquals(SharedRole.CONTRIBUTOR, joined.role)

            val organizerEventAfterJoin = organizerBackend.observeEvent(created.eventId)
                .first { it.activeMemberCount == 2L && it.revision >= 2L }
            val contributorEventAfterJoin = contributorBackend.observeEvent(created.eventId)
                .first { it.activeMemberCount == 2L && it.revision == organizerEventAfterJoin.revision }
            val organizerMembers = organizerBackend.observeMembers(created.eventId)
                .first { it.size == 2 }
            val contributorMembers = contributorBackend.observeMembers(created.eventId)
                .first { it.size == 2 }
            assertEquals(
                organizerMembers.map(SharedMember::uid).toSet(),
                contributorMembers.map(SharedMember::uid).toSet()
            )

            organizerBackend.publishPresence(created.eventId, SharedPresenceState.ACTIVE)
            contributorBackend.publishPresence(created.eventId, SharedPresenceState.ACTIVE)
            val contributorPresence = contributorBackend.observePresence(
                created.eventId,
                listOf(organizer.uid, contributor.uid)
            ).first { presence ->
                presence[organizer.uid]?.state == SharedPresenceState.ACTIVE &&
                    presence[contributor.uid]?.state == SharedPresenceState.ACTIVE
            }
            assertEquals(2, contributorPresence.size)

            val organizerSubmission = organizerBackend.submitEntry(
                validEntryRequest(
                    eventId = created.eventId,
                    expectedRevision = contributorEventAfterJoin.revision,
                    ledgerPersonUid = contributor.uid,
                    amountMinor = 12_345L,
                    reference = "ANDROIDCONVERGENCE001"
                )
            )
            assertEquals(SharedEntryStatus.PENDING, organizerSubmission.status)
            val organizerEvidence = organizerBackend.loadPrivateEvidence(
                created.eventId,
                requireNotNull(organizerSubmission.entryId)
            )
            assertEquals("ANDROIDCONVERGENCE001", organizerEvidence.paymentReference)
            val organizerReviewed = organizerBackend.reviewEntry(
                eventId = created.eventId,
                entryId = requireNotNull(organizerSubmission.entryId),
                expectedRevision = contributorEventAfterJoin.revision,
                confirm = true,
                reason = null
            )
            assertEquals(SharedEntryStatus.CONFIRMED, organizerReviewed.status)

            val organizerAfterImmediate = organizerBackend.observeEvent(created.eventId)
                .first { it.totalCollectedMinor == 12_345L && it.confirmedReceiptCount == 1L }
            val contributorAfterImmediate = contributorBackend.observeEvent(created.eventId)
                .first {
                    it.totalCollectedMinor == organizerAfterImmediate.totalCollectedMinor &&
                        it.revision == organizerAfterImmediate.revision
                }
            assertEquals(12_345L, contributorAfterImmediate.totalCollectedMinor)

            val pending = contributorBackend.submitEntry(
                validEntryRequest(
                    eventId = created.eventId,
                    expectedRevision = contributorAfterImmediate.revision,
                    ledgerPersonUid = contributor.uid,
                    amountMinor = 700L,
                    reference = "ANDROIDCONVERGENCE002"
                )
            )
            assertEquals(SharedEntryStatus.PENDING, pending.status)
            val pendingEntry = organizerBackend.observeEntries(
                created.eventId,
                SharedRole.ORGANIZER,
                organizer.uid
            ).first { entries -> entries.any { it.id == pending.entryId && it.status == SharedEntryStatus.PENDING } }
                .single { it.id == pending.entryId }
            assertEquals(700L, pendingEntry.amountMinor)
            val contributorEvidence = organizerBackend.loadPrivateEvidence(
                created.eventId,
                requireNotNull(pending.entryId)
            )
            assertEquals("ANDROIDCONVERGENCE002", contributorEvidence.paymentReference)

            val reviewed = organizerBackend.reviewEntry(
                eventId = created.eventId,
                entryId = requireNotNull(pending.entryId),
                expectedRevision = contributorAfterImmediate.revision,
                confirm = true,
                reason = null
            )
            assertEquals(SharedEntryStatus.CONFIRMED, reviewed.status)

            val organizerFinal = organizerBackend.observeEvent(created.eventId)
                .first { it.totalCollectedMinor == 13_045L && it.confirmedReceiptCount == 2L }
            val contributorFinal = contributorBackend.observeEvent(created.eventId)
                .first { it.totalCollectedMinor == 13_045L && it.revision == organizerFinal.revision }
            assertEquals(organizerFinal, contributorFinal.copy(fromCache = organizerFinal.fromCache))

            val organizerEntries = organizerBackend.observeEntries(
                created.eventId,
                SharedRole.ORGANIZER,
                organizer.uid
            ).first { entries -> entries.count { it.status == SharedEntryStatus.CONFIRMED } == 2 }
            val contributorEntries = contributorBackend.observeEntries(
                created.eventId,
                SharedRole.CONTRIBUTOR,
                contributor.uid
            ).first { entries -> entries.count { it.status == SharedEntryStatus.CONFIRMED } == 2 }
            assertEquals(organizerEntries.map(SharedEntry::id).toSet(), contributorEntries.map(SharedEntry::id).toSet())
            assertTrue(contributorEntries.all { it.amountMinor > 0L })
        }
    }

    private fun validEntryRequest(
        eventId: String,
        expectedRevision: Long,
        ledgerPersonUid: String,
        amountMinor: Long,
        reference: String
    ) = SubmitSharedEntryRequest(
        eventId = eventId,
        ledgerPersonUid = ledgerPersonUid,
        ledgerType = "Donated",
        amountMinor = amountMinor,
        amountEvidenceSource = "AMOUNT_LABEL",
        amountEvidenceConfidence = 90,
        expectedRevision = expectedRevision,
        paymentReference = reference,
        paymentDate = "2026-07-15",
        paymentApp = "Instrumented Test",
        counterparty = "Convergence Fixture",
        confidence = 90,
        warnings = emptyList()
    )
}