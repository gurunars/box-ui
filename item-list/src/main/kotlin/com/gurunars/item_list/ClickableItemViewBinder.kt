package com.gurunars.item_list

import android.content.Context
import com.gurunars.databinding.BindableField

/**
 * Component binder that accepts a selectable item wrapper.
 *
 * @see ItemViewBinder
 * @see SelectableItem
 */
typealias SelectableItemViewBinder<ItemType> = ItemViewBinder<SelectableItem<ItemType>>

internal fun <ItemType : Item> clickableSelector(
    selectedItems: BindableField<Set<ItemType>>,
    itemViewBinder: SelectableItemViewBinder<ItemType>
) :SelectableItemViewBinder<ItemType> {

    return {
        context: Context,
        itemType: Enum<*>,
        payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>
    ->
        itemViewBinder(context, itemType, payload).apply {
            isClickable = true
            setOnClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                selectedItems.set(
                    if (sel.isEmpty())
                        setOf()
                    else if (selectedItems.get().has(item))
                        sel.exclude(item.item)
                    else
                        sel.include(item.item)
                )

            }
            setOnLongClickListener {
                val item = payload.get().first
                val sel = selectedItems.get()
                if (sel.isEmpty()) selectedItems.set(sel.include(item.item))
                true
            }
        }

    }

}
