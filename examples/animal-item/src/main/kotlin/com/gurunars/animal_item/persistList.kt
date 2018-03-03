package com.gurunars.animal_item

import com.esotericsoftware.kryo.Kryo
import com.gurunars.item_list.Item
import org.objenesis.strategy.StdInstantiatorStrategy

typealias RunInTransaction = (transition: Runnable) -> Unit

private fun <ItemType : Item> asMap(items: List<ItemType>) = mapOf(*items
    .filterNot { it.id == 0L }
    .map { Pair(it.id, it) }.toTypedArray()
)

// TODO: move to own package
fun <ItemType : DbItem> persistList(
    runInTransaction: RunInTransaction,
    dao: PlainDao<ItemType>,
    items: List<ItemType>
): List<ItemType> = with(dao) {
    val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }
    val source = all()
    runInTransaction(Runnable {
        delete(with(asMap(items)) { source?.filter { !this.contains(it.id) } ?: listOf() })
        update(items.filterNot { it.id == 0L })
        insert(items.filter { it.id <= 0L }.map {
            kryo.copy(it).apply {
                id = 0L
            }
        })
    })
    return all()
}
