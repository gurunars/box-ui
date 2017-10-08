package com.gurunars.item_list

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.view.ViewManager


import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun <ItemType : Item> ViewGroup.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(context),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Activity.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(this),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)

fun <ItemType : Item> Context.itemListView(
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(this),
    init: ItemListView<ItemType>.() -> Unit
): ItemListView<ItemType> = ankoView({ ItemListView(it, itemViewBinders, emptyViewBinder) }, 0, init)