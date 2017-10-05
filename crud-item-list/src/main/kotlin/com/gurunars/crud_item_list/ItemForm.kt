package com.gurunars.crud_item_list

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.HorizontalAlignment
import com.gurunars.shortcuts.VerticalAlignment
import com.gurunars.shortcuts.alignInParent
import org.jetbrains.anko.dip
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.margin
import org.jetbrains.anko.uiThread

@SuppressLint("ViewConstructor")
internal class ItemForm<ItemType : Item>(
    context: Context,
    private val itemInEdit: BindableField<ItemType?>,
    private val confirmationHandler: () -> Unit,
    private val canSave: (item: ItemType) -> Boolean,
    private val confirmIconColors: BindableField<IconColorBundle>
) : RelativeLayout(context) {

    fun bind(
        item: ItemType,
        formBinder: ItemTypeDescriptor<ItemType>
    ) {
        val field = BindableField(item)
        field.onChange { itemInEdit.set(it) }
        removeAllViews()

        addView(
            formBinder.bindForm(context, field).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                ).apply {
                    leftMargin = dip(12)
                    rightMargin = dip(12)
                    topMargin = dip(12)
                    bottomMargin = dip(90)
                }
            }
        )

        addView(IconView(context).apply {
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
                    val can = canSave(it)
                    uiThread {
                        enabled.set(can)
                    }
                }
            }
            setOnClickListener { confirmationHandler() }
            layoutParams = RelativeLayout.LayoutParams(
                context.dip(60),
                context.dip(60)
            ).apply {
                margin = dip(16)
                alignInParent(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM)
            }
        })

    }

}
