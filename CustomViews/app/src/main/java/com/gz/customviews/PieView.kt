package com.gz.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

private val RADIUS = 150f.dp
private val OFFSET_LENGTH = 20f.dp

private const val INIT_START_ANGLE = -90f

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

private const val TAG = "PieView"

class PieView(context: Context, attr: AttributeSet? = null) : View(context, attr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        textSize = 14.dp
        typeface = Typeface.DEFAULT
    }

    private var pointX = 0f
    private var pointY = 0f
    private var clickIndex = -1

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        var startAngle = INIT_START_ANGLE
        for ((index, angle) in ANGLES.withIndex()) {
            if (index == clickIndex) {
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
            if (index == clickIndex) {
                canvas.restore()
            }
            startAngle += angle
        }

        paint.color = Color.BLACK
        canvas.drawText(
            "x:${pointX - width.half}, y:${pointY - height.half}",
            width.half - RADIUS,
            height.half - RADIUS,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            pointX = event.x
            pointY = event.y
            if (isInCircle(pointX, pointY)) {
                clickIndex = getIndex(getDegree(pointX, pointY))
                postInvalidate()
            }
        }
        return false
    }

    private fun isInCircle(x: Float, y: Float): Boolean {
        return (x - width.half).toDouble().pow(2.toDouble()) + (y - height.half).toDouble()
            .pow(2.toDouble()) < RADIUS.toDouble()
            .pow(2.toDouble())
    }

    private fun getDegree(x: Float, y: Float): Float {
        val xDistance = x - width.half
        val yDistance = y - height.half
        var degree: Float
        if (xDistance.toInt() == 0) {
            degree = if (yDistance > 0) {
                90f
            } else {
                270f
            }
        } else {
            degree = Math.toDegrees(atan(((yDistance / xDistance).toDouble()))).toFloat()
            if (xDistance < 0) {
                degree += 180f
            }
        }
        degree = (degree + 360f) % 360f
        Log.w(TAG, "x:${x}, y=${y}")
        Log.w(TAG, " degree=${degree}")
        return degree
    }

    private fun getIndex(touchDegree: Float): Int {
        var realDegreeInPie = touchDegree - INIT_START_ANGLE
        if (realDegreeInPie < 0) {
            realDegreeInPie += 360f
        }
        if (realDegreeInPie >= 360f) {
            realDegreeInPie -= 360f
        }
        var degree = 0f
        for ((index, angle) in ANGLES.withIndex()) {
            if (realDegreeInPie >= degree && realDegreeInPie < degree + angle) {
                return index
            }
            degree += angle
        }
        return -1
    }
}