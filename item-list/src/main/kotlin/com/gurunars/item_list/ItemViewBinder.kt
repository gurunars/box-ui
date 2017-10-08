package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.asRow
import org.jetbrains.anko.UI
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView

interface ItemViewBinder<ItemType : Item> {
    /**
     * @param field field representing item's payload
     * @return a view bound to a field holding the item
     */
    fun bind(field: BindableField<ItemType>): View
}

class DefaultItemViewBinder<ItemType : Item>(
    private val context: Context
) : ItemViewBinder<ItemType> {
    override fun bind(field: BindableField<ItemType>) = with(context) {
        UI {
            textView {
                backgroundColor = Color.YELLOW
                textColor = Color.RED
                field.onChange {
                    text = with(field.get().toString()) {
                        substring(0, minOf(42, length))
                    }
                    asRow()
                }
            }
        }.view
    }
}
