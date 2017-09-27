package com.gurunars.crud_item_list

import com.gurunars.item_list.Item


data class StringItem(val text: String) : Item {
    override val id = text.hashCode().toLong()
}

fun Set<String>.itemize() = map { StringItem(it) }.toSet()
fun List<String>.itemize() = map { StringItem(it) }

internal fun Action<StringItem>.canPerform(items: List<String>, selectedItems: Set<String>) =
    canPerform(items.itemize(), selectedItems.itemize())

internal fun Action<StringItem>.perform(items: List<String>, selectedItems: Set<String>) =
    with(perform(items.itemize(), selectedItems.itemize())) {
        Pair(first.map { it.text }, second.map { it.text }.toSet())
    }
