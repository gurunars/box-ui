package com.gurunars.item_list

import com.gurunars.databinding.IBox
import com.gurunars.livedata.ui.onClick
import com.gurunars.livedata.ui.onLongClick

internal fun <ItemType : Item> clickableBind(
    selectedItems: IBox<Set<ItemType>>,
    itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
    field: IBox<SelectableItem<ItemType>>,
    explicitSelectionMode: IBox<Boolean>
) =
    itemViewBinder(field).apply {
        isClickable = true
        onClick {
            val item = field.get()
            val sel = selectedItems.get()
            selectedItems.set(
                when {
                    sel.isEmpty() && !explicitSelectionMode.get() -> setOf()
                    selectedItems.get().has(item) -> sel.exclude(item.item)
                    else -> sel.include(item.item)
                }
            )
        }

        onLongClick {
            val item = field.get()
            val sel = selectedItems.get()
            if (sel.isEmpty()) selectedItems.set(sel.include(item.item))
        }
    }
