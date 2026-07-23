package com.communityledger.app.shared

import android.content.Context

object SharedBackendProvider {
    fun create(context: Context): SharedBackend = FirebaseEmulatorSharedBackend.create(context)
}