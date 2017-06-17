package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.bindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemList
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items.
 */
class CrudItemList<ItemType : Item>  constructor(context: Context) : FrameLayout(context) {

    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    val actionIcon = bindableField(IconColorBundle())
    val contextualIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())
    val isLeftHanded = bindableField(false)
    val items = bindableField(listOf<ItemType>())
    val creationMenu = bindableField(View(context))
    val isSortable = bindableField(true)
    val itemEditListener = bindableField<(item: ItemType) -> Unit>({})

    private val contextualMenu: ContextualMenu<ItemType>
    private val floatingMenu: FloatMenu
    private val itemList: SelectableItemList<ItemType>

    init {

        floatingMenu = FloatMenu(context).apply {
            fullSize()
            id = R.id.floatingMenu
        }
        itemList = SelectableItemList<ItemType>(context).apply {
            fullSize()
            id = R.id.rawItemList
        }

        contextualMenu = ContextualMenu<ItemType>(context,
            isLeftHanded, isSortable,  items, itemList.selectedItems,
            itemEditListener
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        floatingMenu.contentView.set(itemList)
        floatingMenu.menuView.set(contextualMenu)

        addView(floatingMenu)

        floatingMenu.isOpen.onChange {
            if (it) {

            } else {
            }
        }

        reload()
    }

    private fun reload() {
        if (itemList.selectedItems.get().isEmpty()) {
            setUpCreationMenu()
        } else {
            setUpContextualMenu()
        }
    }

    private fun setUpContextualMenu() {
        floatingMenu.menuView.set(contextualMenu)
        floatingMenu.closeIcon.set(IconView.Icon(
            icon = R.drawable.ic_check,
            bgColor = contextualIcon.get().bgColor,
            fgColor = contextualIcon.get().fgColor
        ))
        floatingMenu.hasOverlay.set(false)
    }

    private fun setUpCreationMenu() {
        floatingMenu.menuView.set(creationMenu.get())
        floatingMenu.closeIcon.set(IconView.Icon(
            icon = R.drawable.ic_menu_close,
            bgColor = createCloseIcon.get().bgColor,
            fgColor = createCloseIcon.get().fgColor
        ))
        floatingMenu.hasOverlay.set(true)
    }

}
