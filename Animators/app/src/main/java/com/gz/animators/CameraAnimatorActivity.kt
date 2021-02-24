package com.gz.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View

class CameraAnimatorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_animator)

        val view = findViewById<CameraView>(R.id.camera_view)

        findViewById<View>(R.id.fab).setOnClickListener(View.OnClickListener {
            start(view)
        })

        start(view, 500)
    }

    private fun start(view: CameraView, delay: Int = 0) {
        view.topFlip = 0f
        view.bottomFlip = 0f
        view.flipRotation = 0f
        val topFlipAnimator = ObjectAnimator.ofFloat(view, "topFlip", 0f, 60f).apply {
            duration = 1500
        }
        val flipRotationAnimator = ObjectAnimator.ofFloat(view, "flipRotation", 360f, 0f).apply {
            duration = 1500
        }
        val bottomFlipAnimator = ObjectAnimator.ofFloat(view, "bottomFlip", 0f, -60f).apply {
            duration = 1500
        }
        AnimatorSet().apply {
            playSequentially(topFlipAnimator, flipRotationAnimator, bottomFlipAnimator)
            if (delay > 0) {
                startDelay = delay.toLong()
            }
            start()
        }
    }
}