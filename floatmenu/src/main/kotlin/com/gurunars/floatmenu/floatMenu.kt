package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.field
import org.jetbrains.anko.*

/**
 * An aggregation of view an an icon that controls its visibility in the menu.
 *
 * @property view View to be shown in float menu
 * @property icon FAB icon to be shown when the view is visible
 */
interface ContentView {
    val view: View
    val icon: Icon
}

/**
 * Content view to be show when the menu is open.
 *
 * @property hasOverlay if false - a view below will be clickable
 */
interface MenuView: ContentView {
    val hasOverlay: Boolean
}

/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
 *
 * @param contentView View shown in the background layer of the widget. Semantically it
 * represents the data manipulated by the menu.
 * @param menuView View shown in the foreground layer of the widget when the menu is open.
 * Is supposed to contain menu's controls.
 * @param animationDuration Time it takes to perform all the animated UI transitions.
 * @param isOpen If **true** contents are visible. Menu contents are hidden otherwise.
 * @param hasOverlay If **true** the menu has a shaded background that intercepts clicks.
 * If **false** - the menu does not intercept clicks and passes them to the content area.
 * The flag does not affect clickable elements that are located inside the menu though.
 */
fun Context.floatMenu(
    contentView: ContentView,
    menuView: MenuView,
    animationDuration: Int = 400,
    isOpen: BindableField<Boolean> = false.field
): View = statefulComponent(R.id.floatMenu, "FLOAT MENU") {
    retain(isOpen)

    val openIcon = contentView.icon.field
    val closeIcon = menuView.icon.field
    val hasOverlay = menuView.hasOverlay.field

    isOpen.onChange {
        if (it) {
            hasOverlay.set(menuView.hasOverlay)
            closeIcon.set(menuView.icon)
        } else {
            openIcon.set(contentView.icon)
        }
    }

    relativeLayout {
        fullSize()
        frameLayout {
            id = R.id.contentPane
            contentView.view.setAsOne(this)
        }.fullSize()

        MenuPane(context, hasOverlay, isOpen, animationDuration).add(this) {
            id = R.id.menuPane
            isClickable = true
            menuView.view.setAsOne(this)
        }.fullSize()
        context.fab(animationDuration, openIcon, closeIcon, isOpen).add(this) {
            id = R.id.openFab
        }.lparams {
            margin = dip(16)
            width = dip(60)
            height = dip(60)
            alignParentBottom()
            alignInParent(HorizontalAlignment.RIGHT)
        }
    }
}
