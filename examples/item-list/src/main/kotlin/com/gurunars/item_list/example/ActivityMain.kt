package com.gurunars.item_list.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.gurunars.animal_item.AnimalItem
import com.gurunars.databinding.Box
import com.gurunars.databinding.android.asRow
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.branch
import com.gurunars.item_list.itemListView
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding

private fun Context.bindAnimal(field: IBox<AnimalItem>): View = TextView(this).apply {
    asRow()
    padding = context.dip(5)
    txt(field.branch { toString() })
}

class ActivityMain : Activity() {
    private val storage = PersistentStorage(this, "main")

    private val items: IBox<List<AnimalItem>> =
        storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)


    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), type, 0))
        count.set(count.get() + 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage.load()

        itemListView(
            items = items,
            itemViewBinders = AnimalItem.Type.values().map {
                Pair(it as Enum<*>, { value: IBox<AnimalItem> -> this.bindAnimal(value) })
            }.toMap()
        ).setAsOne(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear -> return showToast(clear())
            R.id.create -> return showToast(create())
            R.id.delete -> return showToast(delete())
            R.id.update -> return showToast(update())
            R.id.moveDown -> return showToast(moveTopToBottom())
            R.id.moveUp -> return showToast(moveBottomToTop())
            R.id.reset -> return showToast(reset())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(@StringRes text: Int): Boolean {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        return true
    }

    @StringRes private fun clear(): Int {
        items.set(listOf())
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
            else
                animalItem
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
        count.set(0)
        items.set(listOf())
        create()
        return R.string.did_reset
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        storage.unbindAll()
    }

}