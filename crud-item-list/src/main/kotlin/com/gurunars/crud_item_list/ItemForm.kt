package com.gurunars.crud_item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.setOneView

internal class ItemForm<ItemType: Item>(
    context: Context,
    private val onClose: () -> Unit,
    private val onConfirm: (item: ItemType) -> Unit
): FrameLayout(context) {
    var itemInEdit: BindableField<ItemType>? = null

    fun close() {
        onClose()
        itemInEdit?.unbindAll()
    }

    fun confirm() {
        val field = itemInEdit
        if (field != null) {
            onConfirm(field.get())
        }
        close()
    }

    fun bind(
        item: ItemType,
        formBinder: ItemFormBinder<ItemType>
    ) {
        val field = bindableField(item)
        itemInEdit = field
        setOneView(formBinder(context, field, this::close, this::confirm))
    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putSerializable("itemInEdit", itemInEdit?.get())
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
            val payload = getSerializable("itemInEdit")
            if (payload != null) {
                itemInEdit?.set(payload as ItemType)
            }
        }
    }

}
