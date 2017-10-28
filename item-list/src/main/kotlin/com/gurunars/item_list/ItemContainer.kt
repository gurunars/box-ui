package com.gurunars.item_list

import com.gurunars.databinding.BindableField

interface ItemContainer<ItemType: Item> {
    val items: BindableField<List<ItemType>>
}