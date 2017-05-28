package com.gurunars.item_list

import java.util.ArrayList

internal class Partitioner<ItemType> {

    internal class Partition<ItemType>(list: List<ItemType>, startOffset: Int, endOffset: Int) {
        var head: List<ItemType> = ArrayList()
            private set
        var middle: List<ItemType> = ArrayList()
            private set
        var tail: List<ItemType> = ArrayList()
            private set

        init {
            val sourceLastIndex = list.size - endOffset
            if (sourceLastIndex < startOffset) {
                head = ArrayList(list)
            } else {
                head = ArrayList(list.subList(0, startOffset))
                middle = ArrayList(list.subList(startOffset, sourceLastIndex))
                tail = ArrayList(list.subList(sourceLastIndex, list.size))
            }
        }

    }

    internal data class PartitionTuple<ItemType>(
        val source: Partition<ItemType>,
        val target: Partition<ItemType>,
        val startOffset: Int,
        val endOffset: Int
    )

    fun getStartOffset(source: List<ItemType>, target: List<ItemType>): Int {
        val sizeLimit = Math.min(source.size, target.size)
        var startOffset = 0
        for (i in 0..sizeLimit - 1) {
            if (source[i] != target[i]) {
                break
            }
            startOffset = i
        }
        return startOffset
    }

    fun getEndOffset(source: List<ItemType>, target: List<ItemType>): Int {
        val sizeLimit = Math.min(source.size, target.size)
        var endOffset = 0
        val sourceLast = source.size - 1
        val targetLast = target.size - 1
        for (i in 0..sizeLimit - 1) {
            if (source[sourceLast - i] != target[targetLast - i]) {
                break
            }
            endOffset = i
        }
        return endOffset
    }

    fun apply(source: List<ItemType>, target: List<ItemType>): PartitionTuple<ItemType> {
        val startOffset = getStartOffset(source, target)
        val endOffset = getEndOffset(source, target)

        return PartitionTuple(
            source = Partition(source, startOffset, endOffset),
            target = Partition(target, startOffset, endOffset),
            startOffset = startOffset,
            endOffset = endOffset
        )
    }

}
