package com.gurunars.item_list

import android.content.Context
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.android_utils.flicker
import com.gurunars.databinding.BindableField
import com.gurunars.shortcuts.asRow

/**
 * A shortcut function to subscribe exclusively on a new version of the field payload.
 *
 * @param listener a function that receives a notification about the changes in a payload whenever
 * they occur
 */
fun<ItemType: Item> BindableField<Pair<ItemType, ItemType?>>.onChange(
    listener: (value: ItemType) -> Unit
) { onChange { listener(it.first) } }


typealias SingleValueItemViewBinder<ItemType> = (
    context: Context,
    itemType: Enum<*>,
    field: BindableField<ItemType>
) -> View

/**
 * @param selectedColor color used to highlight a selected row
 * @return ItemViewBinder that:
 *
 * - is highlighted with a specific color on selection
 * - flickers whenever the payload gets updated
 * - marks the view as row (width: match parent, height: wrap content)
 */
fun<ItemType: Item> flickeringColoredRowSelectionDecorator(
    itemViewBinder: SingleValueItemViewBinder<ItemType>,
    @ColorInt selectedColor: Int
): ItemViewBinder<ItemType> = {
    context: Context, itemType: Enum<*>, field: BindableField<Pair<ItemType, ItemType?>> ->
        val newField = BindableField<ItemType>(field.get().first).apply {
            field.onChange {
                set(it.first)
            }
        }
        itemViewBinder(context, itemType, newField).apply {
            asRow()
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {

                }

                override fun onViewDetachedFromWindow(v: View) {
                    newField.unbindAll()
                }
            })
            field.onChange {
                if (it.first != it.second) { flicker() }
            }
        }
}
