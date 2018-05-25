package com.gurunars.crud_item_list

import com.gurunars.box.IRoBox
import com.gurunars.item_list.Item

internal typealias ItemSetChange<ItemType> =
    (newAll: List<ItemType>, newSelectedItems: Set<ItemType>) -> Unit

internal interface Action<ItemType : Item> {
    fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    )
    fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean>
}
