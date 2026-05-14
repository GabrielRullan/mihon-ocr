package eu.kanade.tachiyomi.data.ocr

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

import android.content.Context

class TranslationProcessor(private val context: Context) {

    init {
        com.google.mlkit.common.sdkinternal.MlKitContext.initializeIfNeeded(context)
    }

    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.CHINESE)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    private val translator by lazy {
        Translation.getClient(options)
    }

    suspend fun translate(text: String): String {
        return try {
            ensureModelDownloaded()
            translator.translate(text).await()
        } catch (e: Exception) {
            "Translation Error: ${e.message}"
        }
    }

    private suspend fun ensureModelDownloaded() {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions).await()
    }
}
