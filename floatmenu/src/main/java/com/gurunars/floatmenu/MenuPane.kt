package com.gurunars.floatmenu

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


internal class MenuPane constructor(context: Context) : FrameLayout(context) {

    override fun setClickable(hasOverlay: Boolean) {
        super.setClickable(hasOverlay)
        setBackgroundColor(if (hasOverlay) Color.parseColor("#99000000") else Color.TRANSPARENT)
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
        bundle.putBoolean("isClickable", isClickable)
        bundle.putBoolean("visible", visibility == View.VISIBLE)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable<Parcelable>("superState"))
        isClickable = localState.getBoolean("isClickable")
        visibility = if (localState.getBoolean("visible")) View.VISIBLE else View.GONE
    }

}
