package com.gurunars.crud_item_list.example

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gurunars.crud_item_list.CrudItemList


class ActivityMain : Activity() {

    private val model = Model(this)

    private lateinit var crudItemList: CrudItemList<AnimalItem>
    private lateinit var creationMenu: View

    private fun initData(force: Boolean) {
        if (model.wasInited() && !force) {
            crudItemList.setItems(model.items)
            return
        }
        model.clear()
        for (i in 0..0) {
            model.createItem(AnimalItem(AnimalItem.Type.LION))
            model.createItem(AnimalItem(AnimalItem.Type.TIGER))
            model.createItem(AnimalItem(AnimalItem.Type.MONKEY))
            model.createItem(AnimalItem(AnimalItem.Type.WOLF))
        }

        crudItemList.setItems(model.items)
    }

    private fun confItemType(@IdRes id: Int, type: AnimalItem.Type) {
        creationMenu.findViewById(id).setOnClickListener {
            model.createItem(AnimalItem(type))
            crudItemList.setItems(model.items)
        }
        crudItemList.registerItemType(type, AnimalRowBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = Model(this)
        setContentView(R.layout.crud_list)

        creationMenu = View.inflate(this, R.layout.create_layout, null)

        crudItemList = ButterKnife.findById(this, R.id.customView)

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
            model.createItem(AnimalItem(AnimalItem.Type.LION))
            model.createItem(AnimalItem(AnimalItem.Type.TIGER))
            model.createItem(AnimalItem(AnimalItem.Type.MONKEY))
            model.createItem(AnimalItem(AnimalItem.Type.WOLF))
        }
        crudItemList.setItems(model.items)
    }

}