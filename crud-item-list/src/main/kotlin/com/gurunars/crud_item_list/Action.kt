package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal interface Action<ItemType : Item> {
    fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>>
    fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean
}
