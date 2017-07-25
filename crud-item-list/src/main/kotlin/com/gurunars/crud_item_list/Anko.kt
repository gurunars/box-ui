package com.gurunars.crud_item_list

import android.app.Activity
import android.view.ViewManager
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemViewBinder
import org.jetbrains.anko.custom.ankoView

/**
 * Anko specific view function for CrudItemListView
 *
 * @see CrudItemListView
 */
fun <ItemT: Item> ViewManager.crudItemListView(
        itemViewBinderFetcher: SelectableItemViewBinder<ItemT>,
        itemEditListener: (item: ItemT) -> Unit,
        emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
        init: CrudItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    CrudItemListView(it, itemViewBinderFetcher, itemEditListener, emptyViewBinder)
}, 0, init)

/**
 * Anko specific view function for CrudItemListView
 *
 * @see CrudItemListView
 */
fun <ItemT: Item> Activity.crudItemListView(
        itemViewBinderFetcher: SelectableItemViewBinder<ItemT>,
        itemEditListener: (item: ItemT) -> Unit,
        emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
        init: CrudItemListView<ItemT>.() -> Unit = {}) =
    ankoView({
        CrudItemListView(it, itemViewBinderFetcher, itemEditListener, emptyViewBinder)
    }, 0, init)