package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.widget.FrameLayout
import com.esotericsoftware.kryo.Kryo
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.objenesis.strategy.StdInstantiatorStrategy


/**
 * @param context Android context
 * @param itemViewBinder a function binding an observable to the actual view
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 */
class ItemListView<ItemType : Item> constructor(
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

        recyclerView {
            id=R.id.recyclerView
            fullSize()
            clipToPadding=false
            bottomPadding=dip(60)
            isSaveEnabled=false
            adapter = ItemAdapter(items, emptyViewBinder, itemViewBinder)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

    }

}
