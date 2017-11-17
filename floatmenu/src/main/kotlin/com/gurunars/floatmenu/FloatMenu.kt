package com.gurunars.floatmenu

import android.content.Context
import android.os.Handler
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.Observable
import com.gurunars.databinding.android.*
import com.gurunars.databinding.branch
import org.jetbrains.anko.*

/**
 * An aggregation of view an an icon that controls its visibility in the menu.
 *
 * @property icon FAB icon to be shown when the view is visible
 */
interface ContentPane: Component {
    val icon: Icon
}

/**
 * Content view to be show when the menu is open.
 *
 * @property hasOverlay if false - a view below will be clickable
 */
interface MenuPane: ContentPane {
    val hasOverlay: Boolean
}

/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
 *
 * @param contentPane View shown in the background layer of the widget. Semantically it
 * represents the data manipulated by the menu.
 * @param menuPane View shown in the foreground layer of the widget when the menu is open.
 * Is supposed to contain menu's controls.
 * @param animationDuration Time it takes to perform all the animated UI transitions.
 */
class FloatMenu(
    private val contentPane: BindableField<ContentPane>,
    private val menuPane: BindableField<MenuPane>,
    private val animationDuration: Int = 400
): Component {
    val isOpen = BindableField(false)

    override fun Context.render() = statefulView(R.id.floatMenu, "FLOAT MENU") {
        retain(isOpen)

        relativeLayout {
            fullSize()
            frameLayout {
                id = R.id.contentPane
                contentPane.onChange { value ->
                    value.setAsOne(this)
                }
            }.fullSize()

            MenuHolder(context,
                menuPane.branch { hasOverlay },
                isOpen,
                animationDuration
            ).add(this) {
                id = R.id.menuPane
                fullSize()
                isClickable = true
                menuPane.onChange { value ->
                    value.setAsOne(this)
                }
            }
            fab(animationDuration,
                contentPane.branch { icon },
                menuPane.branch { icon },
                isOpen
            ).add(this) {
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
}
