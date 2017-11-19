package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.gurunars.item_list.Item
import org.jetbrains.anko.longToast

internal class ActionPasteFromClipboard<ItemType : Item>(
    private val context: Context,
    private val serializer: ClipboardSerializer<ItemType>
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        val payload = clipboard.primaryClip.getItemAt(0).text
        return try {
            val full = payload.split("\n").map(serializer::fromString)
            context.longToast(R.string.pastedFromClipboard)
            Pair(
                all + full,
                selectedItems
            )
        } catch (exe: Exception) {
            Log.e("CRUD-PASTE", exe.message, exe)
            context.longToast(R.string.failedToPaste)
            Pair(
                all,
                selectedItems
            )
        }
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>): Boolean {
        if (!clipboard.hasPrimaryClip()) {
            return false
        } else {
            val clip = clipboard.primaryClip
            if (clip.description.label != serializer.serializationLabel) {
                return false
            }
            return clip.itemCount > 0
        }
    }

}