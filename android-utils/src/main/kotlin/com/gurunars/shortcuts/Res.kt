package com.gurunars.shortcuts

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.leftOf

/**
 * @param colorId color resource ID
 * @return color integer
 */
fun View.color(colorId: Int): Int = ContextCompat.getColor(context, colorId)
