package com.gz.customviews

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan
import kotlin.math.sqrt

private val IMAGE_SIZE = 200.dp

class CameraView(context: Context, attr: AttributeSet? = null) : View(context, attr) {
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#50777777")
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var cameraDegree = -30f

    private val camera = Camera()

    private var rotateDegree = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val animator = ObjectAnimator.ofFloat(this, "rotateDegree", 359f, 0f).apply {
        duration = 1500
        startDelay = 500
        start()
    }

    private val bitmap = getAvatar(R.drawable.avatar_rengwuxian, IMAGE_SIZE.toInt())

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)

        //上半部分
        canvas.save()
        canvas.translate(width.half, height.half)
        canvas.rotate(rotateDegree)
        canvas.clipRect(
            -IMAGE_SIZE,
            -IMAGE_SIZE,
            0f,
            IMAGE_SIZE
        )
        canvas.rotate(-rotateDegree)
        canvas.translate(-width.half, -height.half)
        canvas.drawBitmap(
            bitmap,
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            paint
        )
        canvas.restore()

        // 下半部分
        canvas.save()
        canvas.translate(width.half, height.half)
        canvas.rotate(rotateDegree)
        camera.save()
        camera.setLocation(0f, 0f, -8f * resources.displayMetrics.density)
        camera.rotateY(cameraDegree)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(
            0f,
            -IMAGE_SIZE,
            IMAGE_SIZE,
            IMAGE_SIZE
        )
        canvas.rotate(-rotateDegree)
        canvas.translate(-width.half, -height.half)
        canvas.drawBitmap(
            bitmap,
            width.half - IMAGE_SIZE.half,
            height.half - IMAGE_SIZE.half,
            paint
        )
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            val distance = getDistance(event.x - width.half, event.y - height.half)
            cameraDegree = -(45f * (1 - distance / width.half))
            rotateDegree = getDegree(event.x, event.y)
            invalidate()
        } else if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
            cameraDegree = 0f
            rotateDegree = 0f
            invalidate()
        }
        return true
    }

    private fun getDegree(x: Float, y: Float): Float {
        val xDistance = x - width.half
        val yDistance = y - height.half
        var degree: Float
        if (xDistance.toInt() == 0) {
            degree = if (yDistance > 0) {
                90f
            } else {
                270f
            }
        } else {
            degree = Math.toDegrees(atan(((yDistance / xDistance).toDouble()))).toFloat()
            if (xDistance < 0) {
                degree += 180f
            }
        }
        degree = (degree + 360f) % 360f
        return degree
    }

    private fun getDistance(x: Float, y: Float): Float {
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawLine(20f, height.half, width.toFloat() - 20f, height.half, paint)
        canvas.drawLine(width.half, 20f, width.half, height.toFloat() - 20f, paint)
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