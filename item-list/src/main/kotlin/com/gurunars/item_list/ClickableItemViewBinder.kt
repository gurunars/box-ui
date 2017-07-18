package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField

typealias SelectableItemViewBinder<ItemType> = (
    context: Context,
    payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>
) -> View

internal fun <ItemType : Item> clickableItemViewBinder(
        selectedItems: BindableField<Set<ItemType>>,
        itemViewBinder: SelectableItemViewBinder<ItemType>
) : ItemViewBinder<SelectableItem<ItemType>> {

    return {
        context: Context,
        itemType: Enum<*>,
        payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>
    ->
        itemViewBinder(context, payload).apply {
            isClickable = true
            setOnClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                if (sel.isEmpty()) {
                    return@setOnClickListener
                }
                if (selectedItems.get().indexOfFirst { it.getId() == item.getId() } == -1) {
                    selectedItems.set(sel + item.item)
                } else {
                    selectedItems.set(sel.filterNot { it.getId() == item.getId() }.toHashSet())
                }
            }
            setOnLongClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                if (sel.isEmpty()) selectedItems.set(sel + item.item)
                true
            }
        }
    }

}