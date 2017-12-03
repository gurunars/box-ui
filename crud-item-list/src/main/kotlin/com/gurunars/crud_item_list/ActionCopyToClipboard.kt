package com.gurunars.crud_item_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.gurunars.item_list.Item
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread

internal class ActionCopyToClipboard<ItemType : Item>(
    private val context: Context,
    private val serializer: ClipboardSerializer<ItemType>
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private fun writeToClipboard(label: String, payload: String) {
        clipboard.primaryClip = ClipData.newPlainText(label, payload)
        context.longToast(R.string.copiedToClipboard)
    }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) {
        doAsync {
            val clip = serializer.toString(
                all.filter
                { item -> selectedItems.find { item.id == it.id } != null }
            )
            uiThread {
                writeToClipboard(
                    serializer.serializationLabel,
                    clip
                )
            }
        }
    }

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) = consumer(selectedItems.isNotEmpty())
}