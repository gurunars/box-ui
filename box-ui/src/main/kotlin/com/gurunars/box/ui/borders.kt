package com.gurunars.box.ui

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View


private class BorderDrawable(
    private val bounds: Bounds,
    private @ColorInt val borderColor: Int = Color.BLACK
) : Drawable() {
    private val paint = Paint().apply {
        color = borderColor
    }

    override fun draw(canvas: Canvas) {
        val height = bounds.height().toFloat()
        val width = bounds.width().toFloat()

        //left, top is 0,0
        //left, top, right, bottom
        val topBorder = RectF(0.0f, 0.0f, width, bounds.top.toFloat())
        val bottomBorder = RectF(0.0f, height - bounds.bottom.toFloat(), width, height)
        val leftBorder = RectF(0.0f, 0.0f, bounds.left.toFloat(), height)
        val rightBorder = RectF(width - bounds.right.toFloat(), 0.0f, width, height)

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
    bounds: Bounds,
    @ColorInt color: Int = Color.BLACK
) {
    background = LayerDrawable(
        listOf(
            BorderDrawable(bounds, color),
            background ?: ColorDrawable(Color.TRANSPARENT)
        ).toTypedArray()
    ).apply {
        setLayerInset(1, bounds.left, bounds.top, bounds.right, bounds.bottom)
    }
}