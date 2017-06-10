package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import org.jetbrains.anko.matchParent
import java.util.*

/**
 * Item list that has selection enabled.
 *
 * Items can be selected via long clicking and consequentially clicking them.
 *
 * @param <ItemType> class describing item payload
 */
class SelectableItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

    private val itemList: ItemList<SelectableItem<ItemType>> = itemList {
        id=R.id.itemList
        layoutParams=LayoutParams(matchParent, matchParent)
    }

    private val collectionManager: CollectionManager<ItemType>
    private var selectionChangeListener: Runnable? = null

    init {

        collectionManager = CollectionManager({ itemList.setItems(it) }, Runnable {
            selectionChangeListener?.run()
        })

        itemList.setDefaultViewBinder(ClickableItemViewBinder(
                SelectableItemViewBinderString<ItemType>(), collectionManager
        ))

    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    fun setItems(items: List<ItemType>) {
        collectionManager.setItems(items)
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Item
     *
     * @param itemViewBinder renderer for the items of a given type wrapped into a container with
     * *                       an isSelected flag
     */
    fun registerItemViewBinder(itemType: Enum<*>,
                               itemViewBinder: ItemViewBinder<out View, SelectableItem<ItemType>>) {
        itemList.registerItemViewBinder(itemType,
                ClickableItemViewBinder(itemViewBinder, collectionManager))
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder)
    }

    /**
     * A set of items selected ATM
     */
    var selectedItems: Set<ItemType>
        get() = collectionManager.getSelectedItems()
        set(selectedItems) {
            collectionManager.setSelectedItems(selectedItems)
        }

    /**
     * Configure a listener for the cases when the item collection within the view is updated. I.e.
     * if selection status or payload value gets changed.
     *
     * @param stateChangeListener callable to invoked whenever the changes occur
     */
    fun setSelectionChangeListener(stateChangeListener: Runnable) {
        this.selectionChangeListener = stateChangeListener
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putSerializable("selectedItems", HashSet(collectionManager.getSelectedItems()))
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable<Parcelable>("superState"))
        selectedItems = localState.getSerializable("selectedItems") as HashSet<ItemType>
    }
}
