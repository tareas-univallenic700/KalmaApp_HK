
package ni.univalle.kalma.ui.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import ni.univalle.kalma.R

class SimpleLineChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.chart_axis)
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.chart_line)
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.chart_axis)
        textSize = 28f
    }

    private var values: List<Float> = emptyList()
    private var labels: List<String> = emptyList()
    private var minY: Float = 0f
    private var maxY: Float = 4f

    fun setData(values: List<Float>, labels: List<String>, minY: Float = 0f, maxY: Float = 4f) {
        this.values = values
        this.labels = labels
        this.minY = minY
        this.maxY = maxY
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = paddingLeft + 48f
        val right = width - paddingRight - 16f
        val top = paddingTop + 16f
        val bottom = height - paddingBottom - 48f

        canvas.drawLine(left, top, left, bottom, axisPaint)
        canvas.drawLine(left, bottom, right, bottom, axisPaint)

        if (values.isEmpty()) return

        val count = values.size
        val stepX = if (count > 1) (right - left) / (count - 1) else 0f
        val range = (maxY - minY).takeIf { it != 0f } ?: 1f

        labels.forEachIndexed { i, s ->
            val x = left + stepX * i
            canvas.drawText(s, x - textPaint.measureText(s) / 2, bottom + 28f, textPaint)
        }

        val path = Path()
        for (i in 0 until count) {
            val v = values[i]
            if (v.isNaN()) continue
            val x = left + stepX * i
            val y = bottom - ((v - minY) / range) * (bottom - top)
            if (path.isEmpty) path.moveTo(x, y) else path.lineTo(x, y)
        }
        canvas.drawPath(path, linePaint)
    }
}
