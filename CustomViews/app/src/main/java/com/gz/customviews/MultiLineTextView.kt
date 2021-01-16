package com.gz.customviews

import android.content.Context
import android.graphics.*
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

private val IMAGE_SIZE = 120.dp

private val IMAGE_TOP = 80.dp

private val PADDING = 8.dp

class MultiLineTextView(context: Context, attr: AttributeSet? = null) :
    View(context, attr) {
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
        color = Color.BLACK
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
        textAlign = Paint.Align.LEFT
        color = Color.BLACK
    }

    private val fontMetrics = Paint.FontMetrics()

    private val floatArray = floatArrayOf(0f)

    private val bitmap: Bitmap = getBitmap()

    private val text =
        "Contrary to popular belief, Lorem Ipsum is not simply random text. " +
                "It has roots in a piece of classical Latin literature from 45 BC, " +
                "making it over 2000 years old. Richard McClintock, a Latin professor " +
                "at Hampden-Sydney College in Virginia, looked up one of the more obscure " +
                "Latin words, consectetur, from a Lorem Ipsum passage, and going through " +
                "the cites of the word in classical literature, discovered the undoubtable " +
                "source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus " +
                "Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in " +
                "45 BC. This book is a treatise on the theory of ethics, very popular during " +
                "the Renaissance."

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, width - IMAGE_SIZE - PADDING, IMAGE_TOP, paint)
//        drawTextByStaticLayout(canvas)
        drawTextBySelf(canvas)
    }

    private fun drawTextByStaticLayout(canvas: Canvas) {
        val staticLayout =
            StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                textPaint,
                width
            ).build()
        staticLayout.draw(canvas)
    }

    private fun drawTextBySelf(canvas: Canvas) {
        paint.getFontMetrics(fontMetrics)
        var start = 0
        var count: Int
        var verticalPadding = 0 - fontMetrics.top
        while (start < text.length) {
            count = paint.breakText(
                text, start, text.length,
                true,
                if (verticalPadding + fontMetrics.bottom >= IMAGE_TOP && verticalPadding + fontMetrics.top <= IMAGE_TOP + IMAGE_SIZE) {
                    width - IMAGE_SIZE - PADDING - 2 * PADDING
                } else {
                    width.toFloat() - 2 * PADDING
                },
                floatArray
            )
            canvas.drawText(
                text,
                start,
                start + count,
                PADDING,
                verticalPadding,
                paint
            )
            verticalPadding += paint.fontSpacing
            start += count
        }

    }

    private fun getBitmap(): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = IMAGE_SIZE.toInt()
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}