package com.communityledger.app.data

import java.util.Locale

fun normalizeMemberName(name: String): String {
    return name.trim()
        .lowercase(Locale.ROOT)
        .replace(Regex("\\s+"), " ")
}