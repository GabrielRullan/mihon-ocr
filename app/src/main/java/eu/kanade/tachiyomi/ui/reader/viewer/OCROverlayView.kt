package eu.kanade.tachiyomi.ui.reader.viewer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import eu.kanade.tachiyomi.data.ocr.OCRBlock
import eu.kanade.tachiyomi.data.ocr.OCRResultData

class OCROverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var ocrData: OCRResultData? = null
    private val paint = Paint().apply {
        color = Color.parseColor("#4000F2FE") // Semi-transparent cyan
        style = Paint.Style.FILL
    }
    private val borderPaint = Paint().apply {
        color = Color.parseColor("#8000F2FE")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    var onBlockTapped: ((OCRBlock) -> Unit)? = null

    fun setOcrData(data: OCRResultData?) {
        ocrData = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val data = ocrData ?: return

        // Note: We need to handle scaling relative to the SubsamplingScaleImageView
        // For simplicity in this prototype, we'll draw based on the raw coordinates
        // and assume the user will handle zoom/pan adjustments later.
        
        data.blocks.forEach { block ->
            if (block.boundingBox.size == 4) {
                val rect = Rect(
                    block.boundingBox[0],
                    block.boundingBox[1],
                    block.boundingBox[2],
                    block.boundingBox[3]
                )
                canvas.drawRect(rect, paint)
                canvas.drawRect(rect, borderPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val data = ocrData ?: return false
            data.blocks.forEach { block ->
                if (block.boundingBox.size == 4) {
                    val rect = Rect(
                        block.boundingBox[0],
                        block.boundingBox[1],
                        block.boundingBox[2],
                        block.boundingBox[3]
                    )
                    if (rect.contains(event.x.toInt(), event.y.toInt())) {
                        onBlockTapped?.invoke(block)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
