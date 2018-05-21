package com.gurunars.box.ui.components.listcontroller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.components.listview.WithId

internal class ActionCopyToClipboard<ItemType: WithId>(
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>?
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private fun writeToClipboard(label: String, payload: String) {
        clipboard.primaryClip = ClipData.newPlainText(label, payload)
    }

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType> {
        serializer ?: return state
        writeToClipboard(
            serializer.serializationLabel,
            serializer.toString(state.all.filter { state.selected.contains(it.id) })
        )
        return state
    }

    override fun canPerform(state: IReadOnlyObservableValue<ListState<ItemType>>): IReadOnlyObservableValue<Boolean> =
        state.bind { selected.isNotEmpty() }

}