package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.bindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @see ItemListView
 */
class CrudItemListView<ItemType : Item>  constructor(
    context: Context,
    itemViewBinderFetcher: SelectableItemViewBinder<ItemType>,
    itemEditListener: (item: ItemType) -> Unit,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    /**
     * A combination of icon foreground and background color.
     */
    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    /**
     * Color of the icons meant to manipulate the collection of items in the contextual menu.
     */
    val actionIcon = bindableField(IconColorBundle())
    /**
     * Check mark icon color settings. The icon is shown when contextual menu is opened. Clicking
     * the icon closes contextual menu.
     */
    val contextualCloseIcon = bindableField(IconColorBundle())
    /**
     * Cross icon color settings. The icon is shown when creation menu is opened. Clicking the icon
     * closes the menu.
     */
    val createCloseIcon = bindableField(IconColorBundle())
    /**
     * Plus icon color settings. The icon is shown when the menu is closed. Clicking the icon opens
     * the creation menu.
     */
    val openIcon = bindableField(IconColorBundle())
    /**
     * If true all action buttons are show on the left side of the screen. They are shown on the
     * right side of the screen otherwise.
     */
    val isLeftHanded = bindableField(false)
    /**
     * A set of controls used to create items of various types.
     */
    val creationMenu = bindableField(View(context))
    /**
     * If false move up and move down buttons are hidden.
     */
    val isSortable = bindableField(true)
    /**
     * A collection of items shown and manipulated by the view.
     */
    val items: BindableField<List<ItemType>>

    private val contextualMenu: ContextualMenu<ItemType>
    private val floatingMenu: FloatMenu
    private val itemListView: SelectableItemListView<ItemType>

    init {

        itemListView = SelectableItemListView(context, itemViewBinderFetcher, emptyViewBinder).apply {
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

            fun configureCloseIcon() {
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

            fun configureMenuView() {
                if (itemListView.selectedItems.get().isEmpty()) {
                    menuView.set(creationMenu.get())
                } else {
                    menuView.set(contextualMenu)
                }
            }

            isOpen.onChange {
                if (it) {
                    hasOverlay.set(itemListView.selectedItems.get().isEmpty())
                    configureMenuView()
                    configureCloseIcon()
                } else {
                    itemListView.selectedItems.set(hashSetOf())
                }
            }

            itemListView.selectedItems.onChange { isOpen.set(it.isNotEmpty()) }

            creationMenu.onChange {
                configureMenuView()
            }

            this@CrudItemListView.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor
                ))
            }

            contextualCloseIcon.onChange { configureCloseIcon() }
            createCloseIcon.onChange { configureCloseIcon() }
        }

    }

    fun dismiss() {
        floatingMenu.isOpen.set(false)
        itemListView.selectedItems.set(hashSetOf())
    }

}
