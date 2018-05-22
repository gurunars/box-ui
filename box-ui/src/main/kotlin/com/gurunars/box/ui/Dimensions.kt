package com.gurunars.box.ui

import android.content.Context

/**
 * @value dimension in dip(dp)
 * @return dimension in pixels
 */
fun Context.dip(value: Int): Int =
    (value * resources.displayMetrics.density).toInt()

/**
 * @value dimension in sp
 * @return dimension in pixels
 */
fun Context.sp(value: Int): Int =
    (value * resources.displayMetrics.scaledDensity).toInt()
