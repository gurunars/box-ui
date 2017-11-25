package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ActionMoveDown<ItemType : Item> : Action<ItemType> {

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) {
        val positions = getPositions(all, selectedItems)
        val positionToMoveUp = positions[positions.size - 1] + 1
        val itemToMoveUp = all[positionToMoveUp]
        consumer(mutableListOf<ItemType>().apply {
            addAll(all)
            removeAt(positionToMoveUp)
            add(positions[0], itemToMoveUp)
        }, selectedItems)
    }

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) {
        val positions = getPositions(all, selectedItems)
        consumer(isSolidChunk(positions) && positions[positions.size - 1] < all.size - 1)
    }

}
