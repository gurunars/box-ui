package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.possess
import com.gurunars.shortcuts.asRow

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param itemViewBinder original view binder unaware of selection flag
 * @param selectionColor color integer applied when the row is selected
 * @param regularColor color integer applied when the row is not selected
 */
class ColoredItemViewBinder<ItemType: Item>(
    private val itemViewBinder: ItemViewBinder<ItemType>,
    @ColorInt private val selectionColor: Int = Color.RED,
    @ColorInt private val regularColor: Int = Color.TRANSPARENT
): ItemViewBinder<SelectableItem<ItemType>>{
    override fun bind(context: Context, field: BindableField<SelectableItem<ItemType>>): View {
        val newField = BindableField(field.get().item)
        return itemViewBinder.bind(context, newField).apply {
            asRow()
            possess(newField)
            field.onChange {
                setTag(R.id.isSelected, it.isSelected)
                setBackgroundColor(if (it.isSelected) selectionColor else regularColor)
                newField.set(it.item, true)
            }
        }
    }
}
