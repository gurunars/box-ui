package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.gurunars.android_utils.Icon
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.Service
import com.gurunars.crud_item_list.ClipboardSerializer
import com.gurunars.crud_item_list.IconColorBundle
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.crud_item_list.crudItemListView
import com.gurunars.crud_item_list.oneOf
import com.gurunars.databinding.IBox
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.box
import com.gurunars.databinding.branch
import com.gurunars.databinding.patch
import com.gurunars.item_list.SelectableItem
import com.gurunars.item_list.coloredRowSelectionDecorator
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class AnimalItemSerializer : ClipboardSerializer<AnimalItem> {

    override fun fromString(source: String): List<AnimalItem> =
        source.split("\n").map {
            val parts = it.split("@")
            AnimalItem(
                id = 0L,
                type = AnimalItem.Type.valueOf(parts[0]),
                version = parts[1].toInt()
            )
        }

    override fun toString(source: List<AnimalItem>): String =
        source.joinToString("\n") { "${it.type}@${it.version}" }
}

class Descriptor(
    private val context: Context,
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

    override fun bindRow(field: IBox<SelectableItem<AnimalItem>>, triggerEdit: () -> Unit): View = coloredRowSelectionDecorator(field) {
        TextView(context).apply {
            padding = context.dip(5)
            txt(field.branch { item.toString() })
        }
    }

    override val icon = Icon(icon = iconId)
    override fun createNewItem() = AnimalItem(
        id = 0L,
        version = 0,
        type = type)

    override fun bindForm(
        field: IBox<AnimalItem>
    ) = context.verticalLayout {
        fullSize()
        textView {
            text = context.getString(R.string.newVersion)
        }
        editText {
            id = R.id.versionValue
            inputType = InputType.TYPE_CLASS_NUMBER
            txt(field.branch(
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

    private val srv = Service.getRealService(this)
    private val items = srv.items

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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        isSortable.onChange { reset() }
    }

    private fun addItems(
        count: Int
    ) {
        srv.items.patch {
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

        val descriptors: List<List<ItemTypeDescriptor<AnimalItem>>>

        if (sortable) {
            descriptors = listOf(listOf(
                Descriptor(
                    this,
                    R.drawable.ic_menu_monkey,
                    AnimalItem.Type.MONKEY),
                Descriptor(
                    this,
                    R.drawable.ic_menu_lion,
                    AnimalItem.Type.LION)
            ), listOf(
                Descriptor(
                    this,
                    R.drawable.ic_menu_tiger,
                    AnimalItem.Type.TIGER),
                Descriptor(
                    this,
                    R.drawable.ic_menu_wolf,
                    AnimalItem.Type.WOLF)
            ))
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
                groupedItemTypeDescriptors = descriptors,
                sortable = sortable,
                actionIconColors = IconColorBundle(
                    fgColor = Color.YELLOW,
                    bgColor = Color.BLUE
                ),
                clipboardSerializer = AnimalItemSerializer()
            ).setAsOne(this)
        }.setAsOne(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            R.id.lock -> isSortable.set(false, true)
            R.id.unlock -> isSortable.set(true, true)
            R.id.addMany -> addItems(4 * 20)
        }
        return super.onOptionsItemSelected(item)
    }
}
