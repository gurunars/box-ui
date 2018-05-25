package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.box.merge
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionMoveDown<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = merge(all, selectedItems).oneWayBranch {
        val positions = getPositions(first, second)
        isSolidChunk(positions) && positions[positions.size - 1] < first.size - 1
    }

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

}
