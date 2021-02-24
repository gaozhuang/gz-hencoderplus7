package com.gz.animators

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.toColorInt

class TextAnimatorActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_animator)

        val view = findViewById<ProvinceTextView>(R.id.province_view)

        findViewById<View>(R.id.fab).setOnClickListener(View.OnClickListener {
            start(view)
        })

        start(view, 500)
    }

    private fun start(view: ProvinceTextView, delay: Int = 0) {
        val positionHolder = PropertyValuesHolder.ofInt("position", 0, PROVINCES.size - 1)
        val textSizeFrame0 = Keyframe.ofFloat(0.0f, 36.dp)
        val textSizeFrame1 = Keyframe.ofFloat(0.5f, 50.dp)
        val textSizeFrame4 = Keyframe.ofFloat(1.0f, 36.dp)
        val textSizeHolder = PropertyValuesHolder.ofKeyframe(
            "textSize",
            textSizeFrame0,
            textSizeFrame1,
            textSizeFrame4
        )
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(view, positionHolder, textSizeHolder).apply {
                duration = 10000
                interpolator = AccelerateDecelerateInterpolator()
                if (delay > 0) {
                    startDelay = delay.toLong()
                }
            }
        animator.start()
    }
}

private val PROVINCES = arrayOf(
    "北京市",
    "天津市",
    "河北省",
    "山西省",
    "内蒙古自治区",
    "辽宁省",
    "吉林省",
    "黑龙江省",
    "上海市",
    "江苏省",
    "浙江省",
    "安徽省",
    "福建省",
    "江西省",
    "山东省",
    "河南省",
    "湖北省",
    "湖南省",
    "广东省",
    "广西壮族自治区",
    "海南省",
    "重庆市",
    "四川省",
    "贵州省",
    "云南省",
    "西藏自治区",
    "陕西省",
    "甘肃省",
    "青海省",
    "宁夏回族自治区",
    "新疆维吾尔自治区",
    "香港特别行政区",
    "澳门特别行政区",
    "台湾省"
)

class ProvinceTextView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 36.dp
        color = "#7B7D83".toColorInt()
    }

    private val fontMetrics = Paint.FontMetrics()

    var position = 0
        set(value) {
            field = value
            invalidate()
        }

    var textSize = 36.dp
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        paint.textSize = textSize
        paint.getFontMetrics(fontMetrics)

        canvas.drawText(
            PROVINCES[position],
            width.half,
            height.half - (fontMetrics.top + fontMetrics.bottom) / 2,
            paint
        )
    }
}