package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.closeKeyboard
import com.gurunars.animal_item.AnimalItem
import com.gurunars.crud_item_list.CrudItemListView
import com.gurunars.crud_item_list.IconColorBundle
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.crud_item_list.oneOf
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.childField
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setAsOne
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

class Descriptor(
    private val context: Context,
    private val count: BindableField<Int>,
    iconId: Int,
    override val type: AnimalItem.Type
) : ItemTypeDescriptor<AnimalItem> {

    override fun validate(item: AnimalItem): ItemTypeDescriptor.Status =
        when {
            item.version == 0 ->
                ItemTypeDescriptor.Status.error(context.getString(R.string.isZero))
            item.version % 7 == 0 ->
                ItemTypeDescriptor.Status.error(context.getString(R.string.isSeven))
            item.version % 2 == 0 ->
                ItemTypeDescriptor.Status.warning(context.getString(R.string.isEven))
            item.version % 3 == 0 ->
                ItemTypeDescriptor.Status.info(context.getString(R.string.isOk))
            else ->
                ItemTypeDescriptor.Status.ok()
        }

    override fun bind(field: BindableField<AnimalItem>) = TextView(context).apply {
        padding = context.dip(5)
        txt(field.childField { toString() })
    }

    override val icon = IconView.Icon(icon = iconId)
    override fun createNewItem() = AnimalItem(
        id = (count.get() + 1).toLong(),
        version = 0,
        type = type)

    override fun bindForm(
        field: BindableField<AnimalItem>
    ) = context.verticalLayout {
        fullSize()
        textView {
            text = context.getString(R.string.newVersion)
        }
        editText {
            id = R.id.versionValue
            inputType = InputType.TYPE_CLASS_NUMBER
            txt(field.childField(
                { version.toString() },
                { copy(version = if (it.isEmpty()) 0 else it.toInt()) }
            ))
        }
        button {
            id = R.id.increment
            text = context.getString(R.string.increment)
            setOnClickListener {
                field.apply {
                    set(get().copy(version = get().version + 1))
                }
            }
        }
        gravity = Gravity.CENTER
        backgroundColor = Color.WHITE
    }

}

class ActivityMain : Activity() {

    private val storage = PersistentStorage(this, "main")

    private val isLeftHanded = storage.storageField("isLeftHanded", false)
    private val isSortable = storage.storageField("isSortable", true)
    private val items = storage.storageField("items", listOf<AnimalItem>())
    private val count = storage.storageField("count", 0)

    private lateinit var crudItemListView: CrudItemListView<AnimalItem>

    private fun getType(i: Int, sortable: Boolean): AnimalItem.Type {
        return if (sortable) {
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
        count: Int,
        sortable: Boolean,
        nullify: Boolean = false
    ) {
        val curCount = (if (nullify) 0 else this.count.get())
        items.set((if (nullify) listOf() else items.get()) + (0 until count).map {
            AnimalItem(curCount + it.toLong(), getType(it, sortable), 0)
        })
        this.count.set(curCount + count)
    }

    private fun initView(sortable: Boolean) {
        setTitle(if (sortable) R.string.sortable else R.string.unsortable)

        val descriptors: List<List<ItemTypeDescriptor<AnimalItem>>>

        if (sortable) {
            descriptors = listOf(listOf(
                Descriptor(
                    this,
                    count,
                    R.drawable.ic_menu_monkey,
                    AnimalItem.Type.MONKEY),
                Descriptor(
                    this,
                    count,
                    R.drawable.ic_menu_lion,
                    AnimalItem.Type.LION)
            ), listOf(
                Descriptor(
                    this,
                    count,
                    R.drawable.ic_menu_tiger,
                    AnimalItem.Type.TIGER),
                Descriptor(
                    this,
                    count,
                    R.drawable.ic_menu_wolf,
                    AnimalItem.Type.WOLF)
            ))
        } else {
            descriptors = Descriptor(
                this,
                count,
                R.drawable.ic_menu_monkey,
                AnimalItem.Type.MONKEY).oneOf()
        }

        crudItemListView = CrudItemListView(
            this,
            sortable = sortable,
            groupedItemTypeDescriptors = descriptors
        ).setAsOne(this) {
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

            isOpen.onChange {
                closeKeyboard()
            }

            listActionColors.set(IconColorBundle(
                fgColor = Color.YELLOW,
                bgColor = Color.BLUE
            ))
            confirmationActionColors.set(IconColorBundle(
                bgColor = Color.BLACK,
                fgColor = Color.GREEN
            ))
            cancelActionColors.set(IconColorBundle(
                bgColor = Color.RED,
                fgColor = Color.WHITE
            ))
            openIcon.set(IconColorBundle(
                bgColor = Color.GREEN,
                fgColor = Color.YELLOW
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
