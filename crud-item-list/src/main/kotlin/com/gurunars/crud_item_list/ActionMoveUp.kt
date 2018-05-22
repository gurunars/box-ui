package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.box.merge
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionMoveUp<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = merge(all, selectedItems).oneWayBranch {
        val positions = getPositions(first, second)
        isSolidChunk(positions) && positions[0] > 0
    }

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

}
