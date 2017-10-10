package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.databinding.BindableField

fun ImageView.imageResource(field: BindableField<Int>) =
    field.onChange {
        setImageResource(it)
    }