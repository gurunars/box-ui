package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;

/**
 * A listener for the cases when the item needs to be modified or created.
 *
 * It is responsibility of the outside agent to call setItems with a new payload in the end though.
 *
 * @param <ItemType>
 */
public interface ItemEditListener<ItemType extends Item> {

    /**
     * @param editableItem item to be altered
     */
    void onEdit(ItemType editableItem);

    class DefaultItemEditListener<ItemType extends Item> implements ItemEditListener<ItemType> {

        @Override
        public void onEdit(ItemType editableItem) {

        }
    }
}
