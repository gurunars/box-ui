package com.gurunars.android_utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.widget.TextView
import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.merge
import com.gurunars.box.ui.*

/** Button with automatic background tint. */
fun Context.autoButton(
    text: IBox<String> = Box(""),
    shape: IBox<Shape> = Box(RectShape()),
    textColor: IBox<Int> = Box(Color.BLACK),
    bgColor: IBox<Int> = Box(Color.RED),
    shadowWidth: IBox<Int> = Box(6),
    textSize: IBox<Float> = Box(12f),
    textStyle: IBox<Style> = Box(Style.NORMAL)
): View = TextView(this).apply {
    isClickable = true
    isFocusable = true
    padding = Bounds(
        horizontal = dip(20),
        vertical = dip(15)
    )
    textColor(textColor)
    style(textStyle)
    textSize(textSize)
    text(text)
    merge(shadowWidth, shape, bgColor).onChange {
        background = ColoredShapeDrawable(it.second, it.third)
        setAutoBg(it.first)
    }
}