package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

/**
 * width = matchParent
 * height = matchParent
 */
inline fun ViewGroup.LayoutParams.fullSize() {
    width = matchParent
    height = matchParent
}

/**
 * width = matchParent
 * height = wrapContent
 */
inline fun ViewGroup.LayoutParams.asRow() {
    width = matchParent
    height = wrapContent
}

/**
 * width = matchParent
 * height = matchParent
 */
inline fun View.fullSize() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, matchParent)
    layoutParams.fullSize()
}

/**
 * width = matchParent
 * height = wrapContent
 */
inline fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, wrapContent)
    layoutParams.asRow()
}

/**
 * Removes all the views and adds this view as the only child
 */
inline fun FrameLayout.setOneView(view: View) {
    removeAllViews()
    addView(view)
}

/**
 * @param isVisible if true - marks view as VISIBLE, otherwise marks it as GONE
 */
inline fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
