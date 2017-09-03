package com.gurunars.crud_item_list

import android.app.Activity
import android.view.ViewManager
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import org.jetbrains.anko.custom.ankoView

/**
 * Anko specific view function for CrudItemListView
 *
 * @see CrudItemListView
 */
fun <ItemT: Item> ViewManager.crudItemListView(
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemT>>>,
    init: CrudItemListView<ItemT>.() -> Unit = {}
) = ankoView({
    CrudItemListView(
        it, emptyViewBinder, groupedItemTypeDescriptors
    )
}, 0, init)

/**
 * Anko specific view function for CrudItemListView
 *
 * @see CrudItemListView
 */
fun <ItemT: Item> Activity.crudItemListView(
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemT>>>,
    init: CrudItemListView<ItemT>.() -> Unit = {}
) = ankoView({
    CrudItemListView(
        it, emptyViewBinder, groupedItemTypeDescriptors
    )
}, 0, init)
