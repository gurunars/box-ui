package com.gurunars.item_list

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

internal open class ItemViewBinderString<ItemType : Item> : ItemViewBinder<TextView, ItemType> {

    override fun getView(context: Context): TextView {
        return TextView(context).apply {
            layoutParams= ViewGroup.LayoutParams(matchParent, wrapContent)
            val padding = context.dip(5)
            setPadding(padding, padding, padding, padding)
        }
    }

    override fun bind(itemView: TextView, item: ItemType, previousItem: ItemType?) {
        itemView.text = item.toString()
    }

}
