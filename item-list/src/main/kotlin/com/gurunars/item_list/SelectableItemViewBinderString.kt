package com.gurunars.item_list

import android.graphics.Color
import android.widget.TextView

internal class SelectableItemViewBinderString<ItemType : Item> : ItemViewBinderString<SelectableItem<ItemType>>() {

    override fun bind(itemView: TextView, item: SelectableItem<ItemType>, previousItem: SelectableItem<ItemType>?) {
        super.bind(itemView, item, previousItem)
        itemView.setBackgroundColor(if (item.isSelected) Color.RED else Color.TRANSPARENT)
    }

}
