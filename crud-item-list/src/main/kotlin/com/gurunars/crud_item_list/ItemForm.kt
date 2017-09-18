package com.gurunars.crud_item_list

import android.content.Context
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
    lateinit var itemInEdit: BindableField<ItemType>

    fun close() {
        onClose()
        itemInEdit?.unbindAll()
    }

    fun confirm() {
        onConfirm(itemInEdit.get())
        close()
    }

    fun bind(
        item: ItemType,
        formBinder: ItemFormBinder<ItemType>
    ) {
        itemInEdit = bindableField(item)
        setOneView(formBinder(context, itemInEdit, this::close, this::confirm))
    }

}
