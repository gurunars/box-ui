package com.gurunars.crud_item_list

internal class ActionDelete<ItemType> : Action<ItemType> {

    override fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean {
        all.removeAll(selectedItems)
        return true
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        return selectedItems.isNotEmpty()
    }

}
