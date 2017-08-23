package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.gurunars.animal_item.AnimalItem
import com.gurunars.crud_item_list.CrudItemListView
import com.gurunars.crud_item_list.crudItemListView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.coloredRowSelectionDecorator
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

internal fun bindAnimalItem(
    context: Context,
    itemType: Enum<*>,
    payload: BindableField<AnimalItem>
) = TextView(context).apply {
    padding = context.dip(5)
    payload.onChange { text = it.toString() }
}

class ActivityMain : Activity() {

    private val storage = PersistentStorage(this, "main")

    private val isLeftHanded = storage.storageField("isLeftHanded", false)
    private val isSortable = storage.storageField("sSortable", true)
    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var crudItemListView: CrudItemListView<AnimalItem>

    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), type, 0))
        count.set(count.get() + 1)
    }

    private fun initData() {
        items.set(listOf(
            AnimalItem(0, AnimalItem.Type.LION, 0),
            AnimalItem(1, AnimalItem.Type.TIGER, 0),
            AnimalItem(2, AnimalItem.Type.MONKEY, 0),
            AnimalItem(3, AnimalItem.Type.WOLF, 0)
        ))
        count.set(4)
    }

    private fun addItems(count: Int) {
        for (i in 0..count-1) {
            add(when (i % 4) {
                0 -> AnimalItem.Type.LION
                1 -> AnimalItem.Type.TIGER
                2 -> AnimalItem.Type.MONKEY
                else -> AnimalItem.Type.WOLF
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage.load()

        isSortable.onChange {
            setTitle(if(it) R.string.sortable else R.string.unsortable)
        }

        crudItemListView = crudItemListView(
            coloredRowSelectionDecorator(::bindAnimalItem),
            {
                item -> run {
                    this@ActivityMain.items.set(
                        items.get().toMutableList().apply {
                            val index = indexOfFirst { item.id == it.id }
                            set(index, item.copy(version = item.version+1))
                        }
                    )
                }
            },
            {
                TextView(it).apply {
                    id=R.id.noItemsLabel
                    fullSize()
                    setText(R.string.noItemsAtAll)
                    gravity = Gravity.CENTER
                }
            }
        ) {
            fullSize()
            id=R.id.customView
            this@ActivityMain.items.bind(items)
            this@ActivityMain.isSortable.bind(isSortable)
            this@ActivityMain.isLeftHanded.bind(isLeftHanded)

            actionIcon.set(CrudItemListView.IconColorBundle(
                fgColor=color(R.color.Yellow),
                bgColor=color(R.color.Blue)
            ))
            contextualCloseIcon.set(CrudItemListView.IconColorBundle(
                bgColor=color(R.color.Black),
                fgColor=color(R.color.White)
            ))
            createCloseIcon.set(CrudItemListView.IconColorBundle(
                bgColor=color(R.color.Red),
                fgColor=color(R.color.White)
            ))
            openIcon.set(CrudItemListView.IconColorBundle(
                bgColor=color(R.color.Green),
                fgColor=color(R.color.Yellow)
            ))
            creationMenu.set(UI(false) {
                relativeLayout {
                    fullSize()
                    gravity=Gravity.CENTER

                    fun imgBtn(
                        itemType: AnimalItem.Type,
                        extraLayout: RelativeLayout.LayoutParams.() -> Unit={},
                        custom: ImageButton.() -> Unit)
                    {
                        imageButton {
                            backgroundColor=color(R.color.Red)
                            scaleType=ImageView.ScaleType.FIT_CENTER
                            padding=dip(10)
                            setOnClickListener {
                                add(itemType)
                                isOpen.set(false)
                            }
                        }.lparams {
                            margin=dip(10)
                            width=dip(45)
                            height=dip(45)
                            extraLayout()
                        }.custom()
                    }

                    imgBtn(AnimalItem.Type.MONKEY) {
                        contentDescription=getString(R.string.monkey)
                        id=R.id.monkey
                        image=getDrawable(R.drawable.ic_menu_monkey)
                    }

                    imgBtn(AnimalItem.Type.LION, {
                        below(R.id.monkey)
                    }) {
                        contentDescription=getString(R.string.lion)
                        id=R.id.lion
                        image=getDrawable(R.drawable.ic_menu_lion)
                    }

                    imgBtn(AnimalItem.Type.TIGER, {
                        rightOf(R.id.monkey)
                    }) {
                        contentDescription=getString(R.string.tiger)
                        id=R.id.tiger
                        image=getDrawable(R.drawable.ic_menu_tiger)
                    }

                    imgBtn(AnimalItem.Type.WOLF, {
                        rightOf(R.id.monkey)
                        below(R.id.tiger)
                    }) {
                        contentDescription=getString(R.string.wolf)
                        id=R.id.wolf
                        image=getDrawable(R.drawable.ic_menu_wolf)
                    }
                }
            }.view)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        when (i) {
            R.id.leftHanded -> isLeftHanded.set(true)
            R.id.rightHanded -> isLeftHanded.set(false)
            R.id.reset -> initData()
            R.id.lock -> isSortable.set(false)
            R.id.unlock -> isSortable.set(true)
            R.id.addMany -> addItems(4 * 20)
        }
        return super.onOptionsItemSelected(item)
    }

}