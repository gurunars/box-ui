package com.gurunars.floatmenu

import android.content.Context
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

}

