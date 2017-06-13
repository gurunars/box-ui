package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.widget.FrameLayout
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.recyclerview.v7.recyclerView

class ItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

    val items = bindableField(listOf<ItemType>(), {one, two -> equal(one, two) })
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
