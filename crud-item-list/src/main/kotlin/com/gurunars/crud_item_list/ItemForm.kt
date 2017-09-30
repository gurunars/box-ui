package com.gurunars.crud_item_list

import android.content.Context
import android.util.Log
import android.widget.RelativeLayout
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.bindableField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.HorizontalAlignment
import com.gurunars.shortcuts.VerticalAlignment
import com.gurunars.shortcuts.alignInParent
import org.jetbrains.anko.dip
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.margin
import org.jetbrains.anko.uiThread

internal class ItemForm<ItemType : Item>(
    context: Context,
    private val itemInEdit: BindableField<ItemType?>,
    private val confirmationHandler: () -> Unit,
    private val canSave: (item: ItemType) -> Boolean,
    private val confirmIconColors: BindableField<IconColorBundle>
) : RelativeLayout(context) {

    fun bind(
        item: ItemType,
        formBinder: ItemFormBinder<ItemType>
    ) {
        val field = bindableField(item)
        field.onChange { itemInEdit.set(it) }
        removeAllViews()

        addView(
            formBinder(context, field).apply {
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
