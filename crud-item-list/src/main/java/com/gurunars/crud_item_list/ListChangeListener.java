package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;
import com.gurunars.item_list.Payload;

import java.util.List;

/**
 * Listener to be called whenever the collection manipulated by the widget is modified.
 *
 * @param <PayloadType> class of the items managed by the widget.
 */
public interface ListChangeListener<PayloadType extends Payload> {

    /**
     * @param items a new version ot the list to be saved
     */
    void onChange(List<Item<PayloadType>> items);

    class DefaultListChangeListener<PayloadType extends Payload> implements ListChangeListener<PayloadType> {

        @Override
        public void onChange(List<Item<PayloadType>> items) {

        }
    }

}
