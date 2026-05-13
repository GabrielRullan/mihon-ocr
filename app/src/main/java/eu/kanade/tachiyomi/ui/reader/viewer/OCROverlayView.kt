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
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var ocrData: OCRResultData? = null
    private var imageView: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView? = null

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

    fun setOcrData(data: OCRResultData?, view: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView? = null) {
        ocrData = data
        imageView = view
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val data = ocrData ?: return
        val view = imageView ?: return

        data.blocks.forEach { block ->
            if (block.boundingBox.size == 4) {
                val leftTop = view.sourceToViewCoord(block.boundingBox[0].toFloat(), block.boundingBox[1].toFloat())
                val rightBottom = view.sourceToViewCoord(block.boundingBox[2].toFloat(), block.boundingBox[3].toFloat())

                if (leftTop != null && rightBottom != null) {
                    canvas.drawRect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y, paint)
                    canvas.drawRect(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y, borderPaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val data = ocrData ?: return false
            val view = imageView ?: return false

            data.blocks.forEach { block ->
                if (block.boundingBox.size == 4) {
                    val leftTop = view.sourceToViewCoord(block.boundingBox[0].toFloat(), block.boundingBox[1].toFloat())
                    val rightBottom = view.sourceToViewCoord(block.boundingBox[2].toFloat(), block.boundingBox[3].toFloat())

                    if (leftTop != null && rightBottom != null) {
                        val rect = android.graphics.RectF(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y)
                        if (rect.contains(event.x, event.y)) {
                            onBlockTapped?.invoke(block)
                            return true
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
