package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.field
import com.gurunars.databinding.onChange
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.objenesis.strategy.StdInstantiatorStrategy

/**
 * @param ItemType type of the item to be shown in the list
 * @param items A collection of items shown in the list
 * @param itemViewBinders a mapping between item types and view binders
 * meant  to render the respective items
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param stableIds - if false, forces the whole list to be updated whenever the changes arrive
 */
fun <ItemType : Item> Context.itemListView(
    items: BindableField<List<ItemType>>,
    itemViewBinders: BindableField<Map<Enum<*>, ItemViewBinder<ItemType>>> =
        mapOf<Enum<*>, ItemViewBinder<ItemType>>().field,
    emptyViewBinder: BindableField<EmptyViewBinder> = this::defaultBindEmpty.field,
    stableIds: BindableField<Boolean> = false.field
): View = RecyclerView(this).apply {
    val kryo = getKryo()
    val copyOfItems = BindableField(
        listOf<ItemType>(),
        { item -> kryo.copy(ArrayList(item)) }
    )
    items.onChange {
        copyOfItems.set(it)
    }

    id = R.id.recyclerView
    fullSize()
    clipToPadding = false
    bottomPadding = dip(60)
    isSaveEnabled = false
    listOf(emptyViewBinder, itemViewBinders, stableIds).onChange {
        adapter = ItemAdapter(
            copyOfItems,
            emptyViewBinder.get(),
            itemViewBinders.get()
        ).apply {
            setHasStableIds(stableIds.get())
        }
    }
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }
}


