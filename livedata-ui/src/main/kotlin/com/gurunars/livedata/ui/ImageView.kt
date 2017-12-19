package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.livedata.Box
import com.gurunars.livedata.BoxContext

fun BoxContext<ImageView>.src(field: Box<Int>) =
    field.onChange(listener = context::setImageResource)