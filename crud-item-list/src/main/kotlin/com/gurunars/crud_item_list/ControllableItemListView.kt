package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bindableField
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.SelectableItemListView
import com.gurunars.shortcuts.fullSize

internal class ControllableItemListView<ItemType : Item>  constructor(
    context: Context,
    emptyViewBinder: EmptyViewBinder = ::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    onEdit: (item: ItemType) -> Unit
) : FrameLayout(context) {

    val actionIcon = bindableField(IconColorBundle())
    val contextualCloseIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())
    val isSortable = bindableField(true)

    val isLeftHanded = bindableField(false)
    val items: BindableField<List<ItemType>>
    val isOpen: BindableField<Boolean>

    private val creationMenu: View
    private val contextualMenu: View
    private val floatingMenu: FloatMenu
    private val itemListView: SelectableItemListView<ItemType>

    private val typeCache = mutableMapOf<Enum<*>, ItemTypeDescriptor<ItemType>>()

    private fun itemViewBinder(
        context: Context,
        itemType: Enum<*>,
        field: BindableField<SelectableItem<ItemType>>
    ) = typeCache[itemType]?.rowBinder?.invoke(context, itemType, field) ?:
        throw RuntimeException("Unknown type ${itemType}")

    init {

        groupedItemTypeDescriptors.forEach {
            it.forEach {
                typeCache.put(it.type, it)
            }
        }

        creationMenu = creationMenu(
            context,
            groupedItemTypeDescriptors,
            onEdit,
            isLeftHanded
        )

        itemListView = SelectableItemListView(
            context,
            this::itemViewBinder,
            emptyViewBinder
        ).apply {
            fullSize()
            id = R.id.rawItemList
        }

        items = itemListView.items

        contextualMenu = contextualMenu(context,
            actionIcon,
            isLeftHanded,
            isSortable,
            itemListView.items,
            itemListView.selectedItems,
            onEdit
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        floatingMenu = floatMenu {
            fullSize()
            id = R.id.floatingMenu
            contentView.set(itemListView)
            this@ControllableItemListView.isLeftHanded.bind(isLeftHanded)

            isOpen.onChange {
                if (!it) itemListView.selectedItems.set(hashSetOf())
            }

            itemListView.selectedItems.onChange {
                isOpen.set(it.isNotEmpty())
            }

            listOf(isOpen, itemListView.selectedItems).onChange {
                if (!isOpen.get()) { return@onChange }
                if (itemListView.selectedItems.get().isEmpty()) {
                    hasOverlay.set(true)
                    menuView.set(creationMenu)
                } else  {
                    hasOverlay.set(false)
                    menuView.set(contextualMenu)
                }
            }

            this@ControllableItemListView.openIcon.onChange {
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

        isOpen = floatingMenu.isOpen

    }

}
