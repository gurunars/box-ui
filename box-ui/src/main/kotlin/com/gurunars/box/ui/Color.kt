package com.gurunars.box.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import kotlin.math.roundToInt

/**
 * Returns a color that is either darker of lighter than a given one based on a given factor.
 */
fun alterBrightness(color: Int, factor: Float = 0.8f) =
    Color.HSVToColor(FloatArray(3).apply {
        Color.colorToHSV(color, this)
        set(2, get(2) * factor)
    })

/**
 * Returns a color that has alpha set to a specific float factor.
 */
fun alterAlpha(color: Int, factor: Float = 0.8f): Int =
    Color.argb(
        (Color.alpha(color) * factor).roundToInt(),
        Color.red(color),
        Color.green(color),
        Color.blue(color)
    )

fun Context.ref(@ColorRes res: Int) = getColor(res)

fun hex(value: String): Int = Color.parseColor(value)

val Int.drawable
    get() = ColorDrawable(this)