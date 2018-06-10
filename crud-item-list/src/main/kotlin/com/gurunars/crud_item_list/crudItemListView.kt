package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import com.gurunars.box.*
import com.gurunars.box.ui.decorators.statefulLayer
import com.gurunars.item_list.*
import com.gurunars.floatmenu.R as floatR

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param itemTypeDescriptors a collection of item type descriptors
 * @param confirmationActionColors Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @param cancelActionColors Cross icon color settings. The icon is shown when creation menu is
 * opened. Clicking the icon closes the menu.
 * @param openIconColors Plus icon color settings. The icon is shown when the menu is closed.
 * Clicking the icon opens the creation menu
 * @param items A collection of items shown and manipulated by the view.
 * @param itemInEdit item currently being edited. If not null - the form is opened. The form is
 *                   closed otherwise
 * @param addToTail if false adds new items to the head of the list instead of the tail
 */
fun <ItemType : Item> Context.crudItemListView(
    clipboardSerializer: ClipboardSerializer<ItemType>? = null,
    sortable: Boolean = true,
    addToTail: Boolean = true,
    actionIconColors: IconColorBundle = IconColorBundle(),
    confirmationActionColors: IconColorBundle = IconColorBundle(),
    cancelActionColors: IconColorBundle = IconColorBundle(),
    openIconColors: IconColorBundle = IconColorBundle(),
    // MORE
    items: IBox<List<ItemType>>,
    selectedItems: IBox<Set<ItemType>>,
    explicitContextual: IBox<Boolean>
): View = statefulLayer(R.id.crudItemListView) {
    val isOpen = false.box
    isOpen.onChange {
        if (!it) {
            selectedItems.set(setOf())
            explicitContextual.set(false)
        }
    }

    val contextualMenu = contextualMenu(
        sortable,
        actionIconColors,
        items,
        selectedItems,
        clipboardSerializer
    )
/*
    val creationMenu = creationMenu(itemTypeDescriptors, { stateMachine.loadType(it) })
    itemInEdit.onChange { stateMachine.loadItem(it.toNullable()) }
    stateMachine.state.onChange { itemInEdit.set(it.itemInEdit.toOptional()) }

    val itemListView = itemListView(
        items = items,
        itemViewBinders = itemTypeDescriptors.map { Pair(it.type, it::bindRow) }.toMap()
            .withSelection(
                selectedItems = stateMachine.selectedItems,
                explicitSelectionMode = stateMachine.state.oneWayBranch { explicitContextual }
            ),
        emptyViewBinder = emptyViewBinder
    )

    val contentArea = object : ContentPane {
        override fun Context.render() = itemListView
        override val icon = openIconColors.icon(R.drawable.ic_plus)
    }

    class MenuArea(private val viewMode: ViewMode) : MenuPane {
        override fun Context.render() =
            when (viewMode) {
                ViewMode.CONTEXTUAL -> contextualMenu
                ViewMode.LOADING -> loading
                ViewMode.CREATION -> creationMenu
                ViewMode.FORM -> itemForm
                ViewMode.EMPTY -> View(this)
            }

        override val icon: Icon =
            if (viewMode == ViewMode.CONTEXTUAL) confirmationActionColors.icon(R.drawable.ic_check)
            else cancelActionColors.icon(R.drawable.ic_menu_close)
        override val hasOverlay = viewMode.hasOverlay
    }

    floatMenu(
        contentArea.box,
        stateMachine.viewMode.oneWayBranch { MenuArea(this) },
        isOpen = stateMachine.isOpen
    ).set(this, R.id.contentPane) {
        findViewById<View>(floatR.id.openFab).onLongClick {
            stateMachine.openExplicitContextualMenu()
        }
    }
    */
}
