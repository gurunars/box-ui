package com.gurunars.shortcuts

import android.support.v4.content.ContextCompat
import android.view.View

/**
 * @param colorId color resource ID
 * @return color integer
 */
inline fun View.color(colorId: Int): Int = ContextCompat.getColor(context, colorId)
