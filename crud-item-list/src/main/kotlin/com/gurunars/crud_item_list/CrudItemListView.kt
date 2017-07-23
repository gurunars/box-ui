package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.bindableField
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param itemViewBinder a function binding an observable to the actual view
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 *
 * @property actionIcon Color of the icons meant to manipulate the collection of items in the
 * contextual menu.
 * @property contextualCloseIcon Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @property createCloseIcon Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @property openIcon Plus icon color settings.The icon is shown when the menu is closed. Clicking
 * the icon opens the creation menu
 * @property isLeftHanded If true all action buttons are show on the left side of the screen. They
 * are shown on the right side of the screen otherwise.
 * @property creationMenu A set of controls used to create items of various types.
 * @property isSortable If false move up and move down buttons are hidden.
 * @property items A collection of items shown and manipulated by the view.
 */
class CrudItemListView<ItemType : Item>  constructor(
    context: Context,
    itemViewBinder: SelectableItemViewBinder<ItemType>,
    itemEditListener: (item: ItemType) -> Unit,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    /**
     * Icon color settings
     *
     * @property bgColor background color
     * @property fgColor foreground color
     */
    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    val actionIcon = bindableField(IconColorBundle())
    val contextualCloseIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())
    val isLeftHanded = bindableField(false)
    val creationMenu = bindableField(View(context))
    val isSortable = bindableField(true)
    val items: BindableField<List<ItemType>>

    private val contextualMenu: ContextualMenu<ItemType>
    private val floatingMenu: FloatMenu
    private val itemListView: SelectableItemListView<ItemType>

    init {

        itemListView = SelectableItemListView(context, itemViewBinder, emptyViewBinder).apply {
            fullSize()
            id = R.id.rawItemList
        }

        items = itemListView.items

        contextualMenu = ContextualMenu(context,
            actionIcon,
            isLeftHanded,
            isSortable,
            itemListView.items,
            itemListView.selectedItems,
            itemEditListener
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        floatingMenu = floatMenu {
            fullSize()
            id = R.id.floatingMenu
            contentView.set(itemListView)
            this@CrudItemListView.isLeftHanded.bind(isLeftHanded)

            isOpen.onChange {
                if (!it) itemListView.selectedItems.set(hashSetOf())
            }

            itemListView.selectedItems.onChange {
                isOpen.set(it.isNotEmpty())
            }

            listOf(creationMenu, isOpen).onChange {
                if (itemListView.selectedItems.get().isEmpty()) {
                    hasOverlay.set(true)
                    menuView.set(creationMenu.get())
                } else {
                    hasOverlay.set(false)
                    menuView.set(contextualMenu)
                }
            }

            this@CrudItemListView.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor
                ))
            }

            listOf(contextualCloseIcon, createCloseIcon, itemListView.selectedItems).onChange {
                if (itemListView.selectedItems.get().isEmpty()) {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_menu_close,
                        bgColor = createCloseIcon.get().bgColor,
                        fgColor = createCloseIcon.get().fgColor
                    ))
                } else {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_check,
                        bgColor = contextualCloseIcon.get().bgColor,
                        fgColor = contextualCloseIcon.get().fgColor
                    ))
                }
            }

        }

    }

    /**
     * Close the menu be it a contextual or a creation one.
     */
    fun dismiss() {
        floatingMenu.isOpen.set(false)
    }

}
