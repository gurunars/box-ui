package com.gurunars.databinding


open class BindableField<Type>(private var value: Type) {

    interface ValueProcessor<Type> {
        fun forward(value: Type): Type
        fun backward(value: Type): Type
    }

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

    fun bind(field: BindableField<Type>, transformer: ValueProcessor<Type>?=null): Binding {
        val forwardBinding = bind { field.set(transformer?.forward(it) ?: it) }
        val backwardBinding = field.bind { this.set(transformer?.backward(it) ?: it) }

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
        if (this.value != value) {
            this.value = value
            listeners.forEach { it(value) }
        }
    }

    fun get() : Type {
        return this.value
    }

    fun unbindFromAll() {
        bindings.toList().forEach { it.unbind() }
    }
}