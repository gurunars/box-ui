package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.box.merge
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionSelectAll<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = merge(all, selectedItems)
        .oneWayBranch { second.size < first.size }


    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) = consumer(all, all.toSet())

}
