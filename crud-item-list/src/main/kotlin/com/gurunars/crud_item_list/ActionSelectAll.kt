package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionSelectAll<ItemType : Item> : Action<ItemType> {

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        return Pair(all, all.toSet())
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean = selectedItems.size < all.size

}
