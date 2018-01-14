package com.gurunars.box.ui

import android.view.View
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox

fun View.backgroundColor(field: IRoBox<Int>) =
    field.onChange(listener = this::setBackgroundColor)

fun View.isVisible(field: IRoBox<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }