package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.Box
import com.gurunars.databinding.onChange

fun View.backgroundColor(field: Box<Int>) =
    field.onChange(this::setBackgroundColor)

fun View.isVisible(field: Box<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }