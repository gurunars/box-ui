package com.gurunars.functional

/**
 * @property left
 * @property right
 * @property top
 * @property bottom
 */
data class Bounds(
    val left: Size = 0.dp,
    val right: Size = 0.dp,
    val top: Size = 0.dp,
    val bottom: Size = 0.dp
) {
    /** Padding for all dimensions */
    constructor(all: Size = 0.dp) : this(all, all)

    /** Padding for horizontal and vertical dimensions */
    constructor(horizontal: Size = 0.dp, vertical: Size = 0.dp) : this(
        horizontal,
        horizontal,
        vertical,
        vertical
    )
}