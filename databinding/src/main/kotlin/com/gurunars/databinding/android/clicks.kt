package com.gurunars.databinding.android

import android.view.View

fun View.onLongClick(callback: () -> Any) = setOnLongClickListener {
    callback()
    true
}

fun View.onClick(callback: () -> Any) = setOnClickListener {
    callback()
}
