package com.gurunars.animal_item

import com.gurunars.item_list.Item

// TODO: move to own package
interface DbItem: Item {
    override var id: Long
}