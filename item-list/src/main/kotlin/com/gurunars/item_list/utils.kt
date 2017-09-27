package com.gurunars.item_list

internal fun <ItemType : Item> Collection<ItemType>.has(item: ItemType) =
    find { item.id == it.id } != null

internal fun <ItemType : Item> Set<ItemType>.exclude(item: ItemType) =
    filterNot { it.id == item.id }.toSet()

internal fun <ItemType : Item> Set<ItemType>.include(item: ItemType) =
    this + item