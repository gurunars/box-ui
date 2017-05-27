package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionDelete<ItemType: Item> : Action<ItemType> {
    override val isSynchronous = true

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        return Pair(
            all.filter { item -> selectedItems.indexOfFirst { it.getId() == item.getId() } == -1 },
            setOf()
        )
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>) = selectedItems.isNotEmpty()

}
