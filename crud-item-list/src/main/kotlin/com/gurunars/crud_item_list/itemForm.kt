package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.ERROR
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.WARNING
import com.gurunars.databinding.android.*
import com.gurunars.databinding.field
import com.gurunars.databinding.patch
import com.gurunars.item_list.Item
import org.jetbrains.anko.*

fun <ItemType : Item> Context.itemForm(
    item: ItemType,
    confirmationHandler: (item: ItemType) -> Unit,
    confirmIconColors: IconColorBundle,
    formBinder: ItemTypeDescriptor<ItemType>
) = relativeLayout {
    val field = item.field

    val bound = formBinder.bindForm(field)

    bound.add(this) {
        layoutParams = relativeLayoutParams {
            fullSize()
            leftMargin = dip(12)
            rightMargin = dip(12)
            topMargin = dip(12)
            bottomMargin = dip(90)
        }
    }

    val canSave = false.field

    context.iconView(
        icon = confirmIconColors.icon(R.drawable.ic_check).field,
        enabled = canSave
    ).add(this) {
        id = R.id.confirm
        id = R.id.save
        field.onChange { it ->
            asyncChain(
                { formBinder.validate(it).type.isBlocking },
                { canSave.set(!it) }
            )
        }
        setOnClickListener { confirmationHandler(field.get()) }
        layoutParams = relativeLayoutParams {
            width = dip(60)
            height = dip(60)
            margin = dip(16)
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
        }
    }

    val statusIcon = Icon(
        bgColor = Color.LTGRAY,
        fgColor = Color.BLACK,
        icon = R.drawable.ic_warning_sign
    ).field

    context.iconView(
        icon = statusIcon
    ).add(this) {
        id = R.id.hint
        field.onChange { it ->
            asyncChain(
                {
                    val status = formBinder.validate(it)
                    onClick { context.longToast(status.message) }
                    status
                },
                {
                    when (it.type) {
                        ERROR -> statusIcon.patch { copy(bgColor = Color.RED) }
                        WARNING -> statusIcon.patch { copy(bgColor = Color.YELLOW) }
                        else -> statusIcon.patch { copy(bgColor = Color.LTGRAY) }
                    }
                    setIsVisible(it.type.isBlocking || it.message.isNotEmpty())
                }
            )
        }
        layoutParams = relativeLayoutParams {
            width = dip(35)
            height = dip(35)
            leftMargin = dip(100)
            bottomMargin = dip(30)
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            rightOf(R.id.confirm)
        }
    }
}
