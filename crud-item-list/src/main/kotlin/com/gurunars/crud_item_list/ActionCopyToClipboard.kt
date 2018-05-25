package com.gurunars.crud_item_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.item_list.Item

internal class ActionCopyToClipboard<ItemType : Item>(
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val toast = Toast.makeText(context, R.string.copiedToClipboard, Toast.LENGTH_LONG)

    private fun writeToClipboard(label: String, payload: String) {
        clipboard.primaryClip = ClipData.newPlainText(label, payload)
        toast.show()
    }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) =
        consume<String> { clip ->
            writeToClipboard(
                serializer.serializationLabel,
                clip
            )
        }.from {
            serializer.toString(
                all.filter
                { item -> selectedItems.find { item.id == it.id } != null }
            )
        }

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> = selectedItems.oneWayBranch { isNotEmpty() }

}