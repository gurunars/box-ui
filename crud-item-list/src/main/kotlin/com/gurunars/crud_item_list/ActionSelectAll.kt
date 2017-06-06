package com.gurunars.crud_item_list

internal class ActionSelectAll<ItemType> : Action<ItemType> {

    override fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean {
        selectedItems.addAll(all)
        return true
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        return selectedItems.size < all.size
    }

}
