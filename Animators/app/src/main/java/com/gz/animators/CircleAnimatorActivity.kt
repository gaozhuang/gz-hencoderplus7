package com.gz.animators

import android.app.Activity
import android.os.Bundle
import android.view.View

class CircleAnimatorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_animator)
        val view = findViewById<View>(R.id.v_circle_animator)
        findViewById<View>(R.id.fab).setOnClickListener(View.OnClickListener {
            view.scaleX = 1.0f
            view.scaleY = 1.0f
            start(view)
        })
        start(view, 500)
    }

    private fun start(view: View, delay: Long = 0) {
        view.animate().apply {
            scaleX(2.5f)
            scaleY(2.5f)
            duration = 500
            if (delay > 0) {
                startDelay = delay
            }
            withLayer()
            start()
        }
    }
}