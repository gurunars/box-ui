package com.gurunars.crud_item_list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class ActionCopyToClipboard<ItemType : Item>(
    context: Context,
    private val serializer: ClipboardSerializer<ItemType>?
) : Action<ItemType> {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val toast = Toast.makeText(context, R.string.copiedToClipboard, Toast.LENGTH_LONG)

    private fun writeToClipboard(label: String, payload: String) {
        clipboard.primaryClip = ClipData.newPlainText(label, payload)
        toast.show()
    }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> {
        serializer ?: return Single.just(state)
        return Single.fromCallable {
            serializer.toString(state.all.filter { state.selected.contains(it.id) })
        }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess { writeToClipboard(serializer.serializationLabel, it) }
        .map { state }
    }

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind { selected.isNotEmpty() }

}