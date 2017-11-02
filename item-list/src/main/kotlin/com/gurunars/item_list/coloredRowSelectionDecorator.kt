package com.gurunars.item_list

import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.asRow

typealias ItemRenderer<ItemType> = (field: BindableField<ItemType>) -> View

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param itemViewBinder original view binder unaware of selection flag
 * @param selectionColor color integer applied when the row is selected
 * @param regularColor color integer applied when the row is not selected
 */
fun <ItemType : Item> coloredRowSelectionDecorator(
    field: BindableField<SelectableItem<ItemType>>,
    @ColorInt selectionColor: Int = Color.RED,
    @ColorInt regularColor: Int = Color.TRANSPARENT,
    render: ItemRenderer<ItemType>
): View {
    val newField = BindableField(field.get().item)
    return render(newField).apply {
        asRow()
        field.onChange {
            setTag(R.id.isSelected, it.isSelected)
            setBackgroundColor(if (it.isSelected) selectionColor else regularColor)
            newField.set(it.item, true)
        }
    }
}