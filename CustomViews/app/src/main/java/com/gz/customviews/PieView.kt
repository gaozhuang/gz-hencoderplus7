package com.gz.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

private const val CLICK_INDEX = 3

private val RADIUS = 150f.px
private val OFFSET_LENGTH = 20f.px

private val ANGLES = floatArrayOf(50f, 90f, 60f, 40f, 50f, 10f, 60f)
private val COLORS = arrayOf(
    Color.RED,
    Color.parseColor("#FF9900"),
    Color.YELLOW,
    Color.GREEN,
    Color.CYAN,
    Color.BLUE,
    Color.parseColor("#9900FF")
)

class PieView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        var startAngle = -90f
        for ((index, angle) in ANGLES.withIndex()) {
            if (index == CLICK_INDEX) {
                canvas.save()
                canvas.translate(
                    OFFSET_LENGTH * cos((startAngle + angle.half).toRadius),
                    OFFSET_LENGTH * sin((startAngle + angle.half).toRadius)
                )
            }
            paint.color = COLORS[index]
            canvas.drawArc(
                width.half - RADIUS,
                height.half - RADIUS,
                width.half + RADIUS,
                height.half + RADIUS,
                startAngle,
                angle,
                true,
                paint
            )
            if (index == CLICK_INDEX) {
                canvas.restore()
            }
            startAngle += angle
        }
    }
}