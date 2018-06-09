package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service
import com.gurunars.animal_item.Service.Companion.getRealService
import com.gurunars.animal_item.bindAnimal
import com.gurunars.box.*
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.shortToast
import com.gurunars.box.ui.statefulView
import com.gurunars.item_list.itemListView
import com.gurunars.item_list.withSelection

class ActivityMain : Activity() {

    lateinit var srv: Service
    lateinit var items: IBox<List<AnimalItem>>

    private val selectedItems = Box<Set<AnimalItem>>(setOf())
    private val explicitSelectionMode = false.box

    private fun add(type: AnimalItem.Type) =
        items.patch { this + AnimalItem(-this.size.toLong(), type, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = getRealService()
        items = srv.items

        statefulView(R.id.main) {
            retain(selectedItems, explicitSelectionMode)
            itemListView(
                items = items,
                itemViewBinders = AnimalItem.Type.values().map {
                    Pair(it as Enum<*>, { field: IRoBox<AnimalItem> -> context.bindAnimal(field) })
                }.toMap().withSelection(selectedItems, explicitSelectionMode)
            ).layoutAsOne(this)
        }.layoutAsOne(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    private val actions = mapOf(
        R.id.clear_selection to ::clearSelection,
        R.id.create to ::create,
        R.id.delete_selected to ::deleteSelected,
        R.id.update_selected to ::updateSelected,
        R.id.reset to ::reset,
        R.id.enable_explicit_selection to ::enableExplicitSelection
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        actions.get(item.itemId)?.let {
            shortToast(getString(it()))
            true
        } ?: super.onOptionsItemSelected(item)


    @StringRes private fun enableExplicitSelection(): Int {
        explicitSelectionMode.set(true)
        return R.string.explicit_selection_enabled
    }

    @StringRes private fun updateSelected(): Int {
        val selected = selectedItems.get()
        items.patch {
            this.map {
                if (selected.any { (id) -> it.id == id })
                    it.copy(version = it.version + 1)
                else it
            }
        }
        return R.string.did_update_selected
    }

    @StringRes private fun deleteSelected(): Int {
        items.set(items.get().filterNot { item: AnimalItem ->
            selectedItems.get().any { (id) -> item.id == id }
        })
        return R.string.did_delete_selected
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