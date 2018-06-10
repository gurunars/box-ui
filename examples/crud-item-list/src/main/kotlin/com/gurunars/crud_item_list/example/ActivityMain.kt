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
import com.gurunars.animal_item.Service.Companion.getRealService
import com.gurunars.box.box
import com.gurunars.box.patch
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.decorators.statefulLayer
import com.gurunars.crud_item_list.IconColorBundle
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.crud_item_list.crudItemListView

class ActivityMain : Activity() {

    private lateinit var srv: Service

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
        srv.items.patch {
            val id = (this.map { it.id }.lastOrNull() ?: 0) + 1
            this + (0 until count).map {
                AnimalItem(
                    id = id + it,
                    type = getType(it),
                    version = 0
                )
            }
        }
    }

    private fun reset() {
        srv.clear().subscribe {
            addItems(4)
        }
        val mngr = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mngr.primaryClip = ClipData.newPlainText("", "")
    }

    private fun initView(sortable: Boolean) {
        setTitle(if (sortable) R.string.sortable else R.string.unsortable)

        val descriptors: List<ItemTypeDescriptor<AnimalItem>>

        if (sortable) {
            descriptors = listOf(
                R.drawable.ic_menu_monkey to AnimalItem.Type.MONKEY,
                R.drawable.ic_menu_lion to AnimalItem.Type.LION,
                R.drawable.ic_menu_tiger to AnimalItem.Type.TIGER,
                R.drawable.ic_menu_wolf to AnimalItem.Type.WOLF
            ).map {
                Descriptor(
                    srv.items,
                    this,
                    it.first,
                    it.second
                )
            }
        } else {
            descriptors = listOf(Descriptor(
                srv.items,
                this,
                R.drawable.ic_menu_monkey,
                AnimalItem.Type.MONKEY)
            )
        }

        statefulLayer(R.id.main) {
            setBackgroundColor(Color.WHITE)
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
                items = srv.items,
                itemTypeDescriptors = descriptors,
                sortable = sortable,
                addToTail = sortable,
                actionIconColors = IconColorBundle(
                    fgColor = Color.YELLOW,
                    bgColor = Color.BLUE
                ),
                clipboardSerializer = Serializer()
            ).layoutAsOne(this)
        }.layoutAsOne(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        srv = getRealService()
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
