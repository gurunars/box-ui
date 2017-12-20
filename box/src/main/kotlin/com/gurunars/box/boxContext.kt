package com.gurunars.box

import android.arch.lifecycle.LifecycleOwner

fun <T: LifecycleOwner, R> T.inContext(init: BoxContext<T>.() -> R): R =
    with(BoxContext(this, this)) {
        init()
    }