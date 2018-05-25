package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionDelete<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = selectedItems.oneWayBranch { isNotEmpty() }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) = consumer(
        all.filter { item -> selectedItems.indexOfFirst { it.id == item.id } == -1 },
        setOf()
    )
}
