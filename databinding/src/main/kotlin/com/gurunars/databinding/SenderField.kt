package com.gurunars.databinding

class SenderField<Type>(value: Type): ListenerField<Type>(value) {

    public override fun set(value: Type, force: Boolean) {
        super.set(value, force)
    }

    fun sendTo(field: ListenerField<Type>): Binding {
        return bind { field.set(it) }
    }

    fun<To> sendTo(field: ListenerField<To>, transformer: (source: Type) -> To): Binding {
        return bind { field.set(transformer(it))}
    }

}
