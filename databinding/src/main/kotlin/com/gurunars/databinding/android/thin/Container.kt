package com.gurunars.databinding.android.thin

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.Field

abstract class Container(
    private val component: Component
) : Component {

    private val fields = mutableListOf<BindableField<*>>()

    val width: Field<Size> = bindableField(Size.WRAP_CONTENT)
    val height: Field<Size> = bindableField(Size.WRAP_CONTENT)
    val visible: Field<Boolean> = bindableField(true)

    class Size(val value: Int) {
        companion object {
            val MATCH_PARENT = Size(ViewGroup.LayoutParams.MATCH_PARENT)
            val WRAP_CONTENT = Size(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun <ItemType> bindableField(defaultValue: ItemType) = BindableField(defaultValue).apply {
        fields.add(this)
    }

    override fun render(context: Context) = with(component) {
        width as BindableField
        height as BindableField
        visible as BindableField

        render(context).apply {
            this@Container.width.onChange {
                layoutParams.width = it.value
            }
            this@Container.height.onChange {
                layoutParams.height = it.value
            }
            this@Container.visible.onChange {
                visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

}