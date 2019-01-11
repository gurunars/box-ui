package com.gurunars.item_list

typealias KeyGetter<T> = (item: T) -> Long

internal fun<T: Any> Collection<T>.has(getKey: KeyGetter<T>, item: T) =
    find { getKey(item) == getKey(it) } != null

internal fun<T: Any> Set<T>.exclude(getKey: KeyGetter<T>, item: T) =
    filterNot { getKey(item) == getKey(it) }.toSet()

internal fun<T: Any> Set<T>.include(item: T) =
    this + item
