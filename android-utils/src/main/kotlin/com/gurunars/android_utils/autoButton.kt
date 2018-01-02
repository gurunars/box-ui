package com.gurunars.android_utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.widget.TextView
import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.onChange
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.topPadding

enum class Style(internal val value: Int) {
    NORMAL(Typeface.NORMAL),
    BOLD(Typeface.BOLD),
    ITALIC(Typeface.ITALIC),
    BOLD_ITALIC(Typeface.BOLD_ITALIC)
}

fun Context.autoButton(
    text: IBox<String> = Box(""),
    shape: IBox<Shape> = Box(RectShape()),
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
    textStyle.onChange { value -> setTypeface(null, value.value) }
    textSize.onChange { value -> setTextSize(value) }
    text.onChange { value -> setText(value) }
    listOf(shadowWidth, shape, bgColor).onChange {
        background = ColoredShapeDrawable(shape.get(), bgColor.get())
        setAutoBg(shadowWidth.get())
    }
}