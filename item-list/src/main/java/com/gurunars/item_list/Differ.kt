package com.gurunars.item_list

import java.util.ArrayList
import java.util.Collections
import java.util.HashSet


internal class Differ<ItemType : Item> : BiFunction<List<ItemType>, List<ItemType>, List<Change<ItemType>>> {

    private val diffFetcher = OrderedDiffFetcher<ItemType>()
    private val fetcherPermutations = FetcherPermutations<ItemType>()
    private val partitioner = Partitioner<ItemType>()
    private val plainUpdateFetcher = PlainUpdateFetcher<ItemType>()
    private val mutatedUpdateFetcher = MutatedUpdateFetcher<ItemType>()

    private fun verifyNoDuplicates(items: List<ItemType>) {
        val set = HashSet(items)
        if (set.size != items.size) {
            throw RuntimeException("The list of items contains duplicates")
        }
    }

    private fun reverse(original: List<ItemType>): List<ItemType> {
        Collections.reverse(original)
        return original
    }

    override fun apply(source: List<ItemType>, target: List<ItemType>): List<Change<ItemType>> {

        verifyNoDuplicates(target)

        val tuple = partitioner.apply(source, target)

        val sourceMiddle = tuple.source.middle.toMutableList()
        val targetMiddle = tuple.target.middle

        val changes = ArrayList<Change<ItemType>>()

        // remove in a reverse order to prevent index recalculation
        for (item in reverse(diffFetcher.apply(sourceMiddle, targetMiddle))) {
            val position = sourceMiddle.indexOf(item)
            val realIndex = tuple.startOffset + position
            changes.add(ChangeDelete(item, realIndex, realIndex))
            sourceMiddle.removeAt(position)
        }

        for (item in diffFetcher.apply(targetMiddle, sourceMiddle)) {
            val position = targetMiddle.indexOf(item)
            val realIndex = tuple.startOffset + position
            changes.add(ChangeCreate(item, realIndex, realIndex))
            sourceMiddle.add(position, item)
        }

        // Fetch permutations in both
        changes.addAll(fetcherPermutations.get(sourceMiddle, targetMiddle, tuple.startOffset))
        changes.addAll(plainUpdateFetcher[tuple.source.head, tuple.target.head, 0])
        changes.addAll(mutatedUpdateFetcher.get(sourceMiddle, targetMiddle, tuple.startOffset))
        changes.addAll(plainUpdateFetcher[tuple.source.tail, tuple.target.tail,
                       tuple.startOffset + sourceMiddle.size])

        return changes
    }

}

