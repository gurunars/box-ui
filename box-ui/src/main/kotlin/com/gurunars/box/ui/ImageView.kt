package com.gurunars.box.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.gurunars.box.IRoBox

/** Binds to boxed image resource int. */
fun ImageView.resource(field: IRoBox<Int>) =
    field.onChange(listener=this::setImageResource)

/** Binds to boxed drawable. */
fun ImageView.drawable(field: IRoBox<Drawable>) =
    field.onChange(listener=this::setImageDrawable)
