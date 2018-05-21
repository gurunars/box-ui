package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.*
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.Box
import com.gurunars.box.core.IBox
import com.gurunars.box.core.bind
import com.gurunars.box.ui.*
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.ERROR
import com.gurunars.crud_item_list.ItemTypeDescriptor.Status.Type.WARNING

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
                fullSize()
            }
        }.layout(this) {
            fullSize()
            margin=Bounds(dip(12)).copy(
                bottom = dip(90)
            )
        }

        val status = itemBox.bind { formBinder.validate(this) }

        iconView(
            icon = Box(confirmIconColors.icon(R.drawable.ic_check)),
            enabled = status.bind { !type.isBlocking }
        ).layout(this) {
            id = R.id.save
            setOnClickListener { confirmationHandler(itemBox.get()) }
            width = dip(60)
            height = dip(60)
            margin = Bounds(dip(16))
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
        }

        val statusIcon = Box(Icon(
            bgColor = Color.LTGRAY,
            fgColor = Color.BLACK,
            icon = R.drawable.ic_warning_sign
        ))

        iconView(
            icon = statusIcon
        ).layout(this) {
            id = R.id.hint

            onClick { Toast.makeText(context, status.get().message, Toast.LENGTH_LONG).show() }

            status.onChange {
                when (it.type) {
                    ERROR -> statusIcon.patch { copy(bgColor = Color.RED) }
                    WARNING -> statusIcon.patch { copy(bgColor = Color.YELLOW) }
                    else -> statusIcon.patch { copy(bgColor = Color.LTGRAY) }
                }
                setIsVisible(it.type.isBlocking || it.message.isNotEmpty())
            }

            width = dip(35)
            height = dip(35)
            margin = Bounds(left=dip(100), bottom=dip(30))
            alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            alignWithRespectTo(R.id.confirm, horizontalPosition = HorizontalPosition.RIGHT_OF)
        }
    }
