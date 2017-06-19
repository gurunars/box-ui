
package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gurunars.databinding.bindableField
import com.gurunars.item_list.ItemViewBinderEmpty.Companion.EMPTY_TYPE
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize


internal class BindableViewHolder<ItemType : Item> : RecyclerView.ViewHolder {

    constructor(root: ViewGroup,
                itemViewBinder: ItemViewBinder<ItemType>) :
        super(itemViewBinder.getView(root.context, root.bindableField(Pair(null, null))).apply {
            asRow()
        })

    constructor(root: ViewGroup,
                emptyViewBinder: EmptyViewBinder) :
        super(emptyViewBinder.getView(root.context).apply {
            fullSize()
            setTag(EMPTY_TYPE, EMPTY_TYPE)
        })

}
