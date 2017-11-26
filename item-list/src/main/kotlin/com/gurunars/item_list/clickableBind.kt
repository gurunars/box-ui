package com.gurunars.item_list

import com.gurunars.databinding.Box
import com.gurunars.databinding.android.onClick
import com.gurunars.databinding.android.onLongClick

internal fun <ItemType : Item> clickableBind(
    selectedItems: Box<Set<ItemType>>,
    itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
    field: Box<SelectableItem<ItemType>>,
    explicitSelectionMode: Box<Boolean>
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

