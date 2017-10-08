package com.gurunars.item_list

import com.gurunars.databinding.BindableField

class ClickableItemViewBinder<ItemType : Item>(
    private val selectedItems: BindableField<Set<ItemType>>,
    private val itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>
) : ItemViewBinder<SelectableItem<ItemType>> {
    override fun bind(field: BindableField<SelectableItem<ItemType>>) =
        itemViewBinder.bind(field).apply {
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

}
