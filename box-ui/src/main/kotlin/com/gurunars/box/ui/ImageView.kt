package com.gurunars.box.ui

import android.widget.ImageView
import com.gurunars.box.IBox

fun ImageView.src(field: IBox<Int>) =
    field.onChange(listener = this::setImageResource)