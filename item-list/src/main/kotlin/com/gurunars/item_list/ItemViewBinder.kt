package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField


/**
 * Glue between the Item of a specific type and its view.
 *
 * @param <ItemType> type of a payload to be passed to a view
 */
interface ItemViewBinder<ItemType : Item> {

    /**
     * Return a view instance to be populated
     *
     * @param context to be used for rendering
     * @param payload observable of a Pair, where the first element is the current version
     *                of the item and the second element is a previous version of the item
     *                if there was any
     * @return rendered view
     */
    fun getView(context: Context, payload: BindableField<Pair<ItemType?, ItemType?>>): View
}
