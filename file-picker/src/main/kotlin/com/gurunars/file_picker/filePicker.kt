package com.gurunars.file_picker

import com.gurunars.item_list.itemListView
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.oneWayBranch
import com.gurunars.box.ui.add
import com.gurunars.box.ui.txt
import com.gurunars.box.ui.statefulView
import com.gurunars.box.ui.isVisible
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.backgroundColor
import com.gurunars.box.ui.src
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

fun Context.renderItem(
    field: IBox<FileItem>,
    selectedItem: IBox<FileItem?>,
    pickMode: PickMode,
    regularColor: Int,
    selectedColor: Int
): View = linearLayout {
    asRow()
    padding=dip(3)
    gravity=Gravity.CENTER_VERTICAL

    setOnClickListener {
        val item = field.get()
        if (pickMode.canPick(item.type)) {
            selectedItem.set(item)
        }
    }

    backgroundColor(selectedItem.oneWayBranch {
        if (this == field.get()) selectedColor else regularColor
    })

    imageView {
        src(
            field.oneWayBranch {
                when(this.type) {
                    FileItem.Type.FILE -> R.drawable.ic_file
                    else -> R.drawable.ic_folder
                }
            }
        )
    }.lparams {
        width=dip(24)
        height=dip(24)
    }

    textView {
        txt(field.oneWayBranch { path })
    }.lparams {
        asRow()
        leftMargin=dip(10)
    }
}

fun Context.filePicker(
    pickMode: PickMode,
    fileBrowser: FileBrowser,
    regularColor: Int = Color.TRANSPARENT,
    selectedColor: Int = Color.RED
) = statefulView(R.id.filePicker) {
    verticalLayout {
        linearLayout {
            gravity = Gravity.CENTER_VERTICAL
            padding = dip(3)

            imageView(R.drawable.ic_parent) {
                id = R.id.parentIcon
                isVisible(
                    fileBrowser.currentDirectory.oneWayBranch { this != FileBrowser.ROOT_PATH }
                )
            }.lparams {
                width = dip(24)
                height = dip(24)
            }

            textView {
                id = R.id.currentPathText
                txt(fileBrowser.currentDirectory)
            }.lparams {
                width = matchParent
                leftMargin = dip(10)
            }
        }.lparams {
            width = matchParent
        }

        val selectedItem: Box<FileItem?> = Box(null)

        val render = { field: IBox<FileItem> -> renderItem(
            field,
            selectedItem,
            pickMode,
            regularColor,
            selectedColor
        ) }

        val binders = mapOf<Enum<*>, (field: IBox<FileItem>) -> View>(
            FileItem.Type.FOLDER to render,
            FileItem.Type.FILE to render
        )

        itemListView(
            items = fileBrowser.files,
            itemViewBinders = binders
        ).add(this) {
            id = R.id.files
        }.lparams {
            weight = 1f
            height = dip(0)
            width = matchParent
        }
    }
}