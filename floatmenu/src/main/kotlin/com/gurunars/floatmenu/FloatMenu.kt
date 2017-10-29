package com.gurunars.floatmenu

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.HorizontalAlignment
import com.gurunars.databinding.android.StatefulWidget
import com.gurunars.databinding.android.fullSize
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
 *
 * @property isLeftHanded If **true** - is on the left side of the screen. On the right side
 * otherwise.
 * @property isOpen If **true** contents are visible. Menu contents are hidden otherwise.
 * @property openIcon Icon associated with the open state of the menu. Shown when the menu is
 * closed.
 * @property closeIcon Icon associated with the closed state of the menu. Show when the menu is
 * open.
 * @property hasOverlay If **true** the menu has a shaded background that intercepts clicks.
 * If **false** - the menu does not intercept clicks and passes them to the content area.
 * The flag does not affect clickable elements that are located inside the menu though.
 */
@SuppressLint("ViewConstructor")
class FloatMenu constructor(
    context: Context,
    contentView: View,
    menuView: View,
    animationDuration: Int = 400
) : StatefulWidget(context) {
    val isLeftHanded = BindableField(false)
    val isOpen = BindableField(false)
    val openIcon = BindableField(IconView.Icon(icon = R.drawable.ic_menu))
    val closeIcon = BindableField(IconView.Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = BindableField(true)

    init {
        retain(isOpen)
        relativeLayout {
            fullSize()
            frameLayout {
                id = R.id.contentPane
                contentView.setAsOne(this)
            }.fullSize()
            MenuPane(context, hasOverlay, isOpen, animationDuration).add(this) {
                id = R.id.menuPane
                isClickable = true
                menuView.setAsOne(this)
            }.fullSize()
            context.fab(animationDuration, openIcon, closeIcon, isOpen).add(this) {
                id = R.id.openFab
                isLeftHanded.onChange { contentDescription = "LH:" + it }
            }.lparams {
                margin = dip(16)
                width = dip(60)
                height = dip(60)
                alignParentBottom()
                isLeftHanded.onChange {
                    alignInParent(if (it) HorizontalAlignment.LEFT else HorizontalAlignment.RIGHT)
                    requestLayout()
                }
            }
        }
    }

}

