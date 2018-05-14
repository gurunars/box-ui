package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

data class StringItem(
    val text: String,
    override val id: Long = text.hashCode().toLong()
) : Item

fun Set<String>.itemize() = map { StringItem(it) }.toSet()
fun List<String>.itemize() = map { StringItem(it) }

internal fun Action<StringItem>.canPerform(items: List<String>, selectedItems: Set<String>, consumer: CanDo) =
    canPerform(items.itemize(), selectedItems.itemize(), consumer)

internal fun Action<StringItem>.perform(
    items: List<String>,
    selectedItems: Set<String>,
    consumer: (items: List<String>) -> Unit
) =
    perform(items.itemize(), selectedItems.itemize(),
        { first, _ -> consumer(first.map { it.text }) })
