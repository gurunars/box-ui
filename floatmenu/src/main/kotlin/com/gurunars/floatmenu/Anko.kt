package com.gurunars.floatmenu

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewManager
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.custom.ankoView

fun Context.floatMenu(
    init: FloatMenu.() -> Unit = {}
) =
    ankoView({ FloatMenu(it) }, 0, init)

fun ViewManager.floatMenu(
    init: FloatMenu.() -> Unit = {}
) =
    ankoView({ FloatMenu(it) }, 0, init)

fun Activity.floatMenu(
    init: FloatMenu.() -> Unit = {}
) =
    ankoView({ FloatMenu(it) }, 0, init)