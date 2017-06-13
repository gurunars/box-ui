package com.gurunars.item_list

fun<ItemType: Item> equal(one: Collection<ItemType>, two: Collection<ItemType>) =
        one == two && one.size == two.size && one.zip(two).all { (item1, item2) -> item1.getId() == item2.getId() }