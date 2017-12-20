package com.gurunars.databinding.android

import android.view.View
import com.gurunars.box.Box
import com.gurunars.box.BoxContext

fun BoxContext<View>.backgroundColor(field: Box<Int>) =
    field.onChange(listener = ctx::setBackgroundColor)

fun BoxContext<View>.isVisible(field: Box<Boolean>) =
    field.onChange { status ->
        ctx.visibility = if (status) View.VISIBLE else View.GONE
    }