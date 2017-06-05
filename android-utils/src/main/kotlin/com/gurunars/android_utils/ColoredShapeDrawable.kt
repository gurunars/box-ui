package com.gurunars.android_utils

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape

/**
 * Drawable with customizable shape and color.
 */
class ColoredShapeDrawable(shape: Shape, color: Int) : ShapeDrawable(shape) {

    init {
        paint.color = color
    }

}
