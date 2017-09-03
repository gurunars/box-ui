package com.gurunars.crud_item_list

import android.content.Context
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.*
import org.jetbrains.anko.button
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.relativeLayout

internal class ItemForm<ItemType: Item>(
    context: Context,
    onClose: () -> Unit,
    onConfirm: (item: ItemType) -> Unit
): FrameLayout(context) {
    lateinit var content: FrameLayout
    lateinit var itemInEdit: BindableField<ItemType>

    init {
        relativeLayout {
            content = frameLayout {
                fullSize()
            }

            fun close() {
                onClose()
                itemInEdit?.unbindAll()
            }

            button(R.string.cancel){
                setOnClickListener {
                    close()
                }
            }.lparams {
                alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            }

            button(R.string.save){
                setOnClickListener {
                    onConfirm(itemInEdit.get())
                    close()
                }
            }.lparams {
                alignInParent(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
            }
        }
    }

    fun bind(
        item: ItemType,
        formBinder: ItemFormBinder<ItemType>
    ) {
        itemInEdit = content.bindableField(item)
        content.setOneView(formBinder(context, itemInEdit))
    }

}
