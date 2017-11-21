package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import com.gurunars.item_list.Item
import org.jetbrains.anko.longToast

internal class ActionPasteFromClipboard<ItemType : Item>(
    private val context: Context,
    private val serializer: ClipboardSerializer<ItemType>
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private fun getPasteCandidates(): List<ItemType> {
        if (!clipboard.hasPrimaryClip()) { return listOf() }
        val clip = clipboard.primaryClip
        if(clip.itemCount <= 0) { return listOf() }
        return try {
            serializer.fromString(clip.getItemAt(0).text.toString())
        } catch (exe: Exception) {
            listOf()
        }
    }

    override fun perform(all: List<ItemType>, selectedItems: Set<ItemType>): Pair<List<ItemType>, Set<ItemType>> {
        val full = getPasteCandidates()
        if (full.isNotEmpty()) {
            context.longToast(R.string.pastedFromClipboard)
        } else {
            context.longToast(R.string.failedToPaste)
        }
        return Pair(
            all + full,
            selectedItems
        )
    }

    override fun canPerform(all: List<ItemType>, selectedItems: Set<ItemType>)
        = getPasteCandidates().isNotEmpty()

}