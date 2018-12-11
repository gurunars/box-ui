package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.box.*
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.onLongClick
import com.gurunars.box.ui.statefulView

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @property item the actual item
 * @property isSelected a flag indicating if this particular item should be marked as selected
 */
data class SelectableItem constructor(
    val item: Item,
    val isSelected: Boolean
) : Item {

    /** @see Item.id */
    override val id = item.id

    /** @suppress */
    override fun toString() =
        item.toString() + "|" + isSelected
}

internal fun clickableBind(
    selectedItems: IBox<Set<Item>>,
    renderItem: RenderItem<SelectableItem>,
    field: IRoBox<SelectableItem>,
    explicitSelectionMode: IRoBox<Boolean>
) =
    renderItem(field).apply {
        isClickable = true
        onClick {
            val item = field.get()
            val sel = selectedItems.get()
            selectedItems.set(
                when {
                    sel.isEmpty() && !explicitSelectionMode.get() -> setOf()
                    selectedItems.get().has(item) -> sel.exclude(item.item)
                    else -> sel.include(item.item)
                }
            )
        }

        onLongClick {
            val item = field.get()
            val sel = selectedItems.get()
            if (sel.isEmpty()) selectedItems.set(sel.include(item.item))
        }
    }

/**
 * Item list that has selection enabled.
 *
 * Items can be selected initially via long clicking and further on by consequentially
 * clicking them.
 *
 * @param items A collection of items shown in the list
 * @param selectedItems A collection of items selected at the moment
 * @param itemViewBinders a type based mapping between item type and item renderer
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param explicitSelectionMode when true selection mode is initiated via normal click instead of a long one
 */
fun<ItemType: Item> Context.selectableItemListView(
    itemViewBinders: Set<RenderType<*>> = setOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultEmptyViewBinder,
    decorateSelection: View.(type: Any, isSelected: IRoBox<Boolean>) -> Unit = {
        _, isSelected -> coloredRowSelectionDecorator(isSelected=isSelected)
    },
    items: IRoBox<List<ItemType>>,
    selectedItems: IBox<Set<Item>> = Box(setOf()),
    explicitSelectionMode: IRoBox<Boolean> = false.box
): View = statefulView(R.id.selectableItemListView) {

    retain(selectedItems)

    // The flag is require to prevent selection cleanup during the initialization
    var initialized = false

    val selectables = Box<List<Item>>(listOf())

    items.onChange { its ->
        if (initialized) {
            selectedItems.patch { filter { its.has(it) }.toSet() }
        }
        initialized = true
        val selected = selectedItems.get()
        selectables.set(its.map { SelectableItem(it, selected.has(it)) })
    }

    selectedItems.onChange { its ->
        selectables.set(items.get().map { SelectableItem(it, its.has(it)) })
    }

    itemListView(
        items = selectables,
        renderer = Renderer(
            *itemViewBinders.map { original ->
                renderWith(original.type) { selectable: IRoBox<SelectableItem> ->
                    clickableBind(selectedItems, {
                        original.renderGeneric(selectable.oneWayBranch { item })
                    }, selectable, explicitSelectionMode).apply {
                        decorateSelection(original.type, selectable.oneWayBranch { isSelected })
                    }
                }
            }.toTypedArray(),
            getType = {
                if (it is SelectableItem) {
                    it.item::class
                } else {
                    it::class
                }
            }
        ),
        emptyRenderer = emptyViewBinder
    ).layoutAsOne(this)
}
