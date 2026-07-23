package com.communityledger.app.receipt

import com.communityledger.app.data.normalizeLocalIdentity
import java.util.Locale

data class ReceiptLedgerIdentity(
    val personName: String,
    val personEmail: String,
    val source: String
)

fun defaultReceiptLedgerPersonName(uploaderEmail: String): String {
    val localPart = normalizeLocalIdentity(uploaderEmail)
        ?.substringBefore("@")
        .orEmpty()

    return localPart
        .split(Regex("[._-]+"))
        .filter { it.isNotBlank() }
        .joinToString(" ") { part ->
            part.replaceFirstChar { character ->
                if (character.isLowerCase()) character.titlecase(Locale.getDefault()) else character.toString()
            }
        }
}

fun resolveNewReceiptLedgerIdentity(
    personName: String,
    uploaderEmail: String,
    isUploaderThePerson: Boolean
): ReceiptLedgerIdentity? {
    val cleanName = personName.trim()
    if (cleanName.isBlank()) return null

    val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail)
    if (isUploaderThePerson && cleanUploaderEmail == null) return null

    return ReceiptLedgerIdentity(
        personName = cleanName,
        personEmail = if (isUploaderThePerson) cleanUploaderEmail.orEmpty() else "",
        source = if (isUploaderThePerson) "Confirmed local identity" else "User-entered during review"
    )
}