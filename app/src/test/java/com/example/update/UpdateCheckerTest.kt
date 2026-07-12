package com.example.update

import com.example.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UpdateCheckerTest {

    @Test
    fun `unpublished channel is reported without a download`() {
        val result = UpdateChecker.evaluateManifest(
            jsonText = """{"schemaVersion":1,"available":false}""",
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.NotPublished, result)
    }

    @Test
    fun `current official release is up to date`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.UpToDate(BuildConfig.VERSION_NAME), result)
    }

    @Test
    fun `newer official release exposes validated metadata`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest(),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertTrue(result is UpdateCheckResult.Available)
        result as UpdateCheckResult.Available
        assertEquals(BuildConfig.VERSION_CODE + 1, result.versionCode)
        assertEquals(NEWER_VERSION_NAME, result.versionName)
        assertEquals(listOf("Integrity and trust updates."), result.releaseNotes)
    }

    @Test
    fun `untrusted download host is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace("https://github.com", "https://example.com"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `insecure download URL is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace("https://github.com", "http://github.com"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `lookalike GitHub host is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace("github.com", "github.com.example.org"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `wrong repository download is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace("GopiB9119/event", "GopiB9119/other"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `non-download release path is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace(
                "/releases/download/v$NEWER_VERSION_NAME/community-ledger-$NEWER_VERSION_NAME.apk",
                "/releases/tag/v$NEWER_VERSION_NAME"
            ),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `path traversal in release URL is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace(
                "community-ledger-$NEWER_VERSION_NAME.apk",
                "..%2F..%2Fissues"
            ),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `malformed checksum is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace(VALID_SHA256, "not-a-sha256"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `unsupported manifest schema is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            newerManifest().replace("\"schemaVersion\": 1", "\"schemaVersion\": 2"),
            currentVersionCode = BuildConfig.VERSION_CODE
        )

        assertEquals(UpdateCheckResult.Failed("Release information uses an unsupported format."), result)
    }

    private fun newerManifest(): String = validManifest(
        versionCode = BuildConfig.VERSION_CODE + 1,
        versionName = NEWER_VERSION_NAME
    )

    private fun validManifest(versionCode: Int, versionName: String): String = """
        {
          "schemaVersion": 1,
          "available": true,
          "versionCode": $versionCode,
          "versionName": "$versionName",
          "downloadUrl": "https://github.com/GopiB9119/event/releases/download/v$versionName/community-ledger-$versionName.apk",
          "sha256": "$VALID_SHA256",
          "releaseNotes": ["Integrity and trust updates."]
        }
    """.trimIndent()

    private companion object {
        const val NEWER_VERSION_NAME = "0.2.0-beta.2"
        const val VALID_SHA256 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    }
}
