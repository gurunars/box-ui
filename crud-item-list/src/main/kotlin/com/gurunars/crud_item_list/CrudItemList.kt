package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.bindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemList
import com.gurunars.shortcuts.fullSize
import java.util.*

/**
 * Widget to be used for manipulating a collection of items.
 */
class CrudItemList<ItemType : Item>  constructor(context: Context) : FrameLayout(context) {

    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    val actionIcon = bindableField(IconColorBundle())
    val contextualIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())
    val isLeftHanded = bindableField(false)
    val items = bindableField(listOf<ItemType>())
    val creationMenu = bindableField(View(context))

    private val contextualMenu: ContextualMenu
    private val floatingMenu: FloatMenu
    private val itemList: SelectableItemList<ItemType>

    private var itemEditListener: (item: ItemType) -> Unit = {}

    private val throttleBuffer = UiThrottleBuffer()

    private val actions = object : HashMap<Int, Action<ItemType>>() {
        init {
            put(R.id.selectAll, ActionSelectAll<ItemType>())
            put(R.id.delete, ActionDelete<ItemType>())
            put(R.id.edit, ActionEdit({ payload -> itemEditListener(payload) }))
            put(R.id.moveUp, ActionMoveUp<ItemType>())
            put(R.id.moveDown, ActionMoveDown<ItemType>())
        }
    }

    init {

        floatingMenu = FloatMenu(context).apply {
            fullSize()
            id = R.id.floatingMenu
        }
        itemList = SelectableItemList<ItemType>(context).apply {
            fullSize()
            id = R.id.rawItemList
        }

        contextualMenu = ContextualMenu(context).apply {
            fullSize()
            id = R.id.contextualMenu
            this@CrudItemList.isLeftHanded.bind(isLeftHanded)
        }

        floatingMenu.contentView.set(itemList)
        floatingMenu.menuView.set(contextualMenu)

        addView(floatingMenu)

        floatingMenu.isOpen.onChange {
            if (it) {

            } else {
            }
        }

        for ((key, action) in actions) {
            contextualMenu.findViewById(key).setOnClickListener(View.OnClickListener {
                val selectedItems = itemList.selectedItems
                if (!action.canPerform(items, selectedItems)) {
                    return@OnClickListener
                }
                if (action.perform(items, selectedItems)) {
                    itemList.selectedItems = selectedItems
                    itemList.setItems(items)
                    setUpActions()
                    throttleBuffer.call { listChangeListener.onChange(items) }
                }
            })
        }

        reload()
    }

    private fun reload() {
        if (itemList.selectedItems.get().isEmpty()) {
            setUpCreationMenu()
        } else {
            setUpContextualMenu()
        }
    }

    private fun setUpContextualMenu() {
        floatingMenu.menuView.set(contextualMenu)
        floatingMenu.closeIcon.set(IconView.Icon(
            icon = R.drawable.ic_check,
            bgColor = contextualIcon.get().bgColor,
            fgColor = contextualIcon.get().fgColor
        ))
        floatingMenu.hasOverlay.set(false)
    }

    private fun setUpCreationMenu() {
        floatingMenu.menuView.set(creationMenu.get())
        floatingMenu.closeIcon.set(IconView.Icon(
            icon = R.drawable.ic_menu_close,
            bgColor = createCloseIcon.get().bgColor,
            fgColor = createCloseIcon.get().fgColor
        ))
        floatingMenu.hasOverlay.set(true)
    }

    override fun onDetachedFromWindow() {
        throttleBuffer.shutdown()
        super.onDetachedFromWindow()
    }
}
