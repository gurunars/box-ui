package com.gurunars.floatmenu

import android.view.ViewManager
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.custom.ankoView

internal inline fun ViewManager.fab(
        rotationDuration: BindableField<Int>,
        openIcon: BindableField<IconView.Icon>,
        closeIcon: BindableField<IconView.Icon>,
        isActivated: BindableField<Boolean>,
        theme: Int = 0, init: Fab.() -> Unit) = ankoView({
            Fab(it, rotationDuration, openIcon, closeIcon, isActivated)
        }, theme, init)

internal inline fun ViewManager.menuPane(
        hasOverlay: BindableField<Boolean>,
        isVisible: BindableField<Boolean>,
        animationDuration: BindableField<Int>,
        theme: Int = 0, init: MenuPane.() -> Unit) = ankoView({
            MenuPane(it, hasOverlay, isVisible, animationDuration)
        }, theme, init)

inline fun ViewManager.floatMenu(init: FloatMenu.() -> Unit) = ankoView({ FloatMenu(it) }, 0, init)