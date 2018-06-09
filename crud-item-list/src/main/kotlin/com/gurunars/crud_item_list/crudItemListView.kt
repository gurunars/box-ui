package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.gurunars.android_utils.Icon
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.floatmenu.ContentPane
import com.gurunars.floatmenu.MenuPane
import com.gurunars.floatmenu.floatMenu
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
    itemTypeDescriptors: List<ItemTypeDescriptor<ItemType>>,
    clipboardSerializer: ClipboardSerializer<ItemType>? = null,
    sortable: Boolean = true,
    addToTail: Boolean = true,
    actionIconColors: IconColorBundle = IconColorBundle(),
    emptyViewBinder: EmptyViewBinder = this::defaultEmptyViewBinder,
    confirmationActionColors: IconColorBundle = IconColorBundle(),
    cancelActionColors: IconColorBundle = IconColorBundle(),
    openIconColors: IconColorBundle = IconColorBundle(),
    items: IBox<List<ItemType>>,
    itemInEdit: IBox<Optional<ItemType>> = Optional.None.box
): View = statefulView(R.id.crudItemListView) {

    val typeCache = itemTypeDescriptors.map { Pair(it.type, it) }.toMap()

    val itemForm = with<FrameLayout> {
        fullSize()
        id = R.id.itemForm
    }

    val isOpen = false.box

    val stateMachine = StateMachine(
        openForm = { item ->
            itemForm(
                itemInEdit.branch(item),
                { it ->
                    isOpen.set(false)
                    items.patch { processItemInEdit(addToTail, this, it) }
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

    val loading = with<FrameLayout> {
        with<ProgressBar>().layout(this) {
            width = dip(80)
            height = dip(80)
            gravity = Gravity.CENTER
        }
    }

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
}
