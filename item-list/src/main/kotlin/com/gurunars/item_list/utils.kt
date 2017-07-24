package com.gurunars.item_list

internal fun<ItemType: Item> Collection<ItemType>.has(item: ItemType) =
    find { item.getId() == it.getId() } != null

internal fun<ItemType: Item> Set<ItemType>.exclude(item: ItemType) =
    filterNot { it.getId() == item.getId() }.toSet()

internal fun<ItemType: Item> Set<ItemType>.include(item: ItemType) =
    this + item