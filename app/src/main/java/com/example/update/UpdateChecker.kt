package com.example.update

import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

sealed interface UpdateCheckResult {
    data object Idle : UpdateCheckResult
    data object Checking : UpdateCheckResult
    data object NotPublished : UpdateCheckResult
    data class UpToDate(val latestVersionName: String) : UpdateCheckResult
    data class Available(
        val versionCode: Int,
        val versionName: String,
        val downloadUrl: String,
        val sha256: String,
        val releaseNotes: List<String>
    ) : UpdateCheckResult
    data class Failed(val message: String) : UpdateCheckResult
}

object UpdateChecker {
    const val MANIFEST_URL = "https://gopib9119.github.io/event/releases/latest.json"
    private const val MAX_RESPONSE_BYTES = 128 * 1024
    private val trustedReleaseAssetPath = Regex(
        "^/GopiB9119/event/releases/download/[A-Za-z0-9._-]+/[A-Za-z0-9._-]+\\.apk$"
    )

    suspend fun check(currentVersionCode: Int, currentVersionName: String): UpdateCheckResult =
        withContext(Dispatchers.IO) {
            val connection = (URL(MANIFEST_URL).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5_000
                readTimeout = 5_000
                requestMethod = "GET"
                instanceFollowRedirects = false
                useCaches = false
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "CommunityLedger/$currentVersionName")
            }

            try {
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext UpdateCheckResult.Failed("Release information is unavailable right now.")
                }

                val body = connection.inputStream.bufferedReader().use { reader ->
                    val text = reader.readText()
                    if (text.toByteArray(Charsets.UTF_8).size > MAX_RESPONSE_BYTES) {
                        return@withContext UpdateCheckResult.Failed("Release information was larger than expected.")
                    }
                    text
                }
                evaluateManifest(body, currentVersionCode)
            } catch (e: Exception) {
                UpdateCheckResult.Failed("Could not check for updates. Check your connection and try again.")
            } finally {
                connection.disconnect()
            }
        }

    fun evaluateManifest(jsonText: String, currentVersionCode: Int): UpdateCheckResult {
        return try {
            val manifest = JSONObject(jsonText)
            if (manifest.optInt("schemaVersion", 0) != 1) {
                return UpdateCheckResult.Failed("Release information uses an unsupported format.")
            }
            if (!manifest.optBoolean("available", false)) {
                return UpdateCheckResult.NotPublished
            }

            val versionCode = manifest.optInt("versionCode", 0)
            val versionName = manifest.optString("versionName").trim()
            val downloadUrl = manifest.optString("downloadUrl").trim()
            val sha256 = manifest.optString("sha256").trim().uppercase()
            if (
                versionCode <= 0 ||
                versionName.isBlank() ||
                !isTrustedDownloadUrl(downloadUrl) ||
                !sha256.matches(Regex("[A-F0-9]{64}"))
            ) {
                return UpdateCheckResult.Failed("Release information failed validation.")
            }

            if (versionCode <= currentVersionCode) {
                return UpdateCheckResult.UpToDate(versionName)
            }

            val notesJson = manifest.optJSONArray("releaseNotes")
            val notes = buildList {
                if (notesJson != null) {
                    for (index in 0 until notesJson.length()) {
                        notesJson.optString(index).trim().takeIf { it.isNotBlank() }?.let(::add)
                    }
                }
            }
            UpdateCheckResult.Available(
                versionCode = versionCode,
                versionName = versionName,
                downloadUrl = downloadUrl,
                sha256 = sha256,
                releaseNotes = notes
            )
        } catch (e: Exception) {
            UpdateCheckResult.Failed("Release information could not be read.")
        }
    }

    private fun isTrustedDownloadUrl(value: String): Boolean {
        return try {
            val uri = URI(value)
            uri.scheme.equals("https", ignoreCase = true) &&
                uri.host.equals("github.com", ignoreCase = true) &&
                uri.userInfo == null &&
                uri.port == -1 &&
                uri.rawQuery == null &&
                uri.rawFragment == null &&
                trustedReleaseAssetPath.matches(uri.rawPath.orEmpty())
        } catch (e: Exception) {
            false
        }
    }
}
