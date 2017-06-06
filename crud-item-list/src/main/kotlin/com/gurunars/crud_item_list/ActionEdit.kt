package com.gurunars.crud_item_list

import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy

internal class ActionEdit<ItemType>(private val itemConsumer: (item: ItemType)-> Unit) : Action<ItemType> {

    private val kryo = Kryo()

    init {
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    override fun perform(all: MutableList<ItemType>, selectedItems: MutableSet<ItemType>): Boolean {
        val iterator = selectedItems.iterator()
        if (iterator.hasNext()) {
            itemConsumer(kryo.copy(iterator.next()))
        }
        return false
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        return selectedItems.size == 1
    }

}
