package com.gurunars.android_utils

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

fun ViewManager.iconView(theme: Int = 0) = iconView(theme) {}
fun ViewManager.iconView(theme: Int = 0, init: IconView.() -> Unit) = ankoView({ IconView(it) }, theme, init)
