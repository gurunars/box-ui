package com.gurunars.item_list

import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.box.Box
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param field selectable box to be bound with renderer
 * @param render original view binder unaware of selection flag
 * @param selectionColor color integer applied when the row is selected
 * @param regularColor color integer applied when the row is not selected
 */
fun <ItemType : Item> coloredRowSelectionDecorator(
    field: IRoBox<SelectableItem<ItemType>>,
    @ColorInt selectionColor: Int = Color.RED,
    @ColorInt regularColor: Int = Color.TRANSPARENT,
    render: (field: IRoBox<ItemType>) -> View
): View {
    val newField = Box(field.get().item)
    return render(newField).apply {
        asRow()
        field.onChange { item ->
            setTag(R.id.isSelected, item.isSelected)
            setBackgroundColor(if (item.isSelected) selectionColor else regularColor)
            newField.set(item.item, true)
        }
    }
}