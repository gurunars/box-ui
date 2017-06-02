package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

inline fun ViewGroup.LayoutParams.fullScreen() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.MATCH_PARENT
}

inline fun ViewGroup.LayoutParams.asRow() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.WRAP_CONTENT
}

inline fun ViewGroup.fullScreen() {
    val width = ViewGroup.LayoutParams.MATCH_PARENT
    val height = ViewGroup.LayoutParams.MATCH_PARENT

    layoutParams = layoutParams ?: ViewGroup.LayoutParams(width, height)
    layoutParams.width = width
    layoutParams.height = height
}

inline fun ViewGroup.asRow() {
    val width = ViewGroup.LayoutParams.MATCH_PARENT
    val height = ViewGroup.LayoutParams.WRAP_CONTENT

    layoutParams = layoutParams ?: ViewGroup.LayoutParams(width, height)
    layoutParams.width = width
    layoutParams.height = height
}

inline fun FrameLayout.setOneView(view: View) {
    removeAllViews()
    addView(view)
}

inline fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
