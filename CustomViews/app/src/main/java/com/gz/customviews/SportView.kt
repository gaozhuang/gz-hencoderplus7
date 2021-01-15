package com.gz.customviews

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

private val RADIUS = 150.dp

private val TEXT_SIZE = 50.dp

private const val COLOR_BACKGROUND = Color.GRAY

private val COLOR_FOREGROUND = Color.parseColor("#FA7755")

private val COLOR_TEXT = Color.parseColor("#77BB11")

private const val MAX_DISTANCE = 2000

class SportView(context: Context, attr: AttributeSet? = null) : View(context, attr) {
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 15.dp
    }

    private var distance: Int = 1000

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = COLOR_TEXT
        textAlign = Paint.Align.CENTER
    }

    private val fontMetrics = Paint.FontMetrics()

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#50777777")
    }

    private val animator = ObjectAnimator.ofInt(this, "distance", 1000, 1050, 1250, 1300).apply {
        duration = 10000
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.RESTART
    }

    init {
        animator.start()
    }

    private fun getDistance(): Int {
        return distance
    }

    private fun setDistance(d: Int) {
        distance = d
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        drawLine(canvas)
        drawRing(canvas)
        drawSportData(canvas)
    }

    private fun drawRing(canvas: Canvas) {
        ringPaint.color = COLOR_BACKGROUND
        canvas.drawCircle(width.half, height.half, RADIUS, ringPaint)

        ringPaint.color = COLOR_FOREGROUND
        val angle = distance * 360f / MAX_DISTANCE
        canvas.drawArc(
            width.half - RADIUS,
            height.half - RADIUS,
            width.half + RADIUS,
            height.half + RADIUS,
            -90f,
            angle,
            false,
            ringPaint
        )
    }

    private fun drawLine(canvas: Canvas) {
        canvas.drawLine(
            width.half - RADIUS - 20.dp,
            height.half,
            width.half + RADIUS + 20.dp,
            height.half,
            linePaint
        )
        canvas.drawLine(
            width.half,
            height.half - RADIUS - 20.dp,
            width.half,
            height.half + RADIUS + 20.dp,
            linePaint
        )
    }

    private fun drawSportData(canvas: Canvas) {
        textPaint.getFontMetrics(fontMetrics)
        canvas.drawText(
            getSportString(),
            width.half,
            height.half - (fontMetrics.ascent + fontMetrics.descent).half,
            textPaint
        )
    }

    private fun getSportString(): String {
        return "行走${distance}米"
    }
}