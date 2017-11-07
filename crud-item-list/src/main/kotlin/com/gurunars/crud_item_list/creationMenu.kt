package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import com.gurunars.android_utils.iconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.field
import com.gurunars.item_list.Item
import org.jetbrains.anko.*

internal fun <ItemType : Item> Context.creationMenu(
    groupedItemTypeDescriptors: BindableField<List<List<ItemTypeDescriptor<ItemType>>>>,
    onEdit: (item: ItemType) -> Unit
) = verticalLayout {
    fullSize()
    bottomPadding = dip(85)
    gravity = Gravity.BOTTOM
    rightPadding = dip(23)
    groupedItemTypeDescriptors.onChange {
        removeAllViews()
        it.forEach { group ->
            linearLayout {
                gravity = Gravity.END
                group.forEach { action ->
                    iconView(icon = action.icon.field).add(this) {
                        tag = action.type.name
                        setOnClickListener {
                            // TODO: Add some sort of progress bar to prevent some accidental UI actions
                            asyncChain(
                                action::createNewItem,
                                onEdit
                            )
                        }
                    }.lparams {
                        width = dip(45)
                        height = dip(45)
                    }
                }
            }
        }
    }
}
