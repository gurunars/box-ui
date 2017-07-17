package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField

internal class ClickableItemViewBinder<ItemType : Item>(
        private val selectedItems: BindableField<Set<ItemType>>,
        private val itemViewBinder: SelectableItemViewBinder<ItemType>
): ItemViewBinder<SelectableItem<ItemType>> {

    override fun bind(context: Context, payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>): View {
        return itemViewBinder.bind(context, payload).apply {
            isClickable=true
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