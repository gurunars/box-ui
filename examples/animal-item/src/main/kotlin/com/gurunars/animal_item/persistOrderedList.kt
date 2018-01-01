package com.gurunars.animal_item

import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy

// TODO: move to own package
fun <ItemType : ItemWithPosition> persistOrderedList(
    runInTransaction: RunInTransaction,
    dao: PlainDao<ItemType>,
    items: List<ItemType>
) {
    val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }
    persistList(
        runInTransaction,
        dao,
        items.mapIndexed { index, item -> kryo.copy(item).apply {
            position = index
        } }
    )
}