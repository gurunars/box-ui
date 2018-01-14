package com.gurunars.android_utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.widget.TextView
import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.onChange
import com.gurunars.box.ui.Style
import com.gurunars.box.ui.style
import com.gurunars.box.ui.textColor
import com.gurunars.box.ui.textSize
import com.gurunars.box.ui.text
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.topPadding

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
    leftPadding = dip(20)
    rightPadding = dip(20)
    topPadding = dip(15)
    bottomPadding = dip(15)
    textColor(textColor)
    style(textStyle)
    textSize(textSize)
    text(text)
    listOf(shadowWidth, shape, bgColor).onChange {
        background = ColoredShapeDrawable(shape.get(), bgColor.get())
        setAutoBg(shadowWidth.get())
    }
}