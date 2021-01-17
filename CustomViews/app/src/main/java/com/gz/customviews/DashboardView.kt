package com.gz.customviews

import android.content.Context
import android.graphics.*
import android.text.TextPaint
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

private val TEXT_SIZE = 10.dp

private val TEXT_VERTICAL_OFFSET = 5.dp

private const val GAPS = 20

private const val DURATION = 3000f

class DashboardView(context: Context, attr: AttributeSet? = null) : View(context, attr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private lateinit var pathMeasure: PathMeasure

    private lateinit var dashPathEffect: PathDashPathEffect
    private val dashEffectPath = Path()

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = TEXT_SIZE
        color = Color.GRAY
    }
    private val fontMetrics = Paint.FontMetrics()

    private var progress = 5f / 20f
    private var startTime = 0L
    private var costTime = 0L

    init {
        paint.strokeWidth = 3f.dp
        paint.style = Paint.Style.STROKE
        dashEffectPath.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        // 设置圆弧path
        path.addArc(
            width.half - RADIUS,
            height.half - RADIUS,
            width.half + RADIUS,
            height.half + RADIUS,
            90f + OPEN_ANGLE.half,
            360f - OPEN_ANGLE
        )
        // 测量圆弧path
        pathMeasure = PathMeasure(path, false)
        // 设置刻度pathEffect
        dashPathEffect =
            PathDashPathEffect(
                dashEffectPath,
                (pathMeasure.length - DASH_WIDTH) / GAPS,
                0f,
                PathDashPathEffect.Style.ROTATE
            )
        startTime = System.currentTimeMillis()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画表盘圆弧
        canvas.drawPath(path, paint)

        // 画刻度
        paint.pathEffect = dashPathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        // 画文字
        drawText4(canvas)

        // 中心小圆
        canvas.drawCircle(width.half, height.half, CENTER_RADIUS, paint)

        // 指针
        canvas.drawLine(
            width.half + CENTER_RADIUS * cos(getRadiusByPosition(progress)),
            height.half + CENTER_RADIUS * sin(getRadiusByPosition(progress)),
            width.half + INNER_RADIUS * cos(getRadiusByPosition(progress)),
            height.half + INNER_RADIUS * sin(getRadiusByPosition(progress))
            , paint
        )

        // 动画
        costTime = System.currentTimeMillis() - startTime
        if (costTime < DURATION) {
            progress = costTime / DURATION
            invalidate()
        }
    }

    /**
     * 正向思考：在坐标系的某一点写字，然后不停旋转坐标系，再写字
     * （时刻精准把握坐标系的状态）
     */
    private fun drawText1(canvas: Canvas) {
        val stepDegree = (360f - OPEN_ANGLE) / GAPS
        textPaint.getFontMetrics(fontMetrics)
        // TODO x, y 需要根据角度和cos sin函数计算出来
        val x = width.half - RADIUS - TEXT_VERTICAL_OFFSET - fontMetrics.bottom
        val y = height.half
        canvas.save()
        for (i in 0..GAPS) {
            canvas.rotate(-90f, x, y)
            canvas.drawText((5 * i).toString(), x, y, textPaint)
            canvas.rotate(90f, x, y)
            canvas.rotate(stepDegree, width.half, height.half)
        }
        canvas.restore()
    }

    /**
     * 逆向思考：1.在正上方画一个文字，2.将文字逆向旋转一个角度到指定位置，3.恢复现场
     */
    private fun drawText2(canvas: Canvas) {
        val degreeOffset = -(360f - OPEN_ANGLE) / 2
        val stepDegree = (360f - OPEN_ANGLE) / GAPS
        textPaint.getFontMetrics(fontMetrics)
        val x = width.half
        val y = height.half - RADIUS - TEXT_VERTICAL_OFFSET - fontMetrics.bottom
        for (i in 0..GAPS) {
            canvas.rotate(degreeOffset + i * stepDegree, width.half, height.half)
            canvas.drawText((5 * i).toString(), x, y, textPaint)
            canvas.rotate(-(degreeOffset + i * stepDegree), width.half, height.half)
        }
    }

    /**
     * 混合版：第一个文字绘制逆向思考，之后的文字在第一个的基础上正向思考
     */
    private fun drawText3(canvas: Canvas) {
        val stepDegree = (360f - OPEN_ANGLE) / GAPS
        textPaint.getFontMetrics(fontMetrics)
        val x = width.half
        val y = height.half - RADIUS - TEXT_VERTICAL_OFFSET - fontMetrics.bottom
        canvas.save()
        canvas.rotate(-(360f - OPEN_ANGLE) / 2, width.half, height.half)
        for (i in 0..GAPS) {
            canvas.drawText((5 * i).toString(), x, y, textPaint)
            canvas.rotate(stepDegree, width.half, height.half)
        }
        canvas.restore()
    }

    /**
     * Matrix版(post方法类似反向思维，prev方法类似正向思维)
     */
    private fun drawText4(canvas: Canvas) {
        val matrix = Matrix()
        val stepDegree = (360f - OPEN_ANGLE) / GAPS
        textPaint.getFontMetrics(fontMetrics)
        val x = width.half
        val y = height.half - RADIUS - TEXT_VERTICAL_OFFSET - fontMetrics.bottom
        canvas.save()
        matrix.postRotate(-(360f - OPEN_ANGLE).half, width.half, height.half)
        for (i in 0..GAPS) {
            canvas.setMatrix(matrix)
            canvas.drawText((5 * i).toString(), x, y, textPaint)
            matrix.postRotate(stepDegree, width.half, height.half)
        }
        canvas.restore()
    }

    private fun getRadiusByPosition(progress: Float): Float {
        return Math.toRadians((90 + OPEN_ANGLE / 2f + progress * (360f - OPEN_ANGLE)).toDouble())
            .toFloat()
    }
}