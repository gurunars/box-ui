package com.gurunars.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.*

/**
 * A convenient wrapper around Google's LiveData.
 *
 * Observer functionality is meant to work only within extension functions of
 * BoxContext.
 *
 * Methods **set** and **get**  are available at any context.
 */
class Box<Type>(
    private val initialValue: Type,
    private val preprocess: (item: Type) -> Type = { it }
) {
    private val data = MutableLiveData<Type>()

    init {
        data.value = initialValue
    }

    internal fun onChange(owner: LifecycleOwner, hot: Boolean=true, listener: (value: Type) -> Unit) {
        if (hot) listener(this@Box.get())
        data.observe(owner, object : Observer<Type> {
            override fun onChanged(t: Type?) {
                listener(t ?: initialValue)
            }
        })
    }

    /**
     * Change fields content to a new value. The change is made only if current and new values
     * actually differ content-wise.
     *
     * @param value payload to set the value to
     * @param force if true - the change is made even if current and new values are the same
     */
    fun set(value: Type, force: Boolean=false): Boolean {
        if (force || !Objects.deepEquals(get(), value)) {
            this.data.value = preprocess(value)
            return true
        }
        return false
    }

    /**
     * Fetches the current value.
     */
    fun get(): Type = this.data.value!!
}
