package com.gurunars.box.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View

/**
 * @property left
 * @property right
 * @property top
 * @property bottom
 */
data class Spec(
    val left: Int = 0,
    val right: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0
) {
    /** Padding for all dimensions */
    constructor(all: Int = 0) : this(all, all, all, all)
    /** Padding for horizontal and vertical dimensions */
    constructor(horizontal: Int = 0, vertical: Int = 0) : this(horizontal, horizontal, vertical, vertical)
}

/***/
fun View.setBorders(
    spec: Spec,
    @ColorInt color: Int = Color.BLACK,
    @ColorInt backgroundColor: Int = Color.WHITE
) {
    background = LayerDrawable(listOf(
        ColorDrawable(color),
        background ?: ColorDrawable(backgroundColor)
    ).toTypedArray()).apply {
        setLayerInset(1, spec.left, spec.top, spec.right, spec.bottom)
    }
}