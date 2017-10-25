package com.gurunars.android_utils

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View


private class AutoBgDrawable constructor(
    private val bg: Drawable,
    private val shadowWidth: Int
) : InsetDrawable(bg, shadowWidth * 2) {

    // The color filter to apply when the button is pressed
    private val pressedFilter = LightingColorFilter(Color.LTGRAY, 1)

    override fun onStateChange(states: IntArray): Boolean {
        val stateList = states.toList()

        val enabled = stateList.contains(android.R.attr.state_enabled)
        val pressed = stateList.contains(android.R.attr.state_pressed)

        mutate()
        alpha = if (enabled) 255 else 50
        colorFilter = if (enabled && pressed) pressedFilter else null
        invalidateSelf()

        if (bg is ShapeDrawable && !enabled) {
            bg.paint.setShadowLayer(
                shadowWidth.toFloat(),
                0f,
                0f,
                Color.parseColor("#40000000")
            )
        }

        return super.onStateChange(states)
    }

    override fun isStateful(): Boolean {
        return true
    }
}

private fun transform(bg: Drawable): Drawable {
    if (bg is ColorDrawable) {
        val shapeBg = ShapeDrawable(RectShape())
        shapeBg.paint.color = bg.color
        return shapeBg
    }
    return bg
}

/**
 * Configure a view to have an automatic background with an optional shadow behind it.
 *
 * The background becomes shadier if the view gets disabled.
 *
 * The background gets slightly shadier if the view gets clicked.
 *
 * If **shadowWidth** is greater than 0 and drawable is a shape or color drawable it adds a
 * shadow behind the view. Shadow is not applicable to other view types.
 *
 * If it is a color drawable it also transforms it to a RectShape drawable with a specified
 * color. During the transformation any other attributes of a color drawable are lost.
 *
 * The reason why the actual drawable is is private is to prevent using the drawable a regular
 * way.
 *
 * There is a bug (feature?) in Android that remove the view padding whenever you set the
 * background.
 *
 * @param shadowWidth shadow size (can be 0)
 */
fun View.setAutoBg(shadowWidth: Int) {
    var bg: Drawable = background ?: return
    bg = transform(bg)
    val top = paddingTop
    val left = paddingLeft
    val right = paddingRight
    val bottom = paddingBottom

    if (shadowWidth > 0 && bg is ShapeDrawable) {
        bg.paint.setShadowLayer(
            shadowWidth.toFloat(),
            0f,
            shadowWidth.toFloat(),
            Color.parseColor("#68000000")
        )
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        background = AutoBgDrawable(bg, shadowWidth)
    } else {
        background = AutoBgDrawable(bg, 0)
    }

    setPadding(left, top, right, bottom)
}

