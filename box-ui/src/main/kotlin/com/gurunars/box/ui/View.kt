package com.gurunars.box.ui

import android.view.View
import com.gurunars.box.IBox

fun View.backgroundColor(field: IBox<Int>) =
    field.onChange(listener = this::setBackgroundColor)

fun View.isVisible(field: IBox<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }