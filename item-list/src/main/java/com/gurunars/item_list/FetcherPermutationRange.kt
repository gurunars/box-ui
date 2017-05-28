package com.gurunars.item_list


/*
* For two lists returns such a sublist that the remaining parts represent the unchanged portions
* to the left and to the right of this sublist.
*
* E.g. for [1,2,3,4,5,6] and [1,3,4,5,2,6] returns [3,4,5,2]
*
* */
internal class FetcherPermutationRange<ItemType> : BiFunction<List<ItemType>, List<ItemType>, FetcherPermutationRange.Range> {

    internal class Range(val start: Int, val end: Int) {

        override fun toString(): String {
            return "R($start, $end)"
        }
    }

    override fun apply(source: List<ItemType>, target: List<ItemType>): Range {
        var startOffset: Int
        var endOffset: Int

        val sourceSize = source.size
        val targetSize = target.size

        val limit = Math.min(sourceSize, targetSize)

        startOffset = 0
        while (startOffset < limit) {
            if (source[startOffset] != target[startOffset]) {
                break
            }
            startOffset++
        }

        endOffset = 0
        while (endOffset < limit) {
            if (source[sourceSize - 1 - endOffset] != target[targetSize - 1 - endOffset]) {
                break
            }
            endOffset++
        }

        return Range(startOffset, targetSize - endOffset)
    }

}
