package com.gurunars.floatmenu

import android.content.Context
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.android_utils.IconView
import com.gurunars.anko_generator.AnkoComponent
import com.gurunars.databinding.android.StatefulComponent
import com.gurunars.databinding.android.bindableField
import com.gurunars.shortcuts.HorizontalAlignment
import com.gurunars.shortcuts.alignInParent
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setAsOne
import org.jetbrains.anko.*

/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
 *
 * @property contentView View shown in the background layer of the widget. Semantically it
 * represents the data manipulated by the menu.
 * @property menuView View shown in the foreground layer of the widget when the menu is open.
 * Is supposed to contain menu's controls.
 *
 * @property isLeftHanded If **true** - is on the left side of the screen. On the right side
 * otherwise.
 * @property animationDuration Time it takes to perform all the animated UI transitions.
 * @property isOpen If **true** contents are visible. Menu contents are hidden otherwise.
 * @property openIcon Icon associated with the open state of the menu. Shown when the menu is
 * closed.
 * @property closeIcon Icon associated with the closed state of the menu. Show when the menu is
 * open.
 * @property hasOverlay If **true** the menu has a shaded background that intercepts clicks.
 * If **false** - the menu does not intercept clicks and passes them to the content area.
 * The flag does not affect clickable elements that are located inside the menu though.
 */
@AnkoComponent
class FloatMenu constructor(context: Context) : StatefulComponent(context) {

    companion object {
        private fun Context.getPane(@StringRes stringId: Int): View = TextView(this).apply {
            gravity = Gravity.CENTER
            text = context.getString(stringId)
            fullSize()
        }
    }

    val contentView = bindableField(context.getPane(R.string.contentView))
    val menuView = bindableField(context.getPane(R.string.menuView))
    val isLeftHanded = bindableField(false)
    val animationDuration = bindableField(400)
    val isOpen = bindableField(false)
    val openIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu))
    val closeIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = bindableField(true)

    init {
        retain(isOpen)
        relativeLayout {
            fullSize()
            frameLayout {
                id = R.id.contentPane
                contentView.onChange { it.setAsOne(this) }
            }.fullSize()
            menuPane(hasOverlay, isOpen, animationDuration) {
                id = R.id.menuPane
                isClickable = true
                menuView.onChange { it.setAsOne(this) }
            }.fullSize()
            fab(animationDuration, openIcon, closeIcon, isOpen) {
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

