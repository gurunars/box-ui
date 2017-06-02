package com.gurunars.item_list

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


fun <ItemT: Item> ViewManager.itemList(theme: Int = 0): ItemList<ItemT> = itemList(theme) {}
fun <ItemT: Item> ViewManager.itemList(theme: Int = 0, init: ItemList<ItemT>.() -> Unit) = ankoView({ ItemList(it) }, theme, init)


fun <ItemT: Item> ViewManager.selectableItemList(theme: Int = 0): SelectableItemList<ItemT> = selectableItemList(theme) {}
fun <ItemT: Item> ViewManager.selectableItemList(theme: Int = 0, init: SelectableItemList<ItemT>.() -> Unit) = ankoView({ SelectableItemList(it) }, theme, init)