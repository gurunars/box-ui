package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;
import com.gurunars.item_list.Payload;

/**
 * A listener for the cases when the item needs to be modified or created.
 *
 * It is responsibility of the outside agent to call setItems with a new payload in the end though.
 *
 * @param <PayloadType>
 */
public interface ItemEditListener<PayloadType extends Payload> {

    /**
     * @param editableItem item to be altered
     * @param isNew flag specifying if the item is new or an existing one
     */
    void onEdit(Item<PayloadType> editableItem, boolean isNew);

    class DefaultItemEditListener<PayloadType extends Payload> implements ItemEditListener<PayloadType> {

        @Override
        public void onEdit(Item<PayloadType> editableItem, boolean isNew) {

        }
    }
}
