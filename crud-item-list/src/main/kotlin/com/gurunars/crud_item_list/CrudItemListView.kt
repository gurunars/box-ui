package com.gurunars.crud_item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
import com.gurunars.knob_view.KnobView
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
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
 * @property isSortable If false move up and move down buttons are hidden.
 * @property items A collection of items shown and manipulated by the view.
 * @property isOpen A flag specifying if the menu is open or closed. Be it a creation or contextual
 * one.
 */
class CrudItemListView<ItemType : Item>  constructor(
    context: Context,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>
) : FrameLayout(context) {

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
    val isSortable = bindableField(true)

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
    ) = typeCache[itemType]?.rowBinder?.invoke(context, itemType, field) ?:
        throw RuntimeException("Unknown type ${itemType}")

    init {

        creationMenu = creationMenu(
            context,
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

        contextualMenu = contextualMenu(context,
            listActionColors,
            isLeftHanded,
            isSortable,
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
                item -> typeCache[item.type]?.canSave?.invoke(item) ?: false
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

        floatingMenu = floatMenu(itemListView, knobView) {
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

            fun confCloseIcon() =
                if (
                    itemListView.selectedItems.get().isEmpty() ||
                    itemInEdit.get() != null
                ) {
                    confCrossIcon()
                } else {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_check,
                        bgColor = confirmationActionColors.get().bgColor,
                        fgColor = confirmationActionColors.get().fgColor
                    ))
                }

            itemListView.selectedItems.onChange {
                isOpen.set(it.isNotEmpty())
            }

            isOpen.onChange {
                if (it) {
                    confCloseIcon()
                } else {
                    itemInEdit.set(null)
                    itemListView.selectedItems.set(hashSetOf())
                }
            }

            itemInEdit.onChange {
                if (it != null) {
                    itemForm.bind(
                        it,
                        typeCache[it.type]!!.formBinder
                    )
                    confCloseIcon()
                }
            }

            listOf(isOpen, itemListView.selectedItems, itemInEdit).onChange {
                if (!isOpen.get()) return@onChange
                if (itemInEdit.get() != null) {
                    hasOverlay.set(true)
                    knobView.selectedView.set(ViewMode.FORM)
                } else if (itemListView.selectedItems.get().isNotEmpty()) {
                    hasOverlay.set(false)
                    knobView.selectedView.set(ViewMode.CONTEXTUAL)
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

            listOf(confirmationActionColors, cancelActionColors).onChange {
                confCloseIcon()
            }

        }

        isOpen.bind(floatingMenu.isOpen)
        items = itemListView.items

    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putSerializable("itemInEdit", itemInEdit?.get())
    }

    /**
     * @suppress
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
            itemInEdit.set(getSerializable("itemInEdit") as ItemType?)
        }
    }

}
