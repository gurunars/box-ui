package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setPadding
import org.jetbrains.anko.dip
import org.objenesis.strategy.StdInstantiatorStrategy
import java.util.*
import kotlin.collections.HashSet


fun<ItemType : Item> clickableDecorator(
        itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
        selectedItems: BindableField<Set<ItemType>>): ItemViewBinder<SelectableItem<ItemType>> {
    fun clickableWrapper(
            context: Context,
            payload: BindableField<Pair<SelectableItem<ItemType>?, SelectableItem<ItemType>?>>
    ): View {
        return itemViewBinder(context, payload).apply {
            isClickable=true
            setOnClickListener {
                val item = payload.get().first
                if (item == null) {
                    return@setOnClickListener
                }
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
                if (item == null) {
                    return@setOnLongClickListener true
                }
                val sel = selectedItems.get()
                if (sel.isEmpty()) selectedItems.set(sel + item.item)
                true
            }
        }
    }

    return ::clickableWrapper
}


fun<ItemType: Item> defaultSelectableItemViewBinder(context: Context, payload: BindableField<Pair<SelectableItem<ItemType>?, SelectableItem<ItemType>?>>) : View {
    return TextView(context).apply {
        setPadding(context.dip(5))
        payload.onChange {
            text = it.first.toString()
            setBackgroundColor(if (it.first?.isSelected ?: false) Color.RED else Color.TRANSPARENT)
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

    val emptyViewBinder = bindableField(::defaultEmptyViewBinder)
    val defaultViewBinder = bindableField<ItemViewBinder<SelectableItem<ItemType>>>(
        ::defaultSelectableItemViewBinder
    )
    val itemViewBinders = bindableField<Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>>>(mapOf())

    init {
        itemList<SelectableItem<ItemType>> {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemList

            self.defaultViewBinder.onChange {
                defaultViewBinder.set(clickableDecorator(it, selectedItems))
            }

            self.itemViewBinders.onChange {
                val value = it
                itemViewBinders.set(mutableMapOf<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>>().apply {
                    value.forEach { type, view ->
                        run {
                            put(type, clickableDecorator(view, selectedItems))
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
