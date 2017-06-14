package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gurunars.databinding.contains
import com.gurunars.item_list.*
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setPadding
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import java.util.*


class ActivityMain : Activity() {

    internal class AnimalBinder : ItemViewBinder<TextView, SelectableItem<AnimalItem>> {

        override fun getView(context: Context): TextView {
            return TextView(context).apply {
                setPadding(context.dip(5))
            }
        }

        override fun bind(itemView: TextView, item: SelectableItem<AnimalItem>, previousItem: SelectableItem<AnimalItem>?) {
            itemView.text = "$item"
            itemView.setBackgroundColor(if (item.isSelected) Color.RED else Color.WHITE)
        }

    }

    internal class EmptyBinder : EmptyViewBinder {

        override fun getView(context: Context): View {
            val view = TextView(context)
            view.gravity = Gravity.CENTER
            view.setText(R.string.empty)
            return view
        }

    }

    private lateinit var itemList: SelectableItemList<AnimalItem>
    private val items = ArrayList<AnimalItem>()
    private var count = 0

    private fun add(type: AnimalItem.Type) {
        items.add(AnimalItem(count.toLong(), 0, type))
        count++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            fullSize()
            itemList=selectableItemList<AnimalItem> {
                fullSize()
                id=R.id.selectableItemList
                itemViewBinders.set(mutableMapOf<Enum<*>, ItemViewBinder<*, SelectableItem<AnimalItem>>>().apply {
                    AnimalItem.Type.values().forEach { put(it, AnimalBinder()) }
                })
                emptyViewBinder.set(EmptyBinder())
            }
        }

        reset()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_selection -> return showToast(clearSelection())
            R.id.create -> return showToast(create())
            R.id.delete_selected -> return showToast(deleteSelected())
            R.id.update_selected -> return showToast(updateSelected())
            R.id.reset -> return showToast(reset())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateSelected(): Int {
        for (item in itemList.selectedItems.get()) {
            item.update()
            for (i in items.indices) {
                if (item.getId() == items[i].getId()) {
                    items[i] = item
                    break
                }
            }
        }
        itemList.items.set(items)
        return R.string.did_update_selected
    }

    private fun deleteSelected(): Int {
        fun equal(one: AnimalItem, two: AnimalItem) = one.getId() == two.getId()
        items.removeIf { itemList.selectedItems.get().contains(it, ::equal) }
        itemList.items.set(items)
        return R.string.did_delete_selected
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clearSelection(): Int {
        itemList.selectedItems.set(HashSet<AnimalItem>())
        return R.string.did_clear_selection
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        itemList.items.set(items)
        return R.string.did_create
    }

    @StringRes private fun reset(): Int {
        count = 0
        items.clear()
        create()
        return R.string.did_reset
    }

}