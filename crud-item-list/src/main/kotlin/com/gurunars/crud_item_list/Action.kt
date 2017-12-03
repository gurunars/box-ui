package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

typealias ItemSetChange<ItemType> =
    (newAll: List<ItemType>, newSelectedItems: Set<ItemType>) -> Unit

typealias CanDo = (canDo: Boolean) -> Unit

internal interface Action<ItemType : Item> {
    fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    )
    fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    )
}
