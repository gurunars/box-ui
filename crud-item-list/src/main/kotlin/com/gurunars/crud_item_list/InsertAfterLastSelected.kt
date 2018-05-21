package com.gurunars.crud_item_list

internal fun<ItemType: Item> insertAfterLastSelected(
    all: List<ItemType>,
    selectedItems: Set<Long>,
    newItems: List<ItemType>
): List<ItemType> {
    if (selectedItems.isEmpty()) return all + newItems

    val lastSelectedPosition = all
        .mapIndexed { index: Int, item: ItemType -> Pair(index, item.id) }
        .filter { selectedItems.contains(it.second) }
        .sortedBy { it.first }
        .last().first

    val insertAfter = lastSelectedPosition + 1

    val firstPart = all.subList(0, insertAfter)
    val secondPart = all.subList(insertAfter, all.size)

    return firstPart + newItems + secondPart
}