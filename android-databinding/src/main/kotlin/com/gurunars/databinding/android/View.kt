package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.IBox
import com.gurunars.databinding.onChange

fun View.backgroundColor(field: IBox<Int>) =
    field.onChange(listener = this::setBackgroundColor)

fun View.isVisible(field: IBox<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }