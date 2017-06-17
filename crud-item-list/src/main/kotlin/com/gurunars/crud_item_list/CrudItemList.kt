package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.IconView.Icon
import com.gurunars.databinding.bindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
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

        itemList = SelectableItemList<ItemType>(context).apply {
            fullSize()
            id = R.id.rawItemList
        }

        contextualMenu = ContextualMenu<ItemType>(context,
                isLeftHanded,
                isSortable,
                items,
                itemList.selectedItems,
                itemEditListener
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        floatingMenu = floatMenu {
            fullSize()
            id = R.id.floatingMenu
            contentView.set(itemList)

            fun configureCloseIcon() {
                if (itemList.selectedItems.get().isEmpty()) {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_menu_close,
                        bgColor = createCloseIcon.get().bgColor,
                        fgColor = createCloseIcon.get().fgColor
                    ))
                } else {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_check,
                        bgColor = contextualIcon.get().bgColor,
                        fgColor = contextualIcon.get().fgColor
                    ))
                }
            }

            fun configureMenuView() {
                if (itemList.selectedItems.get().isEmpty()) {
                    menuView.set(creationMenu.get())
                } else {
                    menuView.set(contextualMenu)
                }
            }

            isOpen.onChange {
                hasOverlay.set(itemList.selectedItems.get().isEmpty())
                configureMenuView()
                configureCloseIcon()
            }

            creationMenu.onChange {
                configureMenuView()
            }

            this@CrudItemList.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor
                ))
            }

            contextualIcon.onChange { configureCloseIcon() }
            createCloseIcon.onChange { configureCloseIcon() }
        }

    }

}
