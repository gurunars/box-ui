package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemViewBinder

typealias ItemFormBinder<ItemType> =
    Context.(field: BindableField<ItemType>) -> View

typealias NewItemCreator<ItemType> =
    () -> ItemType

data class ItemTypeDescriptor<ItemType: Item>(
    val icon: IconView.Icon,
    val type: Enum<*>,
    val rowBinder: SelectableItemViewBinder<ItemType>,
    val formBinder: ItemFormBinder<ItemType>,
    val newItemCreator: NewItemCreator<ItemType>
)