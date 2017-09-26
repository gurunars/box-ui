package com.gurunars.crud_item_list

import android.app.Activity
import android.content.Context
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
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    sortable: Boolean = true,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemT>>>,
    init: CrudItemListView<ItemT>.() -> Unit = {}
) = ankoView({
    CrudItemListView(
        it, emptyViewBinder, sortable, groupedItemTypeDescriptors
    )
}, 0, init)

/**
 * Anko specific view function for CrudItemListView
 *
 * @see CrudItemListView
 */
fun <ItemT: Item> Activity.crudItemListView(
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    sortable: Boolean = true,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemT>>>,
    init: CrudItemListView<ItemT>.() -> Unit = {}
) = ankoView({
    CrudItemListView(
        it, emptyViewBinder, sortable, groupedItemTypeDescriptors
    )
}, 0, init)
