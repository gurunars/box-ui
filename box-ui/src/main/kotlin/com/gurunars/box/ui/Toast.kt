package com.gurunars.box.ui

import android.content.Context
import android.widget.Toast

private fun Context.toast(text: String, duration: Int) =
    Toast.makeText(this, text, duration).show()

fun Context.shortToast(text: String) =
    toast(text, Toast.LENGTH_SHORT)

fun Context.longToast(text: String) =
    toast(text, Toast.LENGTH_SHORT)