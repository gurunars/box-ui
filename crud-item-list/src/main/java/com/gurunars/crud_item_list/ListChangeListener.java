package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;

import java.util.List;

/**
 * Listener to be called whenever the collection manipulated by the widget is modified.
 *
 * @param <ItemType> class of the items managed by the widget.
 */
public interface ListChangeListener<ItemType extends Item> {

    /**
     * @param items a new version ot the list to be saved
     */
    void onChange(List<ItemType> items);

    class DefaultListChangeListener<ItemType extends Item> implements ListChangeListener<ItemType> {

        @Override
        public void onChange(List<ItemType> items) {

        }
    }

}
