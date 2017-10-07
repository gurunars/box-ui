package com.gurunars.item_list

import android.app.Activity
import android.content.Context
import android.view.ViewManager


import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun <ItemType : Item> ViewManager.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Activity.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Context.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)