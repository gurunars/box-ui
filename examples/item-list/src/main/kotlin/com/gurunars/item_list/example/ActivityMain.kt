package com.gurunars.item_list.example

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service
import com.gurunars.animal_item.Service.Companion.getRealService
import com.gurunars.animal_item.bindAnimal
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.shortToast
import com.gurunars.item_list.itemListView

class ActivityMain : Activity() {

    lateinit var srv: Service
    lateinit var items: IBox<List<AnimalItem>>

    private fun add(type: AnimalItem.Type) {
        val values = items.get()
        items.set(values + AnimalItem(-values.size.toLong(), type, 0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = getRealService()
        items = srv.items

        itemListView(
            items = items,
            itemViewBinders = AnimalItem.Type.values().map {
                Pair(it as Enum<*>, { value: IRoBox<AnimalItem> -> this.bindAnimal(value) })
            }.toMap()
        ).layoutAsOne(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    private val actions = mapOf(
        R.id.clear to ::clear,
        R.id.create to ::create,
        R.id.delete to ::delete,
        R.id.update to ::update,
        R.id.reset to ::reset,
        R.id.moveDown to ::moveTopToBottom,
        R.id.moveUp to ::moveBottomToTop,
        R.id.reset to ::reset
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        actions.get(item.itemId)?.let {
            shortToast(getString(it()))
            true
        } ?: super.onOptionsItemSelected(item)

    @StringRes private fun clear(): Int {
        srv.clear()
        return R.string.did_clear
    }

    @StringRes private fun create(): Int {
        add(AnimalItem.Type.TIGER)
        add(AnimalItem.Type.WOLF)
        add(AnimalItem.Type.MONKEY)
        add(AnimalItem.Type.LION)
        return R.string.did_create
    }

    @StringRes private fun delete(): Int {
        this.items.set(this.items.get().filterIndexed({ index, _ ->
            index % 2 == 0
        }))
        return R.string.did_delete
    }

    @StringRes private fun update(): Int {
        items.set(items.get().mapIndexed { index, animalItem ->
            if (index % 2 != 0)
                animalItem.copy(version = animalItem.version + 1)
            else animalItem
        })
        return R.string.did_update
    }

    @StringRes private fun moveBottomToTop(): Int {
        if (items.get().isEmpty()) {
            return R.string.no_action
        }
        items.set(items.get().toMutableList().apply {
            val item = get(size - 1)
            removeAt(size - 1)
            add(0, item)
        })
        return R.string.did_move_to_top
    }

    @StringRes private fun moveTopToBottom(): Int {
        if (items.get().isEmpty()) {
            return R.string.no_action
        }
        items.set(items.get().toMutableList().apply {
            val item = get(0)
            removeAt(0)
            add(item)
        })
        return R.string.did_move_to_bottom
    }

    @StringRes private fun reset(): Int {
        srv.clear()
        create()
        return R.string.did_reset
    }
}