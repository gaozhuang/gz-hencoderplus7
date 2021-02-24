package com.gz.animators

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave

private val IMAGE_SIZE = 200.dp

class CameraView(context: Context, attr: AttributeSet? = null) : View(context, attr) {
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#50777777")
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var topFlip = 0f
        set(value) {
            field = value
            invalidate()
        }

    var bottomFlip = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val camera = Camera().apply {
        setLocation(0f, 0f, -8f * resources.displayMetrics.density)
    }

    var flipRotation = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val bitmap = getAvatar(R.drawable.avatar_rengwuxian, IMAGE_SIZE.toInt())

    override fun onDraw(canvas: Canvas) {
        //上半部分
        canvas.withSave {
            canvas.translate(width.half, height.half)
            canvas.rotate(flipRotation)
            camera.save()
            camera.rotateY(topFlip)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(
                -IMAGE_SIZE,
                -IMAGE_SIZE,
                0f,
                IMAGE_SIZE
            )
            canvas.rotate(-flipRotation)
            canvas.translate(-width.half, -height.half)
            canvas.drawBitmap(
                bitmap,
                width.half - IMAGE_SIZE.half,
                height.half - IMAGE_SIZE.half,
                paint
            )
        }

        // 下半部分
        canvas.withSave {
            canvas.translate(width.half, height.half)
            canvas.rotate(flipRotation)
            camera.save()
            camera.rotateY(bottomFlip)
            camera.applyToCanvas(canvas)
            camera.restore()
            canvas.clipRect(
                0f,
                -IMAGE_SIZE,
                IMAGE_SIZE,
                IMAGE_SIZE
            )
            canvas.rotate(-flipRotation)
            canvas.translate(-width.half, -height.half)
            canvas.drawBitmap(
                bitmap,
                width.half - IMAGE_SIZE.half,
                height.half - IMAGE_SIZE.half,
                paint
            )
        }
    }

    private fun getAvatar(resId: Int, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}