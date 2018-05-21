package com.gurunars.crud_item_list

internal fun <ItemType : Item> getPositions(all: List<ItemType>, selectedItems: Set<Long>): List<Int> {
    val idToPositionMap = all.mapIndexed { index: Int, item: ItemType -> Pair(item.id, index) }.toMap()
    return selectedItems.mapNotNull { idToPositionMap[it] }.sorted()
}
