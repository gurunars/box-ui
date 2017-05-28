package com.gurunars.item_list

import java.util.ArrayList

internal class MutatedUpdateFetcher<ItemType : Item> {

    operator fun get(sourceMiddle: List<ItemType>,
                     targetMiddle: List<ItemType>,
                     startOffset: Int): List<Change<ItemType>> {
        val changes = ArrayList<Change<ItemType>>()

        for (sourceItem in sourceMiddle) {
            val index = targetMiddle.indexOf(sourceItem)
            val realIndex = startOffset + index
            val targetItem = targetMiddle[index]
            if (!sourceItem.payloadsEqual(targetItem)) {
                changes.add(ChangeUpdate(targetItem, realIndex, realIndex))
            }
        }

        return changes
    }

}
