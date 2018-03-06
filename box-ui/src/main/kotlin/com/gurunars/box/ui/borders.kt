package com.gurunars.box.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View

data class Spec(
    val left: Int = 0,
    val right: Int = 0,
    val top: Int = 0,
    val bottom: Int = 0
) {
    constructor(all: Int = 0) : this(all, all, all, all)
    constructor(horizontal: Int = 0, vertical: Int = 0) : this(horizontal, horizontal, vertical, vertical)
}

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