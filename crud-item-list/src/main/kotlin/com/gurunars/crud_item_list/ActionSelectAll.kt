package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionSelectAll<ItemType : Item> : Action<ItemType> {

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) = consumer(all, all.toSet())

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) = consumer(selectedItems.size < all.size)

}
