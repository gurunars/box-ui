package com.gurunars.box.ui.components.listcontroller

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.ui.components.listview.WithId

internal class ActionPasteFromClipboard<ItemType: WithId>(
    private val view: View,
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>?
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private fun getPasteCandidates(): List<ItemType> {
        if (!clipboard.hasPrimaryClip()) {
            return listOf()
        }
        val clip = clipboard.primaryClip ?: return emptyList()
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

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType> =
        ListState(
            insertAfterLastSelected(state.all, state.selected, getPasteCandidates()),
            state.selected
        )

    override fun canPerform(
        state: IReadOnlyObservableValue<ListState<ItemType>>
    ): IReadOnlyObservableValue<Boolean> {
        val can = ObservableValue(false)

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