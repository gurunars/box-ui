package com.gurunars.item_list

import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.component
import com.gurunars.databinding.android.render

internal fun <ItemType : Item> clickableBind(
    selectedItems: BindableField<Set<ItemType>>,
    itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
    field: BindableField<SelectableItem<ItemType>>
) = component {
    itemViewBinder(field).render(this).apply {
        isClickable = true
        setOnClickListener {
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
        setOnLongClickListener {
            val item = field.get()
            val sel = selectedItems.get()
            if (sel.isEmpty()) selectedItems.set(sel.include(item.item))
            true
        }
    }
}

