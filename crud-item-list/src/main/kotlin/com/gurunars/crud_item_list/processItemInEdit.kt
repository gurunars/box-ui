package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

fun<ItemType: Item> processItemInEdit(
    item: ItemType?,
    oldItems: List<ItemType>
): List<ItemType> {
    if (item == null) {
        return oldItems
    }
    val foundIndex = oldItems.indexOfFirst { it.id == item.id }
    if (foundIndex == -1) {
        return oldItems + item
    } else {
        return oldItems.toMutableList().apply {
            set(foundIndex, item)
        }
    }
}