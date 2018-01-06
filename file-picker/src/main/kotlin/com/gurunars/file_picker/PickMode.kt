package com.gurunars.file_picker

enum class PickMode(vararg val itemTypes: FileItem.Type) {
    FILES(FileItem.Type.FILE),
    DIRECTORIES(FileItem.Type.FOLDER),
    ALL(FileItem.Type.FILE, FileItem.Type.FOLDER);

    fun canPick(type: FileItem.Type) =
        itemTypes.contains(type)
}