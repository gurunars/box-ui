package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.gurunars.android_utils.iconView
import com.gurunars.box.box
import com.gurunars.box.ui.*
import com.gurunars.item_list.Item

@SuppressLint("RtlHardcoded")
internal fun <ItemType : Item> Context.creationMenu(
    groupedItemTypeDescriptors: List<ItemTypeDescriptor<ItemType>>,
    onEditItemType: (itemType: Enum<*>) -> Unit
) = with<LinearLayout> {
    orientation=LinearLayout.VERTICAL
    fullSize()
    padding= Bounds(bottom=dip(85), right=dip(23))
    gravity = Gravity.BOTTOM
    groupedItemTypeDescriptors.forEach { descriptor ->
        with<LinearLayout> {
            gravity = Gravity.RIGHT
            tag = descriptor.type.name
            onClick { onEditItemType(descriptor.type) }

            with<LinearLayout> {
                gravity = Gravity.CENTER_VERTICAL
                with<TextView> {
                    val drawable = getDrawable(R.drawable.bg_round_corners) as GradientDrawable
                    drawable.setColor(alterBrightness(descriptor.icon.bgColor, 0.6f))
                    gravity = Gravity.RIGHT
                    text = descriptor.title
                    background = drawable
                    setTextColor(alterBrightness(descriptor.icon.fgColor))
                }.layout(this)
            }.layout(this) {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                margin = Bounds(right = dip(10))
            }

            iconView(icon = descriptor.icon.box).layout(this) {
                width = dip(45)
                height = dip(45)
            }
        }.layout(this)
    }
}
