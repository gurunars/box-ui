package com.gurunars.crud_item_list

import android.content.Context
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

internal class ControllableItemListView<ItemType : Item>  constructor(
    context: Context,
    emptyViewBinder: EmptyViewBinder = Context::defaultEmptyViewBinder,
    groupedItemTypeDescriptors: List<List<ItemTypeDescriptor<ItemType>>>,
    onEdit: (item: ItemType) -> Unit
) : FrameLayout(context) {

    val actionIcon = bindableField(IconColorBundle())
    val contextualCloseIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())
    val isSortable = bindableField(true)

    val isLeftHanded = bindableField(false)
    val items: BindableField<List<ItemType>>
    val isOpen: BindableField<Boolean>

    private val creationMenu: View
    private val contextualMenu: View
    private val floatingMenu: FloatMenu
    private val itemListView: SelectableItemListView<ItemType>
    private val knobView: KnobView

    private enum class ViewMode {
        CONTEXTUAL, CREATION
    }

    private val typeCache = mutableMapOf<Enum<*>, ItemTypeDescriptor<ItemType>>()

    private fun itemViewBinder(
        context: Context,
        itemType: Enum<*>,
        field: BindableField<SelectableItem<ItemType>>
    ) = typeCache[itemType]?.rowBinder?.invoke(context, itemType, field) ?:
        throw RuntimeException("Unknown type ${itemType}")

    init {

        groupedItemTypeDescriptors.forEach {
            it.forEach {
                typeCache.put(it.type, it)
            }
        }

        creationMenu = creationMenu(
            context,
            groupedItemTypeDescriptors,
            onEdit,
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
            onEdit
        ).apply {
            fullSize()
            id = R.id.contextualMenu
        }

        knobView = KnobView(context, mapOf(
            ViewMode.CONTEXTUAL to contextualMenu,
            ViewMode.CREATION to creationMenu
        )).apply {
            fullSize()
            id = R.id.knobMenu
        }

        floatingMenu = floatMenu(itemListView, knobView) {
            fullSize()
            id = R.id.floatingMenu
            this@ControllableItemListView.isLeftHanded.bind(isLeftHanded)

            isOpen.onChange {
                if (!it) itemListView.selectedItems.set(hashSetOf())
            }

            itemListView.selectedItems.onChange {
                isOpen.set(it.isNotEmpty())
            }

            listOf(isOpen, itemListView.selectedItems).onChange {
                if (!isOpen.get()) { return@onChange }
                if (itemListView.selectedItems.get().isEmpty()) {
                    hasOverlay.set(true)
                    knobView.selectedView.set(ViewMode.CREATION)
                } else  {
                    hasOverlay.set(false)
                    knobView.selectedView.set(ViewMode.CONTEXTUAL)
                }
            }

            this@ControllableItemListView.openIcon.onChange {
                openIcon.set(openIcon.get().copy(
                    bgColor = it.bgColor,
                    fgColor = it.fgColor
                ))
            }

            fun confCloseIcon() =
                if (itemListView.selectedItems.get().isEmpty()) {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_menu_close,
                        bgColor = createCloseIcon.get().bgColor,
                        fgColor = createCloseIcon.get().fgColor
                    ))
                } else {
                    closeIcon.set(IconView.Icon(
                        icon = R.drawable.ic_check,
                        bgColor = contextualCloseIcon.get().bgColor,
                        fgColor = contextualCloseIcon.get().fgColor
                    ))
                }

            listOf(contextualCloseIcon, createCloseIcon).onChange {
                confCloseIcon()
            }

            isOpen.onChange {
                if(it) {
                    confCloseIcon()
                }
            }

        }

        isOpen = floatingMenu.isOpen
        items = itemListView.items

    }

}
