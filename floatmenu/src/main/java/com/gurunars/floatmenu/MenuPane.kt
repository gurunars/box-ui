package com.gurunars.floatmenu

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gurunars.android_utils.BindableField


internal class MenuPane constructor(context: Context) : FrameLayout(context) {

    private val floatEvaluator = FloatEvaluator()
    private val animatedValue = BindableField(1f)
    private var withAnimation = false

    val hasOverlay = BindableField(true)
    val isVisible = BindableField(false)
    val animationDuration = BindableField(400)

    init {
        hasOverlay.bind {
            setBackgroundColor(if (it) Color.parseColor("#99000000") else Color.TRANSPARENT)
            isClickable = it
        }
        isVisible.bind {
            if (withAnimation) {
                ValueAnimator.ofFloat(0f, 1f).apply {
                    startDelay = 0
                    duration = animationDuration.get().toLong()
                    addUpdateListener { this@MenuPane.animatedValue.set(it.animatedValue as Float) }
                    start()
                }
            }
            updateVisibility()
        }
        animatedValue.bind {
            val visible = isVisible.get()
            alpha = floatEvaluator.evaluate(animatedValue.get(),
                if (visible) 0f else 1f,
                if (visible) 1f else 0f
            )
            updateVisibility()
        }
        withAnimation=true
    }

    private fun updateVisibility() {
        visibility = if (!isVisible.get() && animatedValue.get() == 1f) View.GONE else View.VISIBLE
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
        for (i in 0..viewGroup.childCount - 1) {
            val child = viewGroup.getChildAt(i) ?: continue
            if (child is ViewGroup) {
                // check for all children first
                if (touchBelongsTo(child, ev)) {
                    return true
                    // only after that check view group itself
                } else if (child.isClickable && isWithinBounds(child, ev)) {
                    return true
                }
            } else if (isWithinBounds(child, ev)) {
                return true
            }
        }
        return false
    }

    /* Intercept touch on the view group but not on the icons */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isClickable && !touchBelongsTo(this, ev)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putBoolean("hasOverlay", hasOverlay.get())
        bundle.putBoolean("visible", isVisible.get())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable<Parcelable>("superState"))
        withAnimation=false
        hasOverlay.set(localState.getBoolean("isClickable"))
        isVisible.set(localState.getBoolean("visible"))
        withAnimation=true
    }

}
