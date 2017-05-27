package com.gurunars.floatmenu

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.android.bindableField
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setOneView
import org.jetbrains.anko.*

/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
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
 * @property contentView View shown in the background layer of the widget. Semantically it
 * represents the data manipulated by the menu.
 * @property menuView View shown in the foreground layer of the widget when the menu is open.
 * Is supposed to contain menu's controls.
 */
class FloatMenu constructor(context: Context) : FrameLayout(context) {
    val isLeftHanded = bindableField(false)
    val animationDuration = bindableField(400)
    val isOpen = bindableField(false)
    val openIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu))
    val closeIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = bindableField(true)
    val contentView = bindableField(View(context))
    val menuView = bindableField(View(context))

    init {
        relativeLayout {
            fullSize()
            frameLayout {
                id=R.id.contentPane
                contentView.onChange { setOneView(it) }
            }.fullSize()
            menuPane(hasOverlay, isOpen, animationDuration) {
                id=R.id.menuPane
                isClickable=true
                visibility=View.GONE
                menuView.onChange { setOneView(it) }
            }.fullSize()
            fab(animationDuration, openIcon, closeIcon, isOpen) {
                id=R.id.openFab
                val fab = this
                isLeftHanded.onChange { fab.contentDescription = "LH:" + it }
            }.lparams {
                margin=dip(16)
                width=dip(60)
                height=dip(60)
                alignParentBottom()
                val fab = this
                isLeftHanded.onChange {
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    if(it) fab.alignParentLeft() else fab.alignParentRight()
                    requestLayout()
                }
            }
        }

    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putBoolean("isOpen", isOpen.get())
        }
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        isOpen.set(localState.getBoolean("isOpen"))
    }

}

