package com.example.receipt

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
        if (receipt.amount <= 0.0) {
            add("Amount was not detected.")
        } else if (receipt.amountEvidenceConfidence < MIN_RELIABLE_AMOUNT_EVIDENCE) {
            add(
                if (receipt.amountEvidenceSource == AmountEvidenceSource.USER_ENTERED) {
                    "Confirm the amount you entered against the original receipt."
                } else {
                    "Amount was found only as an unlabelled number. Check it against the original receipt."
                }
            )
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