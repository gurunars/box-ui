package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.BindableField

fun View.backgroundColor(field: BindableField<Int>) =
    field.onChange { setBackgroundColor(it) }

fun View.isVisible(field: BindableField<Boolean>) =
    field.onChange {
        visibility = if (it) View.VISIBLE else View.GONE
    }