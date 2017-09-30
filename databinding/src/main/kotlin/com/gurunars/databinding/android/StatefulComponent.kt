package com.gurunars.databinding.android

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField

abstract class StatefulComponent(context: Context) : FrameLayout(context) {

    private val fields: MutableList<BindableField<*>> = mutableListOf()

    fun retain(vararg fields: BindableField<*>) = this.fields.addAll(fields)

    companion object {

        private inline fun <T> BindableField<T>.read(map: HashMap<Int, Any?>, index: Int) {
            val obtained = map.get(index) as T
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
            val payload = getSerializable("payload")
            if (payload == null) {
                return
            }
            payload as HashMap<Int, Any?>
            fields.forEachIndexed { index, bindableField ->
                bindableField.read(payload, index)
            }
        }
    }

}