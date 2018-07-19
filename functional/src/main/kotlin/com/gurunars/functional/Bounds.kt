package com.gurunars.functional

/**
 * @property left
 * @property right
 * @property top
 * @property bottom
 */
data class Bounds(
    val left: Int = 0,
    val right: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0
) {
    /** Padding for all dimensions */
    constructor(all: Int = 0) : this(all, all)

    /** Padding for horizontal and vertical dimensions */
    constructor(horizontal: Int = 0, vertical: Int = 0) : this(
        horizontal,
        horizontal,
        vertical,
        vertical
    )
}