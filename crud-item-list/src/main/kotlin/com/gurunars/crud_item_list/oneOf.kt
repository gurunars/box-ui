package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/**
 * A shortcut function to wrap a single descriptor into a list of lists.
 */
fun <ItemType : Item> ItemTypeDescriptor<ItemType>.oneOf() = listOf(listOf(this))