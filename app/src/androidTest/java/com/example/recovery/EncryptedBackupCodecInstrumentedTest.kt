package com.example.recovery

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.util.Arrays
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EncryptedBackupCodecInstrumentedTest {

    @Test
    fun requiredProvidersAndPrivateStagingRoundTripOnDevice() {
        assertTrue(
            "PBKDF2-HMAC-SHA256 and AES-GCM must be available on this Android runtime.",
            EncryptedBackupCodec.isSupported()
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val stagingDirectory = context.cacheDir.resolve("backup-codec-device-test-${System.nanoTime()}")
        assertTrue(stagingDirectory.mkdir())
        val passphrase = "device backup test password".toCharArray()
        val payload = "Community Ledger device recovery test".toByteArray()

        try {
            val archive = EncryptedBackupCodec.encryptBytes(payload, passphrase)
            val restored = EncryptedBackupCodec.decryptBytes(
                archive = archive,
                passphrase = passphrase,
                stagingDirectory = stagingDirectory
            )

            assertArrayEquals(payload, restored)
            assertTrue(stagingDirectory.listFiles().orEmpty().isEmpty())
        } finally {
            Arrays.fill(passphrase, '\u0000')
            stagingDirectory.deleteRecursively()
        }
    }
}