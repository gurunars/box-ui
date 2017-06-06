package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

inline fun ViewGroup.LayoutParams.fullSize() {
    width = matchParent
    height = matchParent
}

inline fun ViewGroup.LayoutParams.asRow() {
    width = matchParent
    height = wrapContent
}

inline fun View.fullSize() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, matchParent)
    layoutParams.fullSize()
}

inline fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, wrapContent)
    layoutParams.asRow()
}

inline fun FrameLayout.setOneView(view: View) {
    removeAllViews()
    addView(view)
}

inline fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

inline fun View.setPadding(padding: Int) {
    setPadding(padding, padding, padding, padding)
}