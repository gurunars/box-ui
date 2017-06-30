package com.gurunars.android_utils

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * IconView Anko view function
 */
inline fun ViewManager.iconView(init: IconView.() -> Unit) = ankoView({ IconView(it) }, 0, init)
