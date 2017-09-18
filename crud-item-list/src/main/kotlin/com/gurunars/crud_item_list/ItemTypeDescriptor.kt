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

typealias NewItemCreator<ItemType> =
    () -> ItemType

data class ItemTypeDescriptor<ItemType: Item>(
    /**
     * Item icon for the creation menu.
     */
    val icon: IconView.Icon,
    /**
     * Type of the item the descriptor to be associated to.
     */
    val type: Enum<*>,
    /**
     * Item's renderer for a list row.
     */
    val rowBinder: SelectableItemViewBinder<ItemType>,
    /**
     * Item's renderer for an editable form view.
     */
    val formBinder: ItemFormBinder<ItemType>,
    /**
     * Function that is supposed to return a newly created item of a specific type without saving it.
     */
    val newItemCreator: NewItemCreator<ItemType>
)