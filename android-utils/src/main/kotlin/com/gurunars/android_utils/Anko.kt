package com.gurunars.android_utils

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * Anko specific view function for IconView
 *
 * @see IconView
 */
inline fun ViewManager.iconView(init: IconView.() -> Unit) = ankoView({ IconView(it) }, 0, init)
