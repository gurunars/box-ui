package com.gurunars.item_list

import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.onClick
import com.gurunars.databinding.android.onLongClick

internal fun <ItemType : Item> clickableBind(
    selectedItems: BindableField<Set<ItemType>>,
    itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
    field: BindableField<SelectableItem<ItemType>>
) =
    itemViewBinder(field).apply {
        isClickable = true
        onClick {
            val item = field.get()
            val sel = selectedItems.get()
            selectedItems.set(
                when {
                    sel.isEmpty() -> setOf()
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

