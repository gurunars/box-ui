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
class SelectableItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

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

    val emptyViewBinder = bindableField<EmptyViewBinder>(ItemViewBinderEmpty())
    val defaultViewBinder = bindableField<ItemViewBinder<*, SelectableItem<ItemType>>>(
        SelectableItemViewBinderString<ItemType>()
    )
    val itemViewBinders = bindableField<Map<Enum<*>, ItemViewBinder<*, SelectableItem<ItemType>>>>(mapOf())

    init {
        itemList<SelectableItem<ItemType>> {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemList

            self.defaultViewBinder.onChange {
                defaultViewBinder.set(ClickableItemViewBinder(it, selectedItems))
            }
            self.emptyViewBinder.onChange { emptyViewBinder.set(it) }
            self.itemViewBinders.onChange {
                val value = it
                itemViewBinders.set(mutableMapOf<Enum<*>, ItemViewBinder<*, SelectableItem<ItemType>>>().apply {
                    value.forEach { type, view ->
                        run {
                            put(type, ClickableItemViewBinder(view, selectedItems))
                        }
                    }
                })
            }

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
