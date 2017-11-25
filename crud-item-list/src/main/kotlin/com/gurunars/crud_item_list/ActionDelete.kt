package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionDelete<ItemType : Item> : Action<ItemType> {

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) = consumer(
        all.filter { item -> selectedItems.indexOfFirst { it.id == item.id } == -1 },
        setOf()
    )

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) = consumer(selectedItems.isNotEmpty())

}
