package com.gurunars.box.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView

fun Context.imageView(init: ImageView.() -> Unit) = ImageView(this).apply {
    init()
}

var ImageView.src by PlainConsumerField<ImageView, Drawable?> { setImageDrawable(it)  }

var ImageView.src_ by ConsumerField<ImageView, Drawable> { setImageDrawable(it)  }
