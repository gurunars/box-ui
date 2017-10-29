package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.gurunars.animal_item.AnimalItem
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.branch
import com.gurunars.item_list.*
import com.gurunars.databinding.android.asRow
import com.gurunars.shortcuts.setAsOne
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import java.util.*

private fun Context.bindAnimal(field: BindableField<AnimalItem>) = TextView(this).apply {
    asRow()
    padding = context.dip(5)
    txt(field.branch { toString() })
}

class ActivityMain : Activity() {

    private val storage = PersistentStorage(this, "main")

    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var itemListView: SelectableItemListView<AnimalItem>

    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), type, 0))
        count.set(count.get() + 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage.load()

        itemListView = SelectableItemListView<AnimalItem>(this,
            AnimalItem.Type.values().map {
                Pair(it, { item: BindableField<SelectableItem<AnimalItem>> ->
                    coloredRowSelectionDecorator<AnimalItem>(item) { bindAnimal(it) }
                })
            }.toMap()
        ).setAsOne(this) {
            id = R.id.selectableItemList
            this@ActivityMain.items.bind(items)
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
            if (selected.any { (id) -> it.id == id })
                it.copy(version = it.version + 1)
            else
                it
        })
        return R.string.did_update_selected
    }

    private fun deleteSelected(): Int {
        itemListView.items.set(items.get().filterNot {
            itemListView.selectedItems.get().any { (id) -> it.id == id }
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