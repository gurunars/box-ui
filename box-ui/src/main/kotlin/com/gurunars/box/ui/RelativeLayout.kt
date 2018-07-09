package com.gurunars.box.ui

import android.widget.RelativeLayout

/** Position along X axis. */
enum class HorizontalAlignment(internal val alignment: Int) {
    /** on the left side */
    LEFT(RelativeLayout.ALIGN_PARENT_LEFT),
    /** on the right side */
    RIGHT(RelativeLayout.ALIGN_PARENT_RIGHT),
    /** current text remains unchanged */
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
    /** current text remains unchanged */
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
    /** current text remains unchanged */
    SAME(-42)
}

/**
 * Position along Y axis with respect to another element.
 *
 * @property ABOVE above another item
 * @property BELOW below another item
 * @property SAME current text remains unchanged
 */
enum class VerticalPosition(internal val alignment: Int) {
    /** above another item */
    ABOVE(RelativeLayout.ABOVE),
    /** below another item */
    BELOW(RelativeLayout.BELOW),
    /** current text remains unchanged */
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
