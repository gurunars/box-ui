package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.BindableField

fun View.backgroundColor(field: BindableField<Int>) =
    field.onChange(this::setBackgroundColor)

fun View.isVisible(field: BindableField<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }