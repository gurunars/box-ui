package com.gurunars.item_list

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


fun <ItemT: Item> ViewManager.itemListView(
        itemViewBinderFetcher: ItemViewBinder<ItemT>,
        emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
        init: ItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    ItemListView(it, itemViewBinderFetcher, emptyViewBinder)
}, 0, init)


fun <ItemT: Item> ViewManager.selectableItemListView(
        itemViewBinderFetcher: SelectableItemViewBinder<ItemT>,
        emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
        init: SelectableItemListView<ItemT>.() -> Unit = {}) =
ankoView({
    SelectableItemListView(it, itemViewBinderFetcher, emptyViewBinder)
}, 0, init)