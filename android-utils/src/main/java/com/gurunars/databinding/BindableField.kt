package com.gurunars.databinding

import android.util.Log


class BindableField<Type>(private var value: Type) {

    interface Binding {
        fun unbind()
    }

    val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    val bindings: MutableList<Binding> = mutableListOf()

    fun bind(listener: (value: Type) -> Unit): Binding {
        listeners.add(listener)
        val binding = object : Binding {
            override fun unbind() {
                listeners.remove(listener)
                bindings.remove(this)
            }
        }
        listener(this.value)
        return binding
    }

    fun bind(field: BindableField<Type>): Binding {
        val forwardBinding = bind({ field.set(it) })
        val backwardBinding = field.bind({ this.set(it) })

        val twoWayBinding = object: Binding {
            override fun unbind() {
                forwardBinding.unbind()
                backwardBinding.unbind()
                bindings.remove(this)
                field.bindings.remove(this)
            }
        }

        bindings.add(twoWayBinding)
        field.bindings.add(twoWayBinding)

        return twoWayBinding
    }

    fun set(value: Type) {
        val previousValue = this.value
        this.value = value
        if (this.value != previousValue) {
            listeners.forEach {
                Log.e("SET", "" + value)
                it(value)
            }
        }
    }

    fun get() : Type {
        return this.value
    }

}