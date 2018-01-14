package com.gurunars.box.ui

import android.widget.ImageView
import com.gurunars.box.IRoBox

fun ImageView.src(field: IRoBox<Int>) =
    field.onChange(listener = this::setImageResource)