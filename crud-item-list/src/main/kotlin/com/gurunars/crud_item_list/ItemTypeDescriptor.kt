package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.SelectableItemViewBinder

/**
 * @param field - observable of the payload to be edited in the form
 * @return rendered form
 */
typealias ItemFormBinder<ItemType> =
    Context.(
        field: BindableField<ItemType>
    ) -> View

/**
 * @param field - observable of the selectable payload to be shown in the item list view
 * @return rendered item row
 */
typealias ItemRowBinder<ItemType> =
    Context.(
        field: BindableField<ItemType>
    ) -> View

/**
 * Function that is supposed to return a newly created item of a specific type without saving it.
 */
typealias NewItemCreator<ItemType> =
    () -> ItemType

/**
 * Function saying if the item can be saved or not.
 */
typealias CanSave<ItemType> =
    (item: ItemType) -> Boolean

/**
 * Aggregation of attributes describing a specific item type in the context of showing the item
 * list and editing individual items.
 *
 * @property icon Item icon for the creation menu.
 * @property type Type of the item the descriptor to be associated to.
 * @property rowRegularColor background color applied to a row when it is not selected
 * @property rowSelectionColor background color applied to a row when it is selected
 * @property rowBinder
 * @property formBinder
 * @property newItemCreator
 * @property canSave
 *
 */
data class ItemTypeDescriptor<ItemType: Item>(
    val icon: IconView.Icon,
    val type: Enum<*> = Item.Default.ONLY,
    val rowRegularColor: Int = Color.TRANSPARENT,
    val rowSelectionColor: Int = Color.RED,
    val rowBinder: ItemRowBinder<ItemType>,
    val formBinder: ItemFormBinder<ItemType>,
    val newItemCreator: NewItemCreator<ItemType>,
    val canSave: CanSave<ItemType> = { true }
)