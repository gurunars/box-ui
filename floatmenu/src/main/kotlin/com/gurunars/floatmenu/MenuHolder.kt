package com.gurunars.floatmenu

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.setIsVisible

@SuppressLint("ViewConstructor")
internal class MenuHolder constructor(
    context: Context,
    hasOverlay: BindableField<Boolean>,
    isVisible: BindableField<Boolean>,
    animationDuration: Int
) : FrameLayout(context) {

    init {

        val floatEvaluator = FloatEvaluator()
        val animatedValue = BindableField(1f)

        hasOverlay.onChange { value ->
            setBackgroundColor(if (value) Color.parseColor("#99000000") else Color.TRANSPARENT)
            isClickable = value
        }
        isVisible.onChange { _ ->
            if (isAttachedToWindow) {
                ValueAnimator.ofFloat(0f, 1f).apply {
                    startDelay = 0
                    duration = animationDuration.toLong()
                    addUpdateListener { animatedValue.set(it.animatedValue as Float) }
                    start()
                }
            } else {
                animatedValue.set(1f, true)
            }
        }
        animatedValue.onChange { value ->
            val visible = isVisible.get()
            alpha = floatEvaluator.evaluate(value,
                if (visible) 0f else 1f,
                if (visible) 1f else 0f
            )
            setIsVisible(visible || value != 1f)
        }
    }

    private fun isWithinBounds(view: View, ev: MotionEvent): Boolean {
        val xPoint = Math.round(ev.rawX)
        val yPoint = Math.round(ev.rawY)
        val l = IntArray(2)
        view.getLocationOnScreen(l)
        val x = l[0]
        val y = l[1]
        val w = view.width
        val h = view.height
        return xPoint >= x && xPoint <= x + w && yPoint >= y && yPoint <= y + h
    }

    private fun touchBelongsTo(viewGroup: ViewGroup, ev: MotionEvent): Boolean {
        (0 until viewGroup.childCount)
            .mapNotNull { viewGroup.getChildAt(it) }
            .forEach {
                if (it is ViewGroup) {
                    // check for all children first
                    if (touchBelongsTo(it, ev)) {
                        return true
                        // only after that check view group itself
                    } else if (it.isClickable && isWithinBounds(it, ev)) {
                        return true
                    }
                } else if (isWithinBounds(it, ev)) {
                    return true
                }
            }
        return false
    }

    /* Intercept touch on the view group but not on the icons */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
        !isClickable && !touchBelongsTo(this, ev)

}
