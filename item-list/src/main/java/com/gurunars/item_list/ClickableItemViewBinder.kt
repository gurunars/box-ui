package com.gurunars.item_list

import android.content.Context
import android.view.View

internal class ClickableItemViewBinder<ViewType : View, ItemType : Item>(
        private val itemViewBinder: ItemViewBinder<ViewType, SelectableItem<ItemType>>,
        private val collectionManager: CollectionManager<ItemType>) : ItemViewBinder<ViewType, SelectableItem<ItemType>> {

    override fun getView(context: Context): ViewType {
        return itemViewBinder.getView(context)
    }

    override fun bind(itemView: ViewType, item: SelectableItem<ItemType>, previousItem: SelectableItem<ItemType>?) {
        itemViewBinder.bind(itemView, item, previousItem)

        itemView.setOnClickListener { collectionManager.itemClick(item) }
        itemView.setOnLongClickListener {
            collectionManager.itemLongClick(item)
            true
        }
    }

}
