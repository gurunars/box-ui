package com.gurunars.databinding.android

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField

/**
 * A base component meant to develop custom stateful UI widgets using bindable fields
 */
open class StatefulComponent(context: Context) : FrameLayout(context) {

    private val fields: MutableList<BindableField<*>> = mutableListOf()

    /**
     * Call with multiple fields to notify the component that the state of the fields should
     * be persisted.
     */
    fun retain(vararg fields: BindableField<*>) = this.fields.addAll(fields)

    companion object {

        private fun <T> BindableField<T>.read(map: HashMap<Int, Any?>, index: Int) {
            @Suppress("UNCHECKED_CAST")
            val obtained = map[index] as T
            this.set(obtained)
        }

    }

    /**
     * @suppress
     */
    final override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        val payload = hashMapOf<Int, Any?>()
        fields.forEachIndexed { index, bindableField ->
            payload.put(index, bindableField.get())
        }
        putSerializable("payload", payload)
    }

    /**
     * @suppress
     */
    final override fun onRestoreInstanceState(state: Parcelable) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
            val payload = getSerializable("payload") ?: return
            @Suppress("UNCHECKED_CAST")
            payload as HashMap<Int, Any?>
            fields.forEachIndexed { index, bindableField ->
                bindableField.read(payload, index)
            }
        }
    }

}

fun Context.statefulComponent(
    id: Int,
    vararg fields: BindableField<*>
): StatefulComponent =
    StatefulComponent(this).apply {
        this.id = id
        retain(*fields)
    }

fun ViewGroup.statefulComponent(
    id: Int,
    vararg fields: BindableField<*>
): StatefulComponent = context.statefulComponent(id, *fields).apply {
    addView(this)
}