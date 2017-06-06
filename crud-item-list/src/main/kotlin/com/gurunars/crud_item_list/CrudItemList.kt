package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
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

    private var listChangeListener: ListChangeListener<ItemType> = ListChangeListener.DefaultListChangeListener<ItemType>()
    private var itemEditListener: ItemEditListener<ItemType> = ItemEditListener.DefaultItemEditListener<ItemType>()

    private var items: List<ItemType> = ArrayList()

    private val actions = object : HashMap<Int, Action<ItemType>>() {
        init {
            put(R.id.selectAll, ActionSelectAll<ItemType>())
            put(R.id.delete, ActionDelete<ItemType>())
            put(R.id.edit, ActionEdit({ payload -> itemEditListener.onEdit(payload) }))
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
        floatingMenu.setMenuView(contextualMenu)
        floatingMenu.setOpenIcon(R.drawable.ic_plus)

        addView(floatingMenu)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CrudItemList)

        val bgColor = ContextCompat.getColor(context, R.color.Black)
        val fgColor = ContextCompat.getColor(context, R.color.White)

        actionIconFgColor = a.getColor(R.styleable.CrudItemList_actionIconFgColor, bgColor)
        actionIconBgColor = a.getColor(R.styleable.CrudItemList_actionIconBgColor, fgColor)
        contextualCloseFgColor = a.getColor(R.styleable.CrudItemList_contextualCloseFgColor, bgColor)
        contextualCloseBgColor = a.getColor(R.styleable.CrudItemList_contextualCloseBgColor, fgColor)
        createCloseBgColor = a.getColor(R.styleable.CrudItemList_createCloseBgColor, bgColor)
        createCloseFgColor = a.getColor(R.styleable.CrudItemList_createCloseFgColor, fgColor)
        openBgColor = a.getColor(R.styleable.CrudItemList_openBgColor, bgColor)
        openFgColor = a.getColor(R.styleable.CrudItemList_openFgColor, fgColor)

        a.recycle()

        setLeftHanded(false)

        itemList.setSelectionChangeListener(Runnable {
            if (itemList.selectedItems.isEmpty()) {
                floatingMenu.close()
            } else {
                setUpContextualMenu()
            }
        })

        floatingMenu.setOnCloseListener(object : AnimationListener() {
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
        floatingMenu.setMenuView(contextualMenu)
        floatingMenu.setCloseIconFgColor(contextualCloseFgColor)
        floatingMenu.setCloseIconBgColor(contextualCloseBgColor)
        floatingMenu.setCloseIcon(R.drawable.ic_check)
        floatingMenu.setHasOverlay(false)
        setUpActions()
        floatingMenu.open()
    }

    private fun setUpCreationMenu() {
        floatingMenu.setMenuView(creationMenu)
        floatingMenu.setCloseIconFgColor(createCloseFgColor)
        floatingMenu.setCloseIconBgColor(createCloseBgColor)
        floatingMenu.setCloseIcon(R.drawable.ic_menu_close)
        floatingMenu.setHasOverlay(true)
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
