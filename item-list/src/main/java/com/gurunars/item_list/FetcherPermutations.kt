package com.gurunars.item_list

import javax.inject.Inject

internal class FetcherPermutations<ItemType : Item> {

    @Inject
    private val intersectionFetcher = OrderedIntersectionFetcher<ItemType>()

    private val fetcherUnidirectionalPermutations = FetcherUnidirectionalPermutations<ItemType>()

    private val fetcherComplexPermutation = FetcherComplexPermutation<ItemType>()

    operator fun get(
            sourceMiddle: List<ItemType>,
            targetMiddle: List<ItemType>,
            startOffset: Int): List<Change<ItemType>> {
        val intersectionSourceOrder = intersectionFetcher.apply(sourceMiddle, targetMiddle)
        val intersectionTargetOrder = intersectionFetcher.apply(targetMiddle, sourceMiddle)

        val moves = fetcherUnidirectionalPermutations.get(
                intersectionSourceOrder, intersectionTargetOrder, startOffset, false)
        val movesReverse = fetcherUnidirectionalPermutations.get(
                intersectionSourceOrder, intersectionTargetOrder, startOffset, true)

        if (moves == null && movesReverse != null) {
            return movesReverse
        } else if (movesReverse == null && moves != null) {
            return moves
        } else if (moves != null && movesReverse != null) {
            return if (movesReverse.size < moves.size) movesReverse else moves
        } else {
            return listOf(if (intersectionSourceOrder.isEmpty() || intersectionTargetOrder.isEmpty())
                ChangePersist<ItemType>()
            else
                fetcherComplexPermutation.apply(
                        intersectionSourceOrder,
                        intersectionTargetOrder,
                        startOffset))
        }

    }
}
