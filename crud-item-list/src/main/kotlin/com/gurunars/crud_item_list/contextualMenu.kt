package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.branch
import com.gurunars.databinding.field
import com.gurunars.databinding.onChange
import com.gurunars.item_list.Item
import org.jetbrains.anko.above
import org.jetbrains.anko.dip
import org.jetbrains.anko.relativeLayout

internal fun <ItemType : Item> Context.contextualMenu(
    sortable: BindableField<Boolean>,
    actionIcon: BindableField<IconColorBundle>,
    items: BindableField<List<ItemType>>,
    selectedItems: BindableField<Set<ItemType>>,
    onEdit: (item: ItemType) -> Unit
) = relativeLayout {
    fullSize()
    id = R.id.contextualMenu

    fun configureIcon(icon: Int, init: View.() -> Unit) {
        val enabled = true.field
        val iconView = iconView(actionIcon.branch { icon(icon) }, enabled)
        iconView.add(this@relativeLayout)
        iconView.init()
        iconView.apply {
            (layoutParams as RelativeLayout.LayoutParams).apply {
                topMargin = dip(5)
                width = dip(45)
                height = dip(45)
            }
            @Suppress("UNCHECKED_CAST")
            val action = getTag(R.id.action) as Action<ItemType>

            listOf(items, selectedItems).onChange {
                enabled.set(action.canPerform(items.get(), selectedItems.get()))
            }

            onClick {
                action.perform(items.get(), selectedItems.get()).apply {
                    if (action.isSynchronous) {
                        items.set(first)
                        selectedItems.set(second)
                    }
                }
            }
        }
    }

    configureIcon(R.drawable.ic_move_up) {
        id = R.id.moveUp
        setTag(R.id.action, ActionMoveUp<ItemType>())
        sortable.onChange { setIsVisible(it) }
        lparams {
            alignInParent(HorizontalAlignment.RIGHT)
            above(R.id.moveDown)
            bottomMargin = dip(5)
            leftMargin = dip(23)
            rightMargin = dip(23)
        }
    }

    configureIcon(R.drawable.ic_move_down) {
        id = R.id.moveDown
        setTag(R.id.action, ActionMoveDown<ItemType>())
        sortable.onChange { setIsVisible(it) }
        lparams {
            alignInParent(
                horizontalAlignment = HorizontalAlignment.RIGHT,
                verticalAlignment = VerticalAlignment.BOTTOM
            )
            bottomMargin = dip(85)
            leftMargin = dip(23)
            rightMargin = dip(23)
        }
    }

    configureIcon(R.drawable.ic_delete) {
        id = R.id.delete
        setTag(R.id.action, ActionDelete<ItemType>())
        lparams {
            alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.selectAll, HorizontalPosition.LEFT_OF)
            bottomMargin = dip(23)
            leftMargin = dip(5)
            rightMargin = dip(5)
        }
    }

    configureIcon(R.drawable.ic_select_all) {
        id = R.id.selectAll
        setTag(R.id.action, ActionSelectAll<ItemType>())
        lparams {
            alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.edit, HorizontalPosition.LEFT_OF)
            bottomMargin = dip(23)
            leftMargin = dip(5)
            rightMargin = dip(5)
        }
    }

    configureIcon(R.drawable.ic_edit) {
        id = R.id.edit
        setTag(R.id.action, ActionEdit({ payload: ItemType -> onEdit(payload) }))
        lparams {
            alignInParent(verticalAlignment = VerticalAlignment.BOTTOM)
            leftMargin = dip(5)
            rightMargin = dip(85)
            bottomMargin = dip(23)
        }
    }

}
