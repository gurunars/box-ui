package com.gurunars.android_utils

import android.app.Activity
import android.content.Context
import android.view.ViewManager
import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun ViewManager.iconView(init: IconView.() -> Unit): IconView = ankoView({ IconView(it) }, 0, init)
fun Activity.iconView(init: IconView.() -> Unit): IconView = ankoView({ IconView(it) }, 0, init)
fun Context.iconView(init: IconView.() -> Unit): IconView = ankoView({ IconView(it) }, 0, init)