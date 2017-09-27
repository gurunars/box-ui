package com.gurunars.android_utils

import android.animation.ValueAnimator
import android.view.View

/**
 * Make the view flicker once
 *
 * @param duration time it take to perform the animation
 */
fun View.flicker(duration: Long = 1300) {
    clearAnimation()
    ValueAnimator().apply {
        setFloatValues(1.0f, 0.0f, 1.0f)
        addUpdateListener { animation -> alpha = animation.animatedValue as Float }
        this.duration = duration
        start()
    }
}