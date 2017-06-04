package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
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
    val openIcon = bindableField(Icon(icon = R.drawable.ic_menu))
    val closeIcon = bindableField(Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = bindableField(true)
    val contentView = bindableField(View(context))
    val menuView = bindableField(View(context))

    init {
        relativeLayout {
            fullSize()
            frameLayout {
                id=R.id.contentPane
                contentView.bind { setOneView(it) }
            }.fullSize()
            menuPane {
                id=R.id.menuPane
                isClickable=true
                visibility=View.GONE
                this@FloatMenu.animationDuration.bind(animationDuration)
                isOpen.bind(isVisible)
                this@FloatMenu.hasOverlay.bind(hasOverlay)
                menuView.bind { setOneView(it) }
            }.fullSize()
            fab {
                id=R.id.openFab
                val fab = this
                this@FloatMenu.isLeftHanded.bind { fab.contentDescription = "LH:" + it }
                animationDuration.bind(rotationDuration)
                isOpen.bind(isActivated)
                this@FloatMenu.openIcon.bind(openIcon)
                this@FloatMenu.closeIcon.bind(closeIcon)
            }.lparams {
                margin=dip(16)
                width=dip(60)
                height=dip(60)
                alignParentBottom()
                val fab = this
                this@FloatMenu.isLeftHanded.bind {
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    fab.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    if(it) fab.alignParentLeft() else fab.alignParentRight()
                    requestLayout()
                }
            }
        }

    }

}

