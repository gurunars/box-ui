package com.gurunars.box.ui

import android.view.View

/** @see View.setOnLongClickListener */
fun View.onLongClick(callback: () -> Any) = setOnLongClickListener {
    callback()
    true
}

/** @see View.setOnClickListener */
fun View.onClick(callback: () -> Any) = setOnClickListener {
    callback()
}
