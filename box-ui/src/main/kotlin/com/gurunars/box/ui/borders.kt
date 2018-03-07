package com.gurunars.box.ui

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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


private class BorderDrawable(
    private val spec: Spec,
    private @ColorInt val borderColor: Int = Color.BLACK
): Drawable() {
    private val paint = Paint().apply {
        color = borderColor
    }

    override fun draw(canvas: Canvas) {
        val height = bounds.height().toFloat()
        val width = bounds.width().toFloat()

        //left, top is 0,0
        //left, top, right, bottom
        val topBorder = RectF(0.0f, 0.0f, width, spec.top.toFloat())
        val bottomBorder = RectF(0.0f, height - spec.bottom.toFloat(), width, height)
        val leftBorder = RectF(0.0f, 0.0f, spec.left.toFloat(), height)
        val rightBorder = RectF(width - spec.right.toFloat(), 0.0f, spec.right.toFloat(), height)

        listOf(topBorder, bottomBorder, leftBorder, rightBorder).forEach {
            canvas.drawRect(it, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

}


/***/
fun View.setBorders(
    spec: Spec,
    @ColorInt color: Int = Color.BLACK
) {
    background = LayerDrawable(listOf(
        BorderDrawable(spec, color),
        background ?: ColorDrawable(Color.TRANSPARENT)
    ).toTypedArray()).apply {
        setLayerInset(1, spec.left, spec.top, spec.right, spec.bottom)
    }
}