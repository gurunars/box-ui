package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import com.gurunars.android_utils.iconView
import com.gurunars.box.box
import com.gurunars.box.ui.*
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
                    drawable.setColor(alterBrightness(descriptor.icon.bgColor, 0.6f))

                    gravity = Gravity.RIGHT

                    text = descriptor.title
                    background = drawable
                    textColor = alterBrightness(descriptor.icon.fgColor)
                }.lparams()
            }.lparams {
                height = matchParent
                rightMargin = dip(10)
            }

            iconView(icon = descriptor.icon.box).layout(this) {
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
