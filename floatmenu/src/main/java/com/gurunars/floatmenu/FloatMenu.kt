package com.gurunars.floatmenu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.*

/**
 * Floating menu available via a [FAB](https://material.google.com/components/buttons-floating-action-button.html).
 */
class FloatMenu constructor(context: Context) : FrameLayout(context) {

    private lateinit var openFab: Fab
    private lateinit var menuPane: MenuPane
    private lateinit var contentPane: ViewGroup

    val isLeftHanded = BindableField(false)
    val animationDuration = BindableField(400)
    val isOpen = BindableField(false)
    val openIcon = BindableField(Icon(icon = R.drawable.ic_menu))
    val closeIcon = BindableField(Icon(icon = R.drawable.ic_menu_close))
    val hasOverlay = BindableField(true)

    init {
        relativeLayout {
            layoutParams = LayoutParams(matchParent, matchParent)
            contentPane=frameLayout {
                id=R.id.contentPane
            }.lparams {
                width=matchParent
                height=matchParent
            }
            menuPane=menuPane {
                isClickable=true
                id=R.id.menuPane
                visibility=View.GONE
                animationDuration.bind(this@FloatMenu.animationDuration)
                isVisible.bind(isOpen)
                hasOverlay.bind(this@FloatMenu.hasOverlay)
            }.lparams {
                width=matchParent
                height=matchParent
            }
            openFab=fab {
                val fab = this
                this@FloatMenu.isLeftHanded.bind { fab.contentDescription = "LH:" + it }
                id=R.id.openFab
                animationDuration.bind(rotationDuration)
                isActivated.bind(isOpen)
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

    /**
     * @param contentView view to be shown in the content area (clickable when the menu is closed)
     */
    fun setContentView(contentView: View) {
        contentPane.removeAllViews()
        contentPane.addView(contentView)
    }

    /**
     * @param menuView view to be shown in the menu area (clickable when the menu is open)
     */
    fun setMenuView(menuView: View) {
        menuPane.removeAllViews()
        menuPane.addView(menuView)
    }

}

