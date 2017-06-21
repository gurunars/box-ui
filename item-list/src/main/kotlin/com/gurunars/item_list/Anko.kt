package com.gurunars.item_list

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


fun <ItemT: Item> ViewManager.itemList(
    itemViewBinderFetcher: (Int) -> ItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: ItemList<ItemT>.() -> Unit = {}) =
ankoView({
    ItemList(it, itemViewBinderFetcher, emptyViewBinder)
}, 0, init)


fun <ItemT: Item> ViewManager.selectableItemList(
    itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: SelectableItemList<ItemT>.() -> Unit = {}) =
ankoView({
    SelectableItemList(it, itemViewBinderFetcher, emptyViewBinder)
}, 0, init)