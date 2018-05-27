package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.patch
import com.gurunars.box.ui.*
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.ERROR
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.WARNING
import com.gurunars.item_list.Item

internal fun <ItemType : Item> Context.itemForm(
    itemBox: IBox<ItemType>,
    confirmationHandler: (item: ItemType) -> Unit,
    confirmIconColors: IconColorBundle,
    formBinder: ItemTypeDescriptor<ItemType>
) = with<RelativeLayout> {
        val bound = formBinder.bindForm(itemBox)

        with<LinearLayout> {
            orientation = LinearLayout.VERTICAL

            with<LinearLayout> {
                with<ImageView> {
                    setImageDrawable(
                        context.getDrawable(formBinder.icon.icon)!!.apply {
                            mutate()
                            setColorFilter(formBinder.icon.fgColor, PorterDuff.Mode.SRC_IN)
                        }
                    )
                    padding = Bounds(dip(1))
                }.layout(this) {
                    width = dip(25)
                    height = dip(25)
                    margin = Bounds(right=dip(6))
                }
                with<TextView> {
                    text(formBinder.getItemTitle(itemBox))
                    setTextColor(formBinder.icon.fgColor)
                    textSize = 20f
                }
                setBackgroundColor(formBinder.icon.bgColor)
                padding = Bounds(dip(5))
            }.layout(this) {
                asRow()
            }

            bound.layout(this) {
                padding = Bounds(dip(10))
            }.layout(this) {
                fullSize()
            }
        }.layout(this) {
            fullSize()
            margin=Bounds(dip(12)).copy(
                bottom = dip(90)
            )
        }

        val canSave = false.box

        iconView(
            icon = confirmIconColors.icon(R.drawable.ic_check).box,
            enabled = canSave
        ).layout(this) {
            id = R.id.confirm
            id = R.id.save
            itemBox.onChange { it ->
                asyncChain(
                    { formBinder.validate(it).type.isBlocking },
                    { canSave.set(!it) }
                )
            }
            setOnClickListener { confirmationHandler(itemBox.get()) }
            width = dip(60)
            height = dip(60)
            margin = Bounds(dip(16))
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
        }

        val statusIcon = Icon(
            bgColor = Color.LTGRAY,
            fgColor = Color.BLACK,
            icon = R.drawable.ic_warning_sign
        ).box

        context.iconView(
                icon = statusIcon
        ).layout(this) {
            id = R.id.hint
            itemBox.onChange { it ->
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
            width = dip(35)
            height = dip(35)
            margin = Bounds(left=dip(100), bottom=dip(30))
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.confirm, horizontalPosition = HorizontalPosition.RIGHT_OF)
        }
    }
