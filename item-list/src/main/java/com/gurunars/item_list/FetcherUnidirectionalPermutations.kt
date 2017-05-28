package com.gurunars.item_list

import java.util.ArrayList

internal class FetcherUnidirectionalPermutations<ItemType : Item> {

    operator fun get(remainedOrderOneOriginal: List<ItemType>,
                     remainedOrderTwo: List<ItemType>,
                     startOffset: Int,
                     reverse: Boolean): List<ChangeMove<ItemType>>? {
        val changes = ArrayList<ChangeMove<ItemType>>()
        val remainedOrderOne = ArrayList(remainedOrderOneOriginal)
        var from = if (reverse) remainedOrderOne.size - 1 else 0
        while (from < remainedOrderOne.size && from >= 0) {
            val item = remainedOrderOne[from]
            val to = remainedOrderTwo.indexOf(item)

            if (from != to) {
                changes.add(ChangeMove(item, startOffset + from, startOffset + to))
                if (changes.size > moveThreshold) {
                    return null
                }
                remainedOrderOne.removeAt(from)
                remainedOrderOne.add(to, item)
                from = if (reverse) remainedOrderOne.size - 1 else 0
                continue
            }

            from += if (reverse) -1 else +1
        }

        return changes
    }

    companion object {

        // if the number of moves exceeds a certain limit - it is cheaper to remove/add specific items
        private val moveThreshold = 5
    }
}
