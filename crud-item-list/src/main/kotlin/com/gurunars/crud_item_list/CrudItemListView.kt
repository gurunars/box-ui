package com.gurunars.crud_item_list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.EmptyViewBinder
import com.gurunars.item_list.Item
import com.gurunars.knob_view.KnobView
import com.gurunars.knob_view.knobView
import com.gurunars.shortcuts.fullSize

/**
 * Widget to be used for manipulating a collection of items with a dedicated set of UI controls.
 *
 * @param ItemType type of the item to be shown in the list
 * @param context Android context
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 *
 * @property actionIcon Color of the icons meant to manipulate the collection of items in the
 * contextual menu.
 * @property contextualCloseIcon Check mark icon color settings. The icon is shown when contextual
 * menu is opened. Clicking the icon closes contextual menu.
 * @property createCloseIcon Cross icon color settings. The icon is shown when creation menu is
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
class CrudItemListView<ItemType: Item>(
    context: Context,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>
): FrameLayout(context) {

    private enum class ShownView {
        LIST, FORM
    }

    private val itemInEdit = BindableField<ItemType?>(null)

    private val typeCache = with(groupedItemTypeDescriptors) {
        mutableMapOf<Enum<*>, ItemTypeDescriptor<ItemType>>().apply {
            this@with.forEach {
                it.forEach {
                    this.put(it.type, it)
                }
            }
        }
    }

    private val itemView: ControllableItemListView<ItemType>
    private val itemForm: ItemForm<ItemType>
    private val knobView: KnobView

    val actionIcon: BindableField<IconColorBundle>
    val contextualCloseIcon: BindableField<IconColorBundle>
    val createCloseIcon: BindableField<IconColorBundle>
    val openIcon: BindableField<IconColorBundle>
    val isSortable: BindableField<Boolean>

    val isLeftHanded: BindableField<Boolean>
    val items: BindableField<List<ItemType>>
    val isOpen: BindableField<Boolean>

    init {

        // ITEM VIEW

        itemView = ControllableItemListView(
            context,
            emptyViewBinder,
            groupedItemTypeDescriptors,
            {
                itemInEdit.set(it)
            }
        )

        actionIcon = itemView.actionIcon
        contextualCloseIcon = itemView.contextualCloseIcon
        createCloseIcon = itemView.createCloseIcon
        openIcon = itemView.openIcon
        isSortable = itemView.isSortable
        isLeftHanded = itemView.isLeftHanded
        items = itemView.items
        isOpen = itemView.isOpen

        // FORM VIEW

        itemForm = ItemForm(
            context,
            {
                isOpen.set(false)
                itemInEdit.set(null)
            },
            {
                item: ItemType -> run {
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
            }
        ).apply {
            id=R.id.itemForm
        }

        knobView = knobView(mapOf(
            ShownView.LIST to itemView,
            ShownView.FORM to itemForm
        )) {
            fullSize()
            id=R.id.knobView
        }

        itemInEdit.onChange {
            if (it != null) {
                itemForm.bind(it, typeCache.get(it.type)!!.formBinder)
                knobView.selectedView.set(ShownView.FORM)
            } else {
                knobView.selectedView.set(ShownView.LIST)
            }
        }

    }

    /**
     * @suppress
     */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putSerializable("itemInEdit", itemInEdit.get())
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