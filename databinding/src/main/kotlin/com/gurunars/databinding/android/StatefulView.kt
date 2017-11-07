package com.gurunars.databinding.android

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField

/**
 * A base component meant to develop custom stateful UI widgets using bindable fields
 */
class StatefulView(context: Context) : FrameLayout(context) {

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
    override fun onSaveInstanceState() = Bundle().apply {
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
    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            state.apply {
                super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
                val payload = getSerializable("payload") ?: return
                @Suppress("UNCHECKED_CAST")
                payload as HashMap<Int, Any?>
                fields.forEachIndexed { index, bindableField ->
                    bindableField.read(payload, index)
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

}

fun Context.statefulView(
    id: Int,
    tag: String = "DEFAULT",
    vararg fields: BindableField<*>,
    init: StatefulView.() -> Unit = {}
): StatefulView =
    StatefulView(this).apply {
        this.id = id
        this.tag = tag
        retain(*fields)
    }.apply { init() }
