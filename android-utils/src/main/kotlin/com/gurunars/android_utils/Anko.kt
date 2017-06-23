package com.gurunars.android_utils

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

fun ViewManager.iconView(init: IconView.() -> Unit) = ankoView({ IconView(it) }, 0, init)
