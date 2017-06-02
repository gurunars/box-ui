package com.gurunars.floatmenu

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

internal fun ViewManager.fab(theme: Int = 0) = fab(theme) {}
internal fun ViewManager.fab(theme: Int = 0, init: Fab.() -> Unit) = ankoView({ Fab(it) }, theme, init)

internal fun ViewManager.menuPane(theme: Int = 0) = menuPane(theme) {}
internal fun ViewManager.menuPane(theme: Int = 0, init: MenuPane.() -> Unit) = ankoView({ MenuPane(it) }, theme, init)

fun ViewManager.floatMenu(theme: Int = 0) = floatMenu(theme) {}
fun ViewManager.floatMenu(theme: Int = 0, init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it) }, theme, init)