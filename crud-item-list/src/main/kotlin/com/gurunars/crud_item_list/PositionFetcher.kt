package com.gurunars.crud_item_list

import java.util.ArrayList
import java.util.Collections

import java8.util.function.BiFunction

internal class PositionFetcher<ItemType> : BiFunction<List<ItemType>, Set<ItemType>, List<Int>> {

    override fun apply(all: List<ItemType>, selectedItems: Set<ItemType>): List<Int> {
        val positions = ArrayList<Int>()

        if (all.isEmpty() || selectedItems.isEmpty()) {
            return positions
        }

        for (cursor in selectedItems) {
            val index = all.indexOf(cursor)
            if (index != -1) {
                positions.add(index)
            }
        }
        Collections.sort(positions)
        return positions
    }
}
