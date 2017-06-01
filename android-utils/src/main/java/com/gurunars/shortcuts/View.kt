package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

private fun View.params(width: Int, height: Int) {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(width, height)
    layoutParams.width = width
    layoutParams.height = height
}

fun ViewGroup.LayoutParams.fullScreen() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.MATCH_PARENT
}

fun ViewGroup.LayoutParams.asRow() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.WRAP_CONTENT
}

fun ViewGroup.fullScreen() {
    params(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
}

fun ViewGroup.asRow() {
    params(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun FrameLayout.setToOneView(view: View) {
    removeAllViews()
    addView(view)
}