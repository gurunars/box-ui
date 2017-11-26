package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.databinding.Box

fun ImageView.src(field: Box<Int>) =
    field.onChange(this::setImageResource)