package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.gurunars.item_list.Item
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

internal class ActionPasteFromClipboard<ItemType : Item>(
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val okToast = Toast.makeText(context, R.string.pastedFromClipboard, Toast.LENGTH_LONG)
    private val nokToast = Toast.makeText(context, R.string.failedToPaste, Toast.LENGTH_LONG)

    private fun getPasteCandidates(): List<ItemType> {
        if (!clipboard.hasPrimaryClip()) {
            return listOf()
        }
        val clip = clipboard.primaryClip
        if (clip.itemCount <= 0) {
            return listOf()
        }
        return try {
            serializer.fromString(clip.getItemAt(0).text.toString())
        } catch (exe: Exception) {
            Log.d("crud-item-list", "Can't paste", exe.cause)
            listOf()
        }
    }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) {
        doAsync {
            val full = getPasteCandidates()
            uiThread {
                if (full.isNotEmpty()) {
                    okToast
                } else {
                    nokToast
                }.show()
                consumer(all + full, selectedItems)
            }
        }
    }

    override fun canPerform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: CanDo
    ) {
        doAsync {
            val canDo = getPasteCandidates().isNotEmpty()
            uiThread {
                consumer(canDo)
            }
        }
    }
}