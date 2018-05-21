package com.gurunars.box.ui

import android.content.Context
import android.widget.Button

fun Context.button(init: Button.() -> Unit) = Button(this).apply { init() }