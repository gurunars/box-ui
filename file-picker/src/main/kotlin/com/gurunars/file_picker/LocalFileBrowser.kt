package com.gurunars.file_picker

import android.util.Log
import com.gurunars.box.Box
import java.io.File

class LocalFileBrowser(
    private val topMostDir: String = FileBrowser.ROOT_PATH,
    private val extension: String? = null,
    private val showFiles: Boolean = true
) : FileBrowser {

    override val currentDirectory = Box(FileBrowser.ROOT_PATH)
    override val files = Box(listOf<FileItem>())

    private fun abspath(root: String, path: String) =
        root.removeSuffix("/") + "/" + path

    init {
        currentDirectory.onChange {
            if (it != topMostDir) {
                currentDirectory.set(topMostDir)
                return@onChange
            }
            files.set(getListFiles(it))
        }
    }

    private fun getListFiles(parent: String): List<FileItem> {
        return (File(parent).listFiles() ?: arrayOf<File>()).filter {
            if (it.isDirectory) {
                return@filter true
            } else if (it.isFile && showFiles) {
                if (extension == null) {
                    return@filter true
                } else if (it.name.endsWith("." + extension)) {
                    return@filter true
                }
            }
            false
        }.map { file ->
            FileItem(
                if (file.isDirectory) FileItem.Type.FOLDER else FileItem.Type.FILE,
                file.name)
        }
    }

}