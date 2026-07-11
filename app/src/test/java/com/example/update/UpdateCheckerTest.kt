package com.example.update

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
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.NotPublished, result)
    }

    @Test
    fun `current official release is up to date`() {
        val result = UpdateChecker.evaluateManifest(validManifest(versionCode = 2), currentVersionCode = 2)

        assertEquals(UpdateCheckResult.UpToDate("0.2.0-beta"), result)
    }

    @Test
    fun `newer official release exposes validated metadata`() {
        val result = UpdateChecker.evaluateManifest(validManifest(versionCode = 3), currentVersionCode = 2)

        assertTrue(result is UpdateCheckResult.Available)
        result as UpdateCheckResult.Available
        assertEquals(3, result.versionCode)
        assertEquals("0.2.0-beta", result.versionName)
        assertEquals(listOf("Integrity and trust updates."), result.releaseNotes)
    }

    @Test
    fun `untrusted download host is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace(
                "https://github.com/GopiB9119/event/releases/download/v0.2.0/community-ledger.apk",
                "https://example.com/community-ledger.apk"
            ),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `insecure download URL is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace("https://github.com", "http://github.com"),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `lookalike GitHub host is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace("github.com", "github.com.example.org"),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `wrong repository download is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace("GopiB9119/event", "GopiB9119/other"),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `non-download release path is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace(
                "/releases/download/v0.2.0/community-ledger.apk",
                "/releases/tag/v0.2.0"
            ),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `path traversal in release URL is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace(
                "community-ledger.apk",
                "..%2F..%2Fissues"
            ),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `malformed checksum is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "not-a-sha256"
            ),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information failed validation."), result)
    }

    @Test
    fun `unsupported manifest schema is rejected`() {
        val result = UpdateChecker.evaluateManifest(
            validManifest(versionCode = 3).replace("\"schemaVersion\": 1", "\"schemaVersion\": 2"),
            currentVersionCode = 2
        )

        assertEquals(UpdateCheckResult.Failed("Release information uses an unsupported format."), result)
    }

    private fun validManifest(versionCode: Int): String = """
        {
          "schemaVersion": 1,
          "available": true,
          "versionCode": $versionCode,
          "versionName": "0.2.0-beta",
          "downloadUrl": "https://github.com/GopiB9119/event/releases/download/v0.2.0/community-ledger.apk",
          "sha256": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
          "releaseNotes": ["Integrity and trust updates."]
        }
    """.trimIndent()
}
