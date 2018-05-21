package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service.Companion.getRealService
import com.gurunars.animal_item.bindAnimal
import com.gurunars.box.core.IBox
import com.gurunars.box.core.bind
import com.gurunars.box.core.patch
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.statefulView
import com.gurunars.item_list.coloredRowSelectionDecorator
import com.gurunars.item_list.selectableItemView
import com.gurunars.item_list.selectableListView

class ActivityMain : Activity() {

    private lateinit var srv: Service
    private lateinit var items: IBox<List<AnimalItem>>

    private val selectedItems = Box<Set<Long>>(setOf())
    private val explicitSelectionMode = false.box

    private fun add(type: AnimalItem.Type) {
        items.patch { this + AnimalItem(-this.size.toLong(), type, 0) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = getRealService()
        items = srv.items

        statefulView(R.id.main) {
            retain(selectedItems, explicitSelectionMode)
            selectableListView(
                items = items,
                selectedItems = selectedItems,
                getId = { it.id },
                itemRenderers = {
                    selectableItemView {
                        bindAnimal(it.bind { item }).coloredRowSelectionDecorator(it.bind { isSelected })
                    }
                }
            ).layoutAsOne(this)
        }.layoutAsOne(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.clear_selection -> showToast(clearSelection())
            R.id.create -> showToast(create())
            R.id.delete_selected -> showToast(deleteSelected())
            R.id.update_selected -> showToast(updateSelected())
            R.id.reset -> showToast(reset())
            R.id.enable_explicit_selection -> showToast(enableExplicitSelection())
            else -> super.onOptionsItemSelected(item)
        }

    private fun enableExplicitSelection(): Int {
        explicitSelectionMode.set(true)
        return R.string.explicit_selection_enabled
    }

    private fun updateSelected(): Int {
        val selected = selectedItems.get()
        items.patch {
            map {
                if (selected.any { id -> it.id == id }) {
                    it.copy(version = it.version + 1)
                } else {
                    it
                }
            }
        }
        return R.string.did_update_selected
    }

    private fun deleteSelected(): Int {
        items.patch { filterNot { item: AnimalItem ->
            selectedItems.get().any { id -> item.id == id }
        } }
        return R.string.did_delete_selected
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clearSelection(): Int {
        selectedItems.set(setOf())
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
        srv.clear()
        explicitSelectionMode.set(false)
        create()
        return R.string.did_reset
    }
}