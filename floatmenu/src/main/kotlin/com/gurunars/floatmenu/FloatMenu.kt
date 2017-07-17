package com.gurunars.floatmenu

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setOneView
import org.jetbrains.anko.*

/**
 * Floating menu available via a [FAB](https://material.google.com/components/buttons-floating-action-button.html).
 */
class FloatMenu constructor(context: Context) : FrameLayout(context) {

    /**
     * If **true** - is on the left side of the screen. On the right side otherwise.
     */
    val isLeftHanded = bindableField(false)
    /**
     * Time it takes to perform all the animated UI transitions
     */
    val animationDuration = bindableField(400)
    /**
     * If **true** contents are visible. Menu contents are hidden otherwise.
     */
    val isOpen = bindableField(false)
    /**
     * Icon associated with the open state of the menu. Shown when the menu is closed.
     */
    val openIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu))
    /**
     * Icon associated with the closed state of the menu. Show when the menu is open.
     */
    val closeIcon = bindableField(IconView.Icon(icon = R.drawable.ic_menu_close))
    /**
     * If **true** the menu has a shaded background that intercepts clicks. If **false** - the menu
     * does not intercept clicks and passes them to the content area. The flag does not affect
     * clickable elements that are located inside the menu though.
     */
    val hasOverlay = bindableField(true)
    /**
     * View shown in the background layer of the widget. Semantically it represents the data
     * manipulated by the menu.
     */
    val contentView = bindableField(View(context))
    /**
     * View shown in the foreground layer of the widget when the menu is open. Is supposed to
     * contain menu's controls.
     */
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
                this@FloatMenu.isLeftHanded.onChange { fab.contentDescription = "LH:" + it }
            }.lparams {
                margin=dip(16)
                width=dip(60)
                height=dip(60)
                alignParentBottom()
                val fab = this
                this@FloatMenu.isLeftHanded.onChange {
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    if(it) fab.alignParentLeft() else fab.alignParentRight()
                    requestLayout()
                }
            }
        }

    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putBoolean("isOpen", isOpen.get())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        isOpen.set(localState.getBoolean("isOpen"))
    }

}

