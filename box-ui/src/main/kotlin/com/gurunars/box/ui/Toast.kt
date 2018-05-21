package com.gurunars.box.ui

import android.content.Context
import android.widget.Toast

enum class Duration(val type: Int) {
    SHORT(Toast.LENGTH_SHORT),
    LONG(Toast.LENGTH_LONG)
}

fun Context.showToast(text: String, duration: Duration=Duration.SHORT) = Toast.makeText(this, text, duration.type).show()

