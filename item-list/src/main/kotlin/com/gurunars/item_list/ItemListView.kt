package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setOneView
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.objenesis.strategy.StdInstantiatorStrategy


/**
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param itemViewBinder a function binding an observable to the actual view
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 *
 * @property items A collection of items shown in the list
 */
class ItemListView<ItemType : Item> (
    context: Context,
    itemViewBinder: ItemViewBinder<ItemType>,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    val items = bindableField(
        listOf<ItemType>(),
        {item -> kryo.copy(ArrayList(item))}
    )

    init {
        setOneView(RecyclerView(context).apply {
            id=R.id.recyclerView
            fullSize()
            clipToPadding=false
            bottomPadding=dip(60)
            isSaveEnabled=false
            adapter = ItemAdapter(items, emptyViewBinder, itemViewBinder)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        })

    }

}
