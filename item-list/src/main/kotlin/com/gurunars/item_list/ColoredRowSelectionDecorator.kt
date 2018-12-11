package com.gurunars.item_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.view.View
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param selectionColor color integer applied when the row is selected,
 * @param isSelected indicates selection status
 */
fun View.coloredRowSelectionDecorator(
    isSelected: IRoBox<Boolean>,
    @ColorInt selectionColor: Int = Color.RED
) {
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
}