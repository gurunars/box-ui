package com.gurunars.file_picker

import com.gurunars.item_list.Item

data class FileItem(
    override val type: FileItem.Type,
    internal val path: String
) : Item {

    enum class Type {
        FOLDER, FILE
    }

    override val id: Long
        get() = path.hashCode().toLong()

}