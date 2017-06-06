package com.gurunars.crud_item_list


import android.content.Context
import android.os.Parcelable
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RelativeLayout

import com.gurunars.android_utils.CircularIconButton

import java.util.Arrays

import icepick.Icepick
import icepick.State

import android.widget.RelativeLayout.ALIGN_PARENT_LEFT
import android.widget.RelativeLayout.ALIGN_PARENT_RIGHT
import android.widget.RelativeLayout.LEFT_OF
import android.widget.RelativeLayout.RIGHT_OF

internal class ContextualMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val buttons: List<CircularIconButton>

    @State var iconBgColor: Int = 0
    @State var iconFgColor: Int = 0
    @State var leftHanded = false
    @State var sortable = true

    init {
        View.inflate(context, R.layout.contextual_menu, this)
        buttons = Arrays.asList<CircularIconButton>(
                getButton(R.id.delete),
                getButton(R.id.edit),
                getButton(R.id.selectAll),
                getButton(R.id.moveDown),
                getButton(R.id.moveUp)
        )
    }

    private fun getParams(@IdRes id: Int): RelativeLayout.LayoutParams {
        val params = findViewById(id).layoutParams as RelativeLayout.LayoutParams
        params.removeRule(ALIGN_PARENT_RIGHT)
        params.removeRule(ALIGN_PARENT_LEFT)
        params.removeRule(LEFT_OF)
        params.removeRule(RIGHT_OF)
        return params
    }

    fun setLeftHanded(leftHanded: Boolean) {
        this.leftHanded = leftHanded
        val moveUpParams = getParams(R.id.moveUp)
        val moveDownParams = getParams(R.id.moveDown)
        val editParams = getParams(R.id.edit)
        val deleteParams = getParams(R.id.delete)
        val selectAllParams = getParams(R.id.selectAll)

        moveUpParams.addRule(if (leftHanded) ALIGN_PARENT_LEFT else ALIGN_PARENT_RIGHT)
        moveDownParams.addRule(if (leftHanded) ALIGN_PARENT_LEFT else ALIGN_PARENT_RIGHT)

        editParams.addRule(if (leftHanded) ALIGN_PARENT_LEFT else ALIGN_PARENT_RIGHT)
        editParams.leftMargin = resources.getDimensionPixelSize(
                if (leftHanded) R.dimen.largeMargin else R.dimen.smallMargin)
        editParams.rightMargin = resources.getDimensionPixelSize(
                if (leftHanded) R.dimen.smallMargin else R.dimen.largeMargin)

        selectAllParams.addRule(if (leftHanded) RIGHT_OF else LEFT_OF, R.id.edit)
        deleteParams.addRule(if (leftHanded) RIGHT_OF else LEFT_OF, R.id.selectAll)

        contentDescription = if (leftHanded) "LEFT HANDED" else "RIGHT HANDED"

        findViewById(R.id.menuContainer).requestLayout()
    }

    fun setSortable(sortable: Boolean) {
        this.sortable = sortable
        findViewById(R.id.moveUp).visibility = if (sortable) View.VISIBLE else View.GONE
        findViewById(R.id.moveDown).visibility = if (sortable) View.VISIBLE else View.GONE
    }

    fun setIconFgColor(iconFgColor: Int) {
        this.iconFgColor = iconFgColor
        for (button in buttons) {
            button.setForegroundColor(iconFgColor)
        }
    }

    fun setIconBgColor(iconBgColor: Int) {
        this.iconBgColor = iconBgColor
        for (button in buttons) {
            button.setBackgroundColor(iconBgColor)
        }
    }

    private fun getButton(id: Int): CircularIconButton {
        return findViewById(id) as CircularIconButton
    }

    public override fun onSaveInstanceState(): Parcelable {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState())
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state))
        setIconBgColor(iconBgColor)
        setIconFgColor(iconFgColor)
        setSortable(sortable)
        setLeftHanded(leftHanded)
    }

}
