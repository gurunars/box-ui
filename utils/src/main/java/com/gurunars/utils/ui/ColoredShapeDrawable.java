package com.gurunars.utils.ui;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Drawable with customizable shape and color.
 */
public class ColoredShapeDrawable extends ShapeDrawable {

    public ColoredShapeDrawable(Shape shape, int color) {
        super(shape);
        getPaint().setColor(color);
    }

}
