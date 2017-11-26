package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.databinding.Box
import com.gurunars.databinding.onChange

fun ImageView.src(field: Box<Int>) =
    field.onChange(this::setImageResource)