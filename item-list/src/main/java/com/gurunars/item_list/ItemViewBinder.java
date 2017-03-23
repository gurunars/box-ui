package com.gurunars.item_list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Glue between the Item of a specific type and its view.
 *
 * @param <ItemType> type of a payload to be passed to a view
 */
public interface ItemViewBinder<ItemType extends Item> {

    /**
     * Return a view instance to be populated
     *
     * @param context to be used for rendering
     * @return rendered view
     */
    View getView(Context context);

    /**
     * Populate the view based on data from the item.
     *
     * Use previous item to make a decision about which animation to use by diffing it with the
     * current item.
     *
     * @param itemView view to be populated - the one returned by getView
     * @param item payload to be used for populating the view
     * @param previousItem previous version of the item. Can be null.
     */
    void bind(View itemView, ItemType item, @Nullable ItemType previousItem);
}
