package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal fun <ItemType : Item> processItemInEdit(
    addToTail: Boolean,
    oldItems: List<ItemType>,
    item: ItemType?
): List<ItemType> {
    if (item == null) {
        return oldItems
    }
    val foundIndex = oldItems.indexOfFirst { it.id == item.id }
    return if (foundIndex == -1) {
        if (addToTail) {
            oldItems + item
        } else {
            listOf(item) + oldItems
        }
    } else {
        oldItems.toMutableList().apply {
            set(foundIndex, item)
        }
    }
}