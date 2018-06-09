package com.gurunars.item_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.support.annotation.ColorInt
import android.util.Log
import com.gurunars.box.*
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.onLongClick

/**
 * A decorator to add row coloring behavior to the list view items.
 *
 * @param selectedItems
 * @param selectionColor
 */
fun <ItemType : Item> ItemViewBinder<ItemType>.withSelection(
    selectedItems: IBox<Set<ItemType>>,
    explicitSelectionMode: IRoBox<Boolean> = false.box,
    @ColorInt selectionColor: Int = Color.RED
): ItemViewBinder<ItemType> = { field ->
    this.invoke(field).apply {
        isClickable = true
        asRow()

        onClick {
            val item = field.get()
            val sel = selectedItems.get()
            selectedItems.set(
                when {
                    sel.isEmpty() && !explicitSelectionMode.get() -> setOf()
                    sel.contains(item) -> sel - item
                    else -> sel + item
                }
            )
        }

        onLongClick {
            val item = field.get()
            val sel = selectedItems.get()
            if (sel.isEmpty()) selectedItems.set(sel + item)
        }

        val originalBackground = if (background is LayerDrawable) {
            val current = background as LayerDrawable
            LayerDrawable(
                (0 until current.numberOfLayers).map { current.getDrawable(it) }.toTypedArray()
            )
        } else {
            background.mutate()
        }

        merge(
            selectedItems.oneWayBranch { map { it.id } },
            field.oneWayBranch { id }
        ).oneWayBranch { first.contains(second) }
            .onChange { selected ->
                setTag(R.id.isSelected, selected)
                background =
                    if (selected)
                        LayerDrawable(
                            listOf(
                                ColorDrawable(selectionColor),
                                background
                            ).toTypedArray()
                        )
                    else
                        originalBackground
            }
    }
}

fun <ItemType: Item> Map<Enum<*>, ItemViewBinder<ItemType>>.withSelection(
    selectedItems: IBox<Set<ItemType>>,
    explicitSelectionMode: IRoBox<Boolean> = false.box,
    @ColorInt selectionColor: Int = Color.RED
): Map<Enum<*>, ItemViewBinder<ItemType>> =
    this.entries.map {
        Pair(it.key, it.value.withSelection(selectedItems, explicitSelectionMode, selectionColor))
    }.toMap()