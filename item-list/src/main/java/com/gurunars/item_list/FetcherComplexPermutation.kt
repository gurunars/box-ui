package com.gurunars.item_list


internal class FetcherComplexPermutation<ItemType : Item> {

    private val fetcher = FetcherPermutationRange<ItemType>()

    fun apply(
            source: List<ItemType>,
            target: List<ItemType>,
            startOffset: Int
    ): Change<ItemType> {
        val range = fetcher.apply(source, target)
        return ChangeComplexPermutation(
                startOffset + range.start,
                target.subList(range.start, range.end))
    }
}
