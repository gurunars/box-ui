package com.gurunars.crud_item_list

import com.gurunars.item_list.Item
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

internal class ActionEdit<ItemType : Item>(
    private val itemConsumer: (item: ItemType) -> Unit
) : Action<ItemType> {

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) {
        doAsync {
            val first = all.first { item -> selectedItems.indexOfFirst { it.id == item.id } != -1 }
            uiThread {
                itemConsumer(first)
            }
        }
    }

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) = consumer(selectedItems.size == 1)
}