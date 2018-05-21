package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.ui.components.listview.WithId

data class ListState<ItemType: WithId>(
    val all: List<ItemType>,
    val selected: Set<Any>
)

internal interface Action<ItemType: WithId> {
    suspend fun perform(state: ListState<ItemType>): ListState<ItemType>
    fun canPerform(state: IReadOnlyObservableValue<ListState<ItemType>>): IReadOnlyObservableValue<Boolean>
}

/** A mean to serialize an item as a string to transfer it via clipboard. */
interface ClipboardSerializer<ItemType: WithId> {
    /** Obtains a collection of items from a string value in clipboard. */
    fun fromString(source: String): List<ItemType>
    /** Stores a collection of items as a string value in clipboard. */
    fun toString(source: List<ItemType>): String
}

// TODO: it should be possible to paste via a pure textual serializer
internal val ClipboardSerializer<*>.serializationLabel
    get() = "com.gurunars.crud_item_list:${this::class.java.canonicalName}"