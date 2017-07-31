package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.android.bindableField
import com.gurunars.databinding.onChange
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*
import kotlin.collections.HashSet


/**
 * Item list that has selection enabled.
 *
 * Items can be selected initially via long clicking and further on by consequentially
 * clicking them.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param itemViewBinder a function binding an observable of the selectable payload to the actual
 * view
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 *
 * @property items A collection of items shown in the list
 * @property selectedItems A collection of items selected at the moment
 */

class SelectableItemListView<ItemType : Item> constructor(
    context: Context,
    itemViewBinder: SelectableItemViewBinder<ItemType>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    val selectedItems = bindableField<Set<ItemType>>(
        hashSetOf(),
        {item -> kryo.copy(HashSet(item))}
    )

    val items = bindableField<List<ItemType>>(
        listOf(),
        {item -> kryo.copy(ArrayList(item))}
    )

    init {
        itemListView(
            itemViewBinder=clickableSelector(selectedItems, itemViewBinder),
            emptyViewBinder=emptyViewBinder
        ) {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemListView

            listOf(self.items, self.selectedItems).onChange {
                self.selectedItems.set(
                    self.selectedItems.get().filter { self.items.get().has(it) }.toSet())
                items.set(self.items.get().map { SelectableItem(it, selectedItems.get().has(it)) })
            }

        }
    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putSerializable("selectedItems", HashSet(selectedItems.get()))
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
            selectedItems.set(getSerializable("selectedItems") as HashSet<ItemType>)
        }
    }
}
