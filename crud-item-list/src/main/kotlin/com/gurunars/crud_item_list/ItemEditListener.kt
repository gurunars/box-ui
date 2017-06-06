package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/**
 * A listener for the cases when the item needs to be modified or created.

 * It is responsibility of the outside agent to call setItems with a new payload in the end though.

 * @param <ItemType>
</ItemType> */
interface ItemEditListener<ItemType : Item> {

    /**
     * @param editableItem item to be altered
     */
    fun onEdit(editableItem: ItemType)

    class DefaultItemEditListener<ItemType : Item> : ItemEditListener<ItemType> {

        override fun onEdit(editableItem: ItemType) {

        }
    }
}
