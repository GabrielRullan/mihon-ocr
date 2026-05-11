package eu.kanade.tachiyomi.data.ocr

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.kanade.tachiyomi.util.system.logcat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class OCRWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val chapterPath = inputData.getString("chapter_path") ?: return Result.failure()
        val chapterFolder = File(chapterPath)
        
        if (!chapterFolder.exists() || !chapterFolder.isDirectory) {
            return Result.failure()
        }

        val processor = OCRProcessor(applicationContext)
        val results = mutableMapOf<String, OCRResultData>()

        chapterFolder.listFiles()?.filter { it.extension in listOf("jpg", "jpeg", "png", "webp") }?.forEach { file ->
            processor.processImage(file)?.let {
                results[file.name] = it
            }
        }

        if (results.isNotEmpty()) {
            val json = Json.encodeToString(results)
            File(chapterFolder, "ocr_results.json").writeText(json)
            logcat { "OCR finished for ${chapterFolder.name}" }
        }

        return Result.success()
    }
}
