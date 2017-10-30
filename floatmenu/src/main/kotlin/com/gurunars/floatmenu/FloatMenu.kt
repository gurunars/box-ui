package com.gurunars.floatmenu

import android.content.Context
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.sendTo
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
class FloatMenu constructor(
    private val contentView: Component,
    private val menuView: Component,
    private val animationDuration: Int = 400,
    private val openButtonEnabled: Boolean = true
) : Component {
    val isLeftHanded = BindableField(false)
    val isOpen = BindableField(false)
    val openIcon = BindableField(IconView.Icon(icon = R.drawable.ic_menu))
    val closeIcon = BindableField(IconView.Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = BindableField(true)

    init {
        if (!openButtonEnabled) {
            closeIcon.sendTo(openIcon)
            openIcon.onChange {
                openIcon.set(closeIcon.get())
            }
        }
    }

    override fun Context.render() = statefulWidget(R.id.floatMenu, isOpen) {
        val animatedValue = BindableField(1f)

        fullSize()
        relativeLayout {
            fullSize()
            frameLayout {
                id = R.id.contentPane
                contentView.setAsOne(this@statefulWidget)
            }.fullSize()
            MenuPane(context, hasOverlay, isOpen, animationDuration).add(this) {
                id = R.id.menuPane
                isClickable = true
                menuView.setAsOne(this)
            }.fullSize()
            context.fab(animatedValue, animationDuration, openIcon, closeIcon, isOpen).add(this) {
                id = R.id.openFab
                isLeftHanded.onChange { contentDescription = "LH:" + it }
            }.lparams {
                val initMargin = dip(16)
                val size = dip(60)
                if (openButtonEnabled) {
                    width = size
                    height = size
                    margin = initMargin
                } else {
                    animatedValue.onChange {
                        val dims: Int = if (isOpen.get()) {
                            (size * it).toInt()
                        } else {
                            (size * (1f - it)).toInt()
                        }
                        margin = initMargin + ((size - dims) / 2)
                        width = dims
                        height = dims
                    }
                }
                alignParentBottom()
                isLeftHanded.onChange {
                    alignInParent(if (it) HorizontalAlignment.LEFT else HorizontalAlignment.RIGHT)
                    requestLayout()
                }
            }
        }
    }

}

