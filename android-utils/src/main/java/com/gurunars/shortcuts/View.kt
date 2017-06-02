package com.gurunars.shortcuts

import android.support.v4.content.ContextCompat
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

inline fun FrameLayout.setToOneView(view: View) {
    removeAllViews()
    addView(view)
}

inline fun View.color(colorId: Int): Int {
    return ContextCompat.getColor(context, android.R.color.holo_green_light)
}