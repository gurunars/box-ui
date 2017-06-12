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

    init {

        itemList.setDefaultViewBinder(ClickableItemViewBinder(
                SelectableItemViewBinderString<ItemType>(), collectionManager
        ))

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
