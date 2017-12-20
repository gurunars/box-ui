package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.box.Box
import com.gurunars.box.BoxContext

fun BoxContext<ImageView>.src(field: Box<Int>) =
    field.onChange(listener = ctx::setImageResource)