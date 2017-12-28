package com.gurunars.animal_item

import com.gurunars.item_list.Item

typealias RunInTransaction = (transition: Runnable) -> Unit

private fun <ItemType : Item> asMap(items: List<ItemType>) = mapOf(*items
    .filterNot { it.id == 0L }
    .map { Pair(it.id, it) }.toTypedArray()
)

internal fun <ItemType : Item> persistList(
    runInTransaction: RunInTransaction,
    dao: PlainDao<ItemType>,
    items: List<ItemType>
) = with(dao) {
    val source = all()
    runInTransaction(Runnable {
        delete(with(asMap(items)) { source?.filter { !this.contains(it.id) } ?: listOf() })
        update(items.filterNot { it.id == 0L })
        insert(items.filter { it.id == 0L })
    })
}
