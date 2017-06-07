package com.gurunars.databinding


abstract class ListenerField<Type>(private var value: Type) {

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

    open internal fun set(value: Type, force:Boolean=false) {
        if (force || this.value != value) {
            this.value = value
            listeners.forEach { it(value) }
        }
    }

    open internal fun get() : Type = this.value

    open fun unbindFromAll() { bindings.toList().forEach { it.unbind() } }

}