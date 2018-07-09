package com.gurunars.box.ui.component

import android.graphics.Color as AndroidColor


data class Color(
    val value: Int
) {
    constructor(value: String): this(AndroidColor.parseColor(value))
}