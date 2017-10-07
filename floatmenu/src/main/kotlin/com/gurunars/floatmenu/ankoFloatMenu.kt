package com.gurunars.floatmenu

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewManager
import kotlin.Int
import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun ViewManager.floatMenu(
    contentView: View,
    menuView: View,
    animationDuration: Int = 400,
    init: FloatMenu.() -> Unit
): FloatMenu = ankoView({ FloatMenu(it, contentView, menuView, animationDuration) }, 0, init)

fun Activity.floatMenu(
    contentView: View,
    menuView: View,
    animationDuration: Int = 400,
    init: FloatMenu.() -> Unit
): FloatMenu = ankoView({ FloatMenu(it, contentView, menuView, animationDuration) }, 0, init)

fun Context.floatMenu(
    contentView: View,
    menuView: View,
    animationDuration: Int = 400,
    init: FloatMenu.() -> Unit
): FloatMenu = ankoView({ FloatMenu(it, contentView, menuView, animationDuration) }, 0, init)