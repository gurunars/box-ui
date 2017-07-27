package com.gurunars.crud_item_list

import com.gurunars.item_list.Item


class StringItem(val text: String): Item() {

    enum class Type { ONLY }

    override val id = text.hashCode().toLong()

    override val type = Type.ONLY

    override fun payloadsEqual(other: Item) =
        other is StringItem && text == other.text

    override fun toString(): String {
        return text
    }

}

fun Set<String>.itemize() = map { StringItem(it) }.toSet()
fun List<String>.itemize() = map { StringItem(it) }

internal fun Action<StringItem>.canPerform(items: List<String>, selectedItems: Set<String>) =
    canPerform(items.itemize(), selectedItems.itemize())

internal fun Action<StringItem>.perform(items: List<String>, selectedItems: Set<String>) =
    with(perform(items.itemize(), selectedItems.itemize())) {
        Pair(first.map { it.text }, second.map { it.text }.toSet())
    }
