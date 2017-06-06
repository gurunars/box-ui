package com.gurunars.item_list

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.setPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

internal open class ItemViewBinderString<ItemType : Item> : ItemViewBinder<TextView, ItemType> {

    override fun getView(context: Context): TextView {
        return TextView(context).apply {
            asRow()
            setPadding(context.dip(5))
        }
    }

    override fun bind(itemView: TextView, item: ItemType, previousItem: ItemType?) {
        itemView.text = item.toString()
    }

}
