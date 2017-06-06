package com.gurunars.crud_item_list

internal interface Action<ItemType> {

    fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean
    fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean

}
