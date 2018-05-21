package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import io.reactivex.Single

data class ListState<ItemType: Item>(
    val all: List<ItemType>,
    val selected: Set<Long>
)

internal typealias ItemSetChange<ItemType> = (state: ListState<ItemType>) -> Unit

internal interface Action<ItemType : Item> {
    fun perform(state: ListState<ItemType>): Single<ListState<ItemType>>
    fun canPerform(state: IRoBox<ListState<ItemType>>): IRoBox<Boolean>
}
