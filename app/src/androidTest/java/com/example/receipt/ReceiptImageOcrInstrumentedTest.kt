package com.example.receipt

import android.app.Application
import android.content.res.AssetManager
import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ui.EventViewModel
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReceiptImageOcrInstrumentedTest {
    @Test
    fun extractsOcrFromPrivateReceiptImages() = runBlocking {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val sourceContext = instrumentation.context
        val targetContext = instrumentation.targetContext
        val assetImageNames = sourceContext.assets.list(PRIVATE_ASSET_DIR).orEmpty()
            .filter { name -> name.substringAfterLast('.', "").lowercase() in IMAGE_EXTENSIONS }
            .sorted()

        val externalImageDir = File(targetContext.getExternalFilesDir(null), PRIVATE_ASSET_DIR)
        val externalImageFiles = externalImageDir.listFiles().orEmpty()
            .filter { file -> file.isFile && file.extension.lowercase() in IMAGE_EXTENSIONS }
            .sortedBy { file -> file.name }

        val imageInputs = mutableListOf<ReceiptImageInput>()
        assetImageNames.forEach { imageName ->
            imageInputs += ReceiptImageInput(
                name = "asset-$imageName",
                file = copyAssetToCache(
                    assetManager = sourceContext.assets,
                    assetPath = "$PRIVATE_ASSET_DIR/$imageName",
                    target = File(targetContext.cacheDir, "receipt-ocr-asset-$imageName")
                )
            )
        }
        externalImageFiles.forEach { imageFile ->
            imageInputs += ReceiptImageInput(name = "device-${imageFile.name}", file = imageFile)
        }

        assumeTrue(
            "No private receipt images found. Put real receipt screenshots in app/src/androidTest/assets/$PRIVATE_ASSET_DIR/ or push them to ${externalImageDir.absolutePath}. Do not commit them.",
            imageInputs.isNotEmpty()
        )

        val application = targetContext.applicationContext as Application
        val viewModel = EventViewModel(application)
        val reportRoot = targetContext.getExternalFilesDir(null) ?: targetContext.filesDir
        val reportDir = File(reportRoot, "receipt-ocr-test-output").apply { mkdirs() }

        imageInputs.forEach { imageInput ->
            val parsed = viewModel.extractReceiptFromUri(targetContext, Uri.fromFile(imageInput.file))
            val report = JSONObject()
                .put("image", imageInput.name)
                .put("amount", parsed.amount.takeIf { it > 0.0 } ?: JSONObject.NULL)
                .put("transactionId", parsed.transactionId.ifBlank { JSONObject.NULL })
                .put("paymentApp", parsed.paymentApp.takeIf { it != "Unknown UPI" } ?: JSONObject.NULL)
                .put("payeeName", parsed.payeeName.ifBlank { JSONObject.NULL })
                .put("upiId", parsed.upiId.ifBlank { JSONObject.NULL })
                .put("date", parsed.date.ifBlank { JSONObject.NULL })
                .put("confidence", parsed.confidence)
                .put("extractionMethod", parsed.extractionMethod)
                .put("warnings", JSONArray(parsed.validationWarnings))
                .put("rawTextPreview", parsed.rawTextPreview)

            val reportFile = File(reportDir, "${imageInput.name.safeReportName()}.json")
            reportFile.writeText(report.toString(2))

            Log.i(TAG, "OCR report for ${imageInput.name} written to ${reportFile.absolutePath}\n${report.toString(2)}")
            println("OCR report for ${imageInput.name} written to ${reportFile.absolutePath}")
            println(report.toString(2))

            assertTrue(
                "ML Kit returned no OCR text for ${imageInput.name}. Report: ${reportFile.absolutePath}",
                parsed.rawTextPreview.isNotBlank()
            )
            assertEquals(
                "Unexpected extraction method for ${imageInput.name}. Report: ${reportFile.absolutePath}",
                "On-device OCR (ML Kit Latin + Devanagari)",
                parsed.extractionMethod
            )
            assertTrue(
                "Amount did not pass the receipt save gate for ${imageInput.name}. Report: ${reportFile.absolutePath}",
                parsed.amount > 0.0
            )
            assertTrue(
                "Confidence did not pass the receipt save gate for ${imageInput.name}. Report: ${reportFile.absolutePath}",
                parsed.confidence >= MIN_SAFE_RECEIPT_CONFIDENCE
            )
            assertTrue(
                "Receipt has no reference, UPI ID, or counterparty evidence for ${imageInput.name}. Report: ${reportFile.absolutePath}",
                parsed.transactionId.isNotBlank() || parsed.upiId.isNotBlank() || parsed.payeeName.isNotBlank()
            )
        }
    }

    private data class ReceiptImageInput(
        val name: String,
        val file: File
    )

    private fun copyAssetToCache(assetManager: AssetManager, assetPath: String, target: File): File {
        target.parentFile?.mkdirs()
        assetManager.open(assetPath).use { input ->
            target.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return target
    }

    private fun String.safeReportName(): String {
        return replace(Regex("[^A-Za-z0-9._-]"), "_")
    }

    private companion object {
        const val TAG = "ReceiptImageOcrTest"
        const val PRIVATE_ASSET_DIR = "receipt-images-private"
        const val MIN_SAFE_RECEIPT_CONFIDENCE = 60
        val IMAGE_EXTENSIONS = setOf("png", "jpg", "jpeg", "webp")
    }
}