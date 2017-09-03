package com.gurunars.crud_item_list


import android.content.Context
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.onChange
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.*
import org.jetbrains.anko.*


internal fun<ItemType: Item> contextualMenu (
    context: Context,
    actionIcon: BindableField<CrudItemListView.IconColorBundle>,
    isLeftHanded: BindableField<Boolean>,
    isSortable: BindableField<Boolean>,
    items: BindableField<List<ItemType>>,
    selectedItems: BindableField<Set<ItemType>>,
    itemEditListener: (item: ItemType) -> Unit
) = FrameLayout(context).apply {

    isLeftHanded.onChange {
        contentDescription = if (it) "LEFT HANDED" else "RIGHT HANDED"
    }

    fun RelativeLayout.LayoutParams.isLeftHanded(flag: Boolean) {
        alignInParent(
            if (flag)
                HorizontalAlignment.LEFT
            else
                HorizontalAlignment.RIGHT
        )
    }

    fun RelativeLayout.LayoutParams.alignHorizontallyAroundElement(id: Int, flag: Boolean) {
        alignWithRespectTo(id,
            if (flag)
                HorizontalPosition.RIGHT_OF
            else
                HorizontalPosition.LEFT_OF
        )
    }

    relativeLayout {
        fullSize()
        R.id.menuContainer

        isLeftHanded.onChange {
            requestLayout()
        }

        iconView {
            id=R.id.moveUp
            icon.set(IconView.Icon(icon=R.drawable.ic_move_up))
            setTag(R.id.action, ActionMoveUp<ItemType>())
            isSortable.onChange(listener=this::setIsVisible)
        }.lparams {
            isLeftHanded.onChange(listener=this::isLeftHanded)
            above(R.id.moveDown)
            bottomMargin=dip(5)
            leftMargin=dip(23)
            rightMargin=dip(23)
        }

        iconView {
            id=R.id.moveDown
            icon.set(IconView.Icon(icon=R.drawable.ic_move_down))
            setTag(R.id.action, ActionMoveDown<ItemType>())
            isSortable.onChange(listener=this::setIsVisible)
        }.lparams {
            alignInParent(verticalAlignment=VerticalAlignment.BOTTOM)
            isLeftHanded.onChange(listener=this::isLeftHanded)
            bottomMargin=dip(85)
            leftMargin=dip(23)
            rightMargin=dip(23)
        }

        iconView {
            id=R.id.delete
            icon.set(IconView.Icon(icon=R.drawable.ic_delete))
            setTag(R.id.action, ActionDelete<ItemType>())
        }.lparams {
            alignInParent(verticalAlignment=VerticalAlignment.BOTTOM)
            isLeftHanded.onChange { alignHorizontallyAroundElement(R.id.selectAll, it)}
            bottomMargin=dip(23)
            leftMargin=dip(5)
            rightMargin=dip(5)
        }

        iconView {
            id=R.id.selectAll
            icon.set(IconView.Icon(icon=R.drawable.ic_select_all))
            setTag(R.id.action, ActionSelectAll<ItemType>())
        }.lparams {
            alignInParent(verticalAlignment=VerticalAlignment.BOTTOM)
            isLeftHanded.onChange { alignHorizontallyAroundElement(R.id.edit, it)}
            bottomMargin=dip(23)
            leftMargin=dip(5)
            rightMargin=dip(5)
        }

        iconView {
            id=R.id.edit
            icon.set(IconView.Icon(icon=R.drawable.ic_edit))
            setTag(R.id.action, ActionEdit({ payload: ItemType -> itemEditListener(payload) }))
        }.lparams {
            alignInParent(verticalAlignment=VerticalAlignment.BOTTOM)
            isLeftHanded.onChange {
                this.isLeftHanded(it)
                if (it) {
                    leftMargin=dip(85)
                    rightMargin=dip(5)
                } else {
                    leftMargin=dip(5)
                    rightMargin=dip(85)
                }
            }
            bottomMargin=dip(23)
        }

    }.applyRecursively { when(it) {
        is IconView -> {
            (it.layoutParams as RelativeLayout.LayoutParams).apply {
                topMargin=dip(5)
                width=dip(45)
                height=dip(45)
            }
            val action = it.getTag(R.id.action) as Action<ItemType>

            listOf(items, selectedItems).onChange {
                it.isEnabled = action.canPerform(items.get(), selectedItems.get())
            }

            it.setOnClickListener {
                action.perform(items.get(), selectedItems.get()).apply {
                    if (action.isSynchronous) {
                        items.set(first)
                        selectedItems.set(second)
                    }
                }
            }

            actionIcon.onChange { icon ->
                it.icon.set(it.icon.get().copy(
                    bgColor = icon.bgColor,
                    fgColor = icon.fgColor
                ))
            }
        }
    } }
}
