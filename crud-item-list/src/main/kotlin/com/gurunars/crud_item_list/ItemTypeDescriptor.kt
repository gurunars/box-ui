package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemViewBinder

/**
 * Function that needs to return a form
 *
 * @param field - observable of the payload to be edited in the form
 * @param closeHandler - function to be called when the form needs to be closed
 * @param confirmationHandler - function to be called when the payload needs to be saved
 */
typealias ItemFormBinder<ItemType> =
    Context.(
        field: BindableField<ItemType>,
        closeHandler: () -> Unit,
        confirmationHandler: () -> Unit
    ) -> View

/**
 * Function that is supposed to return a newly created item of a specific type without saving it.
 */
typealias NewItemCreator<ItemType> =
    () -> ItemType

/**
 * Aggregation of attributes describing a specific item type in the context of showing the item
 * list and editing individual items.
 *
 * @property icon Item icon for the creation menu.
 * @property type Type of the item the descriptor to be associated to.
 * @property rowBinder Item's renderer for a list row.
 * @property formBinder Item's renderer for an editable form view.
 * @property newItemCreator
 *
 */
data class ItemTypeDescriptor<ItemType: Item>(
    val icon: IconView.Icon,
    val type: Enum<*>,
    val rowBinder: SelectableItemViewBinder<ItemType>,
    val formBinder: ItemFormBinder<ItemType>,
    val newItemCreator: NewItemCreator<ItemType>
)