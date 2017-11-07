package com.gurunars.crud_item_list

import android.content.Context
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.set
import com.gurunars.databinding.android.statefulComponent
import com.gurunars.databinding.android.viewSelector
import com.gurunars.databinding.branch
import com.gurunars.databinding.field
import com.gurunars.databinding.onChange
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.progressBar


/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param groupedItemTypeDescriptors a collection of item type descriptors
 * @param confirmationActionColors Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @param cancelActionColors Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @param openIconColors Plus icon color settings. The icon is shown when the menu is closed.
 * Clicking the icon opens the creation menu
 * @param items A collection of items shown and manipulated by the view.
 * @param isOpen A flag specifying if the menu is open or closed. Be it a creation or contextual
 * one.
 * @param renderContextualMenu A function that returns a contextual menu to manipulate a set of
 * selected items.
 */
fun <ItemType : Item> Context.crudItemListView(
    renderContextualMenu: (
        items: BindableField<List<ItemType>>,
        selectedItems: BindableField<Set<ItemType>>
    ) -> View,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    confirmationActionColors: IconColorBundle = IconColorBundle(),
    cancelActionColors: IconColorBundle = IconColorBundle(),
    openIconColors: IconColorBundle = IconColorBundle(),
    items: BindableField<List<ItemType>>,
    isOpen: BindableField<Boolean> = false.field
): View = statefulComponent(R.id.crudItemListView, "CRUD ITEM LIST") {
    val selectedItems = BindableField<Set<ItemType>>(setOf())
    val itemInEdit: BindableField<ItemType?> = null.field
    val selectedView = ViewMode.CREATION.field
    val creationCloseIcon = cancelActionColors.icon(R.drawable.ic_menu_close)
    val contextualCloseIcon = confirmationActionColors.icon(R.drawable.ic_check)
    val openIcon = openIconColors.icon(R.drawable.ic_plus)
    val closeIcon = creationCloseIcon.field
    val hasOverlay = true.field

    val typeCache = groupedItemTypeDescriptors.
        flatten().map {
            Pair(it.type, it)
        }.toMap()

    val itemForm = context.frameLayout {
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
        itemTypes = typeCache,
        loadItem = { typeCache[it]!!.createNewItem() },
        bindForm = {
            itemForm(
                it,
                ::onSave,
                confirmationActionColors,
                typeCache[it.type]!!
            ).set(R.id.formContent, itemForm)
        }
    )

    val contextualMenu = renderContextualMenu(
        items,
        selectedItems
    )

    val loading = context.frameLayout {
        progressBar().lparams {
            width = dip(80)
            height = dip(80)
            gravity = Gravity.CENTER
        }
    }

    val creationMenu = creationMenu(
        groupedItemTypeDescriptors,
        { itemInEdit.set(it) }
    )

    val itemListView = selectableItemListView(
        items = items,
        selectedItems = selectedItems,
        itemViewBinders = groupedItemTypeDescriptors.
            flatten().map {
                Pair(it.type,
                    { item: BindableField<SelectableItem<ItemType>> -> it.bindRow(item).apply {
                        val doubleTapDetector = GestureDetector(
                            this@crudItemListView,
                            object: GestureDetector.SimpleOnGestureListener() {
                                override fun onDoubleTap(e: MotionEvent?): Boolean {
                                    itemInEdit.set(item.get().item)
                                    return true
                                }
                            }
                        )
                        setOnTouchListener( { v, event -> doubleTapDetector.onTouchEvent(event) })
                    } }
                )
            }.toMap(),
        emptyViewBinder = emptyViewBinder
    )

    listOf(creationCloseIcon, contextualCloseIcon, selectedView).onChange {
        when (selectedView.get()) {
            ViewMode.CONTEXTUAL
                -> closeIcon.set(contextualCloseIcon.get())
            ViewMode.LOADING, ViewMode.CREATION, ViewMode.FORM
                -> closeIcon.set(creationCloseIcon.get())
            else
                -> { }
        }
    }

    selectedView.onChange {
        if (it.overlay != Overlay.SAME) {
            hasOverlay.set(it.overlay == Overlay.YES)
        }
    }

    val knobView = viewSelector(
        ViewMode.EMPTY to null,
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
            isOpen = isOpen
        ).set(R.id.contentPane, this)
    }
}
