package com.communityledger.app.recovery

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.io.FilterOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class InvalidLedgerBackupPackageException(message: String, cause: Throwable? = null) :
    IOException(message, cause)

data class LedgerBackupPackage(
    val payload: LedgerBackupPayload,
    val receiptEvidence: List<CanonicalReceiptEvidence>
)

object LedgerBackupPackageCodec {
    const val FORMAT_VERSION = 1
    const val MAX_PACKAGE_BYTES = 32 * 1024 * 1024
    const val MAX_TOTAL_RECEIPT_EVIDENCE_BYTES = 16 * 1024 * 1024
    const val MAX_RECEIPT_EVIDENCE_BYTES = 512 * 1024

    private const val HEADER_BYTES = 16L
    private const val EVIDENCE_ID_BYTES = 32
    private const val EVIDENCE_RECORD_HEADER_BYTES = EVIDENCE_ID_BYTES + 8L
    private val magic = byteArrayOf('C'.code.toByte(), 'L'.code.toByte(), 'P'.code.toByte(), 'K'.code.toByte())
    private val evidenceOrder = compareBy<LedgerBackupReceiptEvidence>(
        LedgerBackupReceiptEvidence::sourceTransactionId,
        LedgerBackupReceiptEvidence::id
    )

    fun encode(backupPackage: LedgerBackupPackage): ByteArray {
        val output = ByteArrayOutputStream()
        encode(backupPackage, output)
        return output.toByteArray()
    }

    /**
     * Writes a deterministic package frame. The caller owns [output] and must stage writes
     * atomically because a validation or I/O failure may leave a partial frame.
     */
    fun encode(backupPackage: LedgerBackupPackage, output: OutputStream) {
        val manifest = try {
            LedgerBackupManifestCodec.encode(backupPackage.payload)
        } catch (error: InvalidLedgerBackupManifestException) {
            throw InvalidLedgerBackupPackageException("Backup package manifest is invalid.", error)
        }
        val descriptors = orderedDescriptors(backupPackage.payload)
        val evidenceByKey = validateEvidenceSet(descriptors, backupPackage.receiptEvidence)
        val boundedOutput = PackageBoundedOutputStream(output, MAX_PACKAGE_BYTES.toLong())
        val dataOutput = DataOutputStream(NonClosingOutputStream(boundedOutput))

        try {
            dataOutput.write(magic)
            dataOutput.writeInt(FORMAT_VERSION)
            dataOutput.writeInt(manifest.size)
            dataOutput.writeInt(descriptors.size)
            dataOutput.write(manifest)
            descriptors.forEach { descriptor ->
                val evidence = requireNotNull(evidenceByKey[descriptor.key()])
                dataOutput.write(descriptor.id.toByteArray(StandardCharsets.US_ASCII))
                dataOutput.writeInt(descriptor.sourceTransactionId)
                dataOutput.writeInt(evidence.bytes.size)
                dataOutput.write(evidence.bytes)
            }
            dataOutput.flush()
        } catch (error: PackageByteLimitExceededException) {
            throw InvalidLedgerBackupPackageException("Backup package exceeds the supported size limit.", error)
        }
    }

    fun decode(bytes: ByteArray): LedgerBackupPackage = decode(ByteArrayInputStream(bytes))

    /** The caller owns [input]. This method never closes it. */
    fun decode(input: InputStream): LedgerBackupPackage {
        val dataInput = DataInputStream(input)
        try {
            val actualMagic = ByteArray(magic.size).also(dataInput::readFully)
            if (!actualMagic.contentEquals(magic)) fail("This is not a Community Ledger recovery package.")

            val version = dataInput.readInt()
            if (version != FORMAT_VERSION) fail("Recovery package version $version is not supported.")

            val manifestLength = dataInput.readInt()
            if (manifestLength !in 1..LedgerBackupManifestCodec.MAX_MANIFEST_BYTES) {
                fail("Recovery package manifest length is outside the supported range.")
            }
            val evidenceCount = dataInput.readInt()
            if (evidenceCount !in 0..LedgerBackupManifestCodec.MAX_RECEIPT_EVIDENCE) {
                fail("Recovery package evidence count is outside the supported range.")
            }
            val minimumFrameBytes = safeAdd(
                HEADER_BYTES + manifestLength,
                safeMultiply(evidenceCount.toLong(), EVIDENCE_RECORD_HEADER_BYTES + 1L)
            )
            if (minimumFrameBytes > MAX_PACKAGE_BYTES) {
                fail("Recovery package exceeds the supported size limit.")
            }

            val manifestBytes = ByteArray(manifestLength).also(dataInput::readFully)
            val payload = try {
                LedgerBackupManifestCodec.decode(manifestBytes)
            } catch (error: InvalidLedgerBackupManifestException) {
                throw InvalidLedgerBackupPackageException("Recovery package manifest is invalid.", error)
            }
            val descriptors = orderedDescriptors(payload)
            if (evidenceCount != descriptors.size) {
                fail("Recovery package evidence count does not match its manifest.")
            }

            var totalEvidenceBytes = 0L
            var totalPackageBytes = HEADER_BYTES + manifestLength
            val evidence = ArrayList<CanonicalReceiptEvidence>(evidenceCount)
            descriptors.forEach { descriptor ->
                val evidenceId = readEvidenceId(dataInput)
                val sourceTransactionId = dataInput.readInt()
                val byteCount = dataInput.readInt()
                if (evidenceId != descriptor.id || sourceTransactionId != descriptor.sourceTransactionId) {
                    fail("Recovery package evidence records are missing, duplicated, or out of order.")
                }
                if (byteCount !in 1..MAX_RECEIPT_EVIDENCE_BYTES) {
                    fail("Recovery package evidence length is outside the supported range.")
                }
                if (descriptor.byteCount != byteCount.toLong()) {
                    fail("Recovery package evidence length does not match its manifest.")
                }
                totalEvidenceBytes = safeAdd(totalEvidenceBytes, byteCount.toLong())
                if (totalEvidenceBytes > MAX_TOTAL_RECEIPT_EVIDENCE_BYTES) {
                    fail("Recovery package evidence exceeds the supported total size.")
                }
                totalPackageBytes = safeAdd(
                    totalPackageBytes,
                    EVIDENCE_RECORD_HEADER_BYTES + byteCount
                )
                if (totalPackageBytes > MAX_PACKAGE_BYTES) {
                    fail("Recovery package exceeds the supported size limit.")
                }

                val bytes = ByteArray(byteCount).also(dataInput::readFully)
                requireDescriptorMatchesBytes(descriptor, bytes)
                evidence += CanonicalReceiptEvidence(descriptor, bytes)
            }

            if (dataInput.read() != -1) fail("Recovery package contains trailing data.")
            return LedgerBackupPackage(payload, evidence)
        } catch (error: InvalidLedgerBackupPackageException) {
            throw error
        } catch (error: EOFException) {
            throw InvalidLedgerBackupPackageException("Recovery package is incomplete or damaged.", error)
        } catch (error: IOException) {
            throw InvalidLedgerBackupPackageException("Recovery package could not be read.", error)
        }
    }

    private fun validateEvidenceSet(
        descriptors: List<LedgerBackupReceiptEvidence>,
        evidence: List<CanonicalReceiptEvidence>
    ): Map<EvidenceKey, CanonicalReceiptEvidence> {
        if (evidence.size != descriptors.size) {
            fail("Backup package evidence count does not match its manifest.")
        }
        val descriptorsByKey = descriptors.associateBy { it.key() }
        val evidenceByKey = linkedMapOf<EvidenceKey, CanonicalReceiptEvidence>()
        var totalEvidenceBytes = 0L
        evidence.forEach { item ->
            val key = item.descriptor.key()
            val expected = descriptorsByKey[key]
                ?: fail("Backup package contains evidence that is not in its manifest.")
            if (evidenceByKey.put(key, item) != null) {
                fail("Backup package contains duplicate evidence records.")
            }
            if (item.descriptor != expected) {
                fail("Backup package evidence descriptor does not match its manifest.")
            }
            if (item.bytes.size !in 1..MAX_RECEIPT_EVIDENCE_BYTES) {
                fail("Backup package evidence length is outside the supported range.")
            }
            totalEvidenceBytes = safeAdd(totalEvidenceBytes, item.bytes.size.toLong())
            if (totalEvidenceBytes > MAX_TOTAL_RECEIPT_EVIDENCE_BYTES) {
                fail("Backup package evidence exceeds the supported total size.")
            }
            requireDescriptorMatchesBytes(expected, item.bytes)
        }
        descriptors.forEach { descriptor ->
            if (descriptor.key() !in evidenceByKey) fail("Backup package is missing receipt evidence.")
        }
        return evidenceByKey
    }

    private fun requireDescriptorMatchesBytes(
        descriptor: LedgerBackupReceiptEvidence,
        bytes: ByteArray
    ) {
        if (descriptor.byteCount != bytes.size.toLong()) {
            fail("Recovery package evidence byte count does not match its descriptor.")
        }
        val actualHash = sha256Hex(bytes)
        if (actualHash != descriptor.sha256) {
            fail("Recovery package evidence hash does not match its descriptor.")
        }
        if (descriptor.id != actualHash.take(EVIDENCE_ID_BYTES)) {
            fail("Recovery package evidence ID does not match its hash.")
        }
    }

    private fun orderedDescriptors(payload: LedgerBackupPayload): List<LedgerBackupReceiptEvidence> =
        payload.events.flatMap(LedgerBackupEvent::receiptEvidence).sortedWith(evidenceOrder)

    private fun LedgerBackupReceiptEvidence.key(): EvidenceKey =
        EvidenceKey(sourceTransactionId, id)

    private fun readEvidenceId(input: DataInputStream): String {
        val bytes = ByteArray(EVIDENCE_ID_BYTES).also(input::readFully)
        if (bytes.any { byte ->
                val value = byte.toInt() and 0xff
                value !in '0'.code..'9'.code && value !in 'a'.code..'f'.code
            }
        ) {
            fail("Recovery package evidence ID is invalid.")
        }
        return String(bytes, StandardCharsets.US_ASCII)
    }

    private fun sha256Hex(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { byte -> "%02x".format(byte) }

    private fun safeAdd(left: Long, right: Long): Long =
        if (left > Long.MAX_VALUE - right) Long.MAX_VALUE else left + right

    private fun safeMultiply(left: Long, right: Long): Long =
        if (left != 0L && right > Long.MAX_VALUE / left) Long.MAX_VALUE else left * right

    private fun fail(message: String): Nothing = throw InvalidLedgerBackupPackageException(message)

    private data class EvidenceKey(
        val sourceTransactionId: Int,
        val id: String
    )

    private class NonClosingOutputStream(output: OutputStream) : FilterOutputStream(output) {
        override fun close() = flush()
    }

    private class PackageBoundedOutputStream(
        output: OutputStream,
        private val maxBytes: Long
    ) : FilterOutputStream(output) {
        private var written = 0L

        override fun write(value: Int) {
            requireCapacity(1)
            out.write(value)
            written++
        }

        override fun write(buffer: ByteArray, offset: Int, length: Int) {
            requireCapacity(length)
            out.write(buffer, offset, length)
            written += length
        }

        private fun requireCapacity(additionalBytes: Int) {
            if (additionalBytes < 0 || written > maxBytes - additionalBytes) {
                throw PackageByteLimitExceededException()
            }
        }
    }

    private class PackageByteLimitExceededException : IOException()
}