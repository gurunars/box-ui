package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;

/**
 * Supplier of a blank new items of a given type
 *
 * @param <ItemType>
 */
public interface NewItemSupplier<ItemType extends Item> {

    /**
     * @return item instance
     */
    ItemType supply();
}
