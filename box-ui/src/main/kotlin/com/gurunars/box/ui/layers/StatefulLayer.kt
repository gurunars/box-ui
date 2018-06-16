package com.gurunars.box.ui.layers

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import com.gurunars.box.IBox
import com.gurunars.box.ui.layoutAsOne

interface Retainer {
    /**
     * Call with multiple fields to notify the component that the state of the fields should
     * be persisted.
     */
    fun retain(vararg fields: IBox<*>)
}

private class StatefulView internal constructor(context: Context) : FrameLayout(context),
    Retainer {

    private val fields: MutableList<IBox<*>> = mutableListOf()

    override fun retain(vararg fields: IBox<*>) {
        this.fields.addAll(fields)
    }

    companion object {

        private fun <T> IBox<T>.read(map: HashMap<Int, Any?>, index: Int) {
            @Suppress("UNCHECKED_CAST")
            val obtained = map[index] as T
            this.set(obtained)
        }
    }

    /** @suppress */
    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        val payload = hashMapOf<Int, Any?>()
        fields.forEachIndexed { index, field ->
            payload[index] = field.get()
        }
        putSerializable("payload", payload)
    }

    /** @suppress */
    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            state.apply {
                super.onRestoreInstanceState(getParcelable<Parcelable>("superState"))
                val payload = getSerializable("payload") ?: return
                @Suppress("UNCHECKED_CAST")
                payload as HashMap<Int, Any?>
                fields.forEachIndexed { index, field ->
                    field.read(payload, index)
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

/**
 * UI component capable of retaining the state of observable fields
 * through application state changes.
 *
 * In order to mark the field as retainable just call a **retain** method
 * on it.
 */
fun Context.statefulLayer(
    id: Int,
    viewSupplier: Retainer.() -> View
): View =
    StatefulView(this).apply {
        this.id = id
        viewSupplier().layoutAsOne(this)
    }