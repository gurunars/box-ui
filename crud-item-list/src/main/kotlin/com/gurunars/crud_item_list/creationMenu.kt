package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.add
import com.gurunars.databinding.sendTo
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.MenuComponent
import com.gurunars.item_list.ItemContainer
import com.gurunars.item_list.SelectableItemContainer
import org.jetbrains.anko.*

class CreationMenuComponent<ItemType: Item, T>(
    private val selectionManager: SelectableItemContainer<ItemType>
): SelectableItemContainer<ItemType> by selectionManager {

    val closeIcon = BindableField(IconColorBundle())

    private val floatMenu = FloatMenu(selectionManager, menu()).apply {
        hasOverlay.set(true)
        this@CreationMenuComponent.closeIcon.sendTo(closeIcon, { IconView.Icon(
            bgColor = it.bgColor,
            fgColor = it.fgColor,
            icon = R.drawable.ic_check)
        })
    }

    override val isOpen: BindableField<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val openIcon: BindableField<IconView.Icon>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun Context.render() = floatMenu.render(this)

    fun menu(): Component = component {

    }

}


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
