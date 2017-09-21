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

    val actionIcon = bindableField(IconColorBundle())
    val contextualCloseIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
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
            actionIcon,
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
            contextualCloseIcon
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
                    bgColor = createCloseIcon.get().bgColor,
                    fgColor = createCloseIcon.get().fgColor
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
                        bgColor = contextualCloseIcon.get().bgColor,
                        fgColor = contextualCloseIcon.get().fgColor
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

            listOf(contextualCloseIcon, createCloseIcon).onChange {
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
