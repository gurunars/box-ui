package com.gurunars.databinding


open class BindableField<Type>(private var value: Type) : Bindable<Type> {

    val listeners: MutableList<(value: Type) -> Unit> = mutableListOf()
    val bindings: MutableList<Binding> = mutableListOf()

    override fun bind(listener: (value: Type) -> Unit): Binding {
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

    override fun bind(field: BindableField<Type>, transformer: ValueProcessor<Type>?): Binding {
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

    override fun set(value: Type, force:Boolean) {
        if (this.value != value || force) {
            this.value = value
            listeners.forEach { it(value) }
        }
    }

    override fun get() : Type {
        return this.value
    }

    override fun unbindFromAll() {
        bindings.toList().forEach { it.unbind() }
    }
}