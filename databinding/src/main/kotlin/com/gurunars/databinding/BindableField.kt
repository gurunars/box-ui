package com.gurunars.databinding


class BindableField<Type>(
        private var value: Type,
        private val equal: (one: Type, two: Type) -> Boolean = ::equal,
        private val preset: (one: Type) -> Type = { item -> item }
) : Disposable {

    interface ValueProcessor<From, To> {
        fun forward(value: From): To
        fun backward(value: To): From
    }

    interface Binding {
        fun unbind()
    }

    private val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    private val bindings: MutableList<Binding> = mutableListOf()

    fun onChange(listener: (value: Type) -> Unit): Binding {
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
        if (force || ! equal(this.value, value)) {
            this.value = preset(value)
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
            onChange { field.set(transformer.forward(it)) },
            field.onChange { this.set(transformer.backward(it)) }
        )
    }

    fun bind(field: BindableField<Type>): Binding {
        return join(field,
            onChange { field.set(it) },
            field.onChange { this.set(it) }
        )
    }

    override fun disposeAll() {
        bindings.toList().forEach { it.unbind() }
    }

}