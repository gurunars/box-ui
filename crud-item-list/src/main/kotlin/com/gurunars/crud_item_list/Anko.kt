package com.gurunars.crud_item_list

import android.app.Activity
import android.view.ViewManager
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemViewBinder
import com.gurunars.item_list.defaultEmptyViewBinder
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