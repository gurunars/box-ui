package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.field
import org.jetbrains.anko.*

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
 * @param openIcon Icon associated with the open state of the menu. Shown when the menu is
 * closed.
 * @param closeIcon Icon associated with the closed state of the menu. Show when the menu is
 * open.
 * @param hasOverlay If **true** the menu has a shaded background that intercepts clicks.
 * If **false** - the menu does not intercept clicks and passes them to the content area.
 * The flag does not affect clickable elements that are located inside the menu though.
 */
fun Context.floatMenu(
    contentView: BindableField<View>,
    menuView: BindableField<View>,
    animationDuration: BindableField<Int> = 400.field,
    isOpen: BindableField<Boolean> = false.field,
    openIcon: BindableField<Icon> = Icon(icon = R.drawable.ic_menu).field,
    closeIcon: BindableField<Icon> = Icon(icon = R.drawable.ic_menu_close).field,
    hasOverlay: BindableField<Boolean> = true.field
): View = statefulComponent(R.id.floatMenu, "FLOAT MENU") {
    retain(isOpen)
    relativeLayout {
        fullSize()
        frameLayout {
            id = R.id.contentPane
            contentView.onChange { it.setAsOne(this) }
        }.fullSize()
        MenuPane(context, hasOverlay, isOpen, animationDuration).add(this) {
            id = R.id.menuPane
            isClickable = true
            menuView.onChange { it.setAsOne(this) }
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
