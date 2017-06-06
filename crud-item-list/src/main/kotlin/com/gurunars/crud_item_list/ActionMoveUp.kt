package com.gurunars.crud_item_list

import java8.util.function.BiFunction
import java8.util.function.Function

internal class ActionMoveUp<ItemType> : Action<ItemType> {

    private val positionFetcher = PositionFetcher<ItemType>()

    private val solidChunkChecker = CheckerSolidChunk()

    override fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean {
        val positions = positionFetcher.apply(all, selectedItems)
        val positionToMoveDown = positions[0] - 1
        val itemToMoveDown = all[positionToMoveDown]
        all.add(positions[positions.size - 1] + 1, itemToMoveDown)
        all.removeAt(positionToMoveDown)
        return true
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        val positions = positionFetcher.apply(all, selectedItems)
        return solidChunkChecker.apply(positions) && positions[0] > 0
    }

}
