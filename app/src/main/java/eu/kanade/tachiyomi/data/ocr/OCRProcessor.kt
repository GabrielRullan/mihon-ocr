package eu.kanade.tachiyomi.data.ocr

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.io.File

class OCRProcessor(private val context: Context) {

    private val recognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    suspend fun processImage(file: File): OCRResultData? {
        val image = InputImage.fromFilePath(context, Uri.fromFile(file))
        return try {
            val result = recognizer.process(image).await()
            val textBlocks = result.textBlocks.map { block ->
                val box = block.boundingBox
                OCRBlock(
                    text = block.text,
                    boundingBox = if (box != null) {
                        listOf(box.left, box.top, box.right, box.bottom)
                    } else {
                        emptyList()
                    },
                )
            }
            OCRResultData(textBlocks)
        } catch (e: Exception) {
            null
        }
    }
}

@Serializable
data class OCRResultData(
    val blocks: List<OCRBlock>,
)

@Serializable
data class OCRBlock(
    val text: String,
    val boundingBox: List<Int>,
)
