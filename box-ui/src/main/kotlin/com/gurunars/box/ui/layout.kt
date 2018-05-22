package com.gurunars.box.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.gurunars.box.ui.VerticalPosition.ABOVE
import com.gurunars.box.ui.VerticalPosition.BELOW
import com.gurunars.box.ui.VerticalPosition.SAME

private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

/** Replaces view marked by a specific id with a given new view */
fun <T : View> T.set(parent: ViewGroup, id: Int, init: T.() -> Unit = {}): T {
    val view = parent.findViewById<View>(id)
    if (view != null) parent.removeView(view)
    return layout(parent, init).apply {
        this.id = id
    }
}

/** Returns layout params with WRAP_CONTENT for both width and height */
fun relativeLayoutParams(init: RelativeLayout.LayoutParams.() -> Unit = {}) = RelativeLayout.LayoutParams(
    WRAP_CONTENT,
    WRAP_CONTENT
).apply { init() }

/** Returns layout params with WRAP_CONTENT for both width and height */
fun linearLayoutParams(init: LinearLayout.LayoutParams.() -> Unit = {}) = LinearLayout.LayoutParams(
    WRAP_CONTENT,
    WRAP_CONTENT
).apply { init() }

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun ViewGroup.LayoutParams.fullSize() {
    width = MATCH_PARENT
    height = MATCH_PARENT
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun ViewGroup.LayoutParams.asRow() {
    width = MATCH_PARENT
    height = WRAP_CONTENT
}

/**
 * width = 0
 * height = 0
 */
fun ViewGroup.LayoutParams.asEmpty() {
    width = 0
    height = 0
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun View.fullSize() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    layoutParams.fullSize()
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    layoutParams.asRow()
}

/**
 * width = 0
 * height = 0
 */
fun View.asEmpty() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    layoutParams.asEmpty()
}

/** Removes all the views and adds this view as the only full screen child */
fun <T : View> T.layoutAsOne(parent: FrameLayout, init: T.() -> Unit = {}): T {
    fullSize()
    parent.removeAllViews()
    parent.addView(this)
    this.init()
    return this
}

/** Removes all the views and adds this view as the only full screen child */
fun <T : View> T.layoutAsOne(parent: Activity, init: T.() -> Unit = {}): T {
    fullSize()
    parent.setContentView(this)
    this.init()
    return this
}

/** Add a view to parent */
fun <T : View> T.layout(parent: ViewGroup, init: T.() -> Unit = {}): T {
    parent.addView(this)
    this.init()
    return this
}

/** @param isVisible if true - marks view as VISIBLE, otherwise marks it as GONE */
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/** Position along X axis. */
enum class HorizontalAlignment(internal val alignment: Int) {
    /** on the left side */
    LEFT(RelativeLayout.ALIGN_PARENT_LEFT),
    /** on the right side */
    RIGHT(RelativeLayout.ALIGN_PARENT_RIGHT),
    /** current value remains unchanged */
    SAME(-42),
    /** in the middle */
    CENTER(RelativeLayout.CENTER_HORIZONTAL)
}

/** Position along Y axis. */
enum class VerticalAlignment(internal val alignment: Int) {
    /** in the top */
    TOP(RelativeLayout.ALIGN_PARENT_TOP),
    /** in the bottom */
    BOTTOM(RelativeLayout.ALIGN_PARENT_BOTTOM),
    /** current value remains unchanged */
    SAME(-42),
    /** in the middle */
    CENTER(RelativeLayout.CENTER_VERTICAL)
}

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
        HorizontalAlignment.values()
            .filterNot { it.alignment == -42 }
            .forEach { removeRule(it.alignment) }
        addRule(horizontalAlignment.alignment)
    }
    if (verticalAlignment != VerticalAlignment.SAME) {
        VerticalAlignment.values()
            .filterNot { it.alignment == -42 }
            .forEach { removeRule(it.alignment) }
        addRule(verticalAlignment.alignment)
    }
}

/** Position along X axis. */
enum class HorizontalPosition(internal val alignment: Int) {
    /** left of another item */
    LEFT_OF(RelativeLayout.LEFT_OF),
    /** right of another item */
    RIGHT_OF(RelativeLayout.RIGHT_OF),
    /** current value remains unchanged */
    SAME(-42)
}

/**
 * Position along Y axis with respect to another element.
 *
 * @property ABOVE above another item
 * @property BELOW below another item
 * @property SAME current value remains unchanged
 */
enum class VerticalPosition(internal val alignment: Int) {
    /** above another item */
    ABOVE(RelativeLayout.ABOVE),
    /** below another item */
    BELOW(RelativeLayout.BELOW),
    /** current value remains unchanged */
    SAME(-42)
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
        HorizontalPosition.values().filter { it.alignment != -42 }.forEach { removeRule(it.alignment) }
        addRule(horizontalPosition.alignment, id)
    }
    if (verticalPosition != VerticalPosition.SAME) {
        VerticalPosition.values().filter { it.alignment != -42 }.forEach { removeRule(it.alignment) }
        addRule(verticalPosition.alignment, id)
    }
}
