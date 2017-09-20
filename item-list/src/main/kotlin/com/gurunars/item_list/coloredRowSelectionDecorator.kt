package com.gurunars.item_list

import android.graphics.Color
import android.support.annotation.ColorInt
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.asRow

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param itemViewBinder original view binder unaware of selection flag
 * @param selectionColor color integer applied when the row is selected
 * @param regularColor color integer applied when the row is not selected
 */
fun<ItemType: Item> coloredRowSelectionDecorator(
    itemViewBinder: ItemViewBinder<ItemType>,
    @ColorInt selectionColor: Int=Color.RED,
    @ColorInt regularColor: Int=Color.TRANSPARENT
): ItemViewBinder<SelectableItem<ItemType>> = {
    itemType: Enum<*>,
    field: BindableField<SelectableItem<ItemType>>
->
    val newField = BindableField(field.get().item)
    itemViewBinder(itemType, newField).apply {
        asRow()
        field.onChange {
            setTag(R.id.isSelected, it.isSelected)
            setBackgroundColor(if (it.isSelected) selectionColor else regularColor)
            newField.set(it.item, true)
        }
    }
}