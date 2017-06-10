package com.gurunars.databinding

private fun <Type: Collection<*>> collectionsEqual(one: Type, two: Type) =
        one == two && one.size == two.size && one.zip(two).all { one == two }

private fun <Type: Map<*, *>> mapsEqual(one: Type, two: Type) =
        collectionsEqual(one.entries.map { it.toPair() }, two.entries.map { it.toPair() })

internal fun <Type> equal(one: Type, two: Type) = when(one) {
    is Map<*, *> -> mapsEqual(one, two as Map<*, *>)
    is Collection<*> -> collectionsEqual(one, two as Collection<*>)
    else -> one == two
}

