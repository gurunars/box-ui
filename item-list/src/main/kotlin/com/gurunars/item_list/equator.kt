package com.gurunars.item_list

import com.gurunars.shortcuts.l

fun<ItemType: Item> equal(one: Collection<ItemType>, two: Collection<ItemType>): Boolean {
    val status = one == two &&
           one.size == two.size &&
           one.zip(two).all { (item1, item2) ->
               item1.getId() == item2.getId() && item1.payloadsEqual(item2)
           }

    l("FFF" + status)

    return status
}