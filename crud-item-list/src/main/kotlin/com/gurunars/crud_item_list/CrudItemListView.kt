package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.add
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.statefulComponent
import com.gurunars.databinding.field
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import org.jetbrains.anko.dip

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
fun <ItemType : Item> Context.crudItemListView(
    items: BindableField<List<ItemType>>,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: BindableField<EmptyViewBinder> = this::defaultBindEmpty.field,
    sortable: BindableField<Boolean> = true.field,
    listActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    confirmationActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    cancelActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    openIcon: BindableField<IconColorBundle> = IconColorBundle().field,
    isOpen: BindableField<Boolean> = false.field,
    isLeftHanded: BindableField<Boolean> = false.field
): View = statefulComponent(R.id.crudItemListView) {
    val typeCache = groupedItemTypeDescriptors
        .flatten()
        .map {
            Pair(it.type, it)
        }.toMap()

    val selectedItems = BindableField<Set<ItemType>>(setOf())
    val itemInEdit: BindableField<ItemType?> = null.field

    val itemForm: ItemForm<ItemType>

    fun getDescriptor(itemType: Enum<*>): ItemTypeDescriptor<ItemType> {
        val type =
            if (typeCache.containsKey(itemType))
                itemType
            else
                typeCache.keys.first()
        return typeCache[type]!!
    }

    val creationMenu = context.creationMenu(
        groupedItemTypeDescriptors,
        { itemInEdit.set(it) },
        isLeftHanded
    )

    val itemListView = selectableItemListView(
        items=items,
        selectedItems = selectedItems,
        itemViewBinders=typeCache.map {
            Pair(it.key,
                { item: BindableField<SelectableItem<ItemType>> -> it.value.bindRow(item) }
            )
        }.toMap().field,
        emptyViewBinder=emptyViewBinder
    ).apply {
        fullSize()
        id = R.id.rawItemList
    }

    val contextualMenu = context.contextualMenu(
        sortable,
        listActionColors,
        isLeftHanded,
        items,
        selectedItems,
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
                items.set(processItemInEdit(itemInEdit.get(), items.get()))
                isOpen.set(false)
            }
        },
        confirmationActionColors
    ).apply {
        id = R.id.itemForm
    }

    val loading = FrameLayout(context).apply {
        ProgressBar(context).add(this) {
            layoutParams = FrameLayout.LayoutParams(dip(80), dip(80)).apply {
                gravity = Gravity.CENTER
            }
        }
    }

    val knobView = FrameLayout(context)

    val floatingMenu = floatMenu(itemListView.field, knobView.field).setAsOne(this) {
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
                // TODO: Add some sort of progress bar to prevent some accidental UI actions
                knobView.selectedView.set(ViewMode.LOADING)
                asyncChain(
                    typeCache.values.first()::createNewItem,
                    { itemInEdit.set(it) }
                )
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
    }

}

