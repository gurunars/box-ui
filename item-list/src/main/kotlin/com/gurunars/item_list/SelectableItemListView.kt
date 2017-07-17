package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*
import kotlin.collections.HashSet


/**
 * Item list that has selection enabled.
 *
 * Items can be selected via long clicking and consequentially clicking them.
 *
 * @param <ItemType> class describing item payload
 */

class SelectableItemListView<ItemType : Item> constructor(
        context: Context,
        itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemType>,
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
            itemViewBinderFetcher=
                ClickableItemViewBinder(
                        selectedItems,
                        itemViewBinderFetcher.invoke()),
            emptyViewBinder=emptyViewBinder
        ) {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemListView

            fun isSelected(item: ItemType): Boolean {
                return selectedItems.get().find { item.getId() == it.getId() } != null
            }

            fun updateItemList() {
                items.set(self.items.get().map { SelectableItem(it, isSelected(it)) })
            }

            self.items.onChange { updateItemList() }
            self.selectedItems.onChange { updateItemList() }

        }
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putSerializable("selectedItems", HashSet(selectedItems.get()))
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable<Parcelable>("superState"))
        selectedItems.set(localState.getSerializable("selectedItems") as HashSet<ItemType>)
    }
}
