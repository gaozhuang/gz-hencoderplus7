package com.gz.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View

class PathTestView(context: Context, attr: AttributeSet? = null, defType: Int = 0) :
    View(context, attr, defType) {
    private val mRadius = 180f.px

    // 抗锯齿
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private lateinit var pathMeasure: PathMeasure

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        pathMeasure = PathMeasure(path, false)
        path.fillType = Path.FillType.EVEN_ODD
        path.addCircle(width / 2f, height / 2f, mRadius, Path.Direction.CW)
        val baseLeft = width.half() - mRadius
        val baseTop = height.half()
        val baseRight = baseLeft + mRadius.double()
        val baseBottom = baseTop + mRadius.double()
        path.addRect(baseLeft, baseTop, baseRight, baseBottom, Path.Direction.CCW)
        path.addRect(
            baseLeft + 50.pxValue(),
            baseTop + 30.pxValue(),
            width.half() - 50.pxValue(),
            baseTop + 40.pxValue(),
            Path.Direction.CW
        )
        path.addRect(
            width.half() + 50.pxValue(),
            baseTop + 30.pxValue(),
            baseRight - 50.pxValue(),
            baseTop + 40.pxValue(),
            Path.Direction.CW
        )
        path.addArc(
            width.half() - mRadius.half(),
            height.half() + 80f.pxValue(),
            width.half() + mRadius.half(),
            height.half() + 120f.pxValue(),
            45f,
            90f
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}