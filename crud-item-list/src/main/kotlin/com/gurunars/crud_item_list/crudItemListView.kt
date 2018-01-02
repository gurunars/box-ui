package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.box.IBox
import com.gurunars.box.ui.closeKeyboard
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.onLongClick
import com.gurunars.box.ui.set
import com.gurunars.box.ui.statefulView
import com.gurunars.box.bind
import com.gurunars.box.box
import com.gurunars.box.branch
import com.gurunars.box.patch
import com.gurunars.floatmenu.ContentPane
import com.gurunars.floatmenu.MenuPane
import com.gurunars.floatmenu.floatMenu
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.defaultBindEmpty
import com.gurunars.item_list.selectableItemListView
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.progressBar
import com.gurunars.floatmenu.R as floatR

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
 */
fun <ItemType : Item> Context.crudItemListView(
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    items: IBox<List<ItemType>>,
    clipboardSerializer: ClipboardSerializer<ItemType>? = null,
    sortable: Boolean = true,
    actionIconColors: IconColorBundle = IconColorBundle(),
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    confirmationActionColors: IconColorBundle = IconColorBundle(),
    cancelActionColors: IconColorBundle = IconColorBundle(),
    openIconColors: IconColorBundle = IconColorBundle()
): View = statefulView(R.id.crudItemListView, "CRUD ITEM LIST") {

    val typeCache = groupedItemTypeDescriptors
        .flatten().map {
        Pair(it.type, it)
    }.toMap()

    val itemForm = context.frameLayout {
        fullSize()
        id = R.id.itemForm
    }

    val isOpen = false.box

    val stateMachine = StateMachine(
        openForm = { item ->
            itemForm(
                item,
                { it ->
                    isOpen.set(false)
                    items.patch { processItemInEdit(it, this) }
                },
                confirmationActionColors,
                typeCache[item.type]!!
            ).set(itemForm, R.id.formContent)
        },
        itemTypes = typeCache
    ).apply {
        this.isOpen.bind(isOpen)
        // Items should be retains as a state to prevent the selection being dropped because
        // of async operations done in a wrong order
        // retain(items)
        retain(state)
    }

    isOpen.onChange { it -> if (!it) closeKeyboard() }

    val contextualMenu = contextualMenu(
        { stateMachine.loadItem(it) },
        sortable,
        actionIconColors,
        items,
        stateMachine.selectedItems,
        clipboardSerializer
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
        { stateMachine.loadType(it) }
    )

    val itemListView = selectableItemListView(
        items = items,
        selectedItems = stateMachine.selectedItems,
        itemViewBinders = groupedItemTypeDescriptors.flatten().map {
            Pair(
                it.type,
                { item: IBox<SelectableItem<ItemType>> -> it.bindRow(
                    item, { stateMachine.loadItem(item.get().item) }
                ) }
            )
        }.toMap(),
        emptyViewBinder = emptyViewBinder,
        explicitSelectionMode = stateMachine.state.branch { explicitContextual }
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
        stateMachine.viewMode.branch { MenuArea(this) },
        isOpen = stateMachine.isOpen
    ).set(this, R.id.contentPane) {
        findViewById<View>(floatR.id.openFab).onLongClick {
            stateMachine.openExplicitContextualMenu()
        }
    }
}
