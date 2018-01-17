package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import com.gurunars.android_utils.iconView
import com.gurunars.box.ui.add
import com.gurunars.box.ui.fullSize
import com.gurunars.box.box
import com.gurunars.box.ui.onClick
import com.gurunars.item_list.Item
import org.jetbrains.anko.*

@SuppressLint("RtlHardcoded")
internal fun <ItemType : Item> Context.creationMenu(
    groupedItemTypeDescriptors: List<ItemTypeDescriptor<ItemType>>,
    onEditItemType: (itemType: Enum<*>) -> Unit
) = verticalLayout {
    fullSize()
    bottomPadding = dip(85)
    gravity = Gravity.BOTTOM
    rightPadding = dip(23)
    groupedItemTypeDescriptors.forEach { descriptor ->
        linearLayout {
            gravity = Gravity.RIGHT

            linearLayout {
                gravity = Gravity.CENTER_VERTICAL
                textView {
                    val drawable = getDrawable(R.drawable.bg_round_corners) as GradientDrawable
                    drawable.setColor(descriptor.icon.bgColor)

                    gravity = Gravity.RIGHT

                    text=descriptor.type.name
                    background=drawable
                    textColor=descriptor.icon.fgColor
                }.lparams()
            }.lparams {
                height = matchParent
                rightMargin=dip(10)
            }

            iconView(icon = descriptor.icon.box).add(this) {
                tag = descriptor.type.name
                onClick {
                    onEditItemType(descriptor.type)
                }
            }.lparams {
                width = dip(45)
                height = dip(45)
            }
        }
    }
}
