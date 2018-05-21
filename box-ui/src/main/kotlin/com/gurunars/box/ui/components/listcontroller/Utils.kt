package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.ui.components.listview.WithId

internal fun<ItemType: WithId> insertAfterLastSelected(
    all: List<ItemType>,
    selectedItems: Set<Any>,
    newItems: List<ItemType>
): List<ItemType> {
    if (selectedItems.isEmpty()) return all + newItems

    val lastSelectedPosition = all
        .mapIndexed { index: Int, item: ItemType -> Pair(index, item) }
        .filter { selectedItems.contains(it.second.id) }.maxBy { it.first }!!.first

    val insertAfter = lastSelectedPosition + 1

    val firstPart = all.subList(0, insertAfter)
    val secondPart = all.subList(insertAfter, all.size)

    return firstPart + newItems + secondPart
}

internal fun isSolidChunk(positions: List<Int>) =
    positions.isNotEmpty() && (1 until positions.size).none { positions[it] - positions[it - 1] != 1 }

internal val <ItemType: WithId> ListState<ItemType>.positions: List<Int>
    get() {
        val itemToPositionMap = all.mapIndexed { index: Int, item: ItemType -> Pair(item.id, index) }.toMap()
        return selected.mapNotNull { itemToPositionMap[it] }.sorted()
    }