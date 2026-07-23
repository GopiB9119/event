package com.communityledger.app.shared

import com.communityledger.app.receipt.AmountEvidenceSource
import com.communityledger.app.receipt.ParsedReceipt
import com.communityledger.app.receipt.evaluateReceiptSaveEligibility
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

data class SharedReceiptPreparation(
    val request: SubmitSharedEntryRequest?,
    val blockingReasons: List<String>
) {
    val canSubmit: Boolean
        get() = request != null && blockingReasons.isEmpty()
}

fun prepareSharedReceiptSubmission(
    receipt: ParsedReceipt,
    eventId: String,
    expectedRevision: Long,
    ledgerPersonUid: String?,
    ledgerType: String,
    idempotencyKey: String = newSharedOperationKey()
): SharedReceiptPreparation {
    val reasons = evaluateReceiptSaveEligibility(
        receipt = receipt,
        hasDuplicate = false,
        hasLedgerIdentity = !ledgerPersonUid.isNullOrBlank()
    ).blockingReasons.toMutableList()
    val amountMinor = receipt.amount.toExactMinorUnitsOrNull()
    if (receipt.amount > 0.0 && amountMinor == null) {
        reasons += "Receipt amount cannot be represented exactly in paise."
    }
    if (receipt.amountEvidenceSource !in SHARED_AMOUNT_EVIDENCE_SOURCES) {
        reasons += "Receipt amount evidence is not supported for a shared ledger."
    }
    if (eventId.isBlank() || expectedRevision <= 0L) {
        reasons += "Shared event state is not ready."
    }
    if (ledgerType !in setOf("Donated", "Credit", "Debit", "Expense")) {
        reasons += "Ledger direction is not supported."
    }

    val paymentReference = receipt.transactionId.trim().takeIf(String::isNotBlank)
    val normalizedReference = paymentReference?.uppercase(Locale.ROOT)?.replace(Regex("[^A-Z0-9]"), "")
    if (paymentReference != null && normalizedReference?.length !in 6..64) {
        reasons += "Payment reference is not strong enough for shared duplicate protection."
    }
    val paymentDate = normalizeReceiptDate(receipt.date)
    val paymentApp = receipt.paymentApp.takeUnless { it == "Unknown UPI" }?.trim()?.takeIf(String::isNotBlank)
    val counterparty = receipt.counterpartyName.trim().ifBlank { receipt.upiId.trim() }
        .takeIf(String::isNotBlank)
    if (paymentReference == null && listOf(paymentDate, paymentApp, counterparty).count { it != null } < 2) {
        reasons += "A receipt without a payment reference needs two fallback evidence fields."
    }

    val distinctReasons = reasons.distinct()
    if (distinctReasons.isNotEmpty() || amountMinor == null || ledgerPersonUid.isNullOrBlank()) {
        return SharedReceiptPreparation(request = null, blockingReasons = distinctReasons)
    }

    val warnings = buildList {
        addAll(receipt.validationWarnings)
        if (receipt.date.isNotBlank() && paymentDate == null) {
            add("Payment date could not be normalized for shared storage.")
        }
    }.distinct().take(20).map { it.take(200) }
    return SharedReceiptPreparation(
        request = SubmitSharedEntryRequest(
            idempotencyKey = idempotencyKey,
            eventId = eventId,
            ledgerPersonUid = ledgerPersonUid,
            ledgerType = ledgerType,
            amountMinor = amountMinor,
            amountEvidenceSource = receipt.amountEvidenceSource.name,
            amountEvidenceConfidence = receipt.amountEvidenceConfidence,
            expectedRevision = expectedRevision,
            paymentReference = paymentReference,
            paymentDate = paymentDate,
            paymentApp = paymentApp,
            counterparty = counterparty,
            confidence = receipt.confidence,
            warnings = warnings
        ),
        blockingReasons = emptyList()
    )
}

private fun Double.toExactMinorUnitsOrNull(): Long? {
    if (!isFinite() || this <= 0.0) return null
    return try {
        val minor = BigDecimal.valueOf(this)
            .setScale(2, RoundingMode.UNNECESSARY)
            .movePointRight(2)
            .longValueExact()
        minor.takeIf { it in 1..MAX_SERVER_SAFE_INTEGER }
    } catch (error: ArithmeticException) {
        null
    }
}

internal fun normalizeReceiptDate(value: String): String? {
    val clean = value.trim()
    if (clean.isEmpty()) return null
    val patterns = listOf(
        "yyyy-MM-dd",
        "dd MMMM yyyy",
        "dd MMM yyyy",
        "dd/MM/yyyy",
        "dd-MM-yyyy",
        "dd.MM.yyyy"
    )
    patterns.forEach { pattern ->
        val parser = SimpleDateFormat(pattern, Locale.ENGLISH).apply { isLenient = false }
        val position = ParsePosition(0)
        val parsed = parser.parse(clean, position)
        if (parsed != null && position.index == clean.length) {
            return SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(parsed)
        }
    }
    return null
}

private const val MAX_SERVER_SAFE_INTEGER = 9_007_199_254_740_991L
private val SHARED_AMOUNT_EVIDENCE_SOURCES = setOf(
    AmountEvidenceSource.CURRENCY_MARKED,
    AmountEvidenceSource.AMOUNT_LABEL,
    AmountEvidenceSource.NEAR_AMOUNT_LABEL,
    AmountEvidenceSource.PARTY_LINE_AMOUNT,
    AmountEvidenceSource.TOP_RECEIPT_VALUE
)