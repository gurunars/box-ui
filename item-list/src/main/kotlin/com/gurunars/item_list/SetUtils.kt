package com.gurunars.item_list

typealias KeyGetter<T> = (item: T) -> Long

internal fun<T: Any> Collection<T>.has(item: T) =
    find { item == it } != null

internal fun<T: Any> Set<T>.exclude(item: T) =
    filterNot { item == it }.toSet()

internal fun<T: Any> Set<T>.include(item: T) =
    this + item
