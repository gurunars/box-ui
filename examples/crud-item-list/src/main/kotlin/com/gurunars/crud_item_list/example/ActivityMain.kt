package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service
import com.gurunars.crud_item_list.IconColorBundle
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.crud_item_list.crudItemListView
import com.gurunars.crud_item_list.oneOf
import com.gurunars.box.IBox
import com.gurunars.box.ui.setAsOne
import com.gurunars.box.ui.statefulView
import com.gurunars.box.box
import com.gurunars.box.patch

class ActivityMain : Activity() {

    private lateinit var srv: Service
    private lateinit var items: IBox<List<AnimalItem>>

    private val isSortable = true.box

    private fun getType(i: Int): AnimalItem.Type {
        return if (isSortable.get()) {
            when (i % 4) {
                0 -> AnimalItem.Type.LION
                1 -> AnimalItem.Type.TIGER
                2 -> AnimalItem.Type.MONKEY
                else -> AnimalItem.Type.WOLF
            }
        } else {
            AnimalItem.Type.MONKEY
        }
    }

    private fun addItems(
        count: Int
    ) {
        items.patch {
            this + (0 until count).map {
                AnimalItem(
                    id = -(this.size.toLong() + it.toLong()),
                    type = getType(it),
                    version = 0
                )
            }
        }
    }

    private fun reset() {
        srv.clear()
        addItems(4)
        val mngr = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mngr.primaryClip = ClipData.newPlainText("", "")
    }

    private fun initView(sortable: Boolean) {
        setTitle(if (sortable) R.string.sortable else R.string.unsortable)

        val descriptors: List<ItemTypeDescriptor<AnimalItem>>

        if (sortable) {
            descriptors = listOf(
                Descriptor(
                    this,
                    R.drawable.ic_menu_monkey,
                    AnimalItem.Type.MONKEY),
                Descriptor(
                    this,
                    R.drawable.ic_menu_lion,
                    AnimalItem.Type.LION),
                Descriptor(
                    this,
                    R.drawable.ic_menu_tiger,
                    AnimalItem.Type.TIGER),
                Descriptor(
                    this,
                    R.drawable.ic_menu_wolf,
                    AnimalItem.Type.WOLF)
            )
        } else {
            descriptors = Descriptor(
                this,
                R.drawable.ic_menu_monkey,
                AnimalItem.Type.MONKEY).oneOf()
        }

        statefulView(R.id.main) {
            retain(isSortable)
            crudItemListView(
                confirmationActionColors = IconColorBundle(
                    bgColor = Color.BLACK,
                    fgColor = Color.GREEN
                ),
                cancelActionColors = IconColorBundle(
                    bgColor = Color.RED,
                    fgColor = Color.WHITE
                ),
                openIconColors = IconColorBundle(
                    bgColor = Color.GREEN,
                    fgColor = Color.YELLOW
                ),
                items = items,
                itemTypeDescriptors = descriptors,
                sortable = sortable,
                actionIconColors = IconColorBundle(
                    fgColor = Color.YELLOW,
                    bgColor = Color.BLUE
                ),
                clipboardSerializer = Serializer()
            ).setAsOne(this)
        }.setAsOne(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = Service.getRealService(this)
        items = srv.items
        isSortable.onChange(listener = this::initView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        when (i) {
            R.id.reset -> reset()
            R.id.lock -> {
                isSortable.set(false)
                isSortable.broadcast()
                reset()
            }
            R.id.unlock -> {
                isSortable.set(true)
                isSortable.broadcast()
                reset()
            }
            R.id.addMany -> addItems(4 * 20)
        }
        return super.onOptionsItemSelected(item)
    }
}
