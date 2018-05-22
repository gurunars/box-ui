package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionEdit<ItemType : Item>(
    private val itemConsumer: (item: ItemType) -> Unit
) : Action<ItemType> {

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = selectedItems.oneWayBranch { size == 1 }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) = from {
        all.first { item -> selectedItems.indexOfFirst { it.id == item.id } != -1 }
    }.to { itemConsumer(it) }

}