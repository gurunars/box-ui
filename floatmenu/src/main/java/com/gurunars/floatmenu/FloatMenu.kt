package com.gurunars.floatmenu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import org.jetbrains.anko.*

/**
 * Floating menu available via a [FAB](https://material.google.com/components/buttons-floating-action-button.html).
 */
class FloatMenu constructor(context: Context) : FrameLayout(context) {

    private lateinit var openFab: Fab
    private lateinit var menuPane: MenuPane
    private lateinit var contentPane: ViewGroup

    private var isLeftHanded: Boolean = false

    private var onCloseListener: AnimationListener? = null
    private var onOpenListener: AnimationListener? = null

    init {
        onOpenListener = AnimationListener.Default()
        onCloseListener = onOpenListener

        relativeLayout {
            layoutParams = LayoutParams(matchParent, matchParent)
            contentPane=frameLayout {
                id=R.id.contentPane
            }.lparams {
                width=matchParent
                height=matchParent
            }
            menuPane=menuPane {
                id=R.id.menuPane
                visibility=View.GONE
            }.lparams {
                width=matchParent
                height=matchParent
            }
            openFab=fab {
                id=R.id.openFab
            }.lparams {
                margin=dip(16)
                width=dip(60)
                height=dip(60)
                alignParentBottom()
                alignParentRight()
            }
        }

        openFab.setOnClickListener { setFloatingMenuVisibility(!openFab.isActivated) }

        setAnimationDuration(DURATION_IN_MILLIS)
        setHasOverlay(true)
        setLeftHanded(false)

    }

    private fun setFloatingMenuVisibility(visible: Boolean) {

        if (isOpen == visible) {
            return
        }

        openFab.isClickable = false
        openFab.isActivated = visible
        menuPane.isActivated = visible

        val listener = if (visible) onOpenListener else onCloseListener
        val targetVisibility = if (visible) View.VISIBLE else View.GONE
        val sourceAlpha = if (visible) 0.0f else 1.0f
        val targetAlpha = if (visible) 1.0f else 0.0f

        listener?.onStart(openFab.rotationDuration)
        menuPane.visibility = View.VISIBLE
        menuPane.alpha = sourceAlpha
        menuPane.animate()
                .alpha(targetAlpha)
                .setDuration(openFab.rotationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        menuPane.visibility = targetVisibility
                        listener?.onFinish()
                        openFab.isClickable = true
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        onAnimationEnd(animation)
                    }
                })
    }

    /**
     * @return true if the menu is opened.
     */
    val isOpen: Boolean
        get() = menuPane.visibility == View.VISIBLE

    /*
     * Collapse the menu.
     */
    fun close() {
        setFloatingMenuVisibility(false)
    }

    /**
     * Expand the menu.
     */
    fun open() {
        setFloatingMenuVisibility(true)
    }

    /**
     * @param contentView view to be shown in the content area (clickable when the menu is closed)
     */
    fun setContentView(contentView: View) {
        contentPane.removeAllViews()
        contentPane.addView(contentView)
    }

    /**
     * @param onCloseListener actions to be triggered before and after the menu is closed
     */
    fun setOnCloseListener(onCloseListener: AnimationListener) {
        this.onCloseListener = onCloseListener
    }

    /**
     * @param onOpenListener actions to be triggered before and after the menu is open
     */
    fun setOnOpenListener(onOpenListener: AnimationListener) {
        this.onOpenListener = onOpenListener
    }

    /**
     * @param menuView view to be shown in the menu area (clickable when the menu is open)
     */
    fun setMenuView(menuView: View) {
        menuPane.removeAllViews()
        menuPane.addView(menuView)
    }

    /**
     * @param hasOverlay false to disable a shaded background, true to enable it. If overlay is
     * * disabled the clicks go through the view group to the view in the back. If it is enabled the
     * * clicks are intercepted by the group.
     */
    fun setHasOverlay(hasOverlay: Boolean) {
        menuPane.isClickable = hasOverlay
    }

    /**
     * @param leftHanded if true - FAB shall be in the bottom left corner, if false - in the bottom right.
     */
    fun setLeftHanded(leftHanded: Boolean) {
        contentDescription = "LH:" + leftHanded
        this.isLeftHanded = leftHanded
        val layout = openFab.layoutParams as RelativeLayout.LayoutParams
        layout.removeRule(if (isLeftHanded)
            RelativeLayout.ALIGN_PARENT_RIGHT
        else
            RelativeLayout.ALIGN_PARENT_LEFT)
        layout.addRule(if (isLeftHanded)
            RelativeLayout.ALIGN_PARENT_LEFT
        else
            RelativeLayout.ALIGN_PARENT_RIGHT)
        openFab.layoutParams = layout
    }

    /**
     * @param durationInMillis FAB rotation and menu appearence duration in milliseconds.
     */
    fun setAnimationDuration(durationInMillis: Int) {
        openFab.setRotationDuration(durationInMillis)
    }

    /**
     * @param icon - to be shown in the button clicking which opens the menu.
     */
    fun setOpenIcon(icon: Icon) {
        openFab.setOpenIcon(icon)
    }

    /**
     * @param icon - to be shown in the button clicking which closes the menu.
     */
    fun setCloseIcon(icon: Icon) {
        openFab.setCloseIcon(icon)
    }

    companion object {
        private val DURATION_IN_MILLIS = 400
    }

}

