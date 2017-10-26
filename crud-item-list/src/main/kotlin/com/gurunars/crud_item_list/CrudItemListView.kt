package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.StatefulComponent
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.*
import com.gurunars.knob_view.KnobView
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setAsOne

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param sortable If false move up and move down buttons are hidden.
 * @param groupedItemTypeDescriptors a collection of item type descriptors
 *
 * @property listActionColors Color of the icons meant to manipulate the collection of items in the
 * contextual menu.
 * @property confirmationActionColors Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @property cancelActionColors Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @property openIcon Plus icon color settings.The icon is shown when the menu is closed. Clicking
 * the icon opens the creation menu
 * @property isLeftHanded If true all action buttons are show on the left side of the screen. They
 * are shown on the right side of the screen otherwise.
 * @property items A collection of items shown and manipulated by the view.
 * @property isOpen A flag specifying if the menu is open or closed. Be it a creation or contextual
 * one.
 */
@SuppressLint("ViewConstructor")
class CrudItemListView<ItemType : Item> constructor(
    context: Context,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: EmptyViewBinder = context::defaultBindEmpty,
    sortable: Boolean = true
) : StatefulComponent(context) {
    private val typeCache = groupedItemTypeDescriptors
        .flatten()
        .map {
            Pair(it.type, it)
        }.toMap()

    val listActionColors = BindableField(IconColorBundle())
    val confirmationActionColors = BindableField(IconColorBundle())
    val cancelActionColors = BindableField(IconColorBundle())
    val openIcon = BindableField(IconColorBundle())
    val isLeftHanded = BindableField(false)

    val items: BindableField<List<ItemType>>
    val isOpen: BindableField<Boolean>
    private val itemInEdit = BindableField<ItemType?>(null)

    private val creationMenu: View
    private val contextualMenu: View
    private val itemForm: ItemForm<ItemType>

    private val floatingMenu: FloatMenu
    private val itemListView: SelectableItemListView<ItemType>
    private val knobView: KnobView

    private enum class ViewMode {
        CONTEXTUAL, CREATION, FORM
    }

    private fun getDescriptor(itemType: Enum<*>): ItemTypeDescriptor<ItemType> {
        val type =
            if (typeCache.containsKey(itemType))
                itemType
            else
                typeCache.keys.first()
        return typeCache[type]!!
    }

    init {
        creationMenu = context.creationMenu(
            groupedItemTypeDescriptors,
            { itemInEdit.set(it) },
            isLeftHanded
        )

        itemListView = SelectableItemListView(
            context,
            typeCache.map { Pair(it.key,
                { item: BindableField<SelectableItem<ItemType>> -> it.value.bindRow(item) }
            ) }.toMap(),
            emptyViewBinder
        ).apply {
            fullSize()
            id = R.id.rawItemList
        }

        contextualMenu = context.contextualMenu(
            sortable,
            listActionColors,
            isLeftHanded,
            itemListView.items,
            itemListView.selectedItems,
            { itemInEdit.set(it) }
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        itemForm = ItemForm(
            context,
            itemInEdit,
            {
                run {
                    val item = itemInEdit.get()
                    isOpen.set(false)
                    if (item == null) {
                        return@run
                    }
                    val itemsOld = items.get()
                    val foundIndex = items.get().indexOfFirst { it.id == item.id }
                    if (foundIndex == -1) {
                        items.set(itemsOld + item)
                    } else {
                        items.set(itemsOld.toMutableList().apply {
                            set(foundIndex, item)
                        })
                    }
                }
            },
            confirmationActionColors
        ).apply {
            id = R.id.itemForm
        }

        knobView = KnobView(context, mapOf(
            ViewMode.CONTEXTUAL to contextualMenu,
            ViewMode.CREATION to creationMenu,
            ViewMode.FORM to itemForm
        )).apply {
            fullSize()
            id = R.id.knobMenu
        }

        floatingMenu = FloatMenu(context, itemListView, knobView).setAsOne(this) {
            id = R.id.floatingMenu
            this@CrudItemListView.isLeftHanded.bind(isLeftHanded)

            fun confCrossIcon() {
                closeIcon.set(IconView.Icon(
                    icon = R.drawable.ic_menu_close,
                    bgColor = cancelActionColors.get().bgColor,
                    fgColor = cancelActionColors.get().fgColor
                ))
            }

            fun confCheckIcon() {
                closeIcon.set(IconView.Icon(
                    icon = R.drawable.ic_check,
                    bgColor = confirmationActionColors.get().bgColor,
                    fgColor = confirmationActionColors.get().fgColor
                ))
            }

            itemListView.selectedItems.onChange {
                isOpen.set(!it.isEmpty())
            }

            listOf(isOpen, itemListView.selectedItems, itemInEdit).onChange {
                if (!isOpen.get()) {
                    itemInEdit.set(null)
                    itemListView.selectedItems.set(hashSetOf())
                    return@onChange
                }
                val item = itemInEdit.get()
                if (item != null) {
                    if (knobView.selectedView.get() != ViewMode.FORM) {
                        hasOverlay.set(true)
                        itemForm.bind(item, getDescriptor(item.type))
                        knobView.selectedView.set(ViewMode.FORM)
                    }
                } else if (itemListView.selectedItems.get().isNotEmpty()) {
                    hasOverlay.set(false)
                    knobView.selectedView.set(ViewMode.CONTEXTUAL)
                } else {
                    hasOverlay.set(true)
                    knobView.selectedView.set(ViewMode.CREATION)
                }
            }

            knobView.selectedView.onChange {
                if (typeCache.size == 1 && it == ViewMode.CREATION) {
                    itemInEdit.set(typeCache.values.first().createNewItem())
                }
            }

            this@CrudItemListView.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor,
                    icon = R.drawable.ic_plus
                ))
            }

            listOf(confirmationActionColors, cancelActionColors, knobView.selectedView).onChange {
                when (knobView.selectedView.get()) {
                    ViewMode.FORM -> confCrossIcon()
                    ViewMode.CREATION -> confCrossIcon()
                    else -> confCheckIcon()
                }
            }

            retain(itemInEdit, itemListView.selectedItems, isOpen)
            retain(itemListView.selectedItems, isOpen, itemInEdit)
        }

        isOpen = floatingMenu.isOpen
        items = itemListView.items

    }

}
