package com.gurunars.item_list

import android.app.Activity
import android.view.ViewManager
import com.gurunars.crud_item_list.CrudItemList
import org.jetbrains.anko.custom.ankoView


fun <ItemT: Item> ViewManager.crudItemList(
    itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: CrudItemList<ItemT>.() -> Unit = {}) =
ankoView({
    CrudItemList(it, itemViewBinderFetcher, emptyViewBinder)
}, 0, init)


fun <ItemT: Item> Activity.crudItemList(
    itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemT>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    init: CrudItemList<ItemT>.() -> Unit = {}) =
    ankoView({
        CrudItemList(it, itemViewBinderFetcher, emptyViewBinder)
    }, 0, init)