package eu.kanade.tachiyomi.data.ocr

import android.content.Context
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
                OCRBlock(
                    text = block.text,
                    boundingBox = block.boundingBox?.let { 
                        listOf(it.left, it.top, it.right, it.bottom) 
                    } ?: emptyList()
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
    val blocks: List<OCRBlock>
)

@Serializable
data class OCRBlock(
    val text: String,
    val boundingBox: List<Int>
)
