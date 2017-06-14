package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.l


internal class ClickableItemViewBinder<ViewType : View, ItemType : Item>(
        private val itemViewBinder: ItemViewBinder<ViewType, SelectableItem<ItemType>>,
        private val selectedItems: BindableField<Set<ItemType>>) : ItemViewBinder<ViewType, SelectableItem<ItemType>> {

    override fun getView(context: Context): ViewType {
        return itemViewBinder.getView(context)
    }

    override fun bind(itemView: ViewType, item: SelectableItem<ItemType>, previousItem: SelectableItem<ItemType>?) {
        itemViewBinder.bind(itemView, item, previousItem)
        itemView.apply {
            isClickable=true
            setOnClickListener {
                l("CLICK")
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
                l("LONG CLICK")
                val sel = selectedItems.get()
                if (sel.isEmpty()) selectedItems.set(sel + item.item)
                true
            }
        }
    }

}
