package com.gurunars.android_utils;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Drawable with customizable shape and color.
 */
public final class ColoredShapeDrawable extends ShapeDrawable {

    public ColoredShapeDrawable(Shape shape, int color) {
        super(shape);
        getPaint().setColor(color);
    }

}
