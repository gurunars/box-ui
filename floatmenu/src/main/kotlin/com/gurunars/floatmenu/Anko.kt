package com.gurunars.floatmenu

import android.app.Activity
import android.view.View
import android.view.ViewManager
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.custom.ankoView

internal fun ViewManager.fab(
    rotationDuration: BindableField<Int>,
    openIcon: BindableField<IconView.Icon>,
    closeIcon: BindableField<IconView.Icon>,
    isActivated: BindableField<Boolean>,
    theme: Int = 0, init: Fab.() -> Unit) = ankoView({
    Fab(it, rotationDuration, openIcon, closeIcon, isActivated)
}, theme, init)

internal fun ViewManager.menuPane(
    hasOverlay: BindableField<Boolean>,
    isVisible: BindableField<Boolean>,
    animationDuration: BindableField<Int>,
    theme: Int = 0, init: MenuPane.() -> Unit) = ankoView({
    MenuPane(it, hasOverlay, isVisible, animationDuration)
}, theme, init)

/**
 * Anko specific view function for FloatMenu
 *
 * @see FloatMenu
 */
inline fun ViewManager.floatMenu(
    contentView: View,
    menuView: View,
    init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it, contentView, menuView) }, 0, init)

/**
 * Anko specific view function for FloatMenu
 *
 * @see FloatMenu
 */
inline fun Activity.floatMenu(
    contentView: View,
    menuView: View,
    init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it, contentView, menuView) }, 0, init)