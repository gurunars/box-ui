package com.gurunars.file_picker

import com.gurunars.box.IBox

interface FileBrowser {
    val currentDirectory: IBox<String>
    val files: IBox<List<FileItem>>
    val permissions: List<String>
    val hasPermissions: IBox<Boolean>

    companion object {
        val ROOT_PATH = "/"
    }
}