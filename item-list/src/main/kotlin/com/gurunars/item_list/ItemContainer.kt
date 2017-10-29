package com.gurunars.item_list

import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.Component

interface ItemContainer<ItemType: Item>: Component {
    val items: BindableField<List<ItemType>>
}