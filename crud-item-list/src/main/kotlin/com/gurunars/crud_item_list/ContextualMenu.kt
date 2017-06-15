package com.gurunars.crud_item_list


import android.content.Context
import android.widget.FrameLayout
import android.widget.RelativeLayout.*
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.relativeLayout


internal class ContextualMenu constructor(context: Context) : FrameLayout(context) {

    val isLeftHanded = bindableField(false)
    val isSortable = bindableField(true)

    init {
        relativeLayout {
            fullSize()
            R.id.menuContainer

            iconView {
                id=R.id.moveUp
            }

            iconView {
                id=R.id.moveDown
            }

            iconView {
                id=R.id.delete
            }

            iconView {
                id=R.id.edit
            }

            iconView {
                id=R.id.selectAll
            }

        }
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

}
