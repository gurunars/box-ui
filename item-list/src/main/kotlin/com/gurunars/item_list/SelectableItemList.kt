package com.gurunars.item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize
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

    val selectedItems = bindableField<Set<ItemType>>(hashSetOf())
    val items = bindableField<List<ItemType>>(listOf())

    init {
        itemList<ItemType> {
            id = R.id.itemList
            fullSize()
            defaultViewBinder.set(ClickableItemViewBinder(
                    SelectableItemViewBinderString<ItemType>(), selectedItems
            ))

            items.onChange {
                items.set()
            }

            selectedItems.onChange {
                items.set()
            }

        }
    }

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
