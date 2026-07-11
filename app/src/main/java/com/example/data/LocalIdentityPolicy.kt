package com.example.data

private val localIdentityPattern = Regex(
    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
)

fun normalizeLocalIdentity(email: String): String? {
    val normalized = email.trim().lowercase()
    return normalized.takeIf { localIdentityPattern.matches(it) }
}