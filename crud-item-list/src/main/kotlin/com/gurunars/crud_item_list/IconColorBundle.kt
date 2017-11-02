package com.gurunars.crud_item_list

import android.graphics.Color
import com.gurunars.android_utils.Icon

/**
 * Icon color settings
 *
 * @property bgColor background color
 * @property fgColor foreground color
 */
data class IconColorBundle(
    val bgColor: Int = Color.RED,
    val fgColor: Int = Color.WHITE
)

internal fun IconColorBundle.icon(iconRes: Int) = Icon(
    icon=iconRes,
    bgColor = bgColor,
    fgColor = fgColor
)