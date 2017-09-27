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
import com.gurunars.crud_item_list.*
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bind
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

internal fun Context.bindAnimalItem(
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
        id = R.id.versionValue
        inputType = InputType.TYPE_CLASS_NUMBER
        bind(field, object : BindableField.ValueTransformer<AnimalItem, String> {
            override fun forward(value: AnimalItem) = value.version.toString()
            override fun backward(value: String) = field.get().copy(
                version = if (value.isEmpty()) 0 else value.toInt()
            )
        })
    }
    button {
        id = R.id.increment
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
    private val isSortable = storage.storageField("isSortable", true)
    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var crudItemListView: CrudItemListView<AnimalItem>

    private fun getType(i: Int, sortable: Boolean): AnimalItem.Type {
        if (sortable) {
            return when (i % 4) {
                0 -> AnimalItem.Type.LION
                1 -> AnimalItem.Type.TIGER
                2 -> AnimalItem.Type.MONKEY
                else -> AnimalItem.Type.WOLF
            }
        } else {
            return AnimalItem.Type.MONKEY
        }
    }

    private fun addItems(
        count: Int,
        sortable: Boolean,
        nullify: Boolean = false
    ) {
        val curCount = (if (nullify) 0 else this.count.get())
        items.set((if (nullify) listOf() else items.get()) + (0..count - 1).map {
            AnimalItem(curCount + it.toLong(), getType(it, sortable), 0)
        })
        this.count.set(curCount + count)
    }

    fun initView(sortable: Boolean) {
        setTitle(if (sortable) R.string.sortable else R.string.unsortable)

        fun descriptor(icon: Int, type: AnimalItem.Type) =
            ItemTypeDescriptor(
                icon = IconView.Icon(icon = icon),
                type = type,
                rowBinder = Context::bindAnimalItem,
                formBinder = Context::animalFormRenderer,
                newItemCreator = { AnimalItem(id = (count.get() + 1).toLong(), version = 0, type = type) },
                canSave = { true }
            )

        val descriptors: List<List<ItemTypeDescriptor<AnimalItem>>>

        if (sortable) {
            descriptors = listOf(listOf(
                descriptor(R.drawable.ic_menu_monkey, AnimalItem.Type.MONKEY),
                descriptor(R.drawable.ic_menu_lion, AnimalItem.Type.LION)
            ), listOf(
                descriptor(R.drawable.ic_menu_tiger, AnimalItem.Type.TIGER),
                descriptor(R.drawable.ic_menu_wolf, AnimalItem.Type.WOLF)
            ))
        } else {
            descriptors = descriptor(R.drawable.ic_menu_monkey, AnimalItem.Type.MONKEY).oneOf()
        }

        crudItemListView = crudItemListView(
            {
                TextView(this@crudItemListView).apply {
                    id = R.id.noItemsLabel
                    fullSize()
                    setText(R.string.noItemsAtAll)
                    gravity = Gravity.CENTER
                }
            },
            sortable,
            descriptors
        ) {
            fullSize()
            id = R.id.customView
            this@ActivityMain.items.bind(items)
            items.onChange {
                count.set(
                    items.get()
                        .map { it.id }
                        .fold(0L) { acc, l -> Math.max(acc, l) }
                        .toInt()
                )
            }
            this@ActivityMain.isLeftHanded.bind(isLeftHanded)

            listActionColors.set(IconColorBundle(
                fgColor = color(R.color.Yellow),
                bgColor = color(R.color.Blue)
            ))
            confirmationActionColors.set(IconColorBundle(
                bgColor = color(R.color.Black),
                fgColor = color(R.color.White)
            ))
            cancelActionColors.set(IconColorBundle(
                bgColor = color(R.color.Red),
                fgColor = color(R.color.White)
            ))
            openIcon.set(IconColorBundle(
                bgColor = color(R.color.Green),
                fgColor = color(R.color.Yellow)
            ))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage.load()
        isSortable.onChange(listener = this::initView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId

        fun setSortable(flag: Boolean) {
            addItems(4, flag, true)
            isSortable.set(flag, true)
        }

        when (i) {
            R.id.leftHanded -> isLeftHanded.set(true)
            R.id.rightHanded -> isLeftHanded.set(false)
            R.id.reset -> addItems(4, isSortable.get(), true)
            R.id.lock -> setSortable(false)
            R.id.unlock -> setSortable(true)
            R.id.addMany -> addItems(4 * 20, isSortable.get())
        }
        return super.onOptionsItemSelected(item)
    }

}
