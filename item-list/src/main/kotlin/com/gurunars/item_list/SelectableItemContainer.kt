package com.gurunars.item_list

import com.gurunars.databinding.BindableField

interface SelectableItemContainer<ItemType: Item>: ItemContainer<ItemType> {
    val selectedItems: BindableField<Set<ItemType>>
}