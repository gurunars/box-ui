package com.gurunars.item_list

import android.annotation.SuppressLint
import android.content.Context
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.StatefulWidget
import com.gurunars.databinding.onChange
import com.gurunars.databinding.android.fullSize
import com.gurunars.shortcuts.setAsOne
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
 * @param itemViewBinders a type based mapping between item type and item renderer
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 *
 * @property items A collection of items shown in the list
 * @property selectedItems A collection of items selected at the moment
 */
@SuppressLint("ViewConstructor")
class SelectableItemListView<ItemType : Item> constructor(
    context: Context,
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = context::defaultBindEmpty
) : StatefulWidget(context), SelectableItemContainer<ItemType> {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    override val selectedItems = BindableField<Set<ItemType>>(
        hashSetOf(),
        { item -> kryo.copy(HashSet(item)) }
    )

    override val items = BindableField<List<ItemType>>(
        listOf(),
        { item -> kryo.copy(ArrayList(item)) }
    )

    init {
        retain(selectedItems)
        ItemListView(
            context,
            itemViewBinders = itemViewBinders.entries.map { it.key to ({
                item: BindableField<SelectableItem<ItemType>> -> clickableBind(selectedItems, it.value, item)
            }) }.toMap(),
            emptyViewBinder = emptyViewBinder
        ).apply {
            id = R.id.itemList
            fullSize()

            val self = this@SelectableItemListView

            listOf(self.items, self.selectedItems).onChange {
                self.selectedItems.set(
                    self.selectedItems.get().filter { self.items.get().has(it) }.toSet())
                items.set(self.items.get().map { SelectableItem(it, selectedItems.get().has(it)) })
            }
        }.setAsOne(this)
    }
}
