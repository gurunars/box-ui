package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionMoveUp<ItemType: Item> : Action<ItemType> {
    override val isSynchronous = true

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        val positions = getPositions(all, selectedItems)
        val positionToMoveDown = positions[0] - 1
        val itemToMoveDown = all[positionToMoveDown]
        return Pair(mutableListOf<ItemType>().apply {
            addAll(all)
            add(positions[positions.size - 1] + 1, itemToMoveDown)
            removeAt(positionToMoveDown)
        }, selectedItems)
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        val positions = getPositions(all, selectedItems)
        return isSolidChunk(positions) && positions[0] > 0
    }

}
