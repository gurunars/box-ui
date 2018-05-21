package com.gurunars.crud_item_list.example

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.animal_item.AnimalItem
import com.gurunars.animal_item.bindAnimal
import com.gurunars.crud_item_list.ItemTypeDescriptor
import com.gurunars.box.core.IBox
import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import com.gurunars.box.core.patch
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.text
import com.gurunars.item_list.WithSelection
import com.gurunars.item_list.coloredRowSelectionDecorator
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.button
import org.jetbrains.anko.editText
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

internal class Descriptor(
    private val context: Context,
    iconId: Int,
    override val type: AnimalItem.Type
) : ItemTypeDescriptor<AnimalItem> {

    override fun getItemTitle(item: IRoBox<AnimalItem>) =
        item.bind { "${type.name} $version" }

    override val title = type.name

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

    override fun bindRow(field: IRoBox<WithSelection<AnimalItem>>): View =
        context.bindAnimal(field.bind { item }).coloredRowSelectionDecorator( field.bind { isSelected })

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
            text(field.bind(
                { version.toString() },
                { copy(version = if (it.isEmpty()) 0 else it.toInt()) }
            ))
        }
        button {
            id = R.id.increment
            text = context.getString(R.string.increment)
            setOnClickListener {
                field.patch {
                    copy(version = version+1)
                }
            }
        }
        gravity = Gravity.CENTER
        backgroundColor = Color.WHITE
    }
}