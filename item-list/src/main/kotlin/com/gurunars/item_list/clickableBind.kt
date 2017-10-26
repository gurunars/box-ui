package com.gurunars.item_list

import com.gurunars.databinding.BindableField

internal fun <ItemType : Item> clickableBind(
    selectedItems: BindableField<Set<ItemType>>,
    itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
    field: BindableField<SelectableItem<ItemType>>
) =
    itemViewBinder(field).apply {
        isClickable = true
        setOnClickListener {
            val item = field.get()
            val sel = selectedItems.get()
            selectedItems.set(
                if (sel.isEmpty())
                    setOf()
                else if (selectedItems.get().has(item))
                    sel.exclude(item.item)
                else
                    sel.include(item.item)
            )

        }
        setOnLongClickListener {
            val item = field.get()
            val sel = selectedItems.get()
            if (sel.isEmpty()) selectedItems.set(sel.include(item.item))
            true
        }
    }

