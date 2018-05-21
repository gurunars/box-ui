package com.gurunars.animal_item

import com.gurunars.crud_item_list.Item

interface DbItem: Item {
    override var id: Long
}