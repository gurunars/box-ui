package com.gurunars.crud_item_list.example

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.gurunars.crud_item_list.CrudItemListView
import com.gurunars.crud_item_list.crudItemListView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.SelectableItem
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


internal fun bindAnimalItem(
    context: Context,
    itemType: Enum<*>,
    payload: BindableField<Pair<SelectableItem<AnimalItem>, SelectableItem<AnimalItem>?>>
) = TextView(context).apply {
    asRow()
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

class ActivityMain : Activity() {

    private val storage = PersistentStorage(this, "main")

    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var crudItemListView: CrudItemListView<AnimalItem>

    private fun add(type: AnimalItem.Type) {
        items.set(items.get() + AnimalItem(count.get().toLong(), 0, type))
        count.set(count.get() + 1)
    }

    private fun initData() {
        items.set(listOf())
        count.set(0)
        addItems()
    }

    private fun addItems(many:Boolean=false) {
        val limit = if (many) 19 else 0

        val newList = mutableListOf<AnimalItem>()

        for (i in 0..limit) {
            newList.apply {
                add(AnimalItem(i.toLong(), 0, AnimalItem.Type.LION))
                add(AnimalItem(i.toLong() + 1, 0, AnimalItem.Type.TIGER))
                add(AnimalItem(i.toLong() + 2, 0, AnimalItem.Type.MONKEY))
                add(AnimalItem(i.toLong() + 3, 0, AnimalItem.Type.WOLF))
            }
        }

        items.set(newList)
        count.set((limit + 1) * 4)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage.load()

        crudItemListView = crudItemListView(
            ::bindAnimalItem,
            {
                item -> this@ActivityMain.items.set(
                    items.get().toMutableList().apply {
                        val index = indexOfFirst { item.getId() == it.getId() }
                        set(index, item.copy(version = item.version+1))
                    }
                )
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
                                dismiss()
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
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        when (i) {
            R.id.leftHanded -> crudItemListView.isLeftHanded.set(true)
            R.id.rightHanded -> crudItemListView.isLeftHanded.set(false)
            R.id.reset -> initData()
            R.id.lock -> {
                setTitle(R.string.unsortable)
                crudItemListView.isSortable.set(false)
            }
            R.id.unlock -> {
                setTitle(R.string.sortable)
                crudItemListView.isSortable.set(true)
            }
            R.id.addMany -> addItems(true)
        }
        return super.onOptionsItemSelected(item)
    }

}