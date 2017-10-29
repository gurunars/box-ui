package com.gurunars.databinding.android

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.databinding.android.VerticalPosition.*

/**
 * Removes all the views and adds this view as the only full screen child
 */
fun<T: View> T.setAsOne(parent: FrameLayout, init: T.() -> Unit = {}): T {
    fullSize()
    parent.removeAllViews()
    parent.addView(this)
    this.init()
    return this
}

/**
 * Removes all the views and adds this view as the only full screen child
 */
fun<T: View> T.setAsOne(parent: Activity, init: T.() -> Unit = {}): T {
    fullSize()
    parent.setContentView(this)
    this.init()
    return this
}

/**
 * Add a view to parent
 */
fun<T: View> T.add(parent: ViewGroup, init: T.() -> Unit = {}) : T {
    parent.addView(this)
    this.init()
    return this
}


/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun ViewGroup.LayoutParams.fullSize() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.MATCH_PARENT
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun ViewGroup.LayoutParams.asRow() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
    height = ViewGroup.LayoutParams.WRAP_CONTENT
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun View.fullSize() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT)
    layoutParams.fullSize()
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    layoutParams.asRow()
}

/**
 * @param isVisible if true - marks view as VISIBLE, otherwise marks it as GONE
 */
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Position along X axis.
 */
enum class HorizontalAlignment {
    /**
     * on the left side
     */
    LEFT,
    /**
     * on the right side
     */
    RIGHT,
    /**
     * current value remains unchanged
     */
    SAME,
    /**
     * in the middle
     */
    CENTER
}

/**
 * Position along Y axis.
 */
enum class VerticalAlignment {
    /**
     * in the top
     */
    TOP,
    /**
     * in the bottom
     */
    BOTTOM,
    /**
     * current value remains unchanged
     */
    SAME,
    /**
     * in the middle
     */
    CENTER
}

private fun RelativeLayout.LayoutParams.removeRules(vararg rules: Int) =
    rules.forEach { removeRule(it) }

/**
 * Position the item within parent.
 *
 * @param horizontalAlignment position on X axis
 * @param verticalAlignment position on Y axis
 */
fun RelativeLayout.LayoutParams.alignInParent(
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.SAME,
    verticalAlignment: VerticalAlignment = VerticalAlignment.SAME
) {
    if (horizontalAlignment != HorizontalAlignment.SAME) {
        removeRules(
            RelativeLayout.ALIGN_PARENT_LEFT,
            RelativeLayout.ALIGN_PARENT_RIGHT,
            RelativeLayout.CENTER_VERTICAL
        )
        addRule(when (horizontalAlignment) {
            HorizontalAlignment.LEFT -> RelativeLayout.ALIGN_PARENT_LEFT
            HorizontalAlignment.RIGHT -> RelativeLayout.ALIGN_PARENT_RIGHT
            else -> RelativeLayout.CENTER_VERTICAL
        })
    }
    if (verticalAlignment != VerticalAlignment.SAME) {
        removeRules(
            RelativeLayout.ALIGN_PARENT_TOP,
            RelativeLayout.ALIGN_PARENT_BOTTOM,
            RelativeLayout.CENTER_HORIZONTAL
        )
        addRule(when (verticalAlignment) {
            VerticalAlignment.TOP -> RelativeLayout.ALIGN_PARENT_TOP
            VerticalAlignment.BOTTOM -> RelativeLayout.ALIGN_PARENT_BOTTOM
            else -> RelativeLayout.CENTER_HORIZONTAL
        })
    }
}

/**
 * Position along X axis.
 */
enum class HorizontalPosition {
    /**
     * left of another item
     */
    LEFT_OF,
    /**
     * right of another item
     */
    RIGHT_OF,
    /**
     * current value remains unchanged
     */
    SAME
}

/**
 * Position along Y axis with respect to another element.
 *
 * @property ABOVE above another item
 * @property BELOW below another item
 * @property SAME current value remains unchanged
 */
enum class VerticalPosition {
    /**
     * current value remains unchanged
     */
    ABOVE,
    /**
     * current value remains unchanged
     */
    BELOW,
    /**
     * current value remains unchanged
     */
    SAME
}

/**
 * Position the item relatively to another item.
 *
 * @param id element with respect to which the view has to be positioned
 * @param horizontalPosition position on X axis
 * @param verticalPosition position on Y axis
 */
fun RelativeLayout.LayoutParams.alignWithRespectTo(
    id: Int,
    horizontalPosition: HorizontalPosition = HorizontalPosition.SAME,
    verticalPosition: VerticalPosition = VerticalPosition.SAME
) {
    if (horizontalPosition != HorizontalPosition.SAME) {
        removeRules(
            RelativeLayout.LEFT_OF,
            RelativeLayout.RIGHT_OF
        )
        if (horizontalPosition == HorizontalPosition.LEFT_OF) {
            addRule(RelativeLayout.LEFT_OF, id)
        } else {
            addRule(RelativeLayout.RIGHT_OF, id)
        }
    }
    if (verticalPosition != VerticalPosition.SAME) {
        removeRules(
            RelativeLayout.ABOVE,
            RelativeLayout.BELOW
        )
        if (verticalPosition == VerticalPosition.ABOVE) {
            addRule(RelativeLayout.ABOVE, id)
        } else {
            addRule(RelativeLayout.BELOW, id)
        }
    }
}
