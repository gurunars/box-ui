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

class ItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

    private val kryo = Kryo().apply {
        instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    val items = bindableField(
        listOf<ItemType>(),
        {one, two -> equal(one, two) },
        {item -> kryo.copy(ArrayList(item))}
    )

    val emptyViewBinder = bindableField<EmptyViewBinder>(ItemViewBinderEmpty())
    val itemViewBinders = bindableField<Map<Enum<*>, ItemViewBinder<*, ItemType>>>(mapOf())
    val defaultViewBinder = bindableField<ItemViewBinder<*, ItemType>>(ItemViewBinderString())

    init {

        recyclerView {
            id=R.id.recyclerView
            fullSize()
            clipToPadding=false
            bottomPadding=dip(60)
            isSaveEnabled=false
            adapter = ItemAdapter(
                items,
                emptyViewBinder,
                itemViewBinders,
                defaultViewBinder
            )
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

    }

}
