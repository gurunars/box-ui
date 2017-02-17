package com.gurunars.crud_item_list;

import android.content.Context;

import com.gurunars.item_list.Item;

/**
 * Form view binder to be used to create/edit when creation menu or item edit buttons are clicked.
 *
 * @param <ItemType> type of the item to be processed using the binder
 */
public interface ItemFormSupplier<ItemType extends Item> {

    /**
     * @param context used to create form view instance
     * @return form view
     */
    ItemForm<ItemType> supply(Context context);

}
