package com.gurunars.databinding


class BindableField<Type>(private var value: Type) {

    interface ValueProcessor<From, To> {
        fun forward(value: From): To
        fun backward(value: To): From
    }

    interface Binding {
        fun unbind()
    }

    private val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    private val bindings: MutableList<Binding> = mutableListOf()

    fun bind(listener: (value: Type) -> Unit): Binding {
        listeners.add(listener)
        val binding = object : Binding {
            override fun unbind() {
                listeners.remove(listener)
                bindings.remove(this)
            }
        }
        bindings.add(binding)
        listener(this.value)
        return binding
    }

    fun set(value: Type, force:Boolean=false) {
        if (force || this.value != value) {
            this.value = value
            listeners.forEach { it(value) }
        }
    }

    fun get() : Type = this.value

    private fun join(field: BindableField<*>, forwardBinding: Binding, backwardBinding: Binding): Binding {
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

    fun <To> bind(field: BindableField<To>, transformer: ValueProcessor<Type, To>): Binding {
        return join(field,
            bind { field.set(transformer.forward(it)) },
            field.bind { this.set(transformer.backward(it)) }
        )
    }

    fun bind(field: BindableField<Type>): Binding {
        return join(field,
            bind { field.set(it) },
            field.bind { this.set(it) }
        )
    }

    fun unbindFromAll() {
        bindings.toList().forEach { it.unbind() }
    }

}