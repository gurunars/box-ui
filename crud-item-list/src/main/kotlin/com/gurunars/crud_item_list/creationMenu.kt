package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import com.gurunars.android_utils.iconView
import com.gurunars.box.ui.add
import com.gurunars.box.ui.fullSize
import com.gurunars.box.box
import com.gurunars.item_list.Item
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.verticalLayout

internal fun <ItemType : Item> Context.creationMenu(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    onEditItemType: (itemType: Enum<*>) -> Unit
) = verticalLayout {
    fullSize()
    bottomPadding = dip(85)
    gravity = Gravity.BOTTOM
    rightPadding = dip(23)
    groupedItemTypeDescriptors.forEach { group ->
        linearLayout {
            gravity = Gravity.END
            group.forEach { action ->
                iconView(icon = action.icon.box).add(this) {
                    tag = action.type.name
                    setOnClickListener {
                        onEditItemType(action.type)
                    }
                }.lparams {
                    width = dip(45)
                    height = dip(45)
                }
            }
        }
    }
}
