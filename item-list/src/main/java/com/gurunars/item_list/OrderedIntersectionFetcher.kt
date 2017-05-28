package com.gurunars.item_list

import java.util.ArrayList


internal class OrderedIntersectionFetcher<ItemType> : BiFunction<List<ItemType>, List<ItemType>, List<ItemType>> {

    override fun apply(one: List<ItemType>, two: List<ItemType>): List<ItemType> {
        val intersection = ArrayList(one)
        intersection.retainAll(two)
        return intersection
    }
}
