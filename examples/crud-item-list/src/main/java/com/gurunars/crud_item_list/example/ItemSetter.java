package com.gurunars.crud_item_list.example;

import com.gurunars.item_list.Item;

import java.util.List;

class ItemSetter {

    /**
     * Helper function to replace the existing item in the list if its id was found or create a
     * new one in the list.
     *
     * Note, the list is changed by reference.
     *
     * @param items collection to alter
     * @param item item to edit/add
     * @param <ItemType> type of the items in a collection
     * @return the list that was passed in with changed payload
     */
    static <ItemType extends Item> List<ItemType> setItem(List<ItemType> items, ItemType item) {
        for (int i = 0; i < items.size(); i++) {
            ItemType cursor = items.get(i);
            if (cursor.getId() == item.getId()) {
                items.set(i, item);
                return items;
            }
        }
        items.add(item);
        return items;
    }
}
