package com.gz.customviews

import android.content.res.Resources
import android.util.TypedValue

inline fun Int.half(): Float {
    return this / 2f
}

inline fun Int.double(): Float {
    return this * 2f
}

inline fun Float.half(): Float {
    return this / 2f
}

inline fun Float.double(): Float {
    return this * 2f
}

inline fun Float.pxValue(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}

inline fun Int.pxValue(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

val Float.px: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
    }

val Float.half: Float
    get() {
        return this / 2f
    }

val Int.half: Float
    get() {
        return this / 2f
    }

val Float.toRadius: Float
    get() {
        return Math.toRadians(this.toDouble()).toFloat()
    }