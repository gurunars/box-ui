package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.gurunars.databinding.*
import com.gurunars.databinding.android.*
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import org.jetbrains.anko.dip



/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param sortable If false move up and move down buttons are hidden.
 * @param groupedItemTypeDescriptors a collection of item type descriptors
 * @param listActionColors Color of the icons meant to manipulate the collection of items in the
 * contextual menu.
 * @param confirmationActionColors Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @param cancelActionColors Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @param openIconColors Plus icon color settings. The icon is shown when the menu is closed.
 * Clicking the icon opens the creation menu
 * @param isLeftHanded If true all action buttons are show on the left side of the screen. They
 * are shown on the right side of the screen otherwise.
 * @param items A collection of items shown and manipulated by the view.
 * @param isOpen A flag specifying if the menu is open or closed. Be it a creation or contextual
 * one.
 */
fun <ItemType : Item> Context.crudItemListView(
    groupedItemTypeDescriptors: BindableField<List<List<ItemTypeDescriptor<ItemType>>>>,
    items: BindableField<List<ItemType>>,
    emptyViewBinder: BindableField<EmptyViewBinder> = this::defaultBindEmpty.field,
    sortable: BindableField<Boolean> = true.field,
    listActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    confirmationActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    cancelActionColors: BindableField<IconColorBundle> = IconColorBundle().field,
    openIconColors: BindableField<IconColorBundle> = IconColorBundle().field,
    isOpen: BindableField<Boolean> = false.field,
    isLeftHanded: BindableField<Boolean> = false.field
): View = statefulComponent(R.id.crudItemListView, "CRUD ITEM LIST") {
    val selectedItems = BindableField<Set<ItemType>>(setOf())
    val itemInEdit: BindableField<ItemType?> = null.field
    val selectedView = ViewMode.CREATION.field

    val typeCache = groupedItemTypeDescriptors.branch {
        flatten().map {
            Pair(it.type, it)
        }.toMap()
    }

    val itemForm = FrameLayout(context).apply {
        fullSize()
        id = R.id.itemForm
    }

    fun onSave(item: ItemType) {
        items.set(processItemInEdit(item, items.get()))
        isOpen.set(false)
    }

    val stateMachine = StateMachine<ItemType>(
        selectedItems,
        isOpen,
        itemInEdit,
        selectedView,
        itemTypes = typeCache.branch { keys },
        loadItem = { typeCache.get()[it]!!.createNewItem() },
        bindForm = { itemForm(
            it,
            ::onSave,
            confirmationActionColors,
            typeCache.get()[it.type]!!
        ).set(R.id.formContent, itemForm) }
    )

    val contextualMenu = contextualMenu(
        sortable,
        listActionColors,
        isLeftHanded,
        items,
        selectedItems,
        { itemInEdit.set(it) }
    )

    val loading = FrameLayout(context).apply {
        ProgressBar(context).add(this) {
            layoutParams = FrameLayout.LayoutParams(dip(80), dip(80)).apply {
                gravity = Gravity.CENTER
            }
        }
    }

    val creationMenu = creationMenu(
        groupedItemTypeDescriptors,
        { itemInEdit.set(it) },
        isLeftHanded
    )

    val itemListView = selectableItemListView(
        items = items,
        selectedItems = selectedItems,
        itemViewBinders = groupedItemTypeDescriptors.branch {
            flatten().map { Pair(it.type,
                { item: BindableField<SelectableItem<ItemType>> -> it.bindRow(item) }
            ) }.toMap()
        },
        emptyViewBinder = emptyViewBinder
    )

    val creationCloseIcon = cancelActionColors.branch { icon(R.drawable.ic_menu_close) }
    val contextualCloseIcon = confirmationActionColors.branch { icon(R.drawable.ic_check) }

    val openIcon = openIconColors.branch { icon(R.drawable.ic_plus) }
    val closeIcon = creationCloseIcon.get().field

    val hasOverlay = true.field

    listOf(creationCloseIcon, contextualCloseIcon, selectedView).onChange {
        when (selectedView.get()) {
            ViewMode.CONTEXTUAL -> closeIcon.set(contextualCloseIcon.get())
            ViewMode.LOADING, ViewMode.CREATION, ViewMode.FORM -> closeIcon.set(creationCloseIcon.get())
            else -> { }
        }
    }

    selectedView.onChange {
        hasOverlay.set(it.hasOverlay)
    }

    val knobView = viewSelector(
        ViewMode.EMPTY to View(context),
        ViewMode.FORM to itemForm,
        ViewMode.LOADING to loading,
        ViewMode.CONTEXTUAL to contextualMenu,
        ViewMode.CREATION to creationMenu,
        selectedView = selectedView
    )

    retain(selectedView, itemInEdit, selectedItems, isOpen)

    groupedItemTypeDescriptors.onChange {
        stateMachine.clear()
        floatMenu(
            itemListView.field,
            knobView.field,
            isLeftHanded = isLeftHanded,
            closeIcon = closeIcon,
            openIcon = openIcon,
            hasOverlay = hasOverlay,
            isOpen = isOpen
        ).set(R.id.contentPane, this)
    }
}
