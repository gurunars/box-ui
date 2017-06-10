package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal fun <ItemType: Item> getPositions(all: List<ItemType>, selectedItems: Set<ItemType>) =
    selectedItems
        .map { cursor -> all.indexOfFirst { it.getId() == cursor.getId() } }
        .filter { it != -1 }
        .sorted()
