package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*
import kotlin.collections.HashSet


private class ClickableItemViewBinder<ItemType : Item>(
        private val selectedItems: BindableField<Set<ItemType>>,
        private val itemViewBinder: SelectableItemViewBinder<ItemType>
): ItemViewBinder<SelectableItem<ItemType>> {

    override fun getEmptyPayload() = SelectableItem(itemViewBinder.getEmptyPayload(), false)

    override fun bind(context: Context, payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>): View {
        return itemViewBinder.bind(context, payload).apply {
            isClickable=true
            setOnClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                if (sel.isEmpty()) {
                    return@setOnClickListener
                }
                if (selectedItems.get().indexOfFirst { it.getId() == item.getId() } == -1) {
                    selectedItems.set(sel + item.item)
                } else {
                    selectedItems.set(sel.filterNot { it.getId() == item.getId() }.toHashSet())
                }
            }
            setOnLongClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                if (sel.isEmpty()) selectedItems.set(sel + item.item)
                true
            }
        }
    }
}


/**
 * Item list that has selection enabled.
 *
 * Items can be selected via long clicking and consequentially clicking them.
 *
 * @param <ItemType> class describing item payload
 */

class SelectableItemList<ItemType : Item> constructor(
    context: Context,
    itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemType>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    val selectedItems = bindableField<Set<ItemType>>(
        hashSetOf(),
        {one, two -> equal(one, two) },
        {item -> kryo.copy(HashSet(item))}
    )
    val items = bindableField<List<ItemType>>(
        listOf(),
        {one, two -> equal(one, two) },
        {item -> kryo.copy(ArrayList(item))}
    )

    init {
        itemList(
            itemViewBinderFetcher={ type -> ClickableItemViewBinder(
                selectedItems,
                itemViewBinderFetcher.invoke(type))
            },
            emptyViewBinder=emptyViewBinder
        ) {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemList

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
