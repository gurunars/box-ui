package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
import java.util.*

/**
 * Item list that has selection enabled.
 *
 * Items can be selected via long clicking and consequentially clicking them.
 *
 * @param <ItemType> class describing item payload
 */
class SelectableItemList<ItemType : Item> constructor(context: Context) : FrameLayout(context) {

    val selectedItems = bindableField<Set<ItemType>>(hashSetOf(), {one, two -> equal(one, two) })
    val items = bindableField<List<ItemType>>(listOf(), {one, two -> equal(one, two) })

    val itemList = itemList<SelectableItem<ItemType>> {
        id = R.id.itemList
        fullSize()
        defaultViewBinder.set(ClickableItemViewBinder(
                SelectableItemViewBinderString<ItemType>(), selectedItems
        ))

        fun isSelected(item: ItemType) : Boolean{
            return selectedItems.get().find { item.getId() == it.getId() } != null
        }

        fun updateItemList() {
            this@SelectableItemList.items.get().map { SelectableItem(it, isSelected(it)) }
        }

        this@SelectableItemList.items.onChange { updateItemList() }
        this@SelectableItemList.selectedItems.onChange { updateItemList() }

    }

    val emptyViewBinder = itemList.emptyViewBinder
    val itemViewBinders = itemList.itemViewBinders
    val defaultViewBinder = itemList.defaultViewBinder

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putSerializable("selectedItems", HashSet(selectedItems.get()))
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable<Parcelable>("superState"))
        selectedItems.set(localState.getSerializable("selectedItems") as HashSet<ItemType>)
    }
}
