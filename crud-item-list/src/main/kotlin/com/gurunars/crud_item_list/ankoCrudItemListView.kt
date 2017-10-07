package com.gurunars.crud_item_list

import android.app.Activity
import android.content.Context
import android.view.ViewManager
import com.gurunars.item_list.DefaultEmptyViewBinder
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item

import kotlin.Boolean
import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun <ItemType : Item> ViewManager.crudItemListView(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    sortable: Boolean = true,
    init: CrudItemListView<ItemType>.() -> Unit
): CrudItemListView<ItemType> = ankoView({ CrudItemListView(it, groupedItemTypeDescriptors, emptyViewBinder, sortable) }, 0, init)

fun <ItemType : Item> Activity.crudItemListView(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    sortable: Boolean = true,
    init: CrudItemListView<ItemType>.() -> Unit
): CrudItemListView<ItemType> = ankoView({ CrudItemListView(it, groupedItemTypeDescriptors, emptyViewBinder, sortable) }, 0, init)

fun <ItemType : Item> Context.crudItemListView(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: EmptyViewBinder = DefaultEmptyViewBinder(),
    sortable: Boolean = true,
    init: CrudItemListView<ItemType>.() -> Unit
): CrudItemListView<ItemType> = ankoView({ CrudItemListView(it, groupedItemTypeDescriptors, emptyViewBinder, sortable) }, 0, init)