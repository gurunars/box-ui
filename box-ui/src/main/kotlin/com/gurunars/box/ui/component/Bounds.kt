package com.gurunars.box.ui.component

/**
 * @property left
 * @property right
 * @property top
 * @property bottom
 */
data class Bounds(
    val left: Size = dp(0),
    val right: Size = dp(0),
    val top: Size = dp(0),
    val bottom: Size = dp(0)
) {
    /** Padding for all dimensions */
    constructor(all: Size = dp(0)) : this(all, all)

    /** Padding for horizontal and vertical dimensions */
    constructor(horizontal: Size = dp(0), vertical: Size = dp(0)) : this(
        horizontal,
        horizontal,
        vertical,
        vertical
    )
}