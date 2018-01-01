package com.gurunars.crud_item_list.example

import com.gurunars.animal_item.AnimalItem
import com.gurunars.crud_item_list.ClipboardSerializer

internal class Serializer : ClipboardSerializer<AnimalItem> {

    override fun fromString(source: String): List<AnimalItem> =
        source.split("\n").map {
            val parts = it.split("@")
            AnimalItem(
                id = 0L,
                type = AnimalItem.Type.valueOf(parts[0]),
                version = parts[1].toInt()
            )
        }

    override fun toString(source: List<AnimalItem>): String =
        source.joinToString("\n") { "${it.type}@${it.version}" }
}