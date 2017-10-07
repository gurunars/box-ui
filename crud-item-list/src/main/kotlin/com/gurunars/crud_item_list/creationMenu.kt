package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.add
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*


internal fun <ItemType : Item> Context.creationMenu(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    onEdit: (item: ItemType) -> Unit,
    isLeftHanded: BindableField<Boolean>
) = verticalLayout {
    fullSize()
    bottomPadding = dip(85)
    gravity = Gravity.BOTTOM
    isLeftHanded.onChange {
        leftPadding = dip(0)
        rightPadding = dip(0)
        if (it) {
            leftPadding = dip(23)
        } else {
            rightPadding = dip(23)
        }
    }
    groupedItemTypeDescriptors.forEach { group ->
        linearLayout {
            isLeftHanded.onChange {
                @SuppressLint("RtlHardcoded")
                gravity = (if (it) Gravity.LEFT else Gravity.RIGHT)
            }
            group.forEach { action ->
                IconView(context).add(this) {
                    tag = action.type.name
                    icon.set(action.icon)
                    setOnClickListener {
                        onEdit(action.createNewItem())
                    }
                }.lparams {
                    width = dip(45)
                    height = dip(45)
                }
            }
        }
    }
}
