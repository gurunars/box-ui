package com.gurunars.shortcuts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.shortcuts.VerticalPosition.*
import org.jetbrains.anko.*

/**
 * width = matchParent
 * height = matchParent
 */
fun ViewGroup.LayoutParams.fullSize() {
    width = matchParent
    height = matchParent
}

/**
 * width = matchParent
 * height = wrapContent
 */
fun ViewGroup.LayoutParams.asRow() {
    width = matchParent
    height = wrapContent
}

/**
 * width = matchParent
 * height = matchParent
 */
fun View.fullSize() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, matchParent)
    layoutParams.fullSize()
}

/**
 * width = matchParent
 * height = wrapContent
 */
fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(matchParent, wrapContent)
    layoutParams.asRow()
}

/**
 * Removes all the views and adds this view as the only child
 */
fun FrameLayout.setOneView(view: View) {
    removeAllViews()
    addView(view)
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

/**
 * Position the item within parent.
 *
 * @param horizontalAlignment position on X axis
 * @param verticalAlignment position on Y axis
 */
fun RelativeLayout.LayoutParams.alignInParent(
    horizontalAlignment: HorizontalAlignment=HorizontalAlignment.SAME,
    verticalAlignment: VerticalAlignment=VerticalAlignment.SAME
) {
    if (horizontalAlignment != HorizontalAlignment.SAME) {
        removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        if (horizontalAlignment == HorizontalAlignment.LEFT) {
            alignParentLeft()
        } else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
            alignParentRight()
        } else {
            centerHorizontally()
        }
    }
    if (verticalAlignment != VerticalAlignment.SAME) {
        removeRule(RelativeLayout.ALIGN_PARENT_TOP)
        removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        if (verticalAlignment == VerticalAlignment.TOP) {
            alignParentTop()
        } else if (verticalAlignment == VerticalAlignment.BOTTOM) {
            alignParentBottom()
        } else {
            centerVertically()
        }
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
    horizontalPosition: HorizontalPosition=HorizontalPosition.SAME,
    verticalPosition: VerticalPosition=VerticalPosition.SAME
) {
    if (horizontalPosition != HorizontalPosition.SAME) {
        removeRule(RelativeLayout.LEFT_OF)
        removeRule(RelativeLayout.RIGHT_OF)
        if (horizontalPosition == HorizontalPosition.LEFT_OF) {
            leftOf(id)
        } else {
            rightOf(id)
        }
    }
    if (verticalPosition != VerticalPosition.SAME) {
        removeRule(RelativeLayout.ABOVE)
        removeRule(RelativeLayout.BELOW)
        if (verticalPosition == VerticalPosition.ABOVE) {
            above(id)
        } else {
            below(id)
        }
    }
}
