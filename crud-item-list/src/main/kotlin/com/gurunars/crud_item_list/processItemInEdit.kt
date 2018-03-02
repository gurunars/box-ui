package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal fun <ItemType : Item> processItemInEdit(
    oldItems: List<ItemType>,
    item: ItemType?
): List<ItemType> {
    if (item == null) {
        return oldItems
    }
    val foundIndex = oldItems.indexOfFirst { it.id == item.id }
    return if (foundIndex == -1) {
        oldItems + item
    } else {
        oldItems.toMutableList().apply {
            set(foundIndex, item)
        }
    }
}