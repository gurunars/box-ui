package com.gurunars.item_list

import android.app.Activity
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * Anko specific view function for ItemListView
 *
 * @see ItemListView
 */
fun <ItemT: Item> ViewManager.itemListView(
    itemViewBinder: ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: ItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    ItemListView(it, itemViewBinder, emptyViewBinder)
}, 0, init)

/**
 * Anko specific view function for ItemListView
 *
 * @see ItemListView
 */
fun <ItemT: Item> Activity.itemListView(
    itemViewBinder: ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: ItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    ItemListView(it, itemViewBinder, emptyViewBinder)
}, 0, init)

/**
 * Anko specific view function for SelectableItemViewBinder
 *
 * @see SelectableItemViewBinder
 */
fun <ItemT: Item> ViewManager.selectableItemListView(
    itemViewBinder: SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: SelectableItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    SelectableItemListView(it, itemViewBinder, emptyViewBinder)
}, 0, init)

/**
 * Anko specific view function for SelectableItemViewBinder
 *
 * @see SelectableItemViewBinder
 */
fun <ItemT: Item> Activity.selectableItemListView(
    itemViewBinder: SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: SelectableItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    SelectableItemListView(it, itemViewBinder, emptyViewBinder)
}, 0, init)