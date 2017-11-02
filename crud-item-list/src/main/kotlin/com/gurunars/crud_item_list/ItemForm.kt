package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.ERROR
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.WARNING
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.patch
import com.gurunars.item_list.Item
import org.jetbrains.anko.*

@SuppressLint("ViewConstructor")
internal class ItemForm<ItemType : Item>(
    context: Context,
    private val itemInEdit: BindableField<ItemType?>,
    private val confirmationHandler: () -> Unit,
    private val confirmIconColors: BindableField<IconColorBundle>
) : RelativeLayout(context) {

    private fun bindField(
        field: BindableField<ItemType>,
        bound: View,
        formBinder: ItemTypeDescriptor<ItemType>
    ) {
        bound.add(this) {
            layoutParams = relativeLayoutParams {
                fullSize()
                leftMargin = dip(12)
                rightMargin = dip(12)
                topMargin = dip(12)
                bottomMargin = dip(90)
            }
        }

        IconView(context).add(this) {
            id = R.id.confirm
            confirmIconColors.onChange {
                icon.set(
                    IconView.Icon(
                        bgColor = it.bgColor,
                        fgColor = it.fgColor,
                        icon = R.drawable.ic_check
                    )
                )
            }
            id = R.id.save
            field.onChange {
                asyncChain(
                    { formBinder.validate(it).type.isBlocking },
                    { enabled.set(!it) }
                )
            }
            setOnClickListener { confirmationHandler() }
            layoutParams = relativeLayoutParams {
                width = dip(60)
                height = dip(60)
                margin = dip(16)
                alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            }
        }

        IconView(context).add(this) {
            id = R.id.hint
            icon.set(IconView.Icon(
                bgColor = Color.LTGRAY,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_warning_sign
            ))

            field.onChange {
                asyncChain(
                    {
                        val status = formBinder.validate(it)
                        onClick { context.longToast(status.message) }
                        status
                    },
                    {
                        when (it.type) {
                            ERROR -> icon.patch { copy(bgColor = Color.RED) }
                            WARNING -> icon.patch { copy(bgColor = Color.YELLOW) }
                            else -> icon.patch { copy(bgColor = Color.LTGRAY) }
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

    fun bind(
        item: ItemType,
        formBinder: ItemTypeDescriptor<ItemType>
    ) {
        val field = BindableField(item)
        field.onChange { itemInEdit.set(it) }
        removeAllViews()
        bindField(field, formBinder.bindForm(field), formBinder)
    }
}
