package com.gurunars.databinding


class BindableField<Type>(value: Type): ListenerField<Type>(value) {

    private val bindings: MutableList<Binding> = mutableListOf()

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

    override fun unbindFromAll() {
        super.unbindFromAll()
        bindings.toList().forEach { it.unbind() }
    }

    public override fun set(value: Type, force: Boolean) {
        super.set(value, force)
    }

    public override fun get() = super.get()

}