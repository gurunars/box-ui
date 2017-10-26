package com.gurunars.item_list

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import org.jetbrains.anko.custom.ankoView

fun <ItemType : Item> ViewGroup.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = context::defaultBindEmpty,
    stableIds: Boolean = false,
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder, stableIds) }, 0, init)

fun <ItemType : Item> Activity.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    stableIds: Boolean = false,
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder, stableIds) }, 0, init)

fun <ItemType : Item> Context.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    stableIds: Boolean = false,
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder, stableIds) }, 0, init)