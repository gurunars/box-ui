package com.gurunars.state_machine_crawler

import java.util.*

fun<ItemType: KeyBasedItem> flattenGraph(
    getNeighbours: (item: ItemType) -> Collection<ItemType>,
    items: List<ItemType>
): List<ItemType> {
    val all = mutableListOf<ItemType>()
    val processed = mutableSetOf<String>()
    val toProcess = Stack<ItemType>().apply { items.forEach { push(it) } }

    while (toProcess.isNotEmpty()) {
        val cursor = toProcess.peek()
        processed.add(cursor.key)
        getNeighbours(cursor)
            .filter { !processed.contains(it.key) }
            .forEach { toProcess.push(it) }
    }

    return all
}