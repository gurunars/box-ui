package com.gurunars.databinding.android

import android.view.View
import com.gurunars.livedata.Box
import com.gurunars.livedata.BoxContext

fun BoxContext<View>.backgroundColor(field: Box<Int>) =
    field.onChange(listener = context::setBackgroundColor)

fun BoxContext<View>.isVisible(field: Box<Boolean>) =
    field.onChange { status ->
        context.visibility = if (status) View.VISIBLE else View.GONE
    }