package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import org.jetbrains.anko.*

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

/**
 * @param left if true - aligns to the left. Aligns to the right otherwise.
 */
inline fun RelativeLayout.LayoutParams.alignHorizontally(left: Boolean) {
    removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
    removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
    if (left) {
        alignParentLeft()
    } else {
        alignParentRight()
    }
}

/**
 * @param top if true - aligns to the top. Aligns to the bottom otherwise.
 */
inline fun RelativeLayout.LayoutParams.alignVertically(top: Boolean) {
    removeRule(RelativeLayout.ALIGN_PARENT_TOP)
    removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    if (top) {
        alignParentTop()
    } else {
        alignParentBottom()
    }
}
