package com.gurunars.floatmenu

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

internal inline fun ViewManager.fab(theme: Int = 0) = fab(theme) {}
internal inline fun ViewManager.fab(theme: Int = 0, init: Fab.() -> Unit) = ankoView({ Fab(it) }, theme, init)

internal inline fun ViewManager.menuPane(theme: Int = 0) = menuPane(theme) {}
internal inline fun ViewManager.menuPane(theme: Int = 0, init: MenuPane.() -> Unit) = ankoView({ MenuPane(it) }, theme, init)

inline fun ViewManager.floatMenu(theme: Int = 0) = floatMenu(theme) {}
inline fun ViewManager.floatMenu(theme: Int = 0, init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it) }, theme, init)