package com.gz.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

private val RADIUS = 150f.dp
private val INNER_RADIUS = 120f.dp
private val CENTER_RADIUS = 4f.dp
private const val OPEN_ANGLE = 120f

private val DASH_WIDTH = 2f.dp
private val DASH_LENGTH = 8f.dp

private const val GAPS = 20

private const val DURATION = 3000f

class DashboardView(context: Context, attr: AttributeSet? = null) : View(context, attr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private lateinit var pathMeasure: PathMeasure
    private val dash = Path()
    private lateinit var dashPathEffect: PathDashPathEffect
    private var progress = 5f / 20f
    private var startTime = 0L
    private var costTime = 0L

    init {
        paint.strokeWidth = 3f.dp
        paint.style = Paint.Style.STROKE
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CW)

    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addArc(
            width.half - RADIUS,
            height.half - RADIUS,
            width.half + RADIUS,
            height.half + RADIUS,
            90f + OPEN_ANGLE.half,
            360f - OPEN_ANGLE
        )
        pathMeasure = PathMeasure(path, false)
        dashPathEffect =
            PathDashPathEffect(
                dash,
                (pathMeasure.length - DASH_WIDTH) / GAPS,
                0f,
                PathDashPathEffect.Style.ROTATE
            )
        startTime = System.currentTimeMillis()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(path, paint)

        paint.pathEffect = dashPathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        canvas.drawCircle(width.half, height.half, CENTER_RADIUS, paint)

        canvas.drawLine(
            width.half + CENTER_RADIUS * cos(getRadiusByPosition(progress)),
            height.half + CENTER_RADIUS * sin(getRadiusByPosition(progress)),
            width.half + INNER_RADIUS * cos(getRadiusByPosition(progress)),
            height.half + INNER_RADIUS * sin(getRadiusByPosition(progress))
            , paint
        )

        costTime = System.currentTimeMillis() - startTime
        if (costTime < DURATION) {
            progress = costTime / DURATION
            invalidate()
        }
    }

    private fun getRadiusByPosition(progress: Float): Float {
        return Math.toRadians((90 + OPEN_ANGLE / 2f + progress * (360f - OPEN_ANGLE)).toDouble())
            .toFloat()
    }
}