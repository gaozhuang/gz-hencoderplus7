package com.gz.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

private val IMAGE_SIZE = 300.dp

private val RING_WIDTH = 10.dp

private val DST_IN = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

class RoundImageView(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val ringBitmap =
        Bitmap.createBitmap(IMAGE_SIZE.toInt(), IMAGE_SIZE.toInt(), Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            paint.color = Color.BLACK
            canvas.drawOval(
                0f + RING_WIDTH.half,
                0f + RING_WIDTH.half,
                IMAGE_SIZE - RING_WIDTH.half,
                IMAGE_SIZE - RING_WIDTH.half,
                paint
            )
        }

    override fun onDraw(canvas: Canvas) {
        paint.reset()
        val count = canvas.saveLayer(
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            width.half + IMAGE_SIZE.half,
            height.half + IMAGE_SIZE.half,
            null
        )

        canvas.drawBitmap(
            getAvatar(IMAGE_SIZE.toInt()),
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            paint
        )
        paint.xfermode = DST_IN

        canvas.drawBitmap(
            ringBitmap, width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            paint
        )
        paint.xfermode = null

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = RING_WIDTH
        canvas.drawOval(
            width.half - IMAGE_SIZE.half  + RING_WIDTH.half,
            height.half - IMAGE_SIZE.half + RING_WIDTH.half,
            width.half + IMAGE_SIZE.half - RING_WIDTH.half,
            height.half + IMAGE_SIZE.half - RING_WIDTH.half,
            paint
        )

        canvas.restoreToCount(count)
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