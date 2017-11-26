package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.databinding.IBox
import com.gurunars.databinding.onChange

fun ImageView.src(field: IBox<Int>) =
    field.onChange(listener = this::setImageResource)