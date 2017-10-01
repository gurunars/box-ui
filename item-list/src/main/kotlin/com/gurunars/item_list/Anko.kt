package com.gurunars.item_list

import android.app.Activity
import android.content.Context
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

fun <ItemT : Item> Context.itemListView(
    itemViewBinder: ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: ItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        ItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)

fun <ItemT : Item> ViewManager.itemListView(
    itemViewBinder: ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: ItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        ItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)

fun <ItemT : Item> Activity.itemListView(
    itemViewBinder: ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: ItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        ItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)

fun <ItemT : Item> Context.selectableItemListView(
    itemViewBinder: SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: SelectableItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        SelectableItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)

fun <ItemT : Item> ViewManager.selectableItemListView(
    itemViewBinder: SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: SelectableItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        SelectableItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)

fun <ItemT : Item> Activity.selectableItemListView(
    itemViewBinder: SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    init: SelectableItemListView<ItemT>.() -> Unit = {}
) =
    ankoView({
        SelectableItemListView(it, itemViewBinder, emptyViewBinder)
    }, 0, init)