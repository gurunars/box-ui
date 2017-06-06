package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/**
 * Listener to be called whenever the collection manipulated by the widget is modified.

 * @param <ItemType> class of the items managed by the widget.
</ItemType> */
interface ListChangeListener<ItemType : Item> {

    /**
     * @param items a new version ot the list to be saved
     */
    fun onChange(items: List<ItemType>)

    class DefaultListChangeListener<ItemType : Item> : ListChangeListener<ItemType> {

        override fun onChange(items: List<ItemType>) {

        }
    }

}
