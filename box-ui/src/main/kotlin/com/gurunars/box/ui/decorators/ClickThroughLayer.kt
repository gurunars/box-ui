package com.gurunars.box.ui.decorators

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gurunars.box.ui.layoutAsOne

@SuppressLint("ViewConstructor")
private class ClickThroughLayer constructor(
    context: Context
) : FrameLayout(context) {

    init {
        layoutTransition = LayoutTransition().apply {
            setDuration(500)
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

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
        !isClickable && !touchBelongsTo(this, ev)
}

/**
 * Creates a view layer that has the ability to pass clicks to the view(s)
 * behind it.
 *
 * @param viewSupplier a function that should return a view that will
 * be set as a full size content area of the layer
 */
fun Context.clickThroughLayer(viewSupplier: () -> View): View =
    ClickThroughLayer(this).apply { viewSupplier().layoutAsOne(this) }