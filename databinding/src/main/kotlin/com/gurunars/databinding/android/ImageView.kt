package com.gurunars.databinding.android

import android.widget.ImageView
import com.gurunars.databinding.BindableField

fun ImageView.src(field: BindableField<Int>) =
    field.onChange {
        setImageResource(it)
    }