package com.gurunars.databinding

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.*

/**
 * An observable box capable to emit changes and listen to change events
 *
 * @param Type type of the value the box is meant to hold
 * @param value initial value of the box
 */
class Box<Type>(
    private val initialValue: Type,
    override val owner: LifecycleOwner
) : IBox<Type> {
    private val data = MutableLiveData<Type>()
    private var prevValue: Type = initialValue

    init {
        data.value = initialValue
    }

    override fun onChange(hot: Boolean, listener: Listener<Type>) {
        if (hot) listener(this.prevValue, this.get())
        data.observe(owner, object : Observer<Type> {
            override fun onChanged(t: Type?) {
                listener(this@Box.prevValue, t ?: initialValue)
            }
        })
    }

    override fun set(value: Type, force: Boolean): Boolean {
        if (force || !Objects.deepEquals(get(), value)) {
            this.data.value = value
            this.prevValue = value
            return true
        }
        return false
    }

    override fun get(): Type = this.data.value!!
}