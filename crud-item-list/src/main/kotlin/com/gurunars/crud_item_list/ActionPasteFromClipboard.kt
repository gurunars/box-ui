package com.gurunars.crud_item_list

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.item_list.Item

internal class ActionPasteFromClipboard<ItemType : Item>(
    private val view: View,
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>?
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
            serializer?.fromString(clip.getItemAt(0).text.toString()) ?: listOf()
        } catch (exe: Exception) {
            Log.d("crud-item-list", "Can't paste", exe.cause)
            listOf()
        }
    }

    override fun perform(
        all: List<ItemType>,
        selectedItems: Set<ItemType>,
        consumer: ItemSetChange<ItemType>
    ) =
        consume<List<ItemType>> { toPaste ->
            if (toPaste.isNotEmpty()) {
                okToast
            } else {
                nokToast
            }.show()
            consumer(insertAfterLastSelected(all, selectedItems, toPaste), selectedItems)
        }.from {
            getPasteCandidates()
        }

    override fun canPerform(
        all: IRoBox<List<ItemType>>,
        selectedItems: IRoBox<Set<ItemType>>
    ): IRoBox<Boolean> {
        val can = Box(false)

        fun onClipChange() {
            can.set(getPasteCandidates().isNotEmpty())
        }

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                clipboard.removePrimaryClipChangedListener(::onClipChange)
            }

            override fun onViewAttachedToWindow(v: View?) {
                clipboard.addPrimaryClipChangedListener(::onClipChange)
            }
        })

        return can
    }

}