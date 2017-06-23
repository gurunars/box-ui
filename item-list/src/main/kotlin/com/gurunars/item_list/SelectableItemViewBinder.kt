package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField

interface SelectableItemViewBinder<ItemType: Item> {
    fun bind(context: Context, payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>): View
    fun getEmptyPayload(): ItemType
}