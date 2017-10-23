package com.gurunars.crud_item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.backgroundColor
import com.gurunars.databinding.android.isVisible
import com.gurunars.databinding.android.txt
import com.gurunars.databinding.childField
import com.gurunars.item_list.Item
import com.gurunars.shortcuts.add
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import org.jetbrains.anko.*

fun <ItemType : Item> Context.renderFormWithStatus(
    validate: (item: ItemType) -> Status,
    field: BindableField<ItemType>,
    formBinder: Context.() -> View
) = verticalLayout {
    fullSize()
    backgroundColor = Color.WHITE
    val status = BindableField(Status.ok())
    field.onChange {
        doAsync {
            val value = validate(it)
            uiThread {
                status.set(value)
            }
        }
    }
    formBinder().add(this).lparams {
        weight = 1f
        width = matchParent
    }
    textView {
        txt(status.childField { message })
        isVisible(status.childField { message.isNotEmpty() })
        asRow()
        backgroundColor(status.childField {
            when (type) {
                Status.Type.ERROR -> Color.RED
                Status.Type.WARNING -> Color.YELLOW
                else -> Color.TRANSPARENT
            }
        })
    }
}
