package com.gurunars.floatmenu

import android.app.Activity
import android.view.View
import android.view.ViewManager
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.floatMenu(
    contentView: View,
    menuView: View,
    init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it, contentView, menuView) }, 0, init)

inline fun Activity.floatMenu(
    contentView: View,
    menuView: View,
    init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it, contentView, menuView) }, 0, init)