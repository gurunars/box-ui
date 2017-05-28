package com.gurunars.item_list

import java.util.ArrayList


internal class OrderedDiffFetcher<ItemType> : BiFunction<List<ItemType>, List<ItemType>, List<ItemType>> {
    /**
     * Returns a list of items that are present in one but missing in two. I.e. the items removed
     * from one to create list #2.
     */

    override fun apply(one: List<ItemType>, two: List<ItemType>): List<ItemType> {
        val missing = ArrayList(one)
        missing.removeAll(two)
        return missing
    }
}
