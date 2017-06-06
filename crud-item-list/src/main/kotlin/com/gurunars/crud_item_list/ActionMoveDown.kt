package com.gurunars.crud_item_list

internal class ActionMoveDown<ItemType> : Action<ItemType> {

    private val positionFetcher = PositionFetcher<ItemType>()

    private val solidChunkChecker = CheckerSolidChunk()

    override fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean {
        val positions = positionFetcher.apply(all, selectedItems)
        val positionToMoveUp = positions[positions.size - 1] + 1
        val itemToMoveUp = all[positionToMoveUp]
        all.removeAt(positionToMoveUp)
        all.add(positions[0], itemToMoveUp)
        return true
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        val positions = positionFetcher.apply(all, selectedItems)
        return solidChunkChecker.apply(positions) && positions[positions.size - 1] < all.size - 1
    }

}
