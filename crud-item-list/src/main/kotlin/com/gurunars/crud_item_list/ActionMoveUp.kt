package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionMoveUp<ItemType : Item> : Action<ItemType> {

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) {
        val positions = getPositions(all, selectedItems)
        val positionToMoveDown = positions[0] - 1
        val itemToMoveDown = all[positionToMoveDown]
        consumer(mutableListOf<ItemType>().apply {
            addAll(all)
            add(positions[positions.size - 1] + 1, itemToMoveDown)
            removeAt(positionToMoveDown)
        }, selectedItems)
    }

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) {
        val positions = getPositions(all, selectedItems)
        consumer(isSolidChunk(positions) && positions[0] > 0)
    }
}
