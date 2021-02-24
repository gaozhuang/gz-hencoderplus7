package com.gz.animators

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.util.AttributeSet
import android.view.View

class PointAnimatorActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_animator)
        val view = findViewById<PointView>(R.id.point_view)
        findViewById<View>(R.id.fab).setOnClickListener(View.OnClickListener {
            start(view)
        })
        start(view, 500)
    }

    private fun start(view: PointView, delay: Int = 0) {
        ObjectAnimator.ofObject(
            view,
            "point",
            PointEvaluator(),
            PointF(0f, 0f),
            PointF(200.dp, 350.dp)
        ).also {
            it.duration = 2000
            if (delay > 0) {
                it.startDelay = delay.toLong()
            }
            it.start()
        }
    }
}

class PointEvaluator : TypeEvaluator<PointF> {
    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        val x = startValue.x + (endValue.x - startValue.x) * fraction
        val y = startValue.y + (endValue.y - startValue.y) * fraction
        return PointF(x, y)
    }
}

private val STROKE_WIDTH = 20.dp

class PointView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    var point: PointF = PointF(0.dp, 0.dp)
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(point.x, point.y, paint)
    }
}