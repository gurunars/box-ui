package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.StatefulComponent
import com.gurunars.databinding.android.bindableField
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import com.gurunars.knob_view.KnobView
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param isSortable If false move up and move down buttons are hidden.
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
class CrudItemListView<ItemType : Item> constructor(
    context: Context,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    sortable: Boolean = true,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>
) : StatefulComponent(context) {

    private val typeCache = with(groupedItemTypeDescriptors) {
        mutableMapOf<Enum<*>, ItemTypeDescriptor<ItemType>>().apply {
            this@with.forEach {
                it.forEach {
                    this.put(it.type, it)
                }
            }
        }
    }

    val listActionColors = bindableField(IconColorBundle())
    val confirmationActionColors = bindableField(IconColorBundle())
    val cancelActionColors = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())

    val isLeftHanded = bindableField(false)
    val items: BindableField<List<ItemType>>

    val isOpen = bindableField(false)
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

    private fun itemViewBinder(
        context: Context,
        itemType: Enum<*>,
        field: BindableField<SelectableItem<ItemType>>
    ): View {
        val binder = typeCache[itemType] ?:
            throw RuntimeException("Unknown type ${itemType}")

        val wrapper = coloredRowSelectionDecorator(
            { type, fld: BindableField<ItemType> -> binder.rowBinder(context, fld) },
            binder.rowSelectionColor,
            binder.rowRegularColor)

        return context.wrapper(itemType, field)
    }

    init {
        retain(itemInEdit)

        creationMenu = context.creationMenu(
            groupedItemTypeDescriptors,
            { itemInEdit.set(it) },
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
            {
                item ->
                typeCache[item.type]?.canSave?.invoke(item) ?: false
            },
            confirmationActionColors
        )

        knobView = KnobView(context, mapOf(
            ViewMode.CONTEXTUAL to contextualMenu,
            ViewMode.CREATION to creationMenu,
            ViewMode.FORM to itemForm
        )).apply {
            fullSize()
            id = R.id.knobMenu
        }

        floatingMenu = floatMenu {
            // itemListView, knobView
            contentView.set(itemListView)
            menuView.set(knobView)
            fullSize()
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

            isOpen.onChange({
                if (it) {
                    if (!it) {
                        knobView.selectedView.set(ViewMode.CREATION)
                    }
                }
            }) {
                if (!it) {
                    itemInEdit.set(null)
                    itemListView.selectedItems.set(hashSetOf())
                }
            }

            listOf(isOpen, itemListView.selectedItems, itemInEdit).onChange {
                if (!isOpen.get()) {
                    return@onChange
                }
                val item = itemInEdit.get()
                if (item != null) {
                    if (knobView.selectedView.get() != ViewMode.FORM) {
                        hasOverlay.set(true)
                        itemForm.bind(item, typeCache[item.type]!!.formBinder)
                        knobView.selectedView.set(ViewMode.FORM)
                    }
                } else if (itemListView.selectedItems.get().isNotEmpty()) {
                    hasOverlay.set(false)
                    knobView.selectedView.set(ViewMode.CONTEXTUAL)
                } else if (typeCache.size == 1) {
                    itemInEdit.set(typeCache.values.first().newItemCreator())
                } else {
                    hasOverlay.set(true)
                    knobView.selectedView.set(ViewMode.CREATION)
                }
            }

            this@CrudItemListView.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor
                ))
            }

            listOf(confirmationActionColors, cancelActionColors, knobView.selectedView).onChange {
                when (knobView.selectedView.get()) {
                    ViewMode.FORM -> confCrossIcon()
                    ViewMode.CREATION -> confCrossIcon()
                    else -> confCheckIcon()
                }
            }

        }

        isOpen.bind(floatingMenu.isOpen)
        items = itemListView.items

    }

}
