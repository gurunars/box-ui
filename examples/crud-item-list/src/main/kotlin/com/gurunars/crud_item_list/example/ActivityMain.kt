package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.gurunars.android_utils.IconView
import com.gurunars.animal_item.AnimalItem
import com.gurunars.crud_item_list.CrudItemListView
import com.gurunars.crud_item_list.IconColorBundle
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.crud_item_list.crudItemListView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bind
import com.gurunars.item_list.coloredRowSelectionDecorator
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

internal fun Context.bindAnimalItem(
    itemType: Enum<*>,
    payload: BindableField<AnimalItem>
) = TextView(this).apply {
    padding = context.dip(5)
    payload.onChange { text = it.toString() }
}

internal fun Context.animalFormRenderer(
    field: BindableField<AnimalItem>
) = verticalLayout {
    fullSize()
    textView {
        text = getString(R.string.newVersion)
    }
    editText {
        id=R.id.versionValue
        inputType=InputType.TYPE_CLASS_NUMBER
        bind(field, object : BindableField.ValueTransformer<AnimalItem, String> {
            override fun forward(value: AnimalItem) = value.version.toString()
            override fun backward(value: String) = field.get().copy(
                version = if (value.isEmpty()) 0 else value.toInt()
            )
        })
    }
    button {
        id=R.id.increment
        text = getString(R.string.increment)
        setOnClickListener {
            field.apply {
                set(get().copy(version = get().version + 1))
            }
        }
    }
    gravity = Gravity.CENTER
    backgroundColor = color(R.color.White)
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

        fun descriptor(icon: Int, type: AnimalItem.Type) =
            ItemTypeDescriptor(
                icon = IconView.Icon(icon = icon),
                type = type,
                rowBinder = coloredRowSelectionDecorator(Context::bindAnimalItem),
                formBinder = Context::animalFormRenderer,
                newItemCreator = { AnimalItem(id=(count.get() + 1).toLong(), version= 0, type = type) },
                canSave = { true }
            )

        crudItemListView = crudItemListView(
            {
                TextView(this@crudItemListView).apply {
                    id=R.id.noItemsLabel
                    fullSize()
                    setText(R.string.noItemsAtAll)
                    gravity = Gravity.CENTER
                }
            },
            listOf(listOf(
                descriptor(R.drawable.ic_menu_monkey, AnimalItem.Type.MONKEY),
                descriptor(R.drawable.ic_menu_lion, AnimalItem.Type.LION)
            ), listOf(
                descriptor(R.drawable.ic_menu_tiger, AnimalItem.Type.TIGER),
                descriptor(R.drawable.ic_menu_wolf, AnimalItem.Type.WOLF)
            ))
        ) {
            fullSize()
            id=R.id.customView
            this@ActivityMain.items.bind(items)
            items.onChange {
                count.set(
                    items.get()
                        .map { it.id }
                        .fold(0L) { acc, l -> Math.max(acc, l)  }
                        .toInt()
                )
            }
            this@ActivityMain.isSortable.bind(isSortable)
            this@ActivityMain.isLeftHanded.bind(isLeftHanded)

            listActionColors.set(IconColorBundle(
                fgColor=color(R.color.Yellow),
                bgColor=color(R.color.Blue)
            ))
            confirmationActionColors.set(IconColorBundle(
                bgColor=color(R.color.Black),
                fgColor=color(R.color.White)
            ))
            cancelActionColors.set(IconColorBundle(
                bgColor=color(R.color.Red),
                fgColor=color(R.color.White)
            ))
            openIcon.set(IconColorBundle(
                bgColor=color(R.color.Green),
                fgColor=color(R.color.Yellow)
            ))
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
