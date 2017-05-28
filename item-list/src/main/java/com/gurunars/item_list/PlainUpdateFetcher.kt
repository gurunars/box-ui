package com.gurunars.item_list

import java.util.ArrayList

internal class PlainUpdateFetcher<ItemType : Item> {

    operator fun get(sourceList: List<ItemType>, targetList: List<ItemType>, offset: Int): List<Change<ItemType>> {
        val changes = ArrayList<Change<ItemType>>()

        for (i in sourceList.indices) {
            val newItem = targetList[i]
            val realIndex = offset + i
            if (!sourceList[i].payloadsEqual(newItem)) {
                changes.add(ChangeUpdate(newItem, realIndex, realIndex))
            }
        }

        return changes
    }


}
