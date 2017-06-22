package com.gurunars.crud_item_list.example

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.crud_item_list.CrudItemList
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.SelectableItemViewBinder
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.wrapContent


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


    private lateinit var crudItemList: CrudItemList<AnimalItem>
    private lateinit var creationMenu: View

    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), 0, type))
        count.set(count.get() + 1)
    }

    private fun initData(force: Boolean) {
        if (!force) {
            return
        }
        items.set(listOf())
        count.set(0)
        for (i in 0..0) {
            add(AnimalItem.Type.LION)
            add(AnimalItem.Type.TIGER)
            add(AnimalItem.Type.MONKEY)
            add(AnimalItem.Type.WOLF)
        }
    }

    private fun confItemType(@IdRes id: Int, type: AnimalItem.Type) {
        creationMenu.findViewById(id).setOnClickListener {
            add(type))
            crudItemList.setItems(model.items)
        }
        crudItemList.registerItemType(type, AnimalRowBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crudItemList({ AnimalBinder() }) {

        }


        crudItemList.setListChangeListener(object : ListChangeListener<AnimalItem>() {
            fun onChange(items: List<AnimalItem>) {
                for (item in items) {
                    model.updateItem(item)
                }
            }

        })
        crudItemList.setEmptyViewBinder(EmptyBinder())
        crudItemList.setCreationMenu(creationMenu)
        crudItemList.setItemEditListener(object : ItemEditListener<AnimalItem>() {
            fun onEdit(editableItem: AnimalItem) {
                editableItem.update()
                model.updateItem(editableItem)
                crudItemList.setItems(model.items)
            }

        })

        confItemType(R.id.lion, AnimalItem.Type.LION)
        confItemType(R.id.tiger, AnimalItem.Type.TIGER)
        confItemType(R.id.monkey, AnimalItem.Type.MONKEY)
        confItemType(R.id.wolf, AnimalItem.Type.WOLF)

        initData(false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        when (i) {
            R.id.leftHanded -> crudItemList.setLeftHanded(true)
            R.id.rightHanded -> crudItemList.setLeftHanded(false)
            R.id.reset -> initData(true)
            R.id.lock -> {
                setTitle(R.string.unsortable)
                crudItemList.setSortable(false)
            }
            R.id.unlock -> {
                setTitle(R.string.sortable)
                crudItemList.setSortable(true)
            }
            R.id.addMany -> addMany()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMany() {
        model.clear()
        for (i in 0..19) {
            add(AnimalItem.Type.LION))
            add(AnimalItem.Type.TIGER))
            add(AnimalItem.Type.MONKEY))
            add(AnimalItem.Type.WOLF))
        }
        crudItemList.setItems(model.items)
    }

}