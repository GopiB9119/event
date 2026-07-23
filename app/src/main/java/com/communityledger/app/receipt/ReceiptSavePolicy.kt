package com.communityledger.app.receipt

const val MIN_RELIABLE_AMOUNT_EVIDENCE = 65

data class ReceiptSaveEligibility(
    val canSave: Boolean,
    val blockingReasons: List<String>
)

fun evaluateReceiptSaveEligibility(
    receipt: ParsedReceipt,
    hasDuplicate: Boolean,
    hasLedgerIdentity: Boolean
): ReceiptSaveEligibility {
    val reasons = buildList {
        if (!receipt.amount.isFinite() || receipt.amount <= 0.0) {
            add("Amount was not detected.")
        } else if (
            receipt.amountEvidenceSource == AmountEvidenceSource.USER_ENTERED ||
            receipt.amountEvidenceSource == AmountEvidenceSource.USER_CONFIRMED
        ) {
            add("Receipt amounts cannot be entered or changed manually. Scan a clearer receipt.")
        } else if (receipt.amountEvidenceConfidence < MIN_RELIABLE_AMOUNT_EVIDENCE) {
            add("Amount evidence is not reliable. Scan a clearer receipt with a labelled amount.")
        }
        if (!receipt.isReceiptLike) add("Payment receipt context was not detected.")
        if (hasDuplicate) add("This receipt may already be saved in this event.")
        if (!hasLedgerIdentity) add("Choose the person or vendor for this ledger entry.")
    }

    return ReceiptSaveEligibility(
        canSave = reasons.isEmpty(),
        blockingReasons = reasons
    )
}