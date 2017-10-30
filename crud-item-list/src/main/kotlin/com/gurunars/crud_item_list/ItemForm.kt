package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.onClick
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.ERROR
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.WARNING
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.*
import com.gurunars.databinding.patch
import com.gurunars.item_list.Item
import org.jetbrains.anko.*

class ItemForm<ItemType : Item>(
    initialItem: ItemType,
    private val bindForm: (field: BindableField<ItemType>) -> Component,
    private val validate: (item: ItemType) -> ItemTypeDescriptor.Status,
    private val confirmationHandler: () -> Unit,
    private val confirmIconColors: BindableField<IconColorBundle>
) : Component {

    private val field = BindableField(initialItem)

    override fun Context.render() = relativeLayout {

        bindForm(field).add(this) {
            layoutParams = relativeLayoutParams {
                fullSize()
                leftMargin = dip(12)
                rightMargin = dip(12)
                topMargin = dip(12)
                bottomMargin = dip(90)
            }
        }

        IconView(this@render).add(this) {
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
                doAsync {
                    val can = !validate(it).type.isBlocking
                    uiThread {
                        enabled.set(can)
                    }
                }
            }
            setOnClickListener { confirmationHandler() }
            layoutParams = relativeLayoutParams {
                width = dip(60)
                width = dip(60)
                margin = dip(16)
                alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            }
        }

        IconView(this@render).add(this) {
            id = R.id.hint
            icon.set(IconView.Icon(
                bgColor = Color.LTGRAY,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_warning_sign
            ))

            field.onChange {
                doAsync {
                    val status = validate(it)

                    onClick { longToast(status.message) }

                    uiThread {
                        when (status.type) {
                            ERROR -> icon.patch { copy(bgColor = Color.RED) }
                            WARNING -> icon.patch { copy(bgColor = Color.YELLOW) }
                            else -> icon.patch { copy(bgColor = Color.LTGRAY) }
                        }
                        setIsVisible(status.type.isBlocking || status.message.isNotEmpty())
                    }
                }
            }
            layoutParams = relativeLayoutParams {
                width = dip(35)
                width = dip(35)
                leftMargin = dip(100)
                bottomMargin = dip(30)
                alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
                rightOf(R.id.confirm)
            }
        }

    }

}
