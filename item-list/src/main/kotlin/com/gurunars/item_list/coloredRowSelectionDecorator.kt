package com.gurunars.item_list

import android.graphics.Color
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.asRow
import com.gurunars.databinding.android.component
import com.gurunars.databinding.branch

typealias ItemRenderer<ItemType> = (field: BindableField<ItemType>) -> View

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param field selectable payload to be rendered
 * @param selectionColor color integer applied when the row is selected
 * @param regularColor color integer applied when the row is not selected
 * @param render regular item renderer
 */
fun <ItemType : Item> coloredRowSelectionDecorator(
    field: BindableField<SelectableItem<ItemType>>,
    @ColorInt selectionColor: Int = Color.RED,
    @ColorInt regularColor: Int = Color.TRANSPARENT,
    render: ItemRenderer<ItemType>
) = component {
    render(field.branch { item }).apply {
        asRow()
        field.onChange {
            setTag(R.id.isSelected, it.isSelected)
            setBackgroundColor(if (it.isSelected) selectionColor else regularColor)
        }
    }
}