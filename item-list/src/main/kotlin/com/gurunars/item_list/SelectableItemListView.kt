package com.gurunars.item_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.box.*
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.onLongClick


/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param selectionColor color integer applied when the row is selected,
 * @param isSelected indicates selection status
 */
fun View.coloredRowSelectionDecorator(
    isSelected: IRoBox<Boolean>,
    @ColorInt selectionColor: Int = Color.RED
): View {
    asRow()
    val originalBackground: Drawable
    if (background is LayerDrawable) {
        val current = background as LayerDrawable
        originalBackground = LayerDrawable(
            (0 until current.numberOfLayers).map { current.getDrawable(it) }.toTypedArray()
        )
    } else {
        originalBackground = background.mutate()
    }
    isSelected.onChange { flag ->
        setTag(R.id.isSelected, flag)
        background = if (flag)
            LayerDrawable(listOf(ColorDrawable(selectionColor), background).toTypedArray())
        else
            originalBackground
    }
    return this
}

fun<T: Any> withSelector(
    selectedItems: IBox<Set<Long>>,
    field: IRoBox<T>,
    getKey: KeyGetter<T> = { it.hashCode().toLong() },
    explicitSelectionMode: IRoBox<Boolean> = Box(false),
    render: (isSelected: IRoBox<Boolean>) -> View
) = render(merge(selectedItems, field).oneWayBranch { first.contains(getKey(second)) }).apply {
    isClickable = true
    onClick {
        val item = field.get()
        val sel = selectedItems.get()
        selectedItems.set(
            when {
                sel.isEmpty() && !explicitSelectionMode.get() -> setOf()
                selectedItems.get().has(getKey(item)) -> sel.exclude(getKey(item))
                else -> sel.include(getKey(item))
            }
        )
    }
    onLongClick {
        val item = field.get()
        val sel = selectedItems.get()
        if (sel.isEmpty()) selectedItems.set(sel.include(getKey(item)))
    }
}
