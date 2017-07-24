package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionEdit<ItemType: Item>(
    private val itemConsumer: (item: ItemType)-> Unit
) : Action<ItemType> {
    override val isSynchronous = false

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        all.filter { item -> selectedItems.indexOfFirst { it.getId() == item.getId() } != -1 }.first().apply {
            itemConsumer(this)
        }
        return Pair(all, selectedItems)
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>) = selectedItems.size == 1

}
