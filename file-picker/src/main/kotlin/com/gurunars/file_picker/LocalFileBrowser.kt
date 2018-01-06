package com.gurunars.file_picker

import android.Manifest
import android.os.Environment
import android.util.Log
import com.gurunars.box.Box
import java.io.File

class ExternalStorageBrowser(
    private val extension: String? = null,
    private val showFiles: Boolean = true
) : FileBrowser {

    val topMostDir = Environment.getExternalStorageDirectory().absolutePath

    override val hasPermissions = Box(false)
    override val currentDirectory = Box(FileBrowser.ROOT_PATH)
    override val files = Box(listOf<FileItem>())
    override val permissions = listOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    init {
        currentDirectory.onChange {
            if (it != topMostDir) {
                currentDirectory.set(topMostDir)
                return@onChange
            }
            files.set(getListFiles(it))
        }
        hasPermissions.onChange(false) {
            currentDirectory.set(currentDirectory.get(), true)
        }
    }

    private fun abspath(root: String, path: String) =
        "/" + root.removePrefix(topMostDir).trim('/') + "/" + path

    private fun getListFiles(parent: String): List<FileItem> {
        if (!hasPermissions.get()) {
            return listOf()
        }
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