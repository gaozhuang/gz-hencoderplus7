package com.gz.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

private val IMAGE_SIZE = 300.dp

private val STROKE_WIDTH = 10.dp

private val SRC_IN = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

class RoundImageView(context: Context, attr: AttributeSet? = null) :
    View(context, attr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    override fun onDraw(canvas: Canvas) {
        val count = canvas.saveLayer(
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            width.half + IMAGE_SIZE.half,
            height.half + IMAGE_SIZE.half,
            null
        )

        canvas.drawOval(
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            width.half + IMAGE_SIZE.half,
            height.half + IMAGE_SIZE.half,
            paint
        )
        paint.xfermode = SRC_IN

        canvas.drawBitmap(
            getAvatar(IMAGE_SIZE.toInt()),
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            paint
        )
        paint.xfermode = null

        canvas.restoreToCount(count)

        canvas.drawOval(
            width.half - IMAGE_SIZE.half + STROKE_WIDTH.half,
            height.half - IMAGE_SIZE.half + STROKE_WIDTH.half,
            width.half + IMAGE_SIZE.half - STROKE_WIDTH.half,
            height.half + IMAGE_SIZE.half - STROKE_WIDTH.half,
            borderPaint
        )
    }

    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }
}