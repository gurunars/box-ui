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
 * Widget to be used for manipulating a collection of items.
 */
class CrudItemList<ItemType : Item>  constructor(
        context: Context,
        itemViewBinderFetcher: (Int) -> SelectableItemViewBinder<ItemType>,
        itemEditListener: (item: ItemType) -> Unit,
        emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder
) : FrameLayout(context) {

    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    val actionIcon = bindableField(IconColorBundle())
    val contextualIcon = bindableField(IconColorBundle())
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
            this@CrudItemList.isLeftHanded.bind(isLeftHanded)

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
                        bgColor = contextualIcon.get().bgColor,
                        fgColor = contextualIcon.get().fgColor
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

    fun dismiss() {
        floatingMenu.isOpen.set(false)
        itemListView.selectedItems.set(hashSetOf())
    }

}
