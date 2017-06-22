package com.gurunars.crud_item_list.example

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.gurunars.crud_item_list.CrudItemList
import com.gurunars.crud_item_list.crudItemList
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.SelectableItemViewBinder
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


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

        for (i in 0..limit) {
            add(AnimalItem.Type.LION)
            add(AnimalItem.Type.TIGER)
            add(AnimalItem.Type.MONKEY)
            add(AnimalItem.Type.WOLF)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.crudItemList = crudItemList({ AnimalBinder() }) {
            fullSize()
            id=R.id.customView
            actionIcon.set(CrudItemList.IconColorBundle(
                fgColor=R.color.Yellow,
                bgColor=R.color.Blue
            ))
            contextualIcon.set(CrudItemList.IconColorBundle(
                bgColor=R.color.Black,
                fgColor=R.color.White
            ))
            createCloseIcon.set(CrudItemList.IconColorBundle(
                bgColor=R.color.Red,
                fgColor=R.color.White
            ))
            openIcon.set(CrudItemList.IconColorBundle(
                bgColor=R.color.Green,
                fgColor=R.color.Yellow
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
            itemEditListener.set({ item -> this@ActivityMain.items.set(
                items.get().toMutableList().apply {
                    val id = indexOfFirst { item.getId() == it.getId() }
                    set(id, item.copy(version = item.version+1))
                })
            })
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
            R.id.leftHanded -> crudItemList.isLeftHanded.set(true)
            R.id.rightHanded -> crudItemList.isLeftHanded.set(false)
            R.id.reset -> initData()
            R.id.lock -> {
                setTitle(R.string.unsortable)
                crudItemList.isSortable.set(false)
            }
            R.id.unlock -> {
                setTitle(R.string.sortable)
                crudItemList.isSortable.set(true)
            }
            R.id.addMany -> addItems(true)
        }
        return super.onOptionsItemSelected(item)
    }

}