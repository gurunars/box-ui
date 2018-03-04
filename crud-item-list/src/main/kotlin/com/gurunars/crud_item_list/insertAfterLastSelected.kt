package com.gurunars.crud_item_list

fun<ItemType> insertAfterLastSelected(
    all: List<ItemType>,
    selectedItems: Set<ItemType>,
    newItems: List<ItemType>
): List<ItemType> {
    if (selectedItems.isEmpty()) return all + newItems

    val lastSelectedPosition =
        selectedItems.map { all.indexOf(it) }.fold(0) { acc, it -> Math.max(acc, it) }

    val insertAfter = lastSelectedPosition + 1

    val firstPart = all.subList(0, insertAfter)
    val secondPart = all.subList(insertAfter, all.size)

    return firstPart + newItems + secondPart
}