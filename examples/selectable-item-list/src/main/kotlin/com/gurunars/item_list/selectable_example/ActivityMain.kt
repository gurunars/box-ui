package com.gurunars.item_list.selectable_example

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.contains
import com.gurunars.item_list.*
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*
import java.util.*


internal class AnimalBinder: SelectableItemViewBinder<AnimalItem> {

    override fun bind(context: Context, payload: BindableField<Pair<SelectableItem<AnimalItem>, SelectableItem<AnimalItem>?>>): View {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
            padding = context.dip(5)
            payload.onChange {
                setBackgroundColor(if (it.first.isSelected) Color.RED else Color.WHITE)
                text = it.first.toString()
                if (it.second != null) {
                    clearAnimation()
                    ValueAnimator().apply {
                        setFloatValues(1.0f, 0.0f, 1.0f)
                        addUpdateListener { animation -> alpha = animation.animatedValue as Float }
                        duration = 1300
                        start()
                    }
                }
            }
        }
    }

    override fun getEmptyPayload() = AnimalItem(0, 0, AnimalItem.Type.EMPTY)
}


class ActivityMain : Activity() {

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
            itemList=selectableItemList({ AnimalBinder() }) {
                fullSize()
                id=R.id.selectableItemList
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