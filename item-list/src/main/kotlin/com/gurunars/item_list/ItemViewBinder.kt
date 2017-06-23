package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField

interface ItemViewBinder<ItemType> {
    fun bind(context: Context, payload: BindableField<Pair<ItemType, ItemType?>>): View
    fun getEmptyPayload(): ItemType
}