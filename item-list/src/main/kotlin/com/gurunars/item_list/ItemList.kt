package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.FrameLayout
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * A wrapper around RecyclerView with reasonable default item change animations and a
 * composition based solution as a mean to bind data to item list views.
 *
 * @param <ItemType> Item payload type
 */
class ItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

    private val itemAdapter: ItemAdapter<ItemType>
    private val llm: LinearLayoutManager

    init {
        llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        itemAdapter = ItemAdapter<ItemType>()

        recyclerView {
            id=R.id.recyclerView
            layoutParams=LayoutParams(matchParent, matchParent)
            clipToPadding=false
            bottomPadding=dip(60)
            isSaveEnabled=false
            setAdapter(itemAdapter)
            setLayoutManager(llm)
        }

    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    fun setItems(items: List<ItemType>) {
        itemAdapter.setItems(items)
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Item
     *
     * @param itemViewBinder renderer for the items of a given type
     */
    fun registerItemViewBinder(itemType: Enum<*>, itemViewBinder: ItemViewBinder<out View, ItemType>) {
        itemAdapter.registerItemViewBinder(itemType, itemViewBinder)
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        itemAdapter.setEmptyViewBinder(emptyViewBinder)
    }

    internal fun setDefaultViewBinder(itemViewBinder: ItemViewBinder<*, ItemType>) {
        itemAdapter.setDefaultViewBinder(itemViewBinder)
    }

}
