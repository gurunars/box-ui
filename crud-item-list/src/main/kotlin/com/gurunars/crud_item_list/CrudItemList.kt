package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.gurunars.databinding.bindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItemList
import java.util.*

/**
 * Widget to be used for manipulating a collection of items.
 */
class CrudItemList<ItemType : Item>  constructor(context: Context) : RelativeLayout(context) {

    data class IconColorBundle(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE
    )

    val actionIcon = bindableField(IconColorBundle())
    val contextualIcon = bindableField(IconColorBundle())
    val createCloseIcon = bindableField(IconColorBundle())
    val openIcon = bindableField(IconColorBundle())

    private val contextualMenu: ContextualMenu
    private var creationMenu: View? = null

    private val throttleBuffer = UiThrottleBuffer()

    private val floatingMenu: FloatMenu
    private val itemList: SelectableItemList<ItemType>

    private var listChangeListener: (list: List<ItemType>) -> Unit = {}
    private var itemEditListener: (item: ItemType) -> Unit = {}

    private var items: List<ItemType> = ArrayList()

    private val actions = object : HashMap<Int, Action<ItemType>>() {
        init {
            put(R.id.selectAll, ActionSelectAll<ItemType>())
            put(R.id.delete, ActionDelete<ItemType>())
            put(R.id.edit, ActionEdit({ payload -> itemEditListener(payload) }))
            put(R.id.moveUp, ActionMoveUp<ItemType>())
            put(R.id.moveDown, ActionMoveDown<ItemType>())
        }
    }

    private fun confView(view: View, @IdRes id: Int) {
        view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        view.id = id
    }

    init {

        floatingMenu = FloatMenu(context)
        confView(floatingMenu, R.id.floatingMenu)

        itemList = SelectableItemList<ItemType>(context)
        confView(itemList, R.id.rawItemList)

        contextualMenu = ContextualMenu(context)
        confView(contextualMenu, R.id.contextualMenu)

        setCreationMenu(View(context))

        floatingMenu.contentView.set(itemList)
        floatingMenu.menuView.set(contextualMenu)
        floatingMenu.setOpenIcon(R.drawable.ic_plus)

        addView(floatingMenu)

        setLeftHanded(false)

        itemList.setSelectionChangeListener(Runnable {
            if (itemList.selectedItems.isEmpty()) {
                floatingMenu.close()
            } else {
                setUpContextualMenu()
            }
        })

        floatingMenu.isOpen.onChange {
            if (it) {

            } else {

            }
        }


        (object : AnimationListener() {
            fun onStart(projectedDuration: Int) {
                itemList.selectedItems = HashSet<ItemType>()
            }

            fun onFinish() {
                setUpCreationMenu()
            }
        })

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
        floatingMenu.setOpenIconBgColor(openBgColor)
        floatingMenu.setOpenIconFgColor(openFgColor)
        if (itemList.selectedItems.isEmpty()) {
            setUpCreationMenu()
        } else {
            setUpContextualMenu()
        }
    }

    private fun setUpActions() {
        for ((key, value) in actions) {
            contextualMenu.findViewById(key).isEnabled = value.canPerform(items, itemList.selectedItems)
        }
    }

    private fun setUpContextualMenu() {
        floatingMenu.menuView.set(contextualMenu)
        floatingMenu.setCloseIconFgColor(contextualCloseFgColor)
        floatingMenu.setCloseIconBgColor(contextualCloseBgColor)
        floatingMenu.setCloseIcon(R.drawable.ic_check)
        floatingMenu.hasOverlay.set(false)
        setUpActions()
        floatingMenu.open()
    }

    private fun setUpCreationMenu() {
        floatingMenu.setMenuView(creationMenu)
        floatingMenu.setCloseIconFgColor(createCloseFgColor)
        floatingMenu.setCloseIconBgColor(createCloseBgColor)
        floatingMenu.setCloseIcon(R.drawable.ic_menu_close)
        floatingMenu.hasOverlay.set(true)
    }

    override fun onDetachedFromWindow() {
        throttleBuffer.shutdown()
        super.onDetachedFromWindow()
    }

    /**
     * Map item type to view binder responsible for rending items of this type.

     * @param itemType type of the Items
     * *
     * @param itemViewBinder row renderer for the items of a given type
     */
    fun registerItemType(
            itemType: Enum<*>,
            itemViewBinder: ItemViewBinder<out View, SelectableItem<ItemType>>) {
        itemList.registerItemViewBinder(itemType, itemViewBinder)
    }


    /**
     * @param listChangeListener callback to be executed whenever the list gets changed within
     * *                           the widget
     */
    fun setListChangeListener(listChangeListener: ListChangeListener<ItemType>) {
        this.listChangeListener = listChangeListener
    }

    /**
     * @param creationMenu menu to be show when creation mode is active (no items selected) and the
     * *                     menu is open
     */
    fun setCreationMenu(creationMenu: View) {
        this.creationMenu = creationMenu
        confView(creationMenu, R.id.creationMenu)
        reload()
    }

    /**
     * @param itemEditListener a listener for the cases when a new item has to be created or when
     * *                         the existing one has to be edited
     */
    fun setItemEditListener(itemEditListener: ItemEditListener<ItemType>) {
        this.itemEditListener = itemEditListener
    }

}
