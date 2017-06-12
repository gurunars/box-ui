package com.gurunars.item_list

import com.gurunars.databinding.BindableField
import java.io.Serializable


internal class ItemWrapper<out ItemType: Item>(val item: ItemType) {
    override fun equals(other: Any?) =
        other != null &&
        other is ItemWrapper<*> &&
        item.getId() == other.item.getId()
    override fun hashCode() = item.hashCode()
}


internal fun<ItemType: Item> mergeCollections(items: List<ItemType>, selectedItems: Set<ItemType>) : List<SelectableItem<ItemType>> {
    val all = items.map { ItemWrapper(it) }
    val selected = selectedItems.map { ItemWrapper(it) }
    return all.map { SelectableItem(it.item, selected.contains(it)) }
}


internal fun<ItemType: Item> selectionChange(oldSelection: Set<ItemType>, newSelection: Set<ItemType>) {

}


internal class CollectionManager<ItemType : Item>(
        private val items: BindableField<List<ItemType>>,
        private val selectedItems: BindableField<HashSet<ItemType>>) : Serializable {

    private fun changed(newItems: List<ItemType>, newSelection: Set<ItemType>) {

        val filteredSelection = newSelection
                .filter { indexOfItem(items, it) != -1 }
                .map { items[indexOfItem(items, it)] }
                .toHashSet()
        val selectionChanged = selectedItems != filteredSelection
    }

    fun itemClick(selectableItem: SelectableItem<ItemType>) {
        if (selectedItems.isEmpty()) {
            return
        }

        val item = selectableItem.item
        changed(items,
                if (selectedItems.indexOfFirst { item.getId() == it.getId() } == -1)
                    selectedItems + item
                else
                    selectedItems.filterNot { item.getId() == it.getId() }.toHashSet())
    }

    fun itemLongClick(selectableItem: SelectableItem<ItemType>): Boolean {
        if (selectedItems.isEmpty()) {
            changed(items, selectedItems + hashSetOf(selectableItem.item))
        }
        return true
    }

}
