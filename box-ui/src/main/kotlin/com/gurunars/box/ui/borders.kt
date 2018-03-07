package com.gurunars.box.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.annotation.ColorInt
import android.view.Gravity
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
    @ColorInt color: Int = Color.BLACK
) {
    background = LayerDrawable(listOf(
        ShapeDrawable(RectShape()).apply {
            paint.color = color
        },
        ColorDrawable(color),
        ColorDrawable(color),
        ColorDrawable(color),
        background ?: ColorDrawable(Color.TRANSPARENT)
    ).toTypedArray()).apply {

    }
}