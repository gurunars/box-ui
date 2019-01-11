package com.gurunars.item_list

typealias KeyGetter = (item: Any) -> Long

internal fun Collection<Any>.has(getKey: KeyGetter, item: Any) =
    find { getKey(item) == getKey(it) } != null

internal fun Set<Any>.exclude(getKey: KeyGetter, item: Any) =
    filterNot { getKey(item) == getKey(it) }.toSet()

internal fun Set<Any>.include(item: Any) =
    this + item
