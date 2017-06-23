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
import com.gurunars.item_list.*
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
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
                val other = it.second
                if (other != null && !it.first.item.payloadsEqual(other.item)) {
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

    private val storage = PersistentStorage(this, "main")

    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var itemListView: SelectableItemListView<AnimalItem>

    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), 0, type))
        count.set(count.get() + 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            fullSize()
            itemListView = selectableItemListView({ AnimalBinder() }) {
                fullSize()
                id=R.id.selectableItemList
                this@ActivityMain.items.bind(items)
            }
        }

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
        val selected = itemListView.selectedItems.get()
        items.set(items.get().map {
            if (selected.any { item -> it.getId() == item.getId() })
                it.copy(version = it.version+1)
            else
                it
        })
        return R.string.did_update_selected
    }

    private fun deleteSelected(): Int {
        itemListView.items.set(items.get().filterNot {
            itemListView.selectedItems.get().any { item -> it.getId() == item.getId()}
        })
        return R.string.did_delete_selected
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clearSelection(): Int {
        itemListView.selectedItems.set(HashSet<AnimalItem>())
        return R.string.did_clear_selection
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        return R.string.did_create
    }

    @StringRes private fun reset(): Int {
        items.set(listOf())
        count.set(0)
        create()
        return R.string.did_reset
    }

}