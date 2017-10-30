package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.sendTo
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemContainer
import org.jetbrains.anko.*

class CreationMenuComponent<ItemType : Item>(
    private val selectionManager: SelectableItemContainer<ItemType>
) : SelectableItemContainer<ItemType> by selectionManager {

    val closeIcon = BindableField(IconColorBundle())

    private val floatMenu = FloatMenu(selectionManager, menu()).apply {
        hasOverlay.set(true)
        this@CreationMenuComponent.closeIcon.sendTo(closeIcon, {
            IconView.Icon(
                bgColor = it.bgColor,
                fgColor = it.fgColor,
                icon = R.drawable.ic_check)
        })
    }

    val isOpen = floatMenu.isOpen
    val isLeftHanded = floatMenu.isLeftHanded
    val openIcon = floatMenu.openIcon

    override fun Context.render() = floatMenu.render(this)

    fun menu(): Component = component {
        verticalLayout {
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
    }

}
