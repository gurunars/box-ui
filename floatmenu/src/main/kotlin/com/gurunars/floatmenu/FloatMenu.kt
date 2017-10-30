package com.gurunars.floatmenu

import android.content.Context
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
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
 * @param isLeftHanded If **true** - is on the left side of the screen. On the right side
 * otherwise.
 * @param openIcon Icon associated with the open state of the menu. Shown when the menu is
 * closed.
 * @param closeIcon Icon associated with the closed state of the menu. Show when the menu is
 * open.
 * @param hasOverlay If **true** the menu has a shaded background that intercepts clicks.
 * If **false** - the menu does not intercept clicks and passes them to the content area.
 * The flag does not affect clickable elements that are located inside the menu though.
 *
 * @property isOpen If **true** contents are visible. Menu contents are hidden otherwise.
 * */
class FloatMenu constructor(
    private val contentView: Component,
    private val menuView: Component,
    private val animationDuration: Int = 400,
    private val openButtonEnabled: Boolean = true,
    private val hasOverlay: Boolean = true,
    private val isLeftHanded: Boolean = false,
    private var openIcon: IconView.Icon = IconView.Icon(icon = R.drawable.ic_menu),
    private val closeIcon: IconView.Icon = IconView.Icon(icon = R.drawable.ic_menu_close)
) : Component {

    val isOpen = BindableField(false)

    init {
        if (!openButtonEnabled) {
            openIcon = closeIcon
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
            fab(animationDuration, openIcon, closeIcon, animatedValue, isOpen).add(this) {
                id = R.id.openFab
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
                alignInParent(
                    if (isLeftHanded)
                        HorizontalAlignment.LEFT
                    else
                        HorizontalAlignment.RIGHT
                )
            }
        }
    }

}

